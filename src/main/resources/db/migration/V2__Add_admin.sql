insert into person (username, firstname, lastname, password, email, active)
    values ('admin', 'firstname_admin', 'lastname_admin', '123', 'some@email.com', true);

insert into person_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN'), (1, 'TEACHER');