CREATE TABLE BONUS
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
     tenantid       INTEGER,
     expirationtime BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE OPERATOR
  (
     id           INTEGER NOT NULL auto_increment,
     name         VARCHAR(32),
     creationtime BIGINT,
     updatetime   BIGINT,
     password     VARCHAR(32),
     tenantid     INTEGER,
     status       SMALLINT,
     PRIMARY KEY (id)
  );

CREATE TABLE PROMOTION
  (
     id                  INTEGER NOT NULL auto_increment,
     name                VARCHAR(64),
     creationtime        BIGINT,
     updatetime          BIGINT,
     description         VARCHAR(255),
     rule                LONGTEXT,
     status              SMALLINT,
     lastupdateoperator  INTEGER,
     bonusexpirationtime BIGINT,
     tenantid            INTEGER,
     starttime           BIGINT,
     endtime             BIGINT,
     maximum             BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE PROMOTIONBALANCE
  (
     id           INTEGER NOT NULL auto_increment,
     creationtime BIGINT,
     updatetime   BIGINT,
     promotionid  INTEGER,
     balance      BIGINT,
     maximum      BIGINT,
     reserved     BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE QUOTA
  (
     id             BIGINT NOT NULL auto_increment,
     creationtime   BIGINT,
     updatetime     BIGINT,
     userid         BIGINT,
     maximum        BIGINT,
     balance        BIGINT,
     activationtime BIGINT,
     expirationtime BIGINT,
     volumetype     SMALLINT,
     tenantid       INTEGER,
     username       VARCHAR(32),
     reserved       BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE TENANT
  (
     id           INTEGER NOT NULL auto_increment,
     name         VARCHAR(64),
     creationtime BIGINT,
     updatetime   BIGINT,
     description  VARCHAR(255),
     cycletype    INTEGER,
     PRIMARY KEY (id)
  );

CREATE TABLE USER
  (
     id           BIGINT NOT NULL auto_increment,
     name         VARCHAR(32),
     creationtime BIGINT,
     updatetime   BIGINT,
     tenantid     INTEGER,
     PRIMARY KEY (id)
  );

CREATE INDEX bonus_user_idx ON BONUS (userid);

CREATE INDEX promotion_tenant_idx ON PROMOTION (tenantid);

CREATE INDEX balance_promotion_idx ON PROMOTIONBALANCE (promotionid);

CREATE INDEX quota_user_idx ON QUOTA (userid);

CREATE INDEX quota_username_idx ON QUOTA (username);

CREATE INDEX user_name_idx ON USER (name); 
