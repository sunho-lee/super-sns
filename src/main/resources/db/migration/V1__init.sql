CREATE TABLE post
(
    post_id BIGINT AUTO_INCREMENT,
    content VARCHAR(180),
    user_id BIGINT NOT NULL,
    PRIMARY KEY (post_id)
) ENGINE = InnoDB;

CREATE TABLE role
(
    role_id BIGINT NOT NULL AUTO_INCREMENT,
    name    VARCHAR(60),
    PRIMARY KEY (role_id)
) ENGINE = InnoDB;

CREATE TABLE user
(
    user_id  BIGINT NOT NULL AUTO_INCREMENT,
    nickname VARCHAR(255),
    password VARCHAR(255),
    username VARCHAR(255),
    PRIMARY KEY (user_id)
) ENGINE = InnoDB;

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
) ENGINE = InnoDB;

ALTER TABLE post
    ADD CONSTRAINT user_post_fk
        FOREIGN KEY (user_id)
            REFERENCES user (user_id);

alter table user_role
    add constraint role_user_fk
        foreign key (role_id)
            references role (role_id);

alter table user_role
    add constraint user_role_fk
        foreign key (user_id)
            references user (user_id);
