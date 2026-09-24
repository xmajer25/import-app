ALTER TABLE import_job
    ADD COLUMN municipalities_created INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN municipalities_updated INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN municipality_parts_created INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN municipality_parts_updated INTEGER NOT NULL DEFAULT 0;

ALTER TABLE import_job
    DROP COLUMN records_created,
    DROP COLUMN records_updated;
