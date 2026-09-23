CREATE TABLE municipality (
    code VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    modified_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE municipality_part (
    code VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    municipality_code VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    modified_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_municipality_part_municipality
        FOREIGN KEY (municipality_code)
        REFERENCES municipality (code)
);

CREATE INDEX idx_municipality_part_municipality_code
    ON municipality_part (municipality_code);