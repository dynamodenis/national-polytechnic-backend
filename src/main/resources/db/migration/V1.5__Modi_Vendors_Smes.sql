ALTER TABLE smes DROP COLUMN IF EXISTS address2;
ALTER TABLE smes DROP COLUMN IF EXISTS address3;

ALTER TABLE smes ADD COLUMN descr varchar(255) null;

ALTER TABLE vendors ADD COLUMN descr varchar(255) null;