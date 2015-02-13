 CREATE TABLE bonus
  (
     id             BIGINT NOT NULL auto_increment,
     userid         BIGINT,
     creationtime   BIGINT,
     updatetime     BIGINT,
     size           BIGINT,
     targetuserid   BIGINT,
     targetquotaid  BIGINT,
     activationtime BIGINT,
     promotionid    INTEGER,
     volumetype     SMALLINT,
     PRIMARY KEY (id)
  );

CREATE TABLE operator
  (
     id           INTEGER NOT NULL auto_increment,
     name         VARCHAR(32),
     creationtime BIGINT,
     updatetime   BIGINT,
     password     VARCHAR(255),
     tenantid     INTEGER,
     status       SMALLINT,
     PRIMARY KEY (id)
  );

CREATE TABLE promotion
  (
     id                  INTEGER NOT NULL auto_increment,
     name                VARCHAR(64),
     creationtime        BIGINT,
     updatetime          BIGINT,
     description         VARCHAR(255),
     rule                VARCHAR(255),
     status              SMALLINT,
     lastupdateoperator  INTEGER,
     bonusexpirationtime BIGINT,
     tenantid            INTEGER,
     PRIMARY KEY (id)
  );

CREATE TABLE promotionbalance
  (
     id           INTEGER NOT NULL auto_increment,
     creationtime BIGINT,
     updatetime   BIGINT,
     promotionid  INTEGER,
     balance      BIGINT,
     max          BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE quota
  (
     id             BIGINT NOT NULL auto_increment,
     creationtime   BIGINT,
     updatetime     BIGINT,
     userid         BIGINT,
     maximum        BIGINT,
     balance        BIGINT,
     reserved       BIGINT,     
     activationtime BIGINT,
     expirationtime BIGINT,
     volumetype     SMALLINT,
     PRIMARY KEY (id)
  );

CREATE TABLE tenant
  (
     id           INTEGER NOT NULL auto_increment,
     name         VARCHAR(64),
     creationtime BIGINT,
     updatetime   BIGINT,
     description  VARCHAR(255),
     cycletype    INTEGER,
     PRIMARY KEY (id)
  );

CREATE TABLE user
  (
     id           BIGINT NOT NULL auto_increment,
     name         VARCHAR(32),
     creationtime BIGINT,
     updatetime   BIGINT,
     tenantid     INTEGER,
     PRIMARY KEY (id)
  );

CREATE INDEX bonus_user_idx ON bonus (userid);

CREATE INDEX promotion_tenant_idx ON promotion (tenantid);

CREATE INDEX balance_promotion_idx ON promotionbalance (promotionid);

CREATE INDEX quota_user_idx ON quota (userid);

CREATE INDEX user_name_idx ON user (name, tenantid);  