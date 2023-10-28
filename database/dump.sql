CREATE DATABASE  IF NOT EXISTS `udrive` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `udrive`;
-- MySQL dump 10.13  Distrib 8.0.33, for macos13 (arm64)
--
-- Host: 127.0.0.1    Database: udrive
-- ------------------------------------------------------
-- Server version	8.1.0

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
-- Table structure for table `email_code`
--

DROP TABLE IF EXISTS `email_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_code` (
  `email` varchar(150) NOT NULL COMMENT 'Email',
  `code` varchar(5) NOT NULL COMMENT 'Code',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',
  `status` tinyint(1) DEFAULT NULL COMMENT '0: Not Used  1: Used',
  PRIMARY KEY (`email`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Email Verification Code';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_code`
--

LOCK TABLES `email_code` WRITE;
/*!40000 ALTER TABLE `email_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_info`
--

DROP TABLE IF EXISTS `file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_info` (
  `file_id` varchar(10) NOT NULL COMMENT 'File ID',
  `user_id` varchar(10) NOT NULL COMMENT 'User ID',
  `file_md5` varchar(32) DEFAULT NULL COMMENT 'MD5 Value, recorded on the first upload',
  `file_pid` varchar(10) DEFAULT NULL COMMENT 'Parent ID',
  `file_size` bigint DEFAULT NULL COMMENT 'File Size',
  `file_name` varchar(200) DEFAULT NULL COMMENT 'File Name',
  `file_cover` varchar(100) DEFAULT NULL COMMENT 'Cover',
  `file_path` varchar(100) DEFAULT NULL COMMENT 'File Path',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',
  `last_update_time` datetime DEFAULT NULL COMMENT 'Last Update Time',
  `folder_type` tinyint(1) DEFAULT NULL COMMENT '0: File 1: Directory',
  `file_category` tinyint(1) DEFAULT NULL COMMENT '1: Video 2: Audio  3: Image 4: Document 5: Other',
  `file_type` tinyint(1) DEFAULT NULL COMMENT ' 1: Video 2: Audio  3: Image 4: PDF 5: DOC 6: Excel 7: TXT 8: Code 9: ZIP 10: Other',
  `status` tinyint(1) DEFAULT NULL COMMENT '0: Transcoding 1: Transcoding Failed 2: Transcoding Successful',
  `recovery_time` datetime DEFAULT NULL COMMENT 'Recycle Bin Time',
  `del_flag` tinyint(1) DEFAULT '2' COMMENT 'Deletion Flag 0: Deleted  1: Recycle Bin  2: Normal',
  PRIMARY KEY (`file_id`,`user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_md5` (`file_md5`) USING BTREE,
  KEY `idx_file_pid` (`file_pid`),
  KEY `idx_del_flag` (`del_flag`),
  KEY `idx_recovery_time` (`recovery_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='File Information';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_info`
--

LOCK TABLES `file_info` WRITE;
/*!40000 ALTER TABLE `file_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_share`
--

DROP TABLE IF EXISTS `file_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_share` (
  `share_id` varchar(20) NOT NULL COMMENT 'Share ID',
  `file_id` varchar(10) NOT NULL COMMENT 'File ID',
  `user_id` varchar(10) NOT NULL COMMENT 'User ID',
  `valid_type` tinyint(1) DEFAULT NULL COMMENT 'Validity Period Type 0: 1 Day 1: 7 Days 2: 30 Days 3: Permanent',
  `expire_time` datetime DEFAULT NULL COMMENT 'Expiration Time',
  `share_time` datetime DEFAULT NULL COMMENT 'Share Time',
  `code` varchar(5) DEFAULT NULL COMMENT 'Access Code',
  `show_count` int DEFAULT '0' COMMENT 'View Count',
  PRIMARY KEY (`share_id`),
  KEY `idx_file_id` (`file_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_share_time` (`share_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Share Information';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_share`
--

LOCK TABLES `file_share` WRITE;
/*!40000 ALTER TABLE `file_share` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `user_id` varchar(10) NOT NULL COMMENT 'User ID',
  `nick_name` varchar(20) DEFAULT NULL COMMENT 'Nickname',
  `email` varchar(150) DEFAULT NULL COMMENT 'Email',
  `qq_open_id` varchar(35) DEFAULT NULL COMMENT 'QQ OpenID',
  `qq_avatar` varchar(150) DEFAULT NULL COMMENT 'QQ Avatar',
  `password` varchar(50) DEFAULT NULL COMMENT 'Password',
  `join_time` datetime DEFAULT NULL COMMENT 'Join Time',
  `last_login_time` datetime DEFAULT NULL COMMENT 'Last Login Time',
  `status` tinyint DEFAULT NULL COMMENT '0: Disabled 1: Normal',
  `use_space` bigint DEFAULT '0' COMMENT 'Space Used in Bytes',
  `total_space` bigint DEFAULT NULL COMMENT 'Total Space',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `key_email` (`email`),
  UNIQUE KEY `key_nick_name` (`nick_name`),
  UNIQUE KEY `key_qq_open_id` (`qq_open_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='User Information';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES ('3178033358','Test Account','test@qq.com',NULL,NULL,'47ec2dd791e31e2ef2076caf64ed9b3d',NULL,'2023-04-28 13:54:01',1,238302835,10737418240);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-19 23:54:55
