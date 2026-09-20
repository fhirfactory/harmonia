#!/bin/bash
# Copyright (c) 2026 Mark Hunter
# Petasos standalone Artemis entrypoint:
# - creates the broker instance if missing (valid artemis create flags only)
# - applies etc-override (broker.xml) without relying on unsupported --override-config
# - generates JAAS users/roles at runtime from environment credentials (no source-controlled secrets)
# - when started as root (compose user: "0"), fixes data-volume ownership then drops to artemis
set -e

cd /var/lib/artemis-instance

# Named Docker volumes are root-owned by default. If we are root, repair ownership and
# re-exec as the artemis runtime user (uid 1001) before create/run.
if [ "$(id -u)" = "0" ]; then
  mkdir -p ./data/bindings ./data/journal ./data/paging ./data/large-messages ./etc ./etc-override ./tmp ./log
  chown -R artemis:artemis /var/lib/artemis-instance || true
  # Preserve argv and re-enter this script as artemis.
  exec runuser -u artemis -- /bin/bash "$0" "$@"
fi

if [ "${ANONYMOUS_LOGIN:-false}" = "true" ]; then
  LOGIN_OPTION="--allow-anonymous"
else
  LOGIN_OPTION="--require-login"
fi

# EXTRA_ARGS must only contain valid `artemis create` options (e.g. --http-host 0.0.0.0 --nio).
# Do NOT pass --override-config; broker overrides are applied via etc-override copy below.
CREATE_EXTRA_ARGS="${EXTRA_ARGS:-}"

if ! [ -f ./etc/broker.xml ]; then
  echo "Creating Artemis instance (login=${LOGIN_OPTION}, extra=${CREATE_EXTRA_ARGS})"
  # Intentionally unquoted LOGIN_OPTION / CREATE_EXTRA_ARGS so multiple create flags expand.
  # shellcheck disable=SC2086
  /opt/activemq-artemis/bin/artemis create \
    --user "${ARTEMIS_USER:-admin}" \
    --password "${ARTEMIS_PASSWORD:-adminPassword}" \
    --role admin \
    --silent \
    ${LOGIN_OPTION} \
    ${CREATE_EXTRA_ARGS} \
    .
else
  echo "skipping broker instance creation; instance already exists"
fi

# Always apply mounted etc-override files (broker.xml, etc.) so standalone config wins.
if [ -d ./etc-override ]; then
  mkdir -p ./etc
  # shellcheck disable=SC2045
  for file in $(ls ./etc-override 2>/dev/null); do
    if [ -f "./etc-override/$file" ]; then
      # Never overwrite runtime-generated credential files from a static mount.
      if [ "$file" = "artemis-users.properties" ] || [ "$file" = "artemis-roles.properties" ]; then
        echo "Skipping credential override mount: $file (generated from environment)"
        continue
      fi
      echo "Copying override file to etc folder: $file"
      cp "./etc-override/$file" "./etc/$file" || true
    fi
  done
fi

# Dynamically synchronize user credentials and role mappings from environment variables.
ART_USER="${ARTEMIS_USER:-admin}"
ART_PASS="${ARTEMIS_PASSWORD:-adminPassword}"
PET_USER="${PETASOS_BROKER_USER:-harmonia}"
PET_PASS="${PETASOS_BROKER_PASSWORD:-harmoniaPassword}"

mkdir -p ./etc
{
  echo "${ART_USER} = ${ART_PASS}"
  if [ "${PET_USER}" != "${ART_USER}" ]; then
    echo "${PET_USER} = ${PET_PASS}"
  fi
} > ./etc/artemis-users.properties

{
  echo "admin = ${ART_USER}"
  if [ "${PET_USER}" != "${ART_USER}" ]; then
    echo "harmonia = ${PET_USER}"
  else
    echo "harmonia = ${ART_USER}"
  fi
} > ./etc/artemis-roles.properties

# Ensure durable storage directories exist on the data volume (fresh volumes are empty).
mkdir -p ./data/bindings ./data/journal ./data/paging ./data/large-messages

if [ "$#" -eq 0 ]; then
  set -- run
fi

exec ./bin/artemis "$@"
