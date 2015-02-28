CREATE TABLE BONUS
  (
     id             BIGINT NOT NULL auto_increment,
     userId         BIGINT,
     creationTime   BIGINT,
     updateTime     BIGINT,
     size           BIGINT,
     targetUserName VARCHAR(32),
     targetQuotaId  BIGINT,
     activationTime BIGINT,
     promotionId    INTEGER,
     volumeType     SMALLINT,
     tenantId       INTEGER,
     expirationTime BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE OPERATOR
  (
     id           INTEGER NOT NULL auto_increment,
     name         VARCHAR(32),
     creationTime BIGINT,
     updateTime   BIGINT,
     password     VARCHAR(40),
     tenantId     INTEGER,
     status       SMALLINT,
     description  VARCHAR(255),
     email        VARCHAR(32),
     phone        VARCHAR(32),
     PRIMARY KEY (id)
  );

CREATE TABLE PROMOTION
  (
     id                  INTEGER NOT NULL auto_increment,
     name                VARCHAR(64),
     creationTime        BIGINT,
     updateTime          BIGINT,
     description         VARCHAR(255),
     rule                LONGTEXT,
     status              SMALLINT,
     lastUpdateOperator  INTEGER,
     bonusExpirationTime BIGINT,
     tenantId            INTEGER,
     startTime           BIGINT,
     endTime             BIGINT,
     maximum             BIGINT,
     volumeType          SMALLINT,
     PRIMARY KEY (id)
  );

CREATE TABLE PROMOTIONBALANCE
  (
     id           INTEGER NOT NULL auto_increment,
     creationTime BIGINT,
     updateTime   BIGINT,
     promotionId  INTEGER,
     balance      BIGINT,
     maximum      BIGINT,
     reserved     BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE QUOTA
  (
     id             BIGINT NOT NULL auto_increment,
     creationTime   BIGINT,
     updateTime     BIGINT,
     userId         BIGINT,
     maximum        BIGINT,
     balance        BIGINT,
     activationTime BIGINT,
     expirationTime BIGINT,
     volumeType     SMALLINT,
     tenantId       INTEGER,
     userName       VARCHAR(32),
     reserved       BIGINT,
     PRIMARY KEY (id)
  );

CREATE TABLE TENANT
  (
     id           INTEGER NOT NULL auto_increment,
     name         VARCHAR(64),
     creationTime BIGINT,
     updateTime   BIGINT,
     description  VARCHAR(255),
     cycleType    INTEGER,
     PRIMARY KEY (id)
  );

CREATE TABLE USER
  (
     id           BIGINT NOT NULL auto_increment,
     name         VARCHAR(32),
     creationTime BIGINT,
     updateTime   BIGINT,
     tenantId     INTEGER,
     PRIMARY KEY (id)
  );

CREATE INDEX bonus_user_idx ON BONUS (userId);

CREATE INDEX bonus_tusername_idx ON BONUS (targetUserName);

CREATE INDEX operator_name_idx ON OPERATOR (name);

CREATE INDEX promotion_start_idx ON PROMOTION (startTime);

CREATE INDEX balance_promotion_idx ON PROMOTIONBALANCE (promotionId);

CREATE INDEX quota_user_idx ON QUOTA (userId);

CREATE INDEX quota_username_idx ON QUOTA (userName);

CREATE INDEX user_name_idx ON USER (name); 
