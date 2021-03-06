-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sellers_service
-- ------------------------------------------------------
-- Server version	5.7.26-log

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
-- Dumping data for table `payment_method`
--

LOCK TABLES `payment_method` WRITE;
/*!40000 ALTER TABLE `payment_method` DISABLE KEYS */;
INSERT INTO `payment_method` (`id`, `name`, `registration_url`, `service_base_url`) VALUES (1,'Credit/Debit Card','https://192.168.43.124:8500/bank-service/registration/', 'https://192.168.43.124:8500/bank-service/'),(2,'PayPal','https://192.168.43.124:8500/paypal-service/registration/', 'https://192.168.43.124:8500/paypal-service/'),(3,'Bitcoin','https://192.168.43.124:8500/bitcoin-service/registration/', 'https://192.168.43.124:8500/bitcoin-service/');
/*!40000 ALTER TABLE `payment_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `seller`
--

LOCK TABLES `seller` WRITE;
/*!40000 ALTER TABLE `seller` DISABLE KEYS */;
INSERT INTO `seller` (`id`, `email`, `name`, `organization`, `password`, `registration_status_callback_url`, `base_website_url`) VALUES (1,'jove@jove.com','Vukasin Jovic','org','$2a$10$TVu9nfgu.9gXTvpVTu.SXeT6PKMVLXlcjzGEqylYWW6imtEubxf8C','https://localhost:8600/kp/registration/status', 'http://localhost:4203'),
(2,'flylivedrve@gmail.com','Vukasin Jovic','vule','$2a$10$89qh8mtfZ8NkJ2bpwELvKOYi8c3m1/yOUxQaHrnql.UbaSTDLB0/C','https://192.168.43.86:8601/kp/registration/status', 'http://192.168.43.86:4202'),
(3, 'naucni.kutak@mail.com', 'Milica Makaric', 'Naucni kutak', '$2a$10$aYTn742eySAc3rm70F2JzeVAW.u9CRI65MR7t9q86qssZSJG9Vidq', 'https://192.168.43.134:8600/kp/registration/status', 'http://192.168.43.134:4201');

INSERT INTO `seller` (`id`,`base_website_url`,`email`,`name`,`organization`,`password`,`registration_status_callback_url`) VALUES (4,'https://192.168.43.124:4203/','construzioni@gmail.com','','org','$2a$10$iliDqdZUhrGmep0jsi4GkOCe1in/Z/gXqHmM/e2XrCRnnPl/kRM1S','https://localhost:8602/api/casopisi/registration/status');
/*!40000 ALTER TABLE `seller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `seller_payment_method`
--

LOCK TABLES `seller_payment_method` WRITE;
/*!40000 ALTER TABLE `seller_payment_method` DISABLE KEYS */;
INSERT INTO `seller_payment_method` (`id`, `registration_success`, `payment_method_id`, `seller_id`) VALUES (1,0,1,1),(2,1,2,1),(3,1,3,1),  (4,0,1,2),(5,1,2,2),(6,0,3,2), (7, 1, 1, 3), (8, 0, 2, 3), (9, 0, 3, 3),(10, 0, 1, 4), (11, 0, 2, 4), (12, 1, 3, 4);
/*!40000 ALTER TABLE `seller_payment_method` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-29 16:49:25
