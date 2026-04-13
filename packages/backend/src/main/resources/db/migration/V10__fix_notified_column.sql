UPDATE schedule
SET notified = 0
WHERE notified IS NULL;