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
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `brand_id` int NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(50) NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`brand_id`),
  UNIQUE KEY `uq_brands_brand_name` (`brand_name`),
  KEY `idx_brands_brand_name` (`brand_name`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'Apple','images/brand/20221124-1506-94dbck.png','Apple Inc,  California'),(2,'Samsung','images/brand/20221124-1505-m6nrn9.webp','Samsung Inc,  Korea'),(3,'Oppo','images/brand/20221124-1505-m0iss6.png','Oppo Inc,  China'),(4,'LG','images/brand/20221124-1505-vgj6rp.png','LG Inc,  Japan'),(5,'Xiaomi','images/brand/20221124-1504-ep3pdt.png','Xiaomi Inc,  China'),(6,'Sony','images/brand/20221124-1504-nak19v.png','Sony Inc,  Japan'),(7,'Huawei','images/brand/20221124-1503-36khjb.png','Huawei made in China'),(8,'Vivo','images/brand/20221124-1503-9dc89b.png','Vivo Inc,  China'),(9,'HTC','images/brand/20221124-1502-d0h72d.png','HTC Inc,  California'),(10,'Asus','images/brand/20221124-1501-73qa6b.svg','Asus Inc,  China'),(11,'Realme','images/brand/20221124-1501-12e3hf.png','Realme Inc,  China'),(13,'Levono 1!',NULL,'China!'),(14,'Levono 2!','images/brand/807183b5-585a-4c80-a146-8bec3834b349.jpg','China'),(16,'Lenvono 3!','images/brand/6488d679-dd90-4e41-ae22-3032aa3ea4ce.jpg','Brand'),(20,'abc','images/brand/4d29dfc3-01fb-44b8-9081-52e4a19c377e.jpg','s'),(21,'Samsung 2',NULL,''),(22,'Nike','images/brand/fed000cc-a559-456c-a928-d21460119af7.png',''),(23,'Adidas','images/brand/beb52b79-2450-4c10-ba5e-78edaff374e0.jpg','Adidas'),(24,'Puma','images/brand/374955b4-3de2-4955-8602-1e16be6d8dec.png','New'),(25,'Sony 2','images/brand/be198af7-38c9-47fd-a6f3-134b5b832352.png','New'),(27,'Dell','images/brand/c290d79f-896f-4f3e-ad63-a6612622fea6.webp','a');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(50) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `brand_id` int NOT NULL,
  `sale_date` date NOT NULL DEFAULT (curdate()),
  `image` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `uq_products_product_name` (`product_name`),
  KEY `idx_products_product_name` (`product_name`),
  KEY `idx_products_brand_id` (`brand_id`),
  KEY `idx_products_price` (`price`),
  CONSTRAINT `fk_products_brands` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`),
  CONSTRAINT `chk_products_price_value` CHECK ((`price` >= 0)),
  CONSTRAINT `chk_products_quantity_value` CHECK ((`quantity` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Iphone XS Max',100,26990000.00,1,'2019-10-12','images/product/IPhone XS Max 128GB.jpg','Made in USA'),(2,'Iphone X',100,21090000.00,1,'2019-10-09','images/product/ipx.jpg','Apple\'s aim with the iPhone X was to create an iPhone.'),(3,'Iphone 8 Plus',100,17980000.00,1,'2019-10-09','images/product/IPhone 8 Plus 64GB.jpg','The iPhone 8 includes a 4.7-inch display.'),(4,'Iphone 7 Plus',100,16500000.00,1,'2019-10-10','images/product/ip7.jpg','The iPhone 7 measures in at 138.3mm tall.'),(5,'Samsung Galaxy Note 10 Plus',100,22390000.00,2,'2019-10-08','images/product/ss note 10+.jpg','It runs on the Samsung Exynos 9 Octa 9825 Chipset.'),(6,'Samsung Galaxy S10',100,21500000.00,2,'2019-10-08','images/product/Samsung Galaxy S10 128GB.jpg','The Galaxy S10 isn’t all that small, of course.'),(7,'Samsung Galaxy S10 Plus',100,21990000.00,2,'2019-10-08','images/product/Samsung Galaxy S10+ 2 128GB.jpg','The Galaxy S10+ is Samsung latest flagship for 2019.'),(8,'Samsung Galaxy A70',100,7990000.00,2,'2019-10-08','images/product/Samsung Galaxy A70 64GB.jpg','It is powered by 2GHz octa-core Qualcomm Snapdragon 675.'),(9,'Samsung Galaxy Note 9',100,20490000.00,2,'2019-10-08','images/product/ss note 9.jpg','Samsung Note version'),(10,'IPhone 11 Pro Max',100,42990000.00,1,'2019-10-08','images/product/iphone-11-pro-max-512gb-gold.jpg','New IPhone'),(11,'Iphone 11',80,21990000.00,1,'2019-10-08','images/product/iphone-11-128gb-purple.jpg','New version'),(12,'Iphone 6S Plus',99,8990000.00,1,'2019-10-12','images/product/IPhone 6 32GB.jpg','Made in USA'),(13,'Xiaomi Note 7',100,4500000.00,6,'2019-10-08','images/product/xiaominote7.jpg','description'),(14,'Huawei P30 Pro',120,20690000.00,9,'2019-10-08','images/product/huawei-p30-pro.jpg','Huawei made in China'),(15,'Huawei P30',100,15290000.00,9,'2019-10-08','images/product/huawei-p30-blue-600x600.jpg','Huawei made in China'),(16,'Oppo Reno 10X',70,19990000.00,3,'2019-10-08','images/product/oppo-reno-10x-zoom-edition-black.jpg','Oppo made in China'),(17,'Oppo A9',100,7890000.00,3,'2019-10-08','images/product/oppo-a9-2020-green-1-600x600.jpg','Oppo China'),(18,'Oppo A7',50,7000000.00,3,'2019-10-08','images/product/oppo-r17-pro-14-600x600.jpg','Oppo China'),(22,'Oppo A10',101,8500000.00,3,'2026-05-19','images/product/d9fcc8bb-1321-4bbb-b3ee-e0058eba85ed.jpeg','New version'),(24,'c',3,1000.00,25,'2026-05-19',NULL,''),(25,'Oppo A11',100,9000000.00,3,'2026-05-19','images/product/f67c894a-659c-465c-a335-1e3b87776e39.jpeg','New version');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `token_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `token` varchar(50) NOT NULL,
  `expired_at` datetime NOT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `uq_refresh_tokens_token` (`token`),
  KEY `idx_refresh_tokens_user_id` (`user_id`),
  KEY `idx_refresh_tokens_token` (`token`),
  CONSTRAINT `fk_refresh_tokens_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES (18,1,'4dda71ba-ae2e-4451-8459-fff9e0528189','2026-05-26 16:38:53');
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN') NOT NULL DEFAULT 'ADMIN',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_users_username` (`username`),
  KEY `idx_users_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin1','$2a$10$cnhfrc7aBdwoVSHSmWiuLuV1c46nyVnqbm7xvfxhxESUSQaSdRi7K','ADMIN');
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

-- Dump completed on 2026-05-19 23:49:49
