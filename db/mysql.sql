create table Bonus 
(
id bigint not null auto_increment,
userId bigint,
creationTime bigint,
updateTime bigint,
size bigint,
targetUserId bigint,
targetQuotaId bigint,
activationTime bigint,
promotionId integer,
volumeType smallint,
primary key (id)
)

create table Operator 
(
id integer not null auto_increment,
name varchar(255),
creationTime bigint,
updateTime bigint,
password varchar(255),
tenantId integer,
status smallint,
primary key (id)
)

create table Promotion 
(
id integer not null auto_increment,
name varchar(255),
creationTime bigint,
updateTime bigint,
description varchar(255),
rule varchar(255),
status smallint,
lastUpdateOperator integer,
primary key (id)
)

create table PromotionBalance 
(
id integer not null auto_increment,
creationTime bigint,
updateTime bigint,
promotionId integer,
balance bigint,
max bigint,
primary key (id)
)

create table Quota 
(
id bigint not null auto_increment,
creationTime bigint,
updateTime bigint,
userId bigint,
max bigint,
balance bigint,
activationTime bigint,
expirationTime bigint,
volumeType smallint,
primary key (id)
)

create table Tenant 
(
id integer not null auto_increment,
name varchar(255),
creationTime bigint,
updateTime bigint,
description varchar(255),
cycleType integer,
primary key (id)
)

create table User 
(
id bigint not null auto_increment,
name varchar(255),
creationTime bigint,
updateTime bigint,
primary key (id)
)
