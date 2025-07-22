-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: Project
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `Project`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `Project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `Project`;

--
-- Table structure for table `auth`
--

DROP TABLE IF EXISTS `auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth` (
  `token` varchar(255) DEFAULT NULL,
  `id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth`
--

LOCK TABLES `auth` WRITE;
/*!40000 ALTER TABLE `auth` DISABLE KEYS */;
INSERT INTO `auth` VALUES ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEaW5lc2giLCJ1c2VySWQiOjIsImlhdCI6MTczMzQ1OTkyNiwiZXhwIjoxNzMzNDYzNTI2fQ.BnBVuMUT_JkiIPVKCnhdp_UCQnKXtOvLZUhlrzQvFxI',2),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYW5qYXkxIiwidXNlcklkIjozLCJpYXQiOjE3MzM3MjExNzksImV4cCI6MTczMzcyNDc3OX0.AEjAUAdV6FvM8bJGhn710fZJgVbzofKd7nr886V08CQ',3),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYWdhcjEiLCJ1c2VySWQiOjQsImlhdCI6MTczMzgzMzc0MCwiZXhwIjoxNzMzODM3MzQwfQ.5XE33sIskbpJSmuQ3_G4RRLH8HF4taN4J2kXiGu1Fkw',4),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYWdhcjIiLCJ1c2VySWQiOjUsImlhdCI6MTczMzgzMzg2NywiZXhwIjoxNzMzODM3NDY3fQ.v5n_ybqWD77gitAAHQLmKb6FIp2rPzLJe8osMR2EYZc',5),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEaW5lc2gxIiwidXNlcklkIjo2LCJpYXQiOjE3MzM4MzM5MzQsImV4cCI6MTczMzgzNzUzNH0.Sb3ONsTmDkXkJnqLOnEwAy_pRFF2e80_rMyIo7205uY',6),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEaW5lc2gyIiwidXNlcklkIjo3LCJpYXQiOjE3MzM4MzQxMTcsImV4cCI6MTczMzgzNzcxN30.RJleoIOMEY336oPCp2XVInYtd90wscCnEXgtYIuPpJM',7),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYW5qYXkyMSIsInVzZXJJZCI6OCwiaWF0IjoxNzMzODkyNDQ1LCJleHAiOjE3MzM4OTYwNDV9.VRPGxdLYq2kyJs1tEp0GXu6cm8tACBsSuy4t1JOrXhs',8),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNZWNrc29uMSIsInVzZXJJZCI6MTAsImlhdCI6MTczMzk4Njg3NCwiZXhwIjoxNzMzOTkwNDc0fQ.TWg5W-lG7hU-F8aS4t7t2B4wzbV_p92JwDdG6nIT83w',10),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNZWNrc29uIiwidXNlcklkIjo5LCJpYXQiOjE3MzM5ODcxMzgsImV4cCI6MTczMzk5MDczOH0.K50IyLsa_BpyxfwIBR0MRfVZySAsiI3usZsDRbkkcjE',9),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQcmF2ZWVuIiwidXNlcklkIjoxMSwiaWF0IjoxNzM0MzQzMTExLCJleHAiOjE3MzQzNDY3MTF9.HhlGUeLQG4ovNd7V43pbRr-ffCl0C7atpMGsQFC1gPA',11),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJSb2hpdGgiLCJ1c2VySWQiOjEyLCJpYXQiOjE3MzQ0MTYzNDYsImV4cCI6MTczNDQxOTk0Nn0.D5nPptOzAnxIHrrHF1uFkv01H95ovplJLtrmvUiUDM0',12),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEaW5lc2gxMCIsInVzZXJJZCI6MTMsImlhdCI6MTczNDQxNjgxMiwiZXhwIjoxNzM0NDIwNDEyfQ.IyNg-5CUBJxWS4N3Wl6aquOEaqBd30Fb57MKUvgZdE4',13),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYW5qYXkxMCIsInVzZXJJZCI6MTQsImlhdCI6MTczNDQyMDE5NywiZXhwIjoxNzM0NDIzNzk3fQ.fx_ocAclcZ8V2E2PzaXKxgQpwEBYGnxcueo9OEpi7zc',14),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYW5qYXkyMCIsImlhdCI6MTczODkwNDc4OSwiZXhwIjoxNzM4OTkxMTg5fQ.p5qQSHlLBKgd9S0i92yH-cW5gPjiIFM6ckJ9WkGvMEQ',17),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYW5qYXkiLCJpYXQiOjE3NTIyMTYxNjAsImV4cCI6MTc1MjIxNjE5MH0.iGhgzuK_MngQ93voPM2MRc1a7iDn7ihz-IEqA_bIBcY',1);
/*!40000 ALTER TABLE `auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (7,1,'Bike','Travel',2),(12,2,'Car','Travel',2),(16,2,'Bike','Travel',1),(27,1,'Bike','Travel',4),(28,1,'Bike','Travel',7),(29,2,'Car','Travel',7),(30,1,'Petrol','Travel',8),(31,3,'Gym','Fitness',1),(33,5,'Groceries','Variable',1),(34,6,'Electricity Bill','Fixed',1),(35,7,'Restaurant Dinner','Variable',1),(36,8,'Internet Subscription','Fixed',1),(41,1,'Bike','Travel',9),(42,1,'Dinner','Food',11),(44,1,'Car','Travel',12),(45,2,'bike','Ride',12),(46,1,'Bike','Travel',17);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expenses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
INSERT INTO `expenses` VALUES (7,1000,1,'2024-12-02','Petrol Expense',2),(12,2000,2,'2024-12-02','Diesel Expense for Today',2),(16,1200,2,'2024-12-31','Petrol Expense Today',1),(27,1000,1,'2024-12-02','Petrol Expense for Today',4),(28,1000,1,'2024-12-30','Petrol Expense for Today',7),(29,2000,2,'2024-12-31','Diesel Expense for Today',7),(30,2000,1,'2024-12-31','Bike',8),(31,1500,3,'2024-12-31','Monthly Membership',1),(33,2000,5,'2024-12-30','Costs for food and household items purchased regularly.',1),(34,2000,6,'2024-12-31','Insurance for a vehicle, paid monthly or annually.',1),(35,999,7,'2024-12-24','Cost of eating out at a restaurant, including food, drinks, and tips.',1),(36,2500,8,'2024-12-23','Monthly payment for home or office internet service.',1),(41,1200,1,'2024-12-23','Petrol',9),(42,1000,1,'2024-12-30','Night Dinner With Teammates',11),(44,100,1,'2024-12-04','Petrol',12),(45,200,2,'2024-12-03','Petrol',12),(46,2100,1,'2025-02-08','Petrol Expense for Today',17);
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income`
--

DROP TABLE IF EXISTS `income`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income` (
  `user_id` int NOT NULL,
  `income` double DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income`
--

LOCK TABLES `income` WRITE;
/*!40000 ALTER TABLE `income` DISABLE KEYS */;
INSERT INTO `income` VALUES (1,30000),(2,30010),(4,30000),(5,30000),(6,30001),(7,20000),(8,20000),(9,20000),(10,30000),(11,40000),(12,10000),(17,25000);
/*!40000 ALTER TABLE `income` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login` (
  `id` int NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` VALUES (1,'UGFzc3dvcmRAMjEzMw==','Sanjay'),(2,'UGFzc3dvcmRAMjEzMw==','Dinesh'),(3,'U2FuamF5QDIxMzM=','Sanjay1'),(4,'UGFzc3dvcmRAMg==','Sagar1'),(5,'UGFzc3dvcmRAMg==','Sagar2'),(6,'UGFzc3dvcmRAMg==','Dinesh1'),(7,'UGFzc3dvcmRAMg==','Dinesh2'),(8,'UGFzc3dvcmRAMg==','Sanjay21'),(9,'UGFzc3dvcmRAMg==','Meckson'),(10,'UEFzc3dvcmRAMg==','Meckson1'),(11,'UGFzc3dvcmRAMg==','Praveen'),(12,'UGFzc3dvcmRAMg==','Rohith'),(13,'UGFzc3dvcmRAMg==','Dinesh10'),(14,'UGFzc3dvcmRAMg==','Sanjay10'),(15,'UGFzc3dvcmRAMjEzMw==','Sanjay22'),(16,'UGFzc3dvcmRAMjEzMw==','Sanjay11'),(17,'UGFzc3dvcmRAMjEzMw==','Sanjay20');
/*!40000 ALTER TABLE `login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `photo`
--

DROP TABLE IF EXISTS `photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `photo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `file_size` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `upload_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `photo`
--

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `photo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-18 15:54:46
