CREATE TABLE `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fcm_token VARCHAR(255) NULL,
    user_name VARCHAR(255) NULL,
    oauth_provider VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    oauth_subject VARCHAR(255) NULL,
    apple_refresh_token TEXT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    day_of_week VARCHAR(255) NULL,
    user_id BIGINT NOT NULL,
    deleted_at DATETIME NULL
);

CREATE TABLE token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    refresh_token VARCHAR(255) NOT NULL,
    is_expired BOOLEAN NOT NULL DEFAULT FALSE
);
