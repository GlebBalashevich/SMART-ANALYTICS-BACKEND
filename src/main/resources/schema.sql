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

INSERT INTO department(id, name, salary_budget, create_date, last_update_date)
values ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'Java Enterprise Solutions', '0', current_timestamp, current_timestamp)
ON CONFLICT DO NOTHING;
INSERT INTO department(id, name, salary_budget, create_date, last_update_date)
values ('83110a26-fa26-4cc2-8963-d86520a918f9', 'Frontend Enterprise Solutions', '0', current_timestamp,
        current_timestamp)
ON CONFLICT DO NOTHING;
INSERT INTO authentication(email, department_id, role)
values ('java_solutions@mail.com', '22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'DEPARTMENT_MANAGER')
ON CONFLICT DO NOTHING;
INSERT INTO authentication(email, department_id, role)
values ('frontend_solutions@mail.com', '83110a26-fa26-4cc2-8963-d86520a918f9', 'DEPARTMENT_MANAGER')
ON CONFLICT DO NOTHING;
