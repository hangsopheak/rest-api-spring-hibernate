DROP DATABASE if EXISTS sample;
CREATE DATABASE sample;
USE sample;
DROP TABLE IF EXISTS `Person`;
CREATE TABLE Person (
 id BIGINT(20) NOT NULL AUTO_INCREMENT,
 name VARCHAR(200), 
 country VARCHAR(200),
 version BIGINT(20),
 createdDate timestamp default CURRENT_TIMESTAMP,
 updatedDate date  NULL,
 PRIMARY KEY (`id`) USING BTREE
 ) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

 INSERT INTO Person (id, name, country) values(1,'sophea', 'Cambodia');
  INSERT INTO Person (id, name, country) values(2,'somnang', 'Cambodia');

DROP TABLE IF EXISTS `stock`;
CREATE TABLE  `stock` (
  `STOCK_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `STOCK_CODE` varchar(100) NOT NULL,
  `STOCK_NAME` varchar(200) NOT NULL,
  PRIMARY KEY (`STOCK_ID`) USING BTREE,
  UNIQUE KEY `UNI_STOCK_NAME` (`STOCK_NAME`),
  UNIQUE KEY `UNI_STOCK_CODE` (`STOCK_CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `stock_detail`;
CREATE TABLE  `stock_detail` (
 `STOCK_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `COMP_NAME` varchar(100) NOT NULL,
 `COMP_DESC` varchar(255) NOT NULL,
 `REMARK` varchar(255) NOT NULL,
 `LISTED_DATE` date NOT NULL,
 PRIMARY KEY (`STOCK_ID`) USING BTREE,
 CONSTRAINT `FK_STOCK_ID` FOREIGN KEY (`STOCK_ID`) REFERENCES `stock` (`STOCK_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `type` varchar(256) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `logoUrl` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;