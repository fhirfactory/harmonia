# Harmonia Platform Verification & Smoke Testing Runbook

This runbook provides executable, end-to-end verification commands to validate platform architecture, build integrity, deployment manifests, and runtime health.

---

## 1. Automated Architecture Guardrail Verification

Execute the ArchUnit and static architectural enforcement rules:

```bash
# Verify all architectural rules (Paradeigma isolation, Petasos API decoupling, Iris decoupling, Package layering)
mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
```
*Expected Result*: `17 tests run, 0 failures, 0 errors, 0 skipped`.

---

## 2. Core Subsystem Unit & Integration Tests

```bash
# 1. Inbound/Outbound MLLP Gateway Tests (REC-001 & REC-002)
mvn test -pl pylai/pylai-mllp-in,pylai/pylai-mllp-base,pylai/pylai-mllp-out -am

# 2. Workflow & Task Processing Engine Tests (Erga / Ponos / Praxis)
mvn test -pl energeia/erga,energeia/praxis,energeia/ponos -am

# 3. Persistence & Cache Store SPI Tests
mvn test -pl hestia/mneme-persistence,hestia/mnemosyne-clinical,hestia/mnemosyne-operations -am

# 4. Security Policy & Audit Tests
mvn test -pl themis/themis-api,themis/themis-core,themis/themis-audit -am

# 5. Presentation Tier Tests
mvn test -pl iris/iris-befe,iris/iris-clinical,iris/iris-console,iris/iris-administration -am
```

---

## 3. Deployment Manifest Syntax & Kustomize Validation

```bash
# Validate Kubernetes Kustomize build for MicroK8s overlay
kubectl kustomize deployment/kubernetes/environments/microk8s > /dev/null
```
*Expected Result*: Zero syntax errors, valid YAML generation.

---

## 4. Live Cluster Smoke Test Commands

After deploying Harmonia onto MicroK8s or Docker Compose:

### 4.1 Inbound MLLP Port Verification
```bash
# Test TCP connectivity to MLLP Inbound Gateway
nc -zv localhost 2575
```

### 4.2 Inbound Message Simulation via CLI
```bash
# Send synthetic HL7 ADT A01 message
java -jar pylai/pylai-mllp-cli/target/pylai-mllp-cli-*.jar \
  --host localhost --port 2575 --event ADT_A01 --mrN 10042
```
*Expected Result*: Returns HL7 `MSA|AA` acknowledgement.

### 4.3 FHIR REST Verification
```bash
# Query FHIR Patient resource
curl -k http://localhost/api/fhir/Patient/10042
```
*Expected Result*: HTTP 200 OK with valid FHIR R5 Patient JSON payload.

### 4.4 Operations & Telemetry Verification
```bash
# Query system health and task sequence telemetry
curl -k http://localhost/api/operations/status
```
*Expected Result*: HTTP 200 OK with active module registration telemetry.
