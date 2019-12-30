/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.27 : Database - brms
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`brms` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `brms`;

/*Table structure for table `db_admin` */

DROP TABLE IF EXISTS `db_admin`;

CREATE TABLE `db_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `password` varchar(300) NOT NULL,
  `perms` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `db_admin` */

insert  into `db_admin`(`id`,`username`,`password`,`perms`) values (1,'20190001','bb8ce661128c8341533f10b34576d49ecac94c0b31edc6864f13bb76171c92e9','super');

/*Table structure for table `db_book_info` */

DROP TABLE IF EXISTS `db_book_info`;

CREATE TABLE `db_book_info` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `author` varchar(50) NOT NULL,
  `publish` varchar(50) NOT NULL,
  `pubdate` varchar(20) NOT NULL,
  `introduction` text,
  `ISBN` varchar(50) NOT NULL,
  `class_id` varchar(10) NOT NULL,
  `pressmark` varchar(10) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

/*Data for the table `db_book_info` */

/*Table structure for table `db_book_resourse` */

DROP TABLE IF EXISTS `db_book_resourse`;

CREATE TABLE `db_book_resourse` (
  `res_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) NOT NULL,
  `res_name` varchar(100) NOT NULL,
  `res_src` varchar(200) DEFAULT NULL,
  `res_type` varchar(10) NOT NULL,
  `res_size` bigint(20) NOT NULL,
  `down_sum` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`res_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `db_book_resourse` */

insert  into `db_book_resourse`(`res_id`,`book_id`,`res_name`,`res_src`,`res_type`,`res_size`,`down_sum`) values (1,1,'111.pdf','/upload/brms/1/','pdf',341229,0),(2,2,'1 2月份营销企划案(1)(1).docx','/upload/brms/2/','docx',36638,0),(3,2,'56号 学院关于印发一生一品管理办法的通知.pdf','/upload/brms/2/','pdf',341229,0),(4,3,'批量导入样表 - 错误.xlsx','/upload/brms/3/','xlsx',12177,0),(5,4,'批量导入样表.xlsx','/upload/brms/4/','xlsx',12205,0),(6,5,'批量导入样表 - 错误.xlsx','/upload/brms/5/','xlsx',12177,0),(7,6,'批量导入样表 - 错误.xlsx','/upload/brms/6/','xlsx',12177,0),(8,7,'批量导入样表.xlsx','/upload/brms/7/','xlsx',12205,0);

/*Table structure for table `db_download_log` */

DROP TABLE IF EXISTS `db_download_log`;

CREATE TABLE `db_download_log` (
  `reader_id` bigint(20) NOT NULL,
  `ip` varchar(20) NOT NULL,
  `book_id` int(11) NOT NULL,
  `book_name` varchar(50) NOT NULL,
  `res_name` varchar(100) NOT NULL,
  `download_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `db_download_log` */

/*Table structure for table `db_notice` */

DROP TABLE IF EXISTS `db_notice`;

CREATE TABLE `db_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `context` text,
  `creat_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `db_notice` */

/*Table structure for table `db_reader_card` */

DROP TABLE IF EXISTS `db_reader_card`;

CREATE TABLE `db_reader_card` (
  `reader_id` bigint(20) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `passwd` varchar(300) NOT NULL DEFAULT 'bb8ce661128c8341533f10b34576d49ecac94c0b31edc6864f13bb76171c92e9',
  PRIMARY KEY (`reader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `db_reader_card` */

/*Table structure for table `db_reader_info` */

DROP TABLE IF EXISTS `db_reader_info`;

CREATE TABLE `db_reader_info` (
  `reader_id` bigint(20) NOT NULL,
  `name` varchar(16) NOT NULL,
  `department` varchar(20) DEFAULT NULL,
  `major` varchar(20) DEFAULT NULL,
  `grade` varchar(10) DEFAULT NULL,
  `class_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`reader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `db_reader_info` */

/*Table structure for table `db_sports_horse` */

DROP TABLE IF EXISTS `db_sports_horse`;

CREATE TABLE `db_sports_horse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `author` varchar(50) NOT NULL,
  `grade` varchar(10) NOT NULL,
  `content` text,
  `time` datetime NOT NULL,
  `file1_src` varchar(100) DEFAULT NULL,
  `file2_src` varchar(100) DEFAULT NULL,
  `file3_src` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `db_sports_horse` */

/*Table structure for table `db_web_config` */

DROP TABLE IF EXISTS `db_web_config`;

CREATE TABLE `db_web_config` (
  `web_name` varchar(50) NOT NULL,
  `title` varchar(100) NOT NULL,
  `logo_img` longblob,
  `bg_img` longblob,
  `copyright` varchar(100) DEFAULT NULL,
  `icp` varchar(100) DEFAULT NULL,
  `file1_name` varchar(20) DEFAULT NULL,
  `file2_name` varchar(20) DEFAULT NULL,
  `file3_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`web_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `db_web_config` */


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;