-- V001 development bootstrap only. Production migrations require their governed deployment process.
CREATE TABLE IF NOT EXISTS mnemosyne_iu_identity (
    iu_uuid UUID PRIMARY KEY,
    topic_name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS mnemosyne_iu_versions (
    iu_uuid UUID NOT NULL REFERENCES mnemosyne_iu_identity(iu_uuid),
    version BIGINT NOT NULL CHECK (version > 0),
    canonical_json TEXT NOT NULL,
    content_sha256 CHAR(64) NOT NULL,
    persisted_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (iu_uuid, version)
);

CREATE INDEX IF NOT EXISTS idx_mnemosyne_iu_versions_latest
    ON mnemosyne_iu_versions (iu_uuid, version DESC);

CREATE TABLE IF NOT EXISTS mnemosyne_operational_status_versions (
    owner_id TEXT NOT NULL,
    subject_id TEXT NOT NULL,
    status_type TEXT NOT NULL,
    version BIGINT NOT NULL CHECK (version > 0),
    payload TEXT NOT NULL,
    persisted_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (owner_id, subject_id, status_type, version)
);

CREATE INDEX IF NOT EXISTS idx_mnemosyne_operational_status_latest
    ON mnemosyne_operational_status_versions (owner_id, subject_id, status_type, version DESC);