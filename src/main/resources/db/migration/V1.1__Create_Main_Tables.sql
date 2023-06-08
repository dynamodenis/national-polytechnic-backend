CREATE TABLE IF NOT EXISTS config
(
    id integer generated always as identity (maxvalue 99999999)primary key,
    compname   VARCHAR(255) null,
    mail       VARCHAR(50)  null,
    licence    VARCHAR(75)  null,
    agepsw    INTEGER      null,
    support    VARCHAR(255) null
);

DELETE FROM config;
INSERT INTO config (compname, mail, licence, agepsw, support) values ('Nyeri National Polytechnic- Supporting Innovation and Training in Vocational Education Sector( NNP-SITVES)',
                    'gracewangarimigwi@gmail.com', 'N/A', 0, 'MABAWA INNOVATIONS LTD');
CREATE TABLE IF NOT EXISTS rolez
(
    id         uuid not null primary key,
    name       varchar(45)          null ,
    descr      varchar(150)         null ,
    status     integer              null ,
    sel        integer              null,
    user_rolez json                 null
);

DELETE FROM rolez;
INSERT INTO rolez (id, descr, name, sel, status, user_rolez) values ('0d85902c-fba3-46fe-97a7-24e7d0d72dec',
                                                                            'Do not delete this role. Administrator is a system-generated role created during the account activation process.',
                                                                            'Administrator', 0, 1,
                                                                            '{"training_support": ["cancreate", "canview", "canedit"],
                                                                              "research_consultancy_innovation": ["cancreate", "canview", "canedit"],
                                                                              "services_marketplace": ["cancreate", "canview", "canedit"],
                                                                              "consultancy": ["cancreate", "canview", "canedit"],
                                                                              "users": ["cancreate", "canview", "canedit"],
                                                                              "dashboard": ["cancreate", "canview", "canedit"]}');
INSERT INTO rolez (id, descr, name, sel, status, user_rolez) values ('91124c48-d8ac-11eb-b8bc-0242ac130003', 'Supervisor Role for Sub-Admin role.',
                                                                            'Trainers', 0, 1,
                                                                            '{"training_support": ["cancreate", "canview", "canedit"],
                                                                              "research_consultancy_innovation": ["cancreate", "canview", "canedit"],
                                                                              "services_marketplace": ["cancreate", "canview", "canedit"],
                                                                              "consultancy": ["cancreate", "canview", "canedit"],
                                                                              "users": ["canview"],
                                                                              "dashboard": ["canview", "canedit"]}');
INSERT INTO rolez (id, descr, name, sel, status, user_rolez) values ('d25daf55-89d6-4e19-ba8b-824a988940c6', 'End User Roles.',
                                                                            'User 01', 0, 1,
                                                                            '{"training_support": ["canview"],
                                                                              "research_consultancy_innovation": ["canview"],
                                                                              "services_marketplace": ["canview"],
                                                                              "consultancy": ["canview"],
                                                                              "dashboard": ["canview"]}');

INSERT INTO rolez (id, descr, name, sel, status, user_rolez) values ('b854640e-3969-4989-a0bc-6ee513a9954c', 'Vendors Roles',
                                                                            'Vendors', 0, 1,
                                                                            '{"services_marketplace": ["cancreate", "canview", "canedit"],
                                                                              "dashboard": ["canview"]}');

INSERT INTO rolez (id, descr, name, sel, status, user_rolez) values ('a24b72bb-c58c-412f-9d9d-8df266b1c89a', 'SME Enterprises roles',
                                                                            'Enterprises', 0, 1,
                                                                            '{"training_support": ["canview"],
                                                                              "research_consultancy_innovation": ["canview"],
                                                                              "services_marketplace": ["cancreate", "canview", "canedit"],
                                                                              "consultancy": ["canview"],
                                                                              "dashboard": ["canview"]}');

CREATE TABLE IF NOT EXISTS user_typez
(
    id integer generated always as identity (maxvalue 99999999)primary key,
    descr  VARCHAR(75) null,
    global INTEGER     null ,
    active INTEGER     null ,
    sel    INTEGER     null
);

INSERT INTO user_typez (descr, global, active, sel) VALUES ('NNP-Users', 0, 1, 0);
INSERT INTO user_typez (descr, global, active, sel) VALUES ('SME Enterprises', 1, 1, 0);
INSERT INTO user_typez (descr, global, active, sel) VALUES ('Farmer', 1, 1, 0);

CREATE TABLE IF NOT EXISTS userz
(
    id       uuid not null primary key,
    name     varchar(75)          null ,
    mail     varchar(45)          null ,
    role     uuid                 null ,
    status   integer              null ,
    admin    integer              null ,
    sel      integer              null,
    password VARCHAR(255)         null,
    created  TIMESTAMP            null,
    type     INTEGER              null,
    phone    varchar(25)          null
);

