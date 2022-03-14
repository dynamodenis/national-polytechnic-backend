ALTER TABLE smes DROP COLUMN location;

ALTER TABLE smes ADD COLUMN location bytea null;