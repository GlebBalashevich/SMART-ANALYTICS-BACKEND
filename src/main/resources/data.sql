INSERT INTO department(id, name, salary_budget, create_date, last_update_date)
VALUES ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'Java Enterprise Solutions', '0', current_timestamp, current_timestamp),
       ('83110a26-fa26-4cc2-8963-d86520a918f9', 'Frontend Enterprise Solutions', '0', current_timestamp,
        current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO authentication(email, department_id, role)
VALUES ('java_solutions@mail.com', '22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'DEPARTMENT_MANAGER'),
       ('frontend_solutions@mail.com', '83110a26-fa26-4cc2-8963-d86520a918f9', 'DEPARTMENT_MANAGER')
ON CONFLICT DO NOTHING;

INSERT INTO employee(department_id, email, given_name, family_name, birth_date, hire_date, salary, title, is_deleted,
                     create_date, last_update_date)
VALUES ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example1@mail.com', 'Ivan', 'Ivanov', '1988-01-01', '2021-04-10',
        '2100', 'MIDDLE', 'false', current_timestamp, current_timestamp),
       ('83110a26-fa26-4cc2-8963-d86520a918f9', 'example2@mail.com', 'Petr', 'Petrov', '1990-03-03', '2022-02-15',
        '1100', 'JUNIOR', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example3@mail.com', 'Sergei', 'Sergeev', '1975-12-24', '2020-11-15',
        '5000', 'LEAD', 'false', current_timestamp, current_timestamp),
       ('83110a26-fa26-4cc2-8963-d86520a918f9', 'example4@mail.com', 'Sidr', 'Sidorov', '1985-09-20', '2018-08-01',
        '3600', 'SENIOR', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example5@mail.com', 'Gleb', 'Glebov', '1990-05-19', '2020-03-05',
        '3100', 'SENIOR', 'false', current_timestamp, current_timestamp),
       ('83110a26-fa26-4cc2-8963-d86520a918f9', 'example6@mail.com', 'Sergei', 'Ivanov', '1987-06-13', '2021-07-21',
        '1800', 'MIDDLE', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example7@mail.com', 'Ivan', 'Petrov', '1992-06-13', '2022-07-21',
        '900', 'JUNIOR', 'true', current_timestamp, current_timestamp),
       ('83110a26-fa26-4cc2-8963-d86520a918f9', 'example8@mail.com', 'Sidr', 'Glebov', '1992-11-29', '2021-02-20',
        '1900', 'MIDDLE', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example9@mail.com', 'Ivan', 'Petrov', '1989-02-02', '2021-04-10',
        '2600', 'MIDDLE', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example10@mail.com', 'Petr', 'Glebov', '1990-04-012', '2022-02-15',
        '9800', 'JUNIOR', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example11@mail.com', 'Sergei', 'Sidorov', '1987-11-20', '2020-11-15',
        '4800', 'LEAD', 'false', current_timestamp, current_timestamp),
       ('83110a26-fa26-4cc2-8963-d86520a918f9', 'example12@mail.com', 'Sidr', 'Sidorov', '1988-05-28', '2018-08-01',
        '3200', 'SENIOR', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example13@mail.com', 'Gleb', 'Glebov', '1993-02-02', '2020-03-05',
        '800', 'JUNIOR', 'true', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example14@mail.com', 'Sergei', 'Petrov', '1985-01-12', '2021-07-21',
        '2900', 'SENIOR', 'false', current_timestamp, current_timestamp),
       ('83110a26-fa26-4cc2-8963-d86520a918f9', 'example15@mail.com', 'Ivan', 'Glebov', '1991-01-12', '2022-07-21',
        '1950', 'MIDDLE', 'false', current_timestamp, current_timestamp),
       ('22a1db07-80a6-4578-b2d8-1d4b7326cae0', 'example16@mail.com', 'Sidr', 'Sergeev', '1992-04-02', '2021-02-20',
        '5100', 'LEAD', 'false', current_timestamp, current_timestamp)
ON CONFLICT DO NOTHING;