DELETE FROM userz;
INSERT INTO userz (id, admin, mail, name, role, sel, status, password, created, type, phone) VALUES (
                'd0314b4e-6189-45c2-8f01-3aafb4a949b4', 99, 'info@mabawainnovations.com', 'Administrator', '0d85902c-fba3-46fe-97a7-24e7d0d72dec', 0, 1, 'vOmjGAoD5TvSIIDdxgDHFA==', '2022-02-02T18:47:15', 1, '254728040438');
INSERT INTO userz (id, admin, mail, name, role, sel, status, password, created, type, phone) VALUES (
                '1241d4ec-a4ea-43ee-9ba1-e545766f27fa', 99, 'george.njoroge@mabawainnovations.com','George ''Josh','0d85902c-fba3-46fe-97a7-24e7d0d72dec', 0, 1,'oK3v/Tld+UAswJ8Evw42rA==', '2022-02-02T18:47:35', 1, '254728040438');

CREATE TABLE IF NOT EXISTS psws
(
    id       serial PRIMARY KEY,
    userid   uuid         null,
    lastdate TIMESTAMP    null,
    psw1     varchar(150) null,
    psw2     varchar(150) null,
    psw3     varchar(150) null,
    psw4     varchar(150) null,
    psw5     varchar(150) null,
    lastpsw  INTEGER      null
);

CREATE TABLE IF NOT EXISTS smes
(
    id         uuid not null primary key,
    name       varchar(75)      null ,
    contact    varchar(75)      null ,
    address1   varchar(45)      null ,
    address2   varchar(45)      null ,
    address3   varchar(45)      null ,
    tel        varchar(75)      null ,
    mail       varchar(45)      null ,
    town       varchar(45)      null ,
    scounty    varchar(75)         null ,
    suspend    integer          null ,
    sel        integer          null ,
    location   point            null ,
    created    timestamp        null
);

CREATE TABLE IF NOT EXISTS vendors
(
    id         uuid not null primary key,
    name       varchar(75)      null ,
    contact    varchar(75)      null ,
    address1   varchar(45)      null ,
    address2   varchar(45)      null ,
    address3   varchar(45)      null ,
    tel        varchar(75)      null ,
    mail       varchar(45)      null ,
    town       varchar(45)      null ,
    suspend    integer          null ,
    sel        integer          null ,
    created    timestamp        null
);

CREATE TABLE IF NOT EXISTS pcategoryz
(
    id        uuid not null primary key,
    sel       integer       null ,
    image_url varchar(255)  null ,
    init_dte  timestamp     null ,
    name      varchar(255)  null ,
    sub       uuid          null
);

CREATE TABLE IF NOT EXISTS products
(
    id          uuid    not null primary key,
    avwcost     decimal(13,2)   null ,
    sel         integer         null ,
    category    uuid            null ,
    created     timestamp       null ,
    del         integer         null ,
    description text            null ,
    frezze      integer         null ,
    image_url   text            null ,
    levy_1      varchar(255)    null ,
    name        varchar(255)    null ,
    pack_1      decimal(13,2)   null ,
    price_1     decimal(13,2)   null ,
    supplier    uuid            null ,
    type        integer         null ,
    units_1     varchar(255)    null ,
    vat_perc    decimal(13,2)   null ,
    weight      decimal(13,2)   null ,
    service     integer         null
);

CREATE TABLE IF NOT EXISTS consultants
(
    id        uuid not null  primary key,
    name      varchar(255)   null,
    userid    uuid           null,
    pdescr    text           null,
    expertise text           null,
    projects  TEXT           null
);

CREATE TABLE IF NOT EXISTS tcategoryz
(
    id        uuid not null primary key,
    sel       integer       null ,
    init_dte  timestamp     null ,
    name      varchar(255)  null
);

CREATE TABLE IF NOT EXISTS trainings
(
    id          uuid not null primary key,
    category    uuid          null,
    trainers    json          null,
    duration    integer       null,
    description text          null,
    sel         integer       null
);

CREATE TABLE IF NOT EXISTS rcategoryz
(
    id        uuid not null primary key,
    sel       integer       null ,
    init_dte  timestamp     null ,
    name      varchar(255)  null
);

CREATE TABLE IF NOT EXISTS research
(
    id          uuid not null primary key,
    category    uuid          null,
    trainers    json          null,
    description text          null,
    sel         integer       null
);

CREATE TABLE IF NOT EXISTS user_trainings
(
    id integer generated always as identity (maxvalue 99999999)primary key,
    userid     uuid           null,
    tid        uuid           null
);

