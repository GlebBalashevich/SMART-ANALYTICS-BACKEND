revoke usage on schema public from public;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS authentication
(
    email         TEXT NOT NULL PRIMARY KEY,
    department_id UUID NOT NULL,
    password      TEXT,
    role          TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS department
(
    id               UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name             VARCHAR(100) NOT NULL UNIQUE,
    salary_budget    NUMERIC      NOT NULL,
    create_date      TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS employee
(
    id               UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    department_id    UUID        NOT NULL,
    email            TEXT        NOT NULL UNIQUE,
    given_name       VARCHAR(50) NOT NULL,
    family_name      VARCHAR(50) NOT NULL,
    birth_date       DATE        NOT NULL,
    hire_date        DATE        NOT NULL,
    salary           NUMERIC     NOT NULL,
    title            TEXT        NOT NULL,
    is_deleted       BOOLEAN     NOT NULL,
    create_date      TIMESTAMP   NOT NULL,
    last_update_date TIMESTAMP   NOT NULL
);
