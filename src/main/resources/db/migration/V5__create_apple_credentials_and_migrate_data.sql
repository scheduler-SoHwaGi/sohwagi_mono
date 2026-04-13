CREATE TABLE apple_credentials
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    oauth_subject       VARCHAR(255) NOT NULL,
    apple_refresh_token TEXT         NULL,
    user_id             BIGINT       NOT NULL
);

ALTER TABLE apple_credentials
    ADD CONSTRAINT uq_apple_credentials_oauth_subject UNIQUE (oauth_subject),
    ADD CONSTRAINT uq_apple_credentials_user_id UNIQUE (user_id);

INSERT INTO apple_credentials (user_id, oauth_subject, apple_refresh_token)
SELECT id, oauth_subject, apple_refresh_token
FROM user
WHERE oauth_provider = 'apple'
  AND oauth_subject IS NOT NULL;

ALTER TABLE user
    DROP COLUMN oauth_subject,
    DROP COLUMN apple_refresh_token;