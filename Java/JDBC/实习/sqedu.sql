/*
Navicat MySQL Data Transfer

Source Server         : baizhi
Source Server Version : 50562
Source Host           : 127.0.0.1:3306
Source Database       : sqedu

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2024-06-17 09:18:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of classes
-- ----------------------------

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cname` varchar(50) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '必修1，选修2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------

-- ----------------------------
-- Table structure for electivecourse
-- ----------------------------
DROP TABLE IF EXISTS `electivecourse`;
CREATE TABLE `electivecourse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stid` int(11) DEFAULT NULL,
  `tid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of electivecourse
-- ----------------------------

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES ('3', 'admin', '123456');

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `scid` int(11) NOT NULL AUTO_INCREMENT,
  `grade` double DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  `sid` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`scid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of score
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `sex` char(1) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `enrollmentTime` date DEFAULT NULL,
  `classid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` varchar(20) NOT NULL COMMENT '教师编号',
  `name` varchar(20) DEFAULT NULL COMMENT '教师姓名',
  `sex` char(1) DEFAULT NULL COMMENT '教师性别',
  `phone` varchar(11) DEFAULT NULL COMMENT '教师电话',
  `address` varchar(50) DEFAULT NULL COMMENT '教师住址',
  `password` varchar(50) DEFAULT NULL COMMENT '教师密码',
  `joineddate` date DEFAULT NULL COMMENT '入职日期',
  `cid` int(11) DEFAULT NULL COMMENT '专业',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1', '关晓彤', '女', '13803712233', '新疆乌鲁木齐', '111222', '2024-06-06', '11');
INSERT INTO `teacher` VALUES ('2', '古力娜扎', '女', '13903703344', '新疆喀什', '444555', '2024-06-04', '22');
INSERT INTO `teacher` VALUES ('2024061221287', '哈妮克孜', '女', '13703721234', '新疆霍尔果斯', '721234', '2024-06-12', null);
INSERT INTO `teacher` VALUES ('2024061510122', '尼格买提', '男', '13903712233', '新疆乌鲁木齐平原路55号', '712233', '2024-06-15', null);

-- ----------------------------
-- Table structure for teachingclass
-- ----------------------------
DROP TABLE IF EXISTS `teachingclass`;
CREATE TABLE `teachingclass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) DEFAULT NULL,
  `classid` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of teachingclass
-- ----------------------------
