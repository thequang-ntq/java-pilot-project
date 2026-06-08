-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: pilot_project_db
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `account_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL DEFAULT 'USER',
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `uq_account_account_name` (`account_name`),
  UNIQUE KEY `uq_account_email` (`email`),
  KEY `idx_account_account_name` (`account_name`),
  CONSTRAINT `chk_account_email_format` CHECK (((`email` is null) or regexp_like(`email`,_utf8mb4'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$')))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin1','123','ADMIN',NULL),(2,'admin2','123','ADMIN',NULL),(3,'admin3','123','ADMIN',NULL),(4,'user1','123','USER',NULL),(5,'user2','123','USER',NULL);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
  `brand_id` int NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(50) NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`brand_id`),
  UNIQUE KEY `uq_brand_brand_name` (`brand_name`),
  KEY `idx_brand_brand_name` (`brand_name`),
  KEY `idx_brand_is_deleted` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brand`
--

LOCK TABLES `brand` WRITE;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` VALUES (1,'Apple','images/brand/20221124-1506-94dbck.png','Apple Inc,  California',0),(2,'Samsung','images/brand/20221124-1505-m6nrn9.webp','Samsung Inc,  Korea',0),(3,'Oppo','images/brand/20221124-1505-m0iss6.png','Oppo Inc,  China',0),(4,'LG','images/brand/20221124-1505-vgj6rp.png','LG Inc,  Japan',0),(5,'Xiaomi','images/brand/20221124-1504-ep3pdt.png','Xiaomi Inc,  China',0),(6,'Sony','images/brand/20221124-1504-nak19v.png','Sony Inc,  Japan',0),(7,'Huawei','images/brand/20221124-1503-36khjb.png','Huawei made in China',0),(8,'Vivo','images/brand/20221124-1503-9dc89b.png','Vivo Inc,  China',0),(9,'HTC','images/brand/20221124-1502-d0h72d.png','HTC Inc,  California',0),(10,'Asus','images/brand/20221124-1501-73qa6b.svg','Asus Inc,  China',0),(11,'Realme','images/brand/20221124-1501-12e3hf.png','Realme Inc,  China',0);
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `account_id` int NOT NULL,
  `order_time` datetime DEFAULT NULL,
  `finish_time` datetime DEFAULT NULL,
  `status` enum('pre-order','new','in progress','completed','failed') NOT NULL DEFAULT 'pre-order',
  PRIMARY KEY (`order_id`),
  KEY `idx_order_account_id` (`account_id`),
  KEY `idx_order_status` (`status`),
  KEY `idx_order_order_time` (`order_time`),
  KEY `idx_order_finish_time` (`finish_time`),
  CONSTRAINT `fk_order_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,4,NULL,NULL,'pre-order'),(2,4,'2026-01-15 09:00:00',NULL,'new'),(3,4,'2026-02-01 10:15:00',NULL,'in progress'),(4,5,'2026-02-20 14:00:00','2026-02-22 16:00:00','completed'),(5,5,'2026-03-05 11:30:00','2026-03-06 09:00:00','failed');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `sale_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `fk_order_detail_product` (`product_id`),
  CONSTRAINT `fk_order_detail_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`),
  CONSTRAINT `fk_order_detail_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `chk_order_detail_quantity_value` CHECK ((`quantity` >= 0)),
  CONSTRAINT `chk_order_detail_sale_price_value` CHECK ((`sale_price` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (1,1,2,26990000.00),(1,2,1,21090000.00),(2,3,1,17980000.00),(2,4,3,16500000.00),(3,5,1,22390000.00),(3,6,2,21500000.00),(4,7,1,21990000.00),(4,8,4,7990000.00),(5,9,2,20490000.00),(5,10,1,42990000.00);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(50) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `brand_id` int NOT NULL,
  `sale_date` date NOT NULL DEFAULT (curdate()),
  `image` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `uq_product_product_name` (`product_name`),
  KEY `idx_product_product_name` (`product_name`),
  KEY `idx_product_brand_id` (`brand_id`),
  KEY `idx_product_price` (`price`),
  KEY `idx_product_is_deleted` (`is_deleted`),
  CONSTRAINT `fk_product_brand` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`brand_id`),
  CONSTRAINT `chk_product_price_value` CHECK ((`price` >= 0)),
  CONSTRAINT `chk_product_quantity_value` CHECK ((`quantity` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Iphone XS Max',100,26990000.00,1,'2019-10-12','IPhone XS Max 128GB.jpg','Made in USA',0),(2,'Iphone X',100,21090000.00,1,'2019-10-09','ipx.jpg','Apple\'s aim with the iPhone X was to create an iPhone.',0),(3,'Iphone 8 Plus',100,17980000.00,1,'2019-10-09','IPhone 8 Plus 64GB.jpg','The iPhone 8 includes a 4.7-inch display.',0),(4,'Iphone 7 Plus',100,16500000.00,1,'2019-10-10','ip7.jpg','The iPhone 7 measures in at 138.3mm tall.',0),(5,'Samsung Galaxy Note 10 Plus',100,22390000.00,2,'2019-10-08','ss note 10+.jpg','It runs on the Samsung Exynos 9 Octa 9825 Chipset.',0),(6,'Samsung Galaxy S10',100,21500000.00,2,'2019-10-08','Samsung Galaxy S10 128GB.jpg','The Galaxy S10 isn’t all that small, of course.',0),(7,'Samsung Galaxy S10 Plus',100,21990000.00,2,'2019-10-08','Samsung Galaxy S10+ 2 128GB.jpg','The Galaxy S10+ is Samsung latest flagship for 2019.',0),(8,'Samsung Galaxy A70',100,7990000.00,2,'2019-10-08','Samsung Galaxy A70 64GB.jpg','It is powered by 2GHz octa-core Qualcomm Snapdragon 675.',0),(9,'Samsung Galaxy Note 9',100,20490000.00,2,'2019-10-08','ss note 9.jpg','Samsung Note version',0),(10,'IPhone 11 Pro Max',100,42990000.00,1,'2019-10-08','iphone-11-pro-max-512gb-gold.jpg','New IPhone',0),(11,'Iphone 11',80,21990000.00,1,'2019-10-08','iphone-11-128gb-purple.jpg','New version',0),(12,'Iphone 6S Plus',100,8990000.00,1,'2019-10-12','IPhone 6 32GB.jpg','Made in USA',0),(13,'Xiaomi Note 7',100,4500000.00,6,'2019-10-08','xiaominote7.jpg','description',0),(14,'Huawei P30 Pro',120,20690000.00,9,'2019-10-08','huawei-p30-pro.jpg','Huawei made in China',0),(15,'Huawei P30',100,15290000.00,9,'2019-10-08','huawei-p30-blue-600x600.jpg','Huawei made in China',0),(16,'Oppo Reno 10X',70,19990000.00,3,'2019-10-08','oppo-reno-10x-zoom-edition-black.jpg','Oppo made in China',0),(17,'Oppo A9',100,7890000.00,3,'2019-10-08','oppo-a9-2020-green-1-600x600.jpg','Oppo China',0),(18,'Oppo A7',50,7000000.00,3,'2019-10-08','oppo-r17-pro-14-600x600.jpg','Oppo China',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'pilot_project_db'
--

--
-- Dumping routines for database 'pilot_project_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-25 17:06:21
