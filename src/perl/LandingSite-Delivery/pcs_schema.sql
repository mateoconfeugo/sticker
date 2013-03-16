DROP DATABASE pcs;
CREATE DATABASE pcs;
USE pcs;

DROP TABLE IF EXISTS offer;
CREATE TABLE offer (
id          INTEGER AUTO_INCREMENT PRIMARY KEY,
created DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
status ENUM('active', 'inactive', 'suspended', 'expired'),
description TEXT,
name VARCHAR(25)
);

DROP TABLE IF EXISTS lead_log;
CREATE TABLE lead_log (
id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
event_time  DATETIME, 
email TEXT,
first_name    TEXT,
last_name     TEXT,
postal_code   INT(20),
offer  INT(15),
phone INT(20),
adnetwork INT(11),
adgroup INT(11),
listing INT(15),
profile INT(10),
campaign INT(11),
market_vector INT(11),
landing_site INT(15),
user_agent TEXT
);

DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `log_id` INT(11) NOT NULL AUTO_INCREMENT,
  `log_source` VARCHAR(100) NOT NULL DEFAULT '',
  `log_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `log_level` VARCHAR(10) NOT NULL DEFAULT '',
  `log_mesg` VARCHAR(200) NOT NULL DEFAULT '',
  PRIMARY KEY  (`log_id`),
  KEY `log_date_idx` (`log_date`),
  KEY `log_source_idx` (`log_source`),
  KEY `log_mesg_idx` (`log_mesg`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

ALTER TABLE lead_log AUTO_INCREMENT = 1;
ALTER TABLE log AUTO_INCREMENT = 1;        
