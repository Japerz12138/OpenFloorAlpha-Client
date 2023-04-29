CREATE DATABASE IF NOT EXISTS openFloor_db;

USE openFloor_db;

CREATE TABLE IF NOT EXISTS accounts (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_banned BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id)
);

INSERT INTO accounts (user_id, username, nickname, password, is_banned)
VALUES (0, 'admin', 'admin', SHA2('admin', 256), 0);