/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : lisayoga

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 13/05/2023 01:44:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for card
-- ----------------------------
DROP TABLE IF EXISTS `card`;
CREATE TABLE `card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_id` bigint(20) NOT NULL COMMENT '客户id',
  `card_rule_id` bigint(20) NOT NULL COMMENT '卡规则id',
  `card_no` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '卡号',
  `remark` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `balance` decimal(16,4) DEFAULT NULL COMMENT '余额',
  `validity_count` int(11) DEFAULT NULL COMMENT '可用次数',
  `start_time` datetime(3) DEFAULT NULL COMMENT '开始时间',
  `validity_period` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '有效期',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 1：正常 2：已退卡',
  `teacher_id` bigint(20) DEFAULT NULL COMMENT '开单教练',
  `enable_state` int(1) DEFAULT '1',
  `cancel_times` int(1) DEFAULT NULL COMMENT '连续取消次数',
  `punish_start_time` datetime DEFAULT NULL COMMENT '惩罚开始时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=latin1 COMMENT='会员卡';

-- ----------------------------
-- Table structure for card_bill
-- ----------------------------
DROP TABLE IF EXISTS `card_bill`;
CREATE TABLE `card_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `customer_id` bigint(32) NOT NULL COMMENT '客户id',
  `source_id` bigint(32) DEFAULT NULL COMMENT '来源id',
  `card_no` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '卡号',
  `type` int(11) DEFAULT NULL COMMENT '类型 1：充值2：退款',
  `pre_balance` decimal(16,6) DEFAULT NULL COMMENT '余额',
  `consumption_amount` decimal(16,6) DEFAULT NULL COMMENT '消费金额',
  `available_balance` decimal(16,6) DEFAULT NULL COMMENT '可用余额',
  `pre_count` int(11) DEFAULT NULL COMMENT '次数',
  `consumption_count` int(11) DEFAULT NULL COMMENT '消费次数',
  `available_count` int(11) DEFAULT NULL COMMENT '可用次数',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `is_deleted` varchar(1) COLLATE utf8_bin DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=650 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for card_rule
-- ----------------------------
DROP TABLE IF EXISTS `card_rule`;
CREATE TABLE `card_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `validity_period` int(11) DEFAULT NULL COMMENT '时长',
  `period` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '时间段 1: 工作日 2: 周末',
  `frequency` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '可用次数',
  `is_group_class` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '是否是vip团课 0否 1是',
  `is_private_education` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '是否是常规私教 0否 1是',
  `is_private_pilates` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '是否是普拉提器械私教 0否 1是',
  `is_rechargeable_card` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '是否是充值卡 0否 1是',
  `discount_rate` text COMMENT '优惠额度',
  `is_special_rechargeable_card` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '是否是大礼包充值卡  0否 1是',
  `is_special_self` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '是否是本馆专用卡  0否 1是',
  `type` int(1) DEFAULT NULL COMMENT '1、时长卡  2、次卡 3、充值卡',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COMMENT='卡规则';

-- ----------------------------
-- Table structure for recharge
-- ----------------------------
DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_id` bigint(20) unsigned NOT NULL COMMENT '客户id',
  `card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '卡号',
  `payment_amount` decimal(16,4) DEFAULT NULL COMMENT '支付金额',
  `pay_method` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '支付方式',
  `card_times` int(11) DEFAULT NULL COMMENT '卡内的次数',
  `channel_fee` decimal(16,4) DEFAULT NULL COMMENT '渠道费用',
  `validity_period` int(11) DEFAULT NULL COMMENT '充值天数',
  `actual_amount` decimal(16,4) DEFAULT NULL COMMENT '实际费用',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` varchar(1) DEFAULT 'N' COMMENT '这里的is_deleted看做是 is_free就好，为了补体验卡的坑',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=392 DEFAULT CHARSET=latin1 COMMENT='会员卡充值记录';

-- ----------------------------
-- Table structure for refund
-- ----------------------------
DROP TABLE IF EXISTS `refund`;
CREATE TABLE `refund` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_id` bigint(20) unsigned NOT NULL COMMENT '客户id',
  `card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '卡号',
  `average_price` decimal(16,4) NOT NULL COMMENT '实际退款平均价格',
  `should_refund_amount` decimal(16,4) NOT NULL COMMENT '手续费前应退',
  `actual_refund_amount` varchar(255) NOT NULL COMMENT '扣除手续费后的退款价格',
  `refund_count` decimal(16,4) NOT NULL COMMENT '应退款次数/天数/元',
  `charge_rate` decimal(16,4) NOT NULL COMMENT '手续费比例',
  `service_charge` decimal(16,4) NOT NULL COMMENT '手续费',
  `refund_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '退款方式',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1 COMMENT='会员卡退款记录';

