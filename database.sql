-- MySQL dump 10.13  Distrib 5.7.34, for Linux (x86_64)
--
-- Host: localhost    Database: smart_pool
-- ------------------------------------------------------
-- Server version	5.7.34-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `smart_pool`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `smart_pool` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `smart_pool`;

--
-- Table structure for table `chlorine_actuator`
--

DROP TABLE IF EXISTS `chlorine_actuator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chlorine_actuator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `signalVal` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chlorine_actuator`
--

LOCK TABLES `chlorine_actuator` WRITE;
/*!40000 ALTER TABLE `chlorine_actuator` DISABLE KEYS */;
INSERT INTO `chlorine_actuator` VALUES (1,'2021-07-16 14:58:17',0),(2,'2021-07-16 15:00:36',1),(3,'2021-07-16 15:03:16',0),(4,'2021-07-16 15:04:36',1),(5,'2021-07-16 15:06:36',0),(6,'2021-07-16 15:09:16',1),(7,'2021-07-16 15:11:56',0);
/*!40000 ALTER TABLE `chlorine_actuator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chlorine_levels`
--

DROP TABLE IF EXISTS `chlorine_levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chlorine_levels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `nodeId` varchar(10) NOT NULL,
  `value` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chlorine_levels`
--

LOCK TABLES `chlorine_levels` WRITE;
/*!40000 ALTER TABLE `chlorine_levels` DISABLE KEYS */;
INSERT INTO `chlorine_levels` VALUES (1,'2021-07-16 14:58:17','6345',2600),(2,'2021-07-16 14:58:56','6345',2100),(3,'2021-07-16 14:59:36','6345',1400),(4,'2021-07-16 15:00:16','6345',1100),(5,'2021-07-16 15:00:36','6345',500),(6,'2021-07-16 15:00:56','6345',1100),(7,'2021-07-16 15:01:36','6345',1200),(8,'2021-07-16 15:02:16','6345',1600),(9,'2021-07-16 15:02:36','6345',2300),(10,'2021-07-16 15:03:16','6345',2700),(11,'2021-07-16 15:03:36','6345',2500),(12,'2021-07-16 15:03:56','6345',1800),(13,'2021-07-16 15:04:16','6345',1100),(14,'2021-07-16 15:04:36','6345',600),(15,'2021-07-16 15:05:16','6345',900),(16,'2021-07-16 15:05:36','6345',1600),(17,'2021-07-16 15:05:56','6345',1900),(18,'2021-07-16 15:06:16','6345',2500),(19,'2021-07-16 15:06:36','6345',2900),(20,'2021-07-16 15:07:36','6345',2300),(21,'2021-07-16 15:07:56','6345',1800),(22,'2021-07-16 15:08:16','6345',1600),(23,'2021-07-16 15:08:36','6345',1000),(24,'2021-07-16 15:09:16','6345',800),(25,'2021-07-16 15:09:36','6345',900),(26,'2021-07-16 15:10:36','6345',1200),(27,'2021-07-16 15:10:56','6345',1300),(28,'2021-07-16 15:11:16','6345',2000),(29,'2021-07-16 15:11:36','6345',2500),(30,'2021-07-16 15:11:56','6345',2600),(31,'2021-07-16 15:12:16','6345',2000),(32,'2021-07-16 15:12:36','6345',1500);
/*!40000 ALTER TABLE `chlorine_levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hydromassage_actuator`
--

DROP TABLE IF EXISTS `hydromassage_actuator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hydromassage_actuator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `power` int(1) NOT NULL DEFAULT '5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hydromassage_actuator`
--

LOCK TABLES `hydromassage_actuator` WRITE;
/*!40000 ALTER TABLE `hydromassage_actuator` DISABLE KEYS */;
INSERT INTO `hydromassage_actuator` VALUES (2,'2021-07-16 14:59:38',0),(3,'2021-07-16 15:00:10',3),(4,'2021-07-16 15:00:38',0),(5,'2021-07-16 15:00:50',3),(6,'2021-07-16 15:01:12',0),(8,'2021-07-16 15:02:32',3),(11,'2021-07-16 15:04:57',0),(12,'2021-07-16 15:05:48',6),(13,'2021-07-16 15:05:58',0),(14,'2021-07-16 15:06:30',10),(16,'2021-07-16 15:08:09',0),(17,'2021-07-16 15:09:01',10),(21,'2021-07-16 15:12:02',8),(22,'2021-07-16 15:12:42',0);
/*!40000 ALTER TABLE `hydromassage_actuator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `presence_detection`
--

DROP TABLE IF EXISTS `presence_detection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `presence_detection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `presence` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presence_detection`
--

LOCK TABLES `presence_detection` WRITE;
/*!40000 ALTER TABLE `presence_detection` DISABLE KEYS */;
INSERT INTO `presence_detection` VALUES (1,'2021-07-16 14:58:36',0),(2,'2021-07-16 14:59:38',0),(3,'2021-07-16 15:00:10',1),(4,'2021-07-16 15:00:38',0),(5,'2021-07-16 15:00:50',1),(6,'2021-07-16 15:01:12',0),(7,'2021-07-16 15:02:14',0),(8,'2021-07-16 15:02:32',1),(9,'2021-07-16 15:02:53',0),(10,'2021-07-16 15:03:55',0),(11,'2021-07-16 15:04:57',0),(12,'2021-07-16 15:05:48',1),(13,'2021-07-16 15:05:58',0),(14,'2021-07-16 15:06:30',1),(15,'2021-07-16 15:07:06',0),(16,'2021-07-16 15:08:09',0),(17,'2021-07-16 15:09:01',1),(18,'2021-07-16 15:09:36',0),(19,'2021-07-16 15:10:38',0),(20,'2021-07-16 15:11:40',0),(21,'2021-07-16 15:12:02',1),(22,'2021-07-16 15:12:42',0);
/*!40000 ALTER TABLE `presence_detection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temperature_actuator`
--

DROP TABLE IF EXISTS `temperature_actuator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temperature_actuator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `signalVal` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temperature_actuator`
--

LOCK TABLES `temperature_actuator` WRITE;
/*!40000 ALTER TABLE `temperature_actuator` DISABLE KEYS */;
INSERT INTO `temperature_actuator` VALUES (1,'2021-07-16 14:59:17',0),(2,'2021-07-16 15:00:57',1),(3,'2021-07-16 15:02:57',0),(4,'2021-07-16 15:06:57',1),(5,'2021-07-16 15:08:37',0),(6,'2021-07-16 15:10:57',1);
/*!40000 ALTER TABLE `temperature_actuator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `water_temperature`
--

DROP TABLE IF EXISTS `water_temperature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `water_temperature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `nodeId` varchar(10) NOT NULL,
  `value` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `water_temperature`
--

LOCK TABLES `water_temperature` WRITE;
/*!40000 ALTER TABLE `water_temperature` DISABLE KEYS */;
INSERT INTO `water_temperature` VALUES (1,'2021-07-16 14:58:37','37005',28),(2,'2021-07-16 14:58:57','37005',29),(3,'2021-07-16 14:59:17','37005',30),(4,'2021-07-16 14:59:37','37005',27),(5,'2021-07-16 15:00:37','37005',24),(6,'2021-07-16 15:00:57','37005',21),(7,'2021-07-16 15:01:17','37005',23),(8,'2021-07-16 15:01:57','37005',24),(9,'2021-07-16 15:02:17','37005',27),(10,'2021-07-16 15:02:37','37005',29),(11,'2021-07-16 15:02:57','37005',30),(12,'2021-07-16 15:03:17','37005',28),(13,'2021-07-16 15:04:37','37005',25),(14,'2021-07-16 15:05:17','37005',23),(15,'2021-07-16 15:06:37','37005',22),(16,'2021-07-16 15:06:57','37005',20),(17,'2021-07-16 15:07:17','37005',21),(18,'2021-07-16 15:07:37','37005',23),(19,'2021-07-16 15:07:57','37005',25),(20,'2021-07-16 15:08:17','37005',28),(21,'2021-07-16 15:08:37','37005',30),(22,'2021-07-16 15:08:57','37005',28),(23,'2021-07-16 15:09:17','37005',26),(24,'2021-07-16 15:09:57','37005',25),(25,'2021-07-16 15:10:17','37005',22),(26,'2021-07-16 15:10:57','37005',20),(27,'2021-07-16 15:11:17','37005',23),(28,'2021-07-16 15:11:37','37005',26),(29,'2021-07-16 15:11:57','37005',27);
/*!40000 ALTER TABLE `water_temperature` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-16 11:12:50
