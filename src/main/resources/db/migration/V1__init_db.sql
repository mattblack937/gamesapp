CREATE TABLE users (
id BIGINT AUTO_INCREMENT,
username VARCHAR(255),
password VARCHAR(255),
enabled INT,
role VARCHAR(50),
CONSTRAINT uq_username UNIQUE (username),
CONSTRAINT pk_account PRIMARY KEY (id)
) engine = innodb;

insert into users(username, password, enabled, role)
values ('admin', '$2a$10$M7/F8ur785gtN51RU.VdiOW/m5/R8HFGiYp9mEC3DjVHZE.PuNK3G', 1, 'ROLE_ADMIN');

insert into users(username, password, enabled, role)
values ('user1', '$2a$10$9p1lh6etpodEo7CL9fLhNOCoM1rhCiT7k7KRoUMCYvgaziLQaRJMS', 1, 'ROLE_USER');

insert into users(username, password, enabled, role)
values ('user2', '$2a$10$MSvTtYLbZy2ebQ/N4ZKC2.JFzbjwx5qpVOBqqkehbIawPgD5oStSy', 1, 'ROLE_USER');