-- ----------------------------
-- Table structure for t_classroom
-- ----------------------------
DROP TABLE IF EXISTS `t_classroom`;
CREATE TABLE `t_classroom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '名称',
  `enable_state` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '停启用状态，1启用 2停用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COMMENT='教室';

-- ----------------------------
-- Table structure for t_color
-- ----------------------------
DROP TABLE IF EXISTS `t_color`;
CREATE TABLE `t_color` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '名称',
  `value` char(7) NOT NULL DEFAULT '' COMMENT '值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='颜色';

-- ----------------------------
-- Table structure for t_course
-- ----------------------------
DROP TABLE IF EXISTS `t_course`;
CREATE TABLE `t_course` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(30) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '名称',
  `enable_state` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '停启用状态，1启用 2停用',
  `duration` int(10) unsigned DEFAULT NULL COMMENT '课程时长，单位分钟',
  `background_color` char(7) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT '背景颜色',
  `description` text COLLATE utf8_bin COMMENT '课程描述',
  `card_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '可用卡种类',
  `maximum` int(255) DEFAULT NULL COMMENT '最多可容纳人数',
  `minimum` int(255) DEFAULT NULL COMMENT '最少开课人数',
  `price` decimal(10,2) DEFAULT NULL COMMENT '课程价格',
  `is_group_class` int(1) DEFAULT NULL COMMENT '是否支持常规团课卡',
  `is_vip_group_class` int(1) DEFAULT NULL COMMENT '是否支持vip团课卡',
  `is_private_education` int(1) DEFAULT NULL COMMENT '是否支持常规私教卡',
  `is_private_pilates` int(1) DEFAULT NULL COMMENT '是否支持普拉提私教卡',
  `is_group_pilates` int(1) DEFAULT NULL COMMENT '是否支持普拉提团课卡',
  `is_special_rechargeable_card` int(1) DEFAULT NULL COMMENT '是否支持充值卡',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='课程';

-- ----------------------------
-- Table structure for t_course_appoint
-- ----------------------------
DROP TABLE IF EXISTS `t_course_appoint`;
CREATE TABLE `t_course_appoint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `schedule_id` bigint(20) DEFAULT NULL COMMENT 'scheduleId',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '会员id',
  `appoint_time` datetime DEFAULT NULL COMMENT '预约时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `state` int(255) DEFAULT NULL COMMENT '预约状态: 1、已约 2、候补 3、取消',
  `attendance` int(255) DEFAULT NULL COMMENT '签到状态: 1、未签到 2、 签到',
  `card_id` bigint(20) DEFAULT NULL COMMENT '会员卡id',
  `is_deleted` varchar(1) COLLATE utf8_bin DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for t_course_scheduling
-- ----------------------------
DROP TABLE IF EXISTS `t_course_scheduling`;
CREATE TABLE `t_course_scheduling` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `classroom_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '教室id',
  `course_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '课程id',
  `teacher_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '教师id',
  `date` date DEFAULT NULL COMMENT '日期',
  `attend_time` time DEFAULT NULL COMMENT '上课时间',
  `finish_time` time DEFAULT NULL COMMENT '下课时间',
  `is_deleted` varchar(1) COLLATE utf8_bin DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=630 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='排课';

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `wechat` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '微信号',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `phone_num` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `enter_time` date DEFAULT NULL COMMENT '入籍日期',
  `channel` int(255) DEFAULT NULL COMMENT '了解渠道',
  `channel_ext` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '了解渠道的拓展',
  `weight` double DEFAULT NULL COMMENT '体重',
  `height` double DEFAULT NULL COMMENT '身高',
  `enable_state` int(1) DEFAULT '1',
  `pic_url` varchar(2000) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL COMMENT '用户的openId',
  `type` int(1) DEFAULT '0' COMMENT '会员类型 0、会员 1、体验会员 2、 体验后转正',
  `has_practiced` int(1) DEFAULT NULL COMMENT '0 没练过 1练过',
  `has_hurt` int(1) DEFAULT NULL COMMENT '0 没受过伤 1受过',
  `aim` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '目的',
  `remark` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '备注',
  `is_deleted` varchar(1) DEFAULT 'N' COMMENT '删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=267 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for t_punishment
