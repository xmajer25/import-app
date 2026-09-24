CREATE TABLE import_job (
    id UUID PRIMARY KEY,
    started_at TIMESTAMPTZ NOT NULL,
    finished_at TIMESTAMPTZ NOT NULL,
    import_status VARCHAR(255) NOT NULL,
    source_url VARCHAR(255) NOT NULL,
    records_created INTEGER NOT NULL,
    records_updated INTEGER NOT NULL,
    error_message VARCHAR(255)
);
