#!/usr/bin/env bash
set -euo pipefail

# Determine output directory
PKI_DIR="${1:-.pki/dev-tls}"
PASSWORD="${2:-harmoniapass}"

echo "Generating Harmonia Development PKI into: ${PKI_DIR}"
mkdir -p "${PKI_DIR}"

TMP_DIR="$(mktemp -d)"
trap 'rm -rf "${TMP_DIR}"' EXIT

cd "${TMP_DIR}"

# 1. Generate Harmonia Development Root CA
echo "1. Generating Root CA..."
openssl req -x509 -newkey rsa:2048 -nodes -keyout ca.key -out ca.crt -days 3650 \
  -subj "/C=AU/ST=NSW/L=Sydney/O=Harmonia Health/OU=Security/CN=Harmonia Development CA"

# 2. Generate Mnemosyne Server Certificate (with DNS SANs)
echo "2. Generating Mnemosyne Server Certificate..."
openssl req -newkey rsa:2048 -nodes -keyout mnemosyne.key -out mnemosyne.csr \
  -subj "/C=AU/ST=NSW/L=Sydney/O=Harmonia Health/OU=Mnemosyne/CN=mnemosyne-clinical"

cat > mnemosyne_ext.cnf <<EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, keyEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1 = mnemosyne-clinical
DNS.2 = hapi-fhir-jpa-server-1
DNS.3 = localhost
IP.1 = 127.0.0.1
EOF

openssl x509 -req -in mnemosyne.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out mnemosyne.crt -days 3650 -extfile mnemosyne_ext.cnf

# 3. Generate Mneme Client Certificate (with normative URI SAN)
echo "3. Generating Mneme Client Certificate (SAN: URI:urn:harmonia:service:mneme)..."
openssl req -newkey rsa:2048 -nodes -keyout mneme.key -out mneme.csr \
  -subj "/C=AU/ST=NSW/L=Sydney/O=Harmonia Health/OU=Mneme/CN=service:mneme"

cat > mneme_ext.cnf <<EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, keyEncipherment
extendedKeyUsage = clientAuth
subjectAltName = @alt_names

[alt_names]
URI.1 = urn:harmonia:service:mneme
EOF

openssl x509 -req -in mneme.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out mneme.crt -days 3650 -extfile mneme_ext.cnf

# 4. Generate Valid Client Certificate with Wrong URI SAN
echo "4. Generating Wrong SAN Client Certificate (SAN: URI:urn:harmonia:service:other)..."
openssl req -newkey rsa:2048 -nodes -keyout wrong-san.key -out wrong-san.csr \
  -subj "/C=AU/ST=NSW/L=Sydney/O=Harmonia Health/OU=Other/CN=service:other"

cat > wrong_san_ext.cnf <<EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, keyEncipherment
extendedKeyUsage = clientAuth
subjectAltName = @alt_names

[alt_names]
URI.1 = urn:harmonia:service:other
EOF

openssl x509 -req -in wrong-san.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out wrong-san.crt -days 3650 -extfile wrong_san_ext.cnf

# 5. Generate Valid Client Certificate with Missing SAN
echo "5. Generating No-SAN Client Certificate (CN=service:mneme, no SAN)..."
openssl req -newkey rsa:2048 -nodes -keyout no-san.key -out no-san.csr \
  -subj "/C=AU/ST=NSW/L=Sydney/O=Harmonia Health/OU=Mneme/CN=service:mneme"

cat > no_san_ext.cnf <<EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, keyEncipherment
extendedKeyUsage = clientAuth
EOF

openssl x509 -req -in no-san.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out no-san.crt -days 3650 -extfile no_san_ext.cnf

# 6. Generate Untrusted Client Certificate (Rogue CA)
echo "6. Generating Untrusted Client Certificate (Rogue CA)..."
openssl req -x509 -newkey rsa:2048 -nodes -keyout rogue-ca.key -out rogue-ca.crt -days 3650 \
  -subj "/C=AU/ST=NSW/L=Sydney/O=Rogue Corp/CN=Rogue Development CA"

openssl req -newkey rsa:2048 -nodes -keyout untrusted.key -out untrusted.csr \
  -subj "/C=AU/ST=NSW/L=Sydney/O=Mneme/CN=service:mneme"

openssl x509 -req -in untrusted.csr -CA rogue-ca.crt -CAkey rogue-ca.key -CAcreateserial \
  -out untrusted.crt -days 3650 -extfile mneme_ext.cnf

# 7. Package PKCS12 Keystores and Truststores
echo "7. Packaging PKCS12 Keystores and Truststores..."

# Server Keystore & Truststore
openssl pkcs12 -export -in mnemosyne.crt -inkey mnemosyne.key -certfile ca.crt \
  -out mnemosyne-keystore.p12 -name mnemosyne -password "pass:${PASSWORD}"

keytool -importcert -noprompt -file ca.crt -alias harmonia-ca \
  -keystore mnemosyne-truststore.p12 -storetype PKCS12 -storepass "${PASSWORD}"

# Client Keystore & Truststore
openssl pkcs12 -export -in mneme.crt -inkey mneme.key -certfile ca.crt \
  -out mneme-keystore.p12 -name mneme -password "pass:${PASSWORD}"

keytool -importcert -noprompt -file ca.crt -alias harmonia-ca \
  -keystore mneme-truststore.p12 -storetype PKCS12 -storepass "${PASSWORD}"

# Negative Test Keystores
openssl pkcs12 -export -in wrong-san.crt -inkey wrong-san.key -certfile ca.crt \
  -out wrong-san-keystore.p12 -name wrong-san -password "pass:${PASSWORD}"

openssl pkcs12 -export -in no-san.crt -inkey no-san.key -certfile ca.crt \
  -out no-san-keystore.p12 -name no-san -password "pass:${PASSWORD}"

openssl pkcs12 -export -in untrusted.crt -inkey untrusted.key -certfile rogue-ca.crt \
  -out untrusted-keystore.p12 -name untrusted -password "pass:${PASSWORD}"

# Move artifacts to destination
cd - > /dev/null
cp -r "${TMP_DIR}"/* "${PKI_DIR}/"

echo "PKI generation completed successfully in ${PKI_DIR}."
