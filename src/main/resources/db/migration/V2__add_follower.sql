CREATE TABLE follower
(
    follower_id BIGINT AUTO_INCREMENT,
    from_user BIGINT,
    to_user BIGINT,
    PRIMARY KEY (follower_id)
) ENGINE = InnoDB;


alter table follower
    ADD CONSTRAINT from_user_fk
        FOREIGN KEY (from_user)
            REFERENCES user (user_id);

alter table follower
    ADD CONSTRAINT to_user_fk
        FOREIGN KEY (to_user)
            REFERENCES user (user_id);
