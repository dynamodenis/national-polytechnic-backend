ALTER TABLE rolez DROP COLUMN IF EXISTS admin;

ALTER TABLE rolez ADD COLUMN admin INTEGER null;