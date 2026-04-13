UPDATE user
SET has_schedule = TRUE
WHERE EXISTS (
    SELECT 1
    FROM schedule AS s
    WHERE s.user_id = user.id
);
