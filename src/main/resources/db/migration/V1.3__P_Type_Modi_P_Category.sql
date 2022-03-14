CREATE TABLE IF NOT EXISTS m_p_typez
(
    id integer generated always as identity (maxvalue 99999999)primary key,
    name       varchar(75)       null,
    sel        INTEGER           null
);

DELETE FROM m_p_typez;
INSERT INTO m_p_typez (name, sel) VALUES ('SMEs Product Types', 0);
INSERT INTO m_p_typez (name, sel) VALUES ('Vendors Product Types', 0);

ALTER TABLE pcategoryz ADD COLUMN type INTEGER NULL;