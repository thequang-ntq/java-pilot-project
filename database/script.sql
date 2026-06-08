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
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'Apple','images/brand/2d87e35a-76f0-4527-9e0e-8eec92d18a77.png','Apple Inc, California'),(2,'Samsung','images/brand/5e781e72-cb1c-47e3-8a5e-ca655c66bb2f.png','Samsung Inc, Korea'),(3,'Oppo','images/brand/7a9e375d-b4f7-428c-8ae5-7f691c5a9897.png','Oppo Inc, China'),(4,'LG','images/brand/f2675966-5f9a-4a3b-8a91-5f7ba7103735.png','LG Inc, Japan'),(5,'Xiaomi','images/brand/fe3e8a13-0dc0-4a63-b7c8-56dca283412e.png','Xiaomi Inc, China'),(6,'Sony','images/brand/91c3f06c-16bd-41a4-8785-597f76128d0b.png','Sony Inc, Japan'),(7,'Huawei','images/brand/189ae546-9df8-4052-9303-b0509115e8ba.webp','Huawei made in China'),(8,'Vivo','images/brand/56d729ee-70d3-47f5-90dd-6c105c471220.webp','Vivo Inc, China'),(9,'HTC','images/brand/462af032-f0b4-4862-9651-daae1a80cd30.png','HTC Inc, California'),(10,'Asus','images/brand/9b4020ec-9310-4ef8-a4db-910f183d8089.png','Asus Inc, China'),(11,'Realme','images/brand/b83de7f8-509a-43d5-a795-20beb9fd76ff.png','Realme Inc, China'),(22,'Nike','images/brand/da81a33c-8081-4f14-b790-24d6e5bfc765.png','Nike Inc, USA'),(23,'Adidas','images/brand/beb52b79-2450-4c10-ba5e-78edaff374e0.jpg','Adidas Inc, Germany'),(24,'Puma','images/brand/374955b4-3de2-4955-8602-1e16be6d8dec.png','Puma Inc, Germany'),(27,'Dell','images/brand/c290d79f-896f-4f3e-ad63-a6612622fea6.webp','Dell Inc, USA'),(28,'Lenovo','images/brand/484cc185-4259-48f0-9885-82ee044555b5.png','Lenovo Inc, China'),(29,'Acer','images/brand/6bc98aea-f2aa-4491-be46-9f7e776736c9.png','Acer Inc, Taiwan'),(30,'MSI','images/brand/861703f1-59f7-4807-a6a6-d7d37289e7cc.webp','MSI Inc, Taiwan'),(31,'Razer','images/brand/325799e5-315b-4913-b144-6d3dc44b841f.webp','Razer Gaming Brand'),(33,'HP','images/brand/e17a0a5b-8a6b-4585-8fb4-582009f943ee.webp','HP Inc, USA'),(34,'Canon','images/brand/541463af-eaa9-4093-9099-58a3629688cb.jpg','Canon Inc, Japan'),(35,'Nokia','images/brand/1668e922-c80e-4eb4-bbb7-ec5c73f7d44f.png','Nokia Mobile, Finland'),(36,'Motorola','images/brand/4e760fe7-63b0-417b-802b-ee03470e8bd3.png','Motorola Mobility'),(37,'OnePlus','images/brand/08dc249d-7b2c-4527-9ef7-168448ec74e5.jpg','OnePlus Technology'),(38,'Google Pixel','images/brand/9d05db5b-5785-41c4-9ccb-43ee8269a27d.webp','Google Smartphone'),(39,'Nothing','images/brand/36eff868-f65e-40ad-921f-f4a6bf56bf81.png','Nothing Phone Brand'),(40,'Toshiba','images/brand/ad8e86a2-7f9a-4c5e-a142-486ef5b48fa4.png','Toshiba Electronics'),(41,'Gigabyte','images/brand/200746d5-9daa-4fa6-b5f4-e5922cc833b9.png','Gigabyte Technology'),(42,'Intel','images/brand/613be36b-f2c4-4e02-97cb-944e7cde8d6a.png','Intel Corporation'),(44,'Nvidia','images/brand/3abd5d0e-178c-4ab9-85f1-0f9a4b45992e.png','Nvidia Graphics'),(45,'Corsair','images/brand/25b5add5-146e-4a45-a45f-693edc2db58f.webp','Corsair Gaming'),(46,'HyperX','images/brand/fa9a17e0-cb03-44ca-89cd-ffb79efd9bec.webp','HyperX Accessories'),(47,'Logitech','images/brand/59c38338-3cc6-4541-b8ef-78282273af38.jpg','Logitech Switzerland'),(49,'Bose','images/brand/ec8355a7-5999-4673-9ee4-0cfc4c0fe43c.jpg','Bose Audio USA'),(50,'Anker','images/brand/20d53b3b-d51c-4967-945e-15d2b0799360.png','Anker Electronics'),(51,'Belkin','images/brand/3e4ef5c8-7827-4281-833d-97d177aaf630.png','Belkin Accessories'),(52,'Philips','images/brand/33caec97-ce2c-4cc1-9019-b7d7b7941286.webp','Philips Electronics'),(53,'Panasonic','images/brand/497e061a-5b94-4e2a-a7cb-4ab404a90474.png','Panasonic Japan'),(54,'Sharp','images/brand/4788736f-0bec-4b3f-a5e7-d501c50cbc0b.png','Sharp Corporation'),(55,'BlackBerry','images/brand/92048d6e-a8a4-473d-b8e7-ab46ecfb6f16.png','BlackBerry Canada'),(56,'Meizu','images/brand/6e69055d-74e5-42c4-b096-e761dd12be38.jpg','Meizu Technology'),(57,'ZTE','images/brand/9ea40661-2303-4cea-ad38-234d977ef9c7.png','ZTE Corporation !!!'),(58,'Honor','images/brand/0632d25f-6331-44f7-a000-72a8279ef66a.png','Honor Smartphone'),(59,'Redmi','images/brand/f1041431-759f-4410-841d-8539815ce55e.png','Redmi by Xiaomi'),(60,'Infinix','images/brand/d8d7f3cb-f07f-4005-bb6e-3cad6b608090.png','Infinix Mobile'),(61,'Tecno','images/brand/c9ba7dbc-42a1-454f-93e8-58e8127115c6.png','Tecno Mobile'),(62,'Micromax','images/brand/6a0a62d4-ce42-4e22-a143-89311f1ab1d1.png','Micromax India'),(63,'Coolpad',NULL,'Coolpad Group'),(64,'Alcatel','images/brand/7f6c6a0c-6df1-4157-be37-9d6657da7700.jpg','Alcatel Mobile'),(84,'JBL','images/brand/131e6493-151d-4356-ad1b-ebfaebfa2642.png','JBL Inc, USA'),(85,'Google','images/brand/24e09409-5251-439e-bc19-3c0cf88d5b33.png','Google Inc, USA');
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
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Iphone XS Max',99,26990000.00,1,'2019-10-12','images/product/e4f67925-3bbe-4e85-bd36-1e31af4f8352.jpg','Made in USA'),(2,'Iphone X',100,21090000.00,1,'2019-10-09','images/product/af4f0918-8af7-4607-abc2-a712b65ac98b.jpg','Apples aim with the iPhone X was to create an iPhone.'),(3,'Iphone 8 Plus',100,17980000.00,1,'2019-10-09','images/product/a68dbe14-d5af-4e85-9349-8e7e9a626cf7.jpg','The iPhone 8 includes a 4.7-inch display.'),(4,'Iphone 7 Plus',100,16500000.00,1,'2019-10-10','images/product/250f7fbb-071c-4043-bfcf-caa101f24e1f.png','The iPhone 7 measures in at 138.3mm tall.'),(5,'Samsung Galaxy Note 10 Plus',100,22390000.00,2,'2019-10-08','images/product/6d75caf3-f638-419a-a4e9-f3101cd1fc6c.png','It runs on the Samsung Exynos 9 Octa 9825 Chipset.'),(6,'Samsung Galaxy S10',100,21500000.00,2,'2019-10-08','images/product/dfabd344-07bd-4a3f-bb45-b8bed22f5554.jpg','The Galaxy S10 isn’t all that small, of course.'),(7,'Samsung Galaxy S10 Plus',100,21990000.00,2,'2019-10-08','images/product/3de7d6c6-9eed-4ea7-856e-a613c6368c5d.png','The Galaxy S10+ is Samsung latest flagship for 2019.'),(8,'Samsung Galaxy A70',100,7990000.00,2,'2019-10-08','images/product/742ddd31-6bb7-4965-a335-e7f308e75d50.jpg','It is powered by 2GHz octa-core Qualcomm Snapdragon 675.'),(9,'Samsung Galaxy Note 9',100,20490000.00,2,'2019-10-08','images/product/9a69a74b-29ea-45f2-b3c4-59e506c93de0.jpg','Samsung Note version'),(10,'IPhone 11 Pro Max',100,42990000.00,1,'2019-10-08','images/product/05427a2a-4ff9-4f78-affa-081d010fcc10.jpg','New IPhone'),(11,'Iphone 11',80,21990000.00,1,'2019-10-08','images/product/5ecec60d-9a6a-498d-9e97-077d3246e841.jpg','New version'),(12,'Iphone 6S Plus',99,8990000.00,1,'2019-10-12','images/product/68b62c4e-b4cc-4fb9-be01-94b85802853a.jpg','Made in USA'),(13,'Xiaomi Note 7',100,4500000.00,6,'2019-10-08','images/product/7c5bd88d-fa98-42ea-baf0-fe2ed11f7842.jpg','description'),(14,'Huawei P30 Pro',120,20690000.00,9,'2019-10-08','images/product/797286ea-9825-4fbc-9656-8ffd34cc883a.jpg','Huawei made in China'),(15,'Huawei P30',100,15290000.00,9,'2019-10-08','images/product/f426daeb-afea-4e8d-91f6-b1858a503178.jpg','Huawei made in China'),(16,'Oppo Reno 10X',70,19990000.00,3,'2019-10-08','images/product/66eefdb2-c76b-46e2-8564-f3f61990207c.jpg','Oppo made in China'),(17,'Oppo A9',100,7890000.00,3,'2019-10-08','images/product/0d915993-9c9c-4513-9e21-c731b4daaac9.jpg','Oppo China'),(18,'Oppo A7',50,7000000.00,3,'2019-10-08','images/product/7cc87325-8da0-4019-8c77-b8b8d1837933.jpg','Oppo China'),(25,'Oppo A11',100,9000000.00,3,'2026-05-19','images/product/0ee7a66f-5b6b-4905-88ce-1e141c256749.jpg','New version'),(26,'Lenovo ThinkPad X1 Carbon',40,35990000.00,28,'2026-05-19','images/product/6b89123b-9d5e-454f-8b54-1c0990d2b3df.jpg','Business laptop'),(27,'Lenovo Legion 5',55,28990000.00,28,'2026-05-19','images/product/5027911e-c8e8-4324-ae64-214b4a09fbf8.jpg','Gaming laptop'),(28,'Acer Aspire 7',70,18990000.00,29,'2026-05-19','images/product/560a2245-c65d-4ccb-a87e-cc02d74a93ee.jpg','Mid-range laptop'),(29,'Acer Nitro 5',65,24990000.00,29,'2026-05-19','images/product/16c124e0-a866-4174-837d-97b3464741dc.jpg','Gaming laptop'),(30,'MSI Katana GF66',35,27990000.00,30,'2026-05-19','images/product/a6ec18b2-c085-45ef-b6e3-54170d313925.webp','MSI gaming series'),(31,'MSI Modern 14',50,17990000.00,30,'2026-05-19','images/product/6a7a3c2d-4b81-4b3f-9592-329fb88519cb.jpg','Portable ultrabook'),(32,'Razer Blade 15',20,55990000.00,31,'2026-05-19','images/product/e67c8f15-6a40-4831-b096-73e7f3462c05.png','Premium gaming laptop'),(34,'HP Pavilion 15',80,20990000.00,33,'2026-05-19','images/product/e0664a1d-a951-492b-83bc-fb72abc501f7.jpg','Office laptop'),(35,'HP Victus 16',42,31990000.00,33,'2026-05-19','images/product/d45da7e8-9820-4330-8433-1170a04ec811.jpg','Gaming performance'),(36,'Canon EOS M50',15,16490000.00,34,'2026-05-19','images/product/858c48ce-ea5e-4fb0-a8dc-67bc47e04f50.jpg','Mirrorless camera'),(37,'Canon EOS R10',12,28990000.00,34,'2026-05-19','images/product/ff61aa79-099d-42ce-b547-bbf7d0c019d0.png','Professional camera'),(38,'Nokia G22',90,4990000.00,35,'2026-05-19','images/product/be5e15ce-78ba-4442-b054-8e8094478ba2.jpg','Budget smartphone'),(39,'Nokia X30',45,9990000.00,35,'2026-05-19','images/product/c1cbae79-7900-4447-a471-68ab0e1e0899.jpg','Mid-range Nokia phone'),(40,'Motorola Edge 40',33,12990000.00,36,'2026-05-19','images/product/29f8c984-e9f8-417c-af4f-7f2b27752c95.jpg','Android smartphone'),(41,'Motorola Moto G84',50,7990000.00,36,'2026-05-19','images/product/71b76c40-b56e-4094-8ef0-a838e01f5bea.jpg','Affordable phone'),(42,'OnePlus 12',38,18990000.00,37,'2026-05-19','images/product/51a2d261-4b90-403a-be8a-0682bd64920c.png','Flagship OnePlus'),(43,'OnePlus Nord CE4',60,8990000.00,37,'2026-05-19','images/product/9f0a92a9-83d1-4d20-8772-8c20ccfbff87.png','Mid-range OnePlus'),(44,'Google Pixel 8',28,20990000.00,38,'2026-05-19','images/product/6d4c2c9d-1379-488d-b98e-8f7f42288b78.avif','Google flagship'),(45,'Google Pixel 8a',46,12990000.00,38,'2026-05-19','images/product/bbd8fad3-6834-4002-bed0-27bfb17eedf9.jpg','Affordable Pixel'),(46,'Nothing Phone 2',30,15990000.00,39,'2026-05-19','images/product/c1e22ace-8a4e-421e-9785-ffed25996232.webp','Transparent design'),(47,'Nothing Phone 2a',55,9990000.00,39,'2026-05-19','images/product/ef4fa985-a442-43de-b37e-54ceb8dc9c3d.webp','Mid-range Nothing'),(48,'Toshiba Dynabook X40',20,23990000.00,40,'2026-05-19','images/product/44aba97b-4490-42b0-8445-52496969fffb.jpg','Business notebook'),(49,'Gigabyte G5',25,25990000.00,41,'2026-05-19','images/product/821110d5-a1c8-40e3-8039-9f573f50ed12.webp','Gaming notebook'),(50,'Intel Core i5 14600K',100,7990000.00,42,'2026-05-19','images/product/68b075d4-afbb-425b-97be-4db6c0c8afd5.jpg','Desktop processor'),(51,'Intel Core i7 14700K',75,12990000.00,42,'2026-05-19','images/product/ba2796bc-7705-4a28-8170-f52ce4f2cb02.webp','High-end processor'),(54,'Nvidia RTX 4060',40,10990000.00,44,'2026-05-19','images/product/2c23aa09-d79d-4d1d-98cf-c1e4575ee163.jpg','Graphics card'),(55,'Nvidia RTX 4070 Ti',25,23990000.00,44,'2026-05-19','images/product/a2b8c894-4a4d-4e3e-9f8d-6d0b31f431be.png','High-end GPU'),(56,'Corsair K70 RGB',120,3490000.00,45,'2026-05-19','images/product/7c5d1520-7f97-44f3-9dba-83a63472ba83.jpg','Mechanical keyboard'),(57,'Corsair HS80',75,2990000.00,45,'2026-05-19','images/product/96dd8f43-1aeb-45ec-9b2a-216d8d0b4b6b.avif','Gaming headset'),(58,'HyperX Cloud II',88,1990000.00,46,'2026-05-19','images/product/d045824d-a738-46e5-a13f-6b05e662e0a8.jpg','Popular gaming headset'),(59,'HyperX Alloy Origins',60,2590000.00,46,'2026-05-19','images/product/ab69ad22-e9fd-4905-beb2-0e7dc5c32255.webp','Mechanical keyboard'),(60,'Logitech G Pro X',95,3290000.00,47,'2026-05-19',NULL,'Gaming mouse'),(61,'Logitech MX Master 3S',45,2490000.00,47,'2026-05-19','images/product/fcbe9368-1a24-4b85-aa34-cced21cff7a4.png','Productivity mouse'),(64,'Bose QuietComfort Ultra',18,9990000.00,49,'2026-05-19','images/product/ef03f880-3245-46d7-b87f-0250775e9a32.avif','Noise cancelling'),(65,'Bose SoundLink Flex',34,4290000.00,49,'2026-05-19',NULL,'Bluetooth speaker');
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
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES (78,1,'186dc329-6912-4260-b103-ed39ed3ac73d','2026-06-05 17:24:16');
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

-- Dump completed on 2026-05-30 15:53:09
