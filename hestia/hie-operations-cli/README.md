# Harmonia Mnemosyne Operations CLI (`hie-operations-cli`)

A command-line tool for interacting with the **Harmonia Mnemosyne Operations JPA Server** (`mnemosyne-operations`), inspecting non-FHIR operational resources, and managing **TaskSequence** definitions across the Harmonia platform.

---

## Architecture Overview

```
┌────────────────────────────────────────────────────────┐
│               hie-operations-cli                       │
│     (Picocli + Java 11 HTTP Client + Jackson)          │
└───────────────────────────┬────────────────────────────┘
                            │ HTTP (JSON REST API)
                            ▼
┌────────────────────────────────────────────────────────┐
│               mnemosyne-operations                     │
│   (Spring Boot JPA Server - Port 8085 / 8086)          │
└───────────────────────────┬────────────────────────────┘
                            │ JDBC
                            ▼
┌────────────────────────────────────────────────────────┐
│             PostgreSQL / H2 Database                   │
│        (hie_operations_resources table)                │
└────────────────────────────────────────────────────────┘
```

The CLI communicates with the REST endpoints exposed by `mnemosyne-operations`:
- `GET /api/operations` & `GET /api/operations/{objectType}` — Query all or filtered operational resources
- `GET /api/operations/{objectType}/{id}` — Fetch JSON data for a specific operational resource / TaskSequence
- `PUT /api/operations/{objectType}/{id}` — Save / create an operational resource / TaskSequence
- `DELETE /api/operations/{objectType}/{id}` — Delete an operational resource / TaskSequence
- `HEAD /api/operations/{objectType}/{id}` — Check existence of an operational resource / TaskSequence

---

## Building the CLI

Build the executable shaded uber-JAR with Maven:

```bash
mvn clean package -pl hestia/hie-operations-cli -am
```

The build produces a self-contained shaded executable JAR at:
```
hestia/hie-operations-cli/target/hie-operations-cli-1.0.0-SNAPSHOT.jar
```

---

## Global Options

| Option | Environment Variable | Default | Description |
|---|---|---|---|
| `-s, --server-url, --url` | `OPS_SERVER_URL` | `http://localhost:8085/api/operations` | Operations JPA Server base REST URL |
| `-H, --host` | `OPS_HOST` | `localhost` | Operations JPA Server host |
| `-p, --port` | `OPS_PORT` | `8085` | Operations JPA Server port |
| `--timeout` | — | `10` | HTTP request timeout in seconds |
| `-o, --output` | — | `table` | Output format: `table`, `json`, `raw`, `summary` |
| `--pretty` | — | `false` | Enable indented pretty-printing for JSON output |
| `-v, --verbose` | — | `false` | Enable verbose HTTP request / response logging |
| `-h, --help` | — | — | Show help message and exit |
| `-V, --version` | — | — | Print version information and exit |

---

## Commands & Usage Recipes

### 1. Requesting a List of ALL TaskSequences

You can request a list of all TaskSequences using either the `-l / --list-sequences` flag or the `list-sequences` subcommand:

```bash
# Formatted Table View
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar --list-sequences

# Pretty JSON View
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar list-sequences -o json --pretty

# Targeting Remote Operations Server
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar -s http://operations-node-1:8080/api/operations --list-sequences
```

Example table output:
```
Found 3 TaskSequence(s):

SEQUENCE ID                    | SEQUENCE NAME                    | STATUS   | GATEWAYS         | TRIGGERS                 | ACTIVITIES
------------------------------------------------------------------------------------------------------------------------------------------------------------
seq-admission-pipeline         | Admission Task Sequence          | ENABLED  | *                | A01,A02,A03,A04,A05,...  | patient-identity-update -> patient-demographics-update
seq-order-result-pipeline      | Orders and Results Task Sequence | ENABLED  | *                | ORM^O01,ORU^R01,MDM^T02  | patient-identity-update -> patient-demographics-update
seq-patient-identity-pipeline  | Patient Identity Update Sequence | ENABLED  | *                | *                        | patient-identity-update -> patient-demographics-update
```

### 2. Inspecting a TaskSequence in Detail

```bash
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar get-sequence seq-admission-pipeline
```

Output:
```
================================================================================
 Task Sequence: Admission Task Sequence
================================================================================
  Sequence ID:      seq-admission-pipeline
  Version:          1.0.0
  Enabled:          true
  Description:      Processes patient admission events (A01, A04, A05, A08, A28, A31, A40)
  Target Gateways:  *
  Target Triggers:  A01, A02, A03, A04, A05, A08, A11, A12, A13, A28, A31, A40
  Activity Count:   2
  Activity Pipeline:
    1. patient-identity-update
    2. patient-demographics-update
================================================================================
```

### 3. Saving or Updating a TaskSequence

From a JSON file:
```bash
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar save-sequence -f custom-sequence.json
```

From inline JSON:
```bash
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar save-sequence -d '{"sequenceId":"seq-custom","sequenceName":"Custom Pipeline","enabled":true,"targetGatewayInstances":["*"],"targetTriggerTypes":["A01"],"activityIds":["patient-identity-update"]}'
```

### 4. Deleting a TaskSequence

```bash
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar delete-sequence seq-custom
```

### 5. Managing Generic Operational Resources

```bash
# List all resources across all object types
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar list-resources

# List all resources for a specific object type (e.g. config)
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar list-resources -t config

# Get a specific resource
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar get-resource config cluster-settings

# Save a resource
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar save-resource config cluster-settings -d '{"maxNodes":5,"mode":"clustered"}'

# Check if a resource exists
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar check-resource config cluster-settings

# Delete a resource
java -jar hie-operations-cli-1.0.0-SNAPSHOT.jar delete-resource config cluster-settings
```

---

## Exit Codes

| Exit Code | Meaning |
|---|---|
| `0` | Success / Operation completed |
| `1` | Error / Resource not found / Connection failure |