-- ----------------------------
DROP TABLE IF EXISTS `t_punishment`;
CREATE TABLE `t_punishment` (
  `id` int(11) NOT NULL COMMENT '主键',
  `tyoe` int(1) DEFAULT NULL COMMENT '1、时长卡  2、次卡 3、充值卡',
  `card_id` bigint(20) DEFAULT NULL COMMENT '卡id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `punish_start_time` date DEFAULT NULL COMMENT '惩罚开始时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for t_school
-- ----------------------------
DROP TABLE IF EXISTS `t_school`;
CREATE TABLE `t_school` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `名称` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='学校';

-- ----------------------------
-- Table structure for t_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '姓名',
  `enable_state` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '停启用状态，1启用 2停用',
  `aka_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
  `enter_time` date DEFAULT NULL COMMENT '入职时间',
  `phone_num` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `picture_url` varchar(255) DEFAULT NULL COMMENT '照片地址',
  `qualification` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '资质',
  `introduction` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '个人介绍',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `pic_url` text COMMENT '图片地址json',
  `type` int(255) DEFAULT '0',
  `basic_salary` decimal(10,2) DEFAULT NULL COMMENT '底薪',
  `royalty_rate` decimal(10,3) DEFAULT NULL COMMENT '提成',
  `open_id` text COMMENT 'openId',
  `allowance` decimal(10,0) DEFAULT '0' COMMENT '饭贴车贴',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1 COMMENT='教师';

-- ----------------------------
-- Table structure for t_teacher_course_fee
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher_course_fee`;
CREATE TABLE `t_teacher_course_fee` (
  `id` bigint(20) NOT NULL,
  `teacher_id` bigint(20) DEFAULT NULL COMMENT '老师id',
  `course_id` bigint(20) DEFAULT NULL COMMENT '课程id',
  `course_fee` decimal(10,0) DEFAULT NULL COMMENT '课时费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for t_teacher_salary
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher_salary`;
CREATE TABLE `t_teacher_salary` (
  `id` bigint(20) NOT NULL,
  `teacher_id` bigint(20) DEFAULT NULL COMMENT '老师id',
  `basic_salary` decimal(10,2) DEFAULT NULL COMMENT '基本工资',
  `count` int(11) DEFAULT NULL COMMENT '上课数',
  `month` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '上课月份',
  `allowance` int(255) DEFAULT NULL COMMENT '补贴',
  `royalty` decimal(10,0) DEFAULT NULL COMMENT '提成',
  `total_salary` decimal(10,2) DEFAULT NULL COMMENT '总工资',
  `is_deleted` varchar(1) COLLATE utf8_bin DEFAULT 'N',
  `course_fee` decimal(10,0) DEFAULT NULL COMMENT '课时费',
  `royalty_rate` decimal(10,0) DEFAULT NULL COMMENT '提成比例',
  `type` int(255) NOT NULL DEFAULT '0' COMMENT '0 月结算 1 灵活结算',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for t_upload_file
-- ----------------------------
DROP TABLE IF EXISTS `t_upload_file`;
CREATE TABLE `t_upload_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文件名',
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文件地址',
  `type` int(255) DEFAULT NULL COMMENT '1、会员 2、教师 ',
  `associate_id` bigint(20) DEFAULT NULL COMMENT '关联id',
  `is_deleted` varchar(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1508663478901018719 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
