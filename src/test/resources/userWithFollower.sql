INSERT INTO user(nickname, password, username)
VALUES ('test1', '1234', 'username1');
INSERT INTO user(nickname, password, username)
VALUES ('test2', '1234', 'username2');
INSERT INTO user(nickname, password, username)
VALUES ('test3', '1234', 'username3');
INSERT INTO user(nickname, password, username)
VALUES ('test4', '1234', 'username4');
INSERT INTO user(nickname, password, username)
VALUES ('test5', '1234', 'username5');
INSERT INTO user(nickname, password, username)
VALUES ('test6', '1234', 'username6');
INSERT INTO user(nickname, password, username)
VALUES ('test7', '1234', 'username7');
INSERT INTO user(nickname, password, username)
VALUES ('test8', '1234', 'username8');
INSERT INTO user(nickname, password, username)
VALUES ('test9', '1234', 'username9');
INSERT INTO user(nickname, password, username)
VALUES ('test10', '1234', 'username10');


INSERT INTO follower(from_user, to_user) VALUES (1, 2);
INSERT INTO follower(from_user, to_user) VALUES (1, 3);
INSERT INTO follower(from_user, to_user) VALUES (1, 4);
INSERT INTO follower(from_user, to_user) VALUES (1, 5);
INSERT INTO follower(from_user, to_user) VALUES (1, 6);
INSERT INTO follower(from_user, to_user) VALUES (1, 7);
INSERT INTO follower(from_user, to_user) VALUES (1, 8);
INSERT INTO follower(from_user, to_user) VALUES (1, 9);
INSERT INTO follower(from_user, to_user) VALUES (1, 10);

INSERT INTO follower(from_user, to_user) VALUES (2, 4);
INSERT INTO follower(from_user, to_user) VALUES (2, 6);
INSERT INTO follower(from_user, to_user) VALUES (2, 8);
INSERT INTO follower(from_user, to_user) VALUES (2, 10);

INSERT INTO follower(from_user, to_user) VALUES (3, 1);
INSERT INTO follower(from_user, to_user) VALUES (3, 6);
INSERT INTO follower(from_user, to_user) VALUES (3, 9);

INSERT INTO follower(from_user, to_user) VALUES (4, 8);

INSERT INTO follower(from_user, to_user) VALUES (5, 10);
