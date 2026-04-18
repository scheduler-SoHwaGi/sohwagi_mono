ALTER TABLE schedule ADD COLUMN schedule_date DATE NULL;

UPDATE schedule
SET schedule_date = STR_TO_DATE(CONCAT(year, '-', month, '-', day), '%Y-%m-%d');

ALTER TABLE schedule MODIFY COLUMN schedule_date DATE NOT NULL;
