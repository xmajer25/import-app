CREATE TABLE municipality_extended (
    code VARCHAR(255) PRIMARY KEY,
    gml_id VARCHAR(255) NOT NULL,
    status_code INTEGER NOT NULL,
    district_code VARCHAR(255) NOT NULL,
    pou_code VARCHAR(255) NOT NULL,
    valid_from TIMESTAMPTZ NOT NULL,
    transaction_id BIGINT NOT NULL,
    global_change_proposal_id BIGINT NOT NULL,
    grammatical_case_2 VARCHAR(255) NOT NULL,
    grammatical_case_3 VARCHAR(255) NOT NULL,
    grammatical_case_4 VARCHAR(255) NOT NULL,
    grammatical_case_6 VARCHAR(255) NOT NULL,
    grammatical_case_7 VARCHAR(255) NOT NULL,
    nuts_lau VARCHAR(255) NOT NULL,
    geometry_gml_id VARCHAR(255) NOT NULL,
    geometry_srs_name VARCHAR(255) NOT NULL,
    geometry_srs_dimension INTEGER NOT NULL,
    geometry_point_gml_id VARCHAR(255) NOT NULL,
    geometry_position VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    modified_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_municipality_extended_municipality
        FOREIGN KEY (code)
        REFERENCES municipality (code)
        ON DELETE CASCADE
);
