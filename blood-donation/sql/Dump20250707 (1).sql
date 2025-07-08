CREATE DATABASE  IF NOT EXISTS `blood_donation_support` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `blood_donation_support`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: blood_donation_support
-- ------------------------------------------------------
-- Server version	8.0.42


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blogs`
--

DROP TABLE IF EXISTS `blogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blogs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `author_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `blogs_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blogs`
--

LOCK TABLES `blogs` WRITE;
/*!40000 ALTER TABLE `blogs` DISABLE KEYS */;
INSERT INTO `blogs` VALUES (1,'Lợi ích của hiến máu','Hiến máu không chỉ cứu người mà còn tốt cho sức khỏe.',NULL,'2025-07-07 03:32:29','2025-07-07 03:32:29',NULL),(2,'Kinh nghiệm đi hiến máu lần đầu','Bạn cần ăn nhẹ, uống đủ nước và nghỉ ngơi sau khi hiến máu.',NULL,'2025-07-07 03:32:29','2025-07-07 03:32:29',NULL),(3,'Cảm nhận sau khi hiến máu cứu người','Một cảm giác thật tuyệt vời khi biết giọt máu của mình đã giúp đỡ được ai đó.','PUBLISHED','2025-07-07 04:38:55','2025-07-07 04:38:55',NULL),(4,'Lần đầu tiên tôi đi hiến máu','Nội dung chi tiết về trải nghiệm lần đầu đi hiến máu...','PUBLISHED','2025-07-07 04:38:56','2025-07-07 04:38:56',NULL),(5,'Những điều cần biết trước khi hiến máu','Chế độ ăn uống, nghỉ ngơi hợp lý là rất quan trọng...','PUBLISHED','2025-07-07 04:38:56','2025-07-07 04:38:56',NULL),(6,'Cảm nhận sau khi hiến máu cứu người','Một cảm giác thật tuyệt vời khi biết giọt máu của mình đã giúp đỡ được ai đó.','PUBLISHED','2025-07-07 04:38:56','2025-07-07 04:38:56',NULL);
/*!40000 ALTER TABLE `blogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_compatibility_rules`
--

DROP TABLE IF EXISTS `blood_compatibility_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_compatibility_rules` (
  `id` int NOT NULL AUTO_INCREMENT,
  `donor_blood_type_id` int NOT NULL,
  `recipient_blood_type_id` int NOT NULL,
  `component` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_rule` (`donor_blood_type_id`,`recipient_blood_type_id`,`component`),
  KEY `recipient_blood_type_id` (`recipient_blood_type_id`),
  CONSTRAINT `blood_compatibility_rules_ibfk_1` FOREIGN KEY (`donor_blood_type_id`) REFERENCES `blood_types` (`id`) ON DELETE CASCADE,
  CONSTRAINT `blood_compatibility_rules_ibfk_2` FOREIGN KEY (`recipient_blood_type_id`) REFERENCES `blood_types` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_compatibility_rules`
--

LOCK TABLES `blood_compatibility_rules` WRITE;
/*!40000 ALTER TABLE `blood_compatibility_rules` DISABLE KEYS */;
INSERT INTO `blood_compatibility_rules` VALUES (52,1,1,'PLASMA'),(96,1,1,'PLATELETS'),(25,1,1,'RED_CELLS'),(88,1,1,'WHOLE_BLOOD'),(70,1,2,'PLASMA'),(97,1,2,'PLATELETS'),(26,1,2,'RED_CELLS'),(98,1,3,'PLATELETS'),(27,1,3,'RED_CELLS'),(99,1,4,'PLATELETS'),(28,1,4,'RED_CELLS'),(100,1,5,'PLATELETS'),(29,1,5,'RED_CELLS'),(101,1,6,'PLATELETS'),(30,1,6,'RED_CELLS'),(102,1,7,'PLATELETS'),(31,1,7,'RED_CELLS'),(103,1,8,'PLATELETS'),(32,1,8,'RED_CELLS'),(53,2,1,'PLASMA'),(71,2,2,'PLASMA'),(104,2,2,'PLATELETS'),(33,2,2,'RED_CELLS'),(89,2,2,'WHOLE_BLOOD'),(105,2,4,'PLATELETS'),(34,2,4,'RED_CELLS'),(106,2,6,'PLATELETS'),(35,2,6,'RED_CELLS'),(107,2,8,'PLATELETS'),(36,2,8,'RED_CELLS'),(54,3,1,'PLASMA'),(72,3,2,'PLASMA'),(60,3,3,'PLASMA'),(108,3,3,'PLATELETS'),(37,3,3,'RED_CELLS'),(90,3,3,'WHOLE_BLOOD'),(78,3,4,'PLASMA'),(109,3,4,'PLATELETS'),(38,3,4,'RED_CELLS'),(110,3,7,'PLATELETS'),(39,3,7,'RED_CELLS'),(111,3,8,'PLATELETS'),(40,3,8,'RED_CELLS'),(55,4,1,'PLASMA'),(73,4,2,'PLASMA'),(61,4,3,'PLASMA'),(79,4,4,'PLASMA'),(112,4,4,'PLATELETS'),(41,4,4,'RED_CELLS'),(91,4,4,'WHOLE_BLOOD'),(113,4,8,'PLATELETS'),(42,4,8,'RED_CELLS'),(56,5,1,'PLASMA'),(74,5,2,'PLASMA'),(64,5,5,'PLASMA'),(114,5,5,'PLATELETS'),(43,5,5,'RED_CELLS'),(92,5,5,'WHOLE_BLOOD'),(82,5,6,'PLASMA'),(115,5,6,'PLATELETS'),(44,5,6,'RED_CELLS'),(116,5,7,'PLATELETS'),(45,5,7,'RED_CELLS'),(117,5,8,'PLATELETS'),(46,5,8,'RED_CELLS'),(57,6,1,'PLASMA'),(75,6,2,'PLASMA'),(65,6,5,'PLASMA'),(83,6,6,'PLASMA'),(118,6,6,'PLATELETS'),(47,6,6,'RED_CELLS'),(93,6,6,'WHOLE_BLOOD'),(119,6,8,'PLATELETS'),(48,6,8,'RED_CELLS'),(58,7,1,'PLASMA'),(76,7,2,'PLASMA'),(62,7,3,'PLASMA'),(80,7,4,'PLASMA'),(66,7,5,'PLASMA'),(84,7,6,'PLASMA'),(68,7,7,'PLASMA'),(120,7,7,'PLATELETS'),(49,7,7,'RED_CELLS'),(94,7,7,'WHOLE_BLOOD'),(86,7,8,'PLASMA'),(121,7,8,'PLATELETS'),(50,7,8,'RED_CELLS'),(59,8,1,'PLASMA'),(77,8,2,'PLASMA'),(63,8,3,'PLASMA'),(81,8,4,'PLASMA'),(67,8,5,'PLASMA'),(85,8,6,'PLASMA'),(69,8,7,'PLASMA'),(87,8,8,'PLASMA'),(122,8,8,'PLATELETS'),(51,8,8,'RED_CELLS'),(95,8,8,'WHOLE_BLOOD');
/*!40000 ALTER TABLE `blood_compatibility_rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_types`
--

DROP TABLE IF EXISTS `blood_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `blood_group` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rh_factor` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_blood_type` (`blood_group`,`rh_factor`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_types`
--

LOCK TABLES `blood_types` WRITE;
/*!40000 ALTER TABLE `blood_types` DISABLE KEYS */;
INSERT INTO `blood_types` VALUES (3,'A','NEGATIVE'),(4,'A','POSITIVE'),(7,'AB','NEGATIVE'),(8,'AB','POSITIVE'),(5,'B','NEGATIVE'),(6,'B','POSITIVE'),(1,'O','NEGATIVE'),(2,'O','POSITIVE');
/*!40000 ALTER TABLE `blood_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_units`
--

DROP TABLE IF EXISTS `blood_units`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_units` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `blood_type_id` int NOT NULL,
  `medical_center_id` int NOT NULL,
  `expiry_date` date DEFAULT NULL,
  `received_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `blood_type_id` (`blood_type_id`),
  KEY `medical_center_id` (`medical_center_id`),
  CONSTRAINT `blood_units_ibfk_1` FOREIGN KEY (`blood_type_id`) REFERENCES `blood_types` (`id`),
  CONSTRAINT `blood_units_ibfk_2` FOREIGN KEY (`medical_center_id`) REFERENCES `medical_centers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_units`
--

LOCK TABLES `blood_units` WRITE;
/*!40000 ALTER TABLE `blood_units` DISABLE KEYS */;
/*!40000 ALTER TABLE `blood_units` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donation_registrations`
--

DROP TABLE IF EXISTS `donation_registrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donation_registrations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `blood_type_id` int NOT NULL,
  `address` text COLLATE utf8mb4_unicode_ci,
  `latitude` decimal(10,8) DEFAULT NULL,
  `longitude` decimal(11,8) DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `province` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `available_date` date NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `completed_at` datetime DEFAULT NULL,
  `next_eligible_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `blood_type_id` (`blood_type_id`),
  CONSTRAINT `donation_registrations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `donation_registrations_ibfk_2` FOREIGN KEY (`blood_type_id`) REFERENCES `blood_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donation_registrations`
--

LOCK TABLES `donation_registrations` WRITE;
/*!40000 ALTER TABLE `donation_registrations` DISABLE KEYS */;
/*!40000 ALTER TABLE `donation_registrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emergency_requests`
--

DROP TABLE IF EXISTS `emergency_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_requests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `requester_user_id` int NOT NULL,
  `patient_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `blood_type_id` int NOT NULL,
  `quantity_needed` int NOT NULL,
  `address` text COLLATE utf8mb4_unicode_ci,
  `latitude` decimal(10,8) DEFAULT NULL,
  `longitude` decimal(11,8) DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reason` text COLLATE utf8mb4_unicode_ci,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `requester_user_id` (`requester_user_id`),
  KEY `blood_type_id` (`blood_type_id`),
  CONSTRAINT `emergency_requests_ibfk_1` FOREIGN KEY (`requester_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `emergency_requests_ibfk_2` FOREIGN KEY (`blood_type_id`) REFERENCES `blood_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_requests`
--

LOCK TABLES `emergency_requests` WRITE;
/*!40000 ALTER TABLE `emergency_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_centers`
--

DROP TABLE IF EXISTS `medical_centers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_centers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `latitude` decimal(38,2) DEFAULT NULL,
  `longitude` decimal(38,2) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_centers`
--

LOCK TABLES `medical_centers` WRITE;
/*!40000 ALTER TABLE `medical_centers` DISABLE KEYS */;
INSERT INTO `medical_centers` VALUES (1,'Trung Tâm Hiến Máu Quốc Gia','Số 1, Phố Tràng Thi, Hoàn Kiếm, Hà Nội',NULL,NULL,'Là đơn vị đầu ngành về huyết học và truyền máu, có sứ mệnh đảm bảo an toàn truyền máu và cung cấp máu cho cả nước.');
/*!40000 ALTER TABLE `medical_centers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `national_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `blood_type_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `FKb2uxwxbut1bsd70hlwk311j3o` (`blood_type_id`),
  CONSTRAINT `FKb2uxwxbut1bsd70hlwk311j3o` FOREIGN KEY (`blood_type_id`) REFERENCES `blood_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (8,'Trần Hoàng Quân','quan1109','quantranhoang24@gmail.com','$2a$10$I/5/o5tt3gKXUmDOsEsiaem04AlMLhe7SrO7ObZYb5539zuVcZnqK',NULL,'MEMBER','2025-07-06 22:27:26',NULL),(9,'Lâm Hoàng Tuấn','admin','lamhoangtuan25@gmai.com','$2a$10$sMcf2mTLu0JbY0T2HAGeuOxcFTIGEb1nozE9836wPA9XpM9nt6Swa',NULL,'ADMIN','2025-07-07 06:29:21',NULL),(10,'Nguyễn Văn A','staff1','a@a.a','$2a$10$qJSxZdbhEUGZxGHIzBhrOebqSlgmBjW/MFasqKIUu4zFLeonRPqq6',NULL,'STAFF','2025-07-07 07:05:26',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-07 14:08:17
