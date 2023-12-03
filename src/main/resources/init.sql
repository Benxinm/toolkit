CREATE TABLE `user`
(
    id         INT AUTO_INCREMENT NOT NULL,
    username   VARCHAR(32)                            NOT NULL unique,
    `password` VARCHAR(255)                           NOT NULL,
    avatar     VARCHAR(255) DEFAULT '123456789'       NOT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `id`
        PRIMARY KEY (`id`),
    INDEX      idx_username (`username`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

