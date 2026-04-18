DELETE FROM token;

ALTER TABLE token
    ADD COLUMN user_id BIGINT NOT NULL,
    ADD COLUMN expires_at DATETIME NOT NULL,
    CHANGE COLUMN refresh_token refresh_token_hash VARCHAR(64) NOT NULL,
    DROP COLUMN is_expired;
