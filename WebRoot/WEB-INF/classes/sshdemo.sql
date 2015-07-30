/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : sshdemo

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2014-11-01 11:31:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `authority`
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `display` tinyint(1) NOT NULL DEFAULT '1',
  `name` varchar(20) NOT NULL,
  `url` varchar(50) NOT NULL DEFAULT '#',
  `parent_id` int(11) DEFAULT NULL,
  `sort` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCF662723382D4219` (`parent_id`),
  CONSTRAINT `FKCF662723382D4219` FOREIGN KEY (`parent_id`) REFERENCES `authority` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of authority
-- ----------------------------
INSERT INTO `authority` VALUES ('1', '1', '在线用户', 'user_login.action', '33', '6');
INSERT INTO `authority` VALUES ('2', '0', '添加部门_js', 'department_add_js.action', '13', '14');
INSERT INTO `authority` VALUES ('3', '0', '更改部门_js', 'department_change_js.action', '13', '15');
INSERT INTO `authority` VALUES ('4', '1', '首页', 'index_rd.action', null, '38');
INSERT INTO `authority` VALUES ('5', '0', '添加角色_js', 'role_add_js.action', '22', '20');
INSERT INTO `authority` VALUES ('6', '1', '权限菜单', '#', null, '25');
INSERT INTO `authority` VALUES ('7', '0', '删除部门_js', 'department_remove_js.action', '13', '16');
INSERT INTO `authority` VALUES ('13', '1', '部门管理', '#', null, '11');
INSERT INTO `authority` VALUES ('18', '0', '添加权限_js', 'authority_add_js.action', '6', '28');
INSERT INTO `authority` VALUES ('19', '0', '更新权限顺序_js', 'authority_changeMenu_js.action', '6', '29');
INSERT INTO `authority` VALUES ('20', '0', '更新权限名称_js', 'authority_change_js.action', '6', '30');
INSERT INTO `authority` VALUES ('21', '0', '删除权限_js', 'authority_remove_js.action', '6', '31');
INSERT INTO `authority` VALUES ('22', '1', '角色管理', '#', null, '17');
INSERT INTO `authority` VALUES ('23', '1', '权限列表', 'authority_list.action', '6', '26');
INSERT INTO `authority` VALUES ('24', '1', '部门列表', 'department_list.action', '13', '12');
INSERT INTO `authority` VALUES ('27', '1', '角色列表', 'role_list.action', '22', '18');
INSERT INTO `authority` VALUES ('28', '1', '添加角色', 'role_addInput.action', '22', '19');
INSERT INTO `authority` VALUES ('29', '0', '查看角色', 'role_show.action', '22', '21');
INSERT INTO `authority` VALUES ('30', '0', '更改角色的权限_js', 'role_changeRA_js.action', '22', '22');
INSERT INTO `authority` VALUES ('31', '0', '更改角色的名称路径_js', 'role_changeN_js.action', '22', '23');
INSERT INTO `authority` VALUES ('32', '0', '删除角色_js', 'role_remove_js.action', '22', '24');
INSERT INTO `authority` VALUES ('33', '1', '用户管理', '#', null, '4');
INSERT INTO `authority` VALUES ('34', '1', '用户列表', 'user_list.action', '33', '5');
INSERT INTO `authority` VALUES ('37', '0', '部门列表_js', 'department_list_js.action', '13', '13');
INSERT INTO `authority` VALUES ('38', '0', '用户列表_js', 'user_list_js.action', '33', '7');
INSERT INTO `authority` VALUES ('39', '0', '更改用户信息_js', 'user_change_js.action', '33', '9');
INSERT INTO `authority` VALUES ('40', '0', '删除用户_js', 'user_remove_js.action', '33', '10');
INSERT INTO `authority` VALUES ('41', '0', '修改自己的密码_js', 'user_changePW_js.action', '33', '8');
INSERT INTO `authority` VALUES ('43', '1', '权限角色', 'authority_role.action', '6', '27');
INSERT INTO `authority` VALUES ('51', '1', '角色权限', 'util_refresh_rf.action', '79', '34');
INSERT INTO `authority` VALUES ('52', '1', '权限菜单', 'util_rfmenu_rf.action', '79', '35');
INSERT INTO `authority` VALUES ('65', '1', '文件浏览', 'file_manager_rd.action', null, '37');
INSERT INTO `authority` VALUES ('76', '0', '文件管理', '#', null, '0');
INSERT INTO `authority` VALUES ('79', '1', '重载数据', '#', null, '32');
INSERT INTO `authority` VALUES ('81', '1', '配置文件', 'util_rfdata_rf.action', '79', '36');
INSERT INTO `authority` VALUES ('82', '1', '全部数据', 'util_rfall_rf.action', '79', '33');
INSERT INTO `authority` VALUES ('86', '1', '后台', 'admin_rd.action', null, '39');
INSERT INTO `authority` VALUES ('87', '0', '下载文件', 'file_download_rd.action', '76', '1');
INSERT INTO `authority` VALUES ('88', '0', '获取session', 'file_session_js.action', '76', '2');
INSERT INTO `authority` VALUES ('89', '1', '日志管理', 'log_list.action', null, '3');

-- ----------------------------
-- Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `sort` tinyint(1) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK328E435256ED4867` (`parent_id`),
  CONSTRAINT `FK328E435256ED4867` FOREIGN KEY (`parent_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', '荷树园电厂', '0', null);
INSERT INTO `department` VALUES ('2', '信息部', '1', '1');
INSERT INTO `department` VALUES ('3', '信息部一值', '2', '2');
INSERT INTO `department` VALUES ('4', '信息部二值', '3', '2');

-- ----------------------------
-- Table structure for `log`
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------
INSERT INTO `log` VALUES ('2', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-25 16:59:48', '登录');
INSERT INTO `log` VALUES ('3', 'b051在一分钟内错误登录5次，锁定5分钟，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-25 18:15:21', '登录');
INSERT INTO `log` VALUES ('4', 'b051在一分钟内错误登录5次，锁定5分钟，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-25 18:22:59', '登录');
INSERT INTO `log` VALUES ('5', 'b051在一天内错误登录11次，锁定账号，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-25 18:28:21', '登录');
INSERT INTO `log` VALUES ('10', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-26 12:29:03', '登录');
INSERT INTO `log` VALUES ('11', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-26 19:03:07', '登录');
INSERT INTO `log` VALUES ('12', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-26 20:08:05', '登录');
INSERT INTO `log` VALUES ('13', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-26 20:48:37', '登录');
INSERT INTO `log` VALUES ('14', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-26 21:19:38', '登录');
INSERT INTO `log` VALUES ('15', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-27 10:03:30', '登录');
INSERT INTO `log` VALUES ('16', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.95 Safari/537.4。', '2014-09-27 10:24:02', '登录');
INSERT INTO `log` VALUES ('17', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-27 17:37:49', '登录');
INSERT INTO `log` VALUES ('18', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-27 19:18:48', '登录');
INSERT INTO `log` VALUES ('19', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 09:00:31', '登录');
INSERT INTO `log` VALUES ('20', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:22:48', '登录');
INSERT INTO `log` VALUES ('21', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:26:20', '登录');
INSERT INTO `log` VALUES ('22', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:27:32', '登录');
INSERT INTO `log` VALUES ('23', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:27:48', '登录');
INSERT INTO `log` VALUES ('24', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:29:21', '登录');
INSERT INTO `log` VALUES ('25', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:29:25', '登录');
INSERT INTO `log` VALUES ('26', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:30:23', '登录');
INSERT INTO `log` VALUES ('27', '刘碧锋(b051)登录成功，IP: 192.168.0.172，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 10:33:27', '登录');
INSERT INTO `log` VALUES ('28', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:35:05', '登录');
INSERT INTO `log` VALUES ('29', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 10:35:26', '登录');
INSERT INTO `log` VALUES ('30', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 16:32:55', '登录');
INSERT INTO `log` VALUES ('31', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 16:34:40', '登录');
INSERT INTO `log` VALUES ('32', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 16:35:06', '登录');
INSERT INTO `log` VALUES ('33', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 16:35:50', '登录');
INSERT INTO `log` VALUES ('34', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 16:36:35', '登录');
INSERT INTO `log` VALUES ('35', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 16:37:47', '登录');
INSERT INTO `log` VALUES ('36', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 16:38:35', '登录');
INSERT INTO `log` VALUES ('37', '刘碧锋(b051)登录成功，IP: 192.168.0.172，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 16:39:15', '登录');
INSERT INTO `log` VALUES ('38', '刘碧锋(b051)登录成功，IP: 192.168.0.172，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 16:39:43', '登录');
INSERT INTO `log` VALUES ('39', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 16:43:36', '登录');
INSERT INTO `log` VALUES ('40', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 17:33:20', '登录');
INSERT INTO `log` VALUES ('41', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 17:36:10', '登录');
INSERT INTO `log` VALUES ('42', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 17:39:08', '登录');
INSERT INTO `log` VALUES ('43', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 17:40:11', '登录');
INSERT INTO `log` VALUES ('44', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 17:41:07', '登录');
INSERT INTO `log` VALUES ('45', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 17:41:46', '登录');
INSERT INTO `log` VALUES ('46', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 17:44:48', '登录');
INSERT INTO `log` VALUES ('47', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:32:11', '登录');
INSERT INTO `log` VALUES ('48', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:33:21', '登录');
INSERT INTO `log` VALUES ('49', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:33:37', '登录');
INSERT INTO `log` VALUES ('50', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:33:54', '登录');
INSERT INTO `log` VALUES ('51', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:34:22', '登录');
INSERT INTO `log` VALUES ('52', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:34:47', '登录');
INSERT INTO `log` VALUES ('53', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:35:31', '登录');
INSERT INTO `log` VALUES ('54', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:36:48', '登录');
INSERT INTO `log` VALUES ('55', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:36:59', '登录');
INSERT INTO `log` VALUES ('56', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:37:32', '登录');
INSERT INTO `log` VALUES ('57', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:37:56', '登录');
INSERT INTO `log` VALUES ('58', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 18:38:19', '登录');
INSERT INTO `log` VALUES ('59', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 18:38:38', '登录');
INSERT INTO `log` VALUES ('60', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 18:38:46', '登录');
INSERT INTO `log` VALUES ('61', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:41:24', '登录');
INSERT INTO `log` VALUES ('62', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:41:32', '登录');
INSERT INTO `log` VALUES ('63', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:41:42', '登录');
INSERT INTO `log` VALUES ('64', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-09-28 18:44:03', '登录');
INSERT INTO `log` VALUES ('65', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36。', '2014-09-28 18:44:15', '登录');
INSERT INTO `log` VALUES ('66', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-09 15:56:16', '登录');
INSERT INTO `log` VALUES ('67', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-09 16:00:33', '登录');
INSERT INTO `log` VALUES ('68', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-09 16:07:49', '登录');
INSERT INTO `log` VALUES ('69', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-09 16:48:35', '登录');
INSERT INTO `log` VALUES ('70', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 09:24:42', '登录');
INSERT INTO `log` VALUES ('71', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 09:57:21', '登录');
INSERT INTO `log` VALUES ('72', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 10:36:50', '登录');
INSERT INTO `log` VALUES ('73', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-10 10:38:11', '登录');
INSERT INTO `log` VALUES ('74', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 11:02:06', '登录');
INSERT INTO `log` VALUES ('75', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-10 11:03:09', '登录');
INSERT INTO `log` VALUES ('76', '刘碧锋(b051)登录成功，IP: 192.168.1.108，浏览器: Mozilla/5.0 (Linux; U; Android 4.1.2; zh-CN; MI 1S Build/JZO54K) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/9.9.5.489 U3/0.8.0 Mobile Safari/533.1。', '2014-10-10 11:08:15', '登录');
INSERT INTO `log` VALUES ('77', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 11:16:45', '登录');
INSERT INTO `log` VALUES ('78', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 11:16:53', '登录');
INSERT INTO `log` VALUES ('79', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 11:17:39', '登录');
INSERT INTO `log` VALUES ('80', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 15:36:16', '登录');
INSERT INTO `log` VALUES ('81', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 15:57:48', '登录');
INSERT INTO `log` VALUES ('82', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-10 17:13:03', '登录');
INSERT INTO `log` VALUES ('83', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 10:04:07', '登录');
INSERT INTO `log` VALUES ('84', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 10:29:45', '登录');
INSERT INTO `log` VALUES ('85', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 10:49:57', '登录');
INSERT INTO `log` VALUES ('86', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 10:50:44', '登录');
INSERT INTO `log` VALUES ('87', '超级管理员(admin)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 10:51:52', '登录');
INSERT INTO `log` VALUES ('88', '超级管理员(admin)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 10:53:14', '登录');
INSERT INTO `log` VALUES ('89', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 10:54:15', '登录');
INSERT INTO `log` VALUES ('90', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 11:18:08', '登录');
INSERT INTO `log` VALUES ('91', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 14:59:28', '登录');
INSERT INTO `log` VALUES ('92', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 15:00:06', '登录');
INSERT INTO `log` VALUES ('93', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 15:01:09', '登录');
INSERT INTO `log` VALUES ('94', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 15:14:33', '登录');
INSERT INTO `log` VALUES ('95', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 15:16:33', '登录');
INSERT INTO `log` VALUES ('96', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 15:19:26', '登录');
INSERT INTO `log` VALUES ('97', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 15:24:59', '登录');
INSERT INTO `log` VALUES ('98', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 15:27:52', '登录');
INSERT INTO `log` VALUES ('99', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 16:42:02', '登录');
INSERT INTO `log` VALUES ('100', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 16:42:31', '登录');
INSERT INTO `log` VALUES ('101', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 16:45:50', '登录');
INSERT INTO `log` VALUES ('102', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 16:46:47', '登录');
INSERT INTO `log` VALUES ('103', '刘碧锋(b051)登录成功，IP: 192.168.1.105，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-11 16:47:00', '登录');
INSERT INTO `log` VALUES ('104', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 16:47:50', '登录');
INSERT INTO `log` VALUES ('105', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 16:49:28', '登录');
INSERT INTO `log` VALUES ('106', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 17:05:49', '登录');
INSERT INTO `log` VALUES ('107', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 17:06:11', '登录');
INSERT INTO `log` VALUES ('108', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 17:06:25', '登录');
INSERT INTO `log` VALUES ('109', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 17:06:33', '登录');
INSERT INTO `log` VALUES ('110', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 17:06:41', '登录');
INSERT INTO `log` VALUES ('111', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 17:06:47', '登录');
INSERT INTO `log` VALUES ('112', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-11 17:06:55', '登录');
INSERT INTO `log` VALUES ('113', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-12 19:22:39', '登录');
INSERT INTO `log` VALUES ('114', '超级管理员(admin)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-12 19:24:51', '登录');
INSERT INTO `log` VALUES ('115', 'test(test)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-12 19:33:08', '登录');
INSERT INTO `log` VALUES ('116', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-12 19:33:22', '登录');
INSERT INTO `log` VALUES ('117', '超级管理员(admin)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-12 19:33:46', '登录');
INSERT INTO `log` VALUES ('118', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-13 10:03:26', '登录');
INSERT INTO `log` VALUES ('119', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-13 10:58:42', '登录');
INSERT INTO `log` VALUES ('120', '刘碧锋(b051)登录成功，IP: 192.168.1.118，浏览器: Mozilla/5.0 (Linux; U; Android 4.1.2; zh-cn; MI 1S Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 XiaoMi/MiuiBrowser/1.0。', '2014-10-13 11:14:27', '登录');
INSERT INTO `log` VALUES ('121', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 11:18:24', '登录');
INSERT INTO `log` VALUES ('122', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 18:24:10', '登录');
INSERT INTO `log` VALUES ('123', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 18:37:46', '登录');
INSERT INTO `log` VALUES ('124', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 18:39:18', '登录');
INSERT INTO `log` VALUES ('125', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-17 18:41:20', '登录');
INSERT INTO `log` VALUES ('126', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 18:53:23', '登录');
INSERT INTO `log` VALUES ('127', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 18:54:15', '登录');
INSERT INTO `log` VALUES ('128', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:07:26', '登录');
INSERT INTO `log` VALUES ('129', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:10:37', '登录');
INSERT INTO `log` VALUES ('130', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:14:20', '登录');
INSERT INTO `log` VALUES ('131', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:15:49', '登录');
INSERT INTO `log` VALUES ('132', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:15:57', '登录');
INSERT INTO `log` VALUES ('133', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:16:44', '登录');
INSERT INTO `log` VALUES ('134', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:17:12', '登录');
INSERT INTO `log` VALUES ('135', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:19:14', '登录');
INSERT INTO `log` VALUES ('136', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:58:50', '登录');
INSERT INTO `log` VALUES ('137', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 19:59:12', '登录');
INSERT INTO `log` VALUES ('138', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 20:00:00', '登录');
INSERT INTO `log` VALUES ('139', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 20:00:27', '登录');
INSERT INTO `log` VALUES ('140', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 20:01:38', '登录');
INSERT INTO `log` VALUES ('141', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 20:04:31', '登录');
INSERT INTO `log` VALUES ('142', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:14:30', '登录');
INSERT INTO `log` VALUES ('143', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:15:02', '登录');
INSERT INTO `log` VALUES ('144', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:39:47', '登录');
INSERT INTO `log` VALUES ('145', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:41:11', '登录');
INSERT INTO `log` VALUES ('146', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:44:06', '登录');
INSERT INTO `log` VALUES ('147', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:45:00', '登录');
INSERT INTO `log` VALUES ('148', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:45:16', '登录');
INSERT INTO `log` VALUES ('149', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko。', '2014-10-17 21:46:23', '登录');
INSERT INTO `log` VALUES ('150', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:46:50', '登录');
INSERT INTO `log` VALUES ('151', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:48:21', '登录');
INSERT INTO `log` VALUES ('152', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:49:03', '登录');
INSERT INTO `log` VALUES ('153', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:51:55', '登录');
INSERT INTO `log` VALUES ('154', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-17 21:53:55', '登录');
INSERT INTO `log` VALUES ('155', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-18 11:42:49', '登录');
INSERT INTO `log` VALUES ('156', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-18 11:44:24', '登录');
INSERT INTO `log` VALUES ('157', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-18 11:45:20', '登录');
INSERT INTO `log` VALUES ('158', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 16:39:25', '登录');
INSERT INTO `log` VALUES ('159', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 16:41:07', '登录');
INSERT INTO `log` VALUES ('160', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 16:41:47', '登录');
INSERT INTO `log` VALUES ('161', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 16:45:41', '登录');
INSERT INTO `log` VALUES ('162', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 16:46:59', '登录');
INSERT INTO `log` VALUES ('163', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:12:33', '登录');
INSERT INTO `log` VALUES ('164', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:18:48', '登录');
INSERT INTO `log` VALUES ('165', '刘碧锋(b051)登录成功，IP: 192.168.1.116，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:20:16', '登录');
INSERT INTO `log` VALUES ('166', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:26:37', '登录');
INSERT INTO `log` VALUES ('167', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:31:07', '登录');
INSERT INTO `log` VALUES ('168', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:31:16', '登录');
INSERT INTO `log` VALUES ('169', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:40:49', '登录');
INSERT INTO `log` VALUES ('170', '刘碧锋(b051)登录成功，IP: 127.0.0.1，浏览器: Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0。', '2014-10-19 17:42:06', '登录');

-- ----------------------------
-- Table structure for `persistent_logins`
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `series` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` char(32) NOT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------
INSERT INTO `persistent_logins` VALUES ('RPJn1/jdu2ceuEyitNkIdA==', '2014-10-19 17:42:45', 'f53e1794d23947a9e3e16c45150061b6', 'b051');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `sort` tinyint(1) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `defaultSet` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK3580768930FB80` (`parent_id`),
  CONSTRAINT `FK3580768930FB80` FOREIGN KEY (`parent_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '管理员', '0', null, '0');
INSERT INTO `role` VALUES ('2', '普通用户', '1', '1', '1');
INSERT INTO `role` VALUES ('5', '测试用户', '2', null, '0');
INSERT INTO `role` VALUES ('7', '宿舍1', '3', null, '0');

-- ----------------------------
-- Table structure for `role_auth`
-- ----------------------------
DROP TABLE IF EXISTS `role_auth`;
CREATE TABLE `role_auth` (
  `role_id` int(11) NOT NULL,
  `authority_id` int(11) NOT NULL,
  PRIMARY KEY (`authority_id`,`role_id`),
  KEY `FK13FEEFD15FDBE034` (`role_id`),
  KEY `FK13FEEFD1F3A110E0` (`authority_id`),
  CONSTRAINT `FK13FEEFD15FDBE034` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK13FEEFD1F3A110E0` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_auth
-- ----------------------------
INSERT INTO `role_auth` VALUES ('1', '1');
INSERT INTO `role_auth` VALUES ('1', '2');
INSERT INTO `role_auth` VALUES ('1', '3');
INSERT INTO `role_auth` VALUES ('1', '4');
INSERT INTO `role_auth` VALUES ('1', '5');
INSERT INTO `role_auth` VALUES ('1', '6');
INSERT INTO `role_auth` VALUES ('1', '7');
INSERT INTO `role_auth` VALUES ('1', '13');
INSERT INTO `role_auth` VALUES ('1', '18');
INSERT INTO `role_auth` VALUES ('1', '19');
INSERT INTO `role_auth` VALUES ('1', '20');
INSERT INTO `role_auth` VALUES ('1', '21');
INSERT INTO `role_auth` VALUES ('1', '22');
INSERT INTO `role_auth` VALUES ('1', '23');
INSERT INTO `role_auth` VALUES ('1', '24');
INSERT INTO `role_auth` VALUES ('1', '27');
INSERT INTO `role_auth` VALUES ('1', '28');
INSERT INTO `role_auth` VALUES ('1', '29');
INSERT INTO `role_auth` VALUES ('1', '30');
INSERT INTO `role_auth` VALUES ('1', '31');
INSERT INTO `role_auth` VALUES ('1', '32');
INSERT INTO `role_auth` VALUES ('1', '33');
INSERT INTO `role_auth` VALUES ('1', '34');
INSERT INTO `role_auth` VALUES ('1', '37');
INSERT INTO `role_auth` VALUES ('1', '38');
INSERT INTO `role_auth` VALUES ('1', '39');
INSERT INTO `role_auth` VALUES ('1', '40');
INSERT INTO `role_auth` VALUES ('1', '41');
INSERT INTO `role_auth` VALUES ('1', '43');
INSERT INTO `role_auth` VALUES ('1', '51');
INSERT INTO `role_auth` VALUES ('1', '52');
INSERT INTO `role_auth` VALUES ('1', '65');
INSERT INTO `role_auth` VALUES ('1', '76');
INSERT INTO `role_auth` VALUES ('1', '79');
INSERT INTO `role_auth` VALUES ('1', '81');
INSERT INTO `role_auth` VALUES ('1', '82');
INSERT INTO `role_auth` VALUES ('1', '86');
INSERT INTO `role_auth` VALUES ('1', '87');
INSERT INTO `role_auth` VALUES ('1', '88');
INSERT INTO `role_auth` VALUES ('2', '4');
INSERT INTO `role_auth` VALUES ('2', '76');
INSERT INTO `role_auth` VALUES ('2', '86');
INSERT INTO `role_auth` VALUES ('2', '87');
INSERT INTO `role_auth` VALUES ('2', '88');
INSERT INTO `role_auth` VALUES ('5', '76');
INSERT INTO `role_auth` VALUES ('5', '87');
INSERT INTO `role_auth` VALUES ('5', '88');
INSERT INTO `role_auth` VALUES ('7', '1');
INSERT INTO `role_auth` VALUES ('7', '33');
INSERT INTO `role_auth` VALUES ('7', '34');
INSERT INTO `role_auth` VALUES ('7', '38');
INSERT INTO `role_auth` VALUES ('7', '39');
INSERT INTO `role_auth` VALUES ('7', '40');
INSERT INTO `role_auth` VALUES ('7', '41');
INSERT INTO `role_auth` VALUES ('7', '76');
INSERT INTO `role_auth` VALUES ('7', '87');
INSERT INTO `role_auth` VALUES ('7', '88');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `himage` varchar(100) NOT NULL DEFAULT 'images/head.gif',
  `name` varchar(20) NOT NULL,
  `password` char(80) NOT NULL DEFAULT 'passwd',
  `department_id` int(11) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `allowLoginTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  KEY `FK36EBCB14D42CBF` (`department_id`),
  CONSTRAINT `FK36EBCB14D42CBF` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '2013-07-06 11:07:53', 'images/head.gif', '超级管理员', '586514b53380677c90c740725eb7f38248c0b5a712ea8cb790c8c26bd9b845689d188a4546c506d5', '1', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('2', 'a402', '2012-11-07 14:07:38', 'images/head.gif', '董良进', '5c8e23144eb5777346c07afdeb58f94b', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('3', 'a017', '2012-11-07 14:07:38', 'images/head.gif', '李标粼', '7a3cd3e558a66728ffcf818fbaaed675', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('4', 'b051中文', '2012-11-07 14:07:38', 'images/head.gif', '刘碧锋', 'd5aedd35d0a738356ab1a185f86623a493967b37', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('5', 'b053', '2012-11-07 14:07:38', 'images/head.gif', '李杨洲', '89e1ebe99b96d043477013c470e8fbaa', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('6', 'b048', '2012-11-07 14:07:38', 'images/head.gif', '刘伟华', 'b961d321f228f50132483fb772fc5a01', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('7', 'b050', '2012-11-07 14:07:38', 'images/head.gif', '黄杵铭', '83cc8f91c6d5d3e56aeb654248125146', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('8', 'b049', '2012-11-07 14:07:38', 'images/head.gif', '邓基', '7e591deed056d8aacbbea772788a63d8', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('9', 'b052', '2012-11-07 14:07:38', 'images/head.gif', '钟文波', '269e84e755496e1657edba261890a0f7', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('10', 'b032', '2012-11-07 14:07:38', 'images/head.gif', '黄兴华', '00c17a49b9b8d4184a182157321c524c', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('11', 'b022', '2012-11-07 14:07:38', 'images/head.gif', '张广新', '9b44e12ef5545ec79446dbcfb242fb13', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('12', 'b057', '2012-11-13 05:04:35', 'images/head.gif', '刘笑兰', 'f4643acae26c1221536d2570bde2c714', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('13', 'a457', '2012-12-21 13:00:27', 'images/head.gif', '刘晓锐', '538ff371bcb757a3aca41bf928930091', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('14', 'b039', '2012-12-21 17:26:18', 'images/head.gif', '冯博', '69b9e98b6df222fbc08f54b76bdb09dc', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('15', 'b055', '2012-12-23 15:47:37', 'images/head.gif', '黄晓慧', '883b7132f665b9cf3c191a1bea1b71b8', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('16', 'b056', '2012-12-29 12:33:42', 'images/head.gif', '李小雪', '4a828a75206c684fe825102394727147', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('17', 'b054', '2013-01-05 16:17:47', 'images/head.gif', '洪冰冰', '45ce36d73da972ae97b2c8ae9e32fb25', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('21', 'b051', '2014-02-25 12:26:43', 'images/head.gif', '刘碧锋', '74c59f51c1d71801a6e56201938b3ce24c0b5ea90815f79a53df4c1ad4fadd86ad0818ea231759e0', '3', '0', '2014-09-25 18:27:59');
INSERT INTO `user` VALUES ('22', 'test', '2014-09-01 17:31:59', 'images/head.gif', 'test', 'a830af6207f76a0ed3571fdca77376cc6e95e37b2410b073f45a7a885cea82cacd3c4dee7b0f8ecb', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('23', 'test中文', '2014-09-02 21:22:55', 'images/head.gif', 'test中文', 'passwd', '3', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('24', 'test003', '2014-09-02 21:31:09', 'images/head.gif', 'test', 'passwd', '1', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('25', 'bb', '2014-09-12 15:34:38', 'images/head.gif', 'bb', 'c3be7cf2877085fed38e0ec93b009e547e2929e0', '2', '0', '2014-09-17 12:17:05');
INSERT INTO `user` VALUES ('26', 'aa', '2014-09-12 15:50:41', 'images/head.gif', 'aa', 'e0934adf096f61c7137baa92436274ef03d4635b72cb043ccbc2f321e74f7d4e53f47657d856c351', '1', '0', '2014-09-17 19:23:08');
INSERT INTO `user` VALUES ('27', 'cc', '2014-09-12 16:09:22', 'images/head.gif', 'cc2', '02215b6ee9b89982532aa811f30f2e1f18f3aff4951a961d0546b9034fa2b43923e464e078d46ecf', '1', '0', '2014-09-17 19:17:44');
INSERT INTO `user` VALUES ('28', '111', '2014-09-17 12:19:16', 'images/head.gif', '111', '16945ddccddea4b3582473b831902dbfc101d8c7daad1f6c7b2b1f9c3d0adecc526b5cf4c3c42041', '2', '0', '2014-09-23 16:12:52');

-- ----------------------------
-- Table structure for `user_login`
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`date`,`user_id`),
  KEY `FK_3fmuuqnycdbyuxsew3r26cg34` (`user_id`),
  CONSTRAINT `FK_3fmuuqnycdbyuxsew3r26cg34` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_login
-- ----------------------------
INSERT INTO `user_login` VALUES ('2014-10-19 17:20:12', '21');
INSERT INTO `user_login` VALUES ('2014-10-12 19:33:05', '22');
INSERT INTO `user_login` VALUES ('2014-09-23 16:07:40', '28');
INSERT INTO `user_login` VALUES ('2014-09-23 16:07:48', '28');
INSERT INTO `user_login` VALUES ('2014-09-23 16:07:49', '28');
INSERT INTO `user_login` VALUES ('2014-09-23 16:07:51', '28');
INSERT INTO `user_login` VALUES ('2014-09-23 16:07:52', '28');
INSERT INTO `user_login` VALUES ('2014-09-23 16:14:40', '28');

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `FK143BF46A5FDBE034` (`role_id`),
  KEY `FK143BF46A53C05DF` (`user_id`),
  CONSTRAINT `FK143BF46A53C05DF` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK143BF46A5FDBE034` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('4', '1');
INSERT INTO `user_role` VALUES ('14', '1');
INSERT INTO `user_role` VALUES ('21', '1');
INSERT INTO `user_role` VALUES ('26', '1');
INSERT INTO `user_role` VALUES ('27', '1');
INSERT INTO `user_role` VALUES ('1', '2');
INSERT INTO `user_role` VALUES ('2', '2');
INSERT INTO `user_role` VALUES ('3', '2');
INSERT INTO `user_role` VALUES ('4', '2');
INSERT INTO `user_role` VALUES ('5', '2');
INSERT INTO `user_role` VALUES ('6', '2');
INSERT INTO `user_role` VALUES ('7', '2');
INSERT INTO `user_role` VALUES ('8', '2');
INSERT INTO `user_role` VALUES ('9', '2');
INSERT INTO `user_role` VALUES ('10', '2');
INSERT INTO `user_role` VALUES ('11', '2');
INSERT INTO `user_role` VALUES ('12', '2');
INSERT INTO `user_role` VALUES ('13', '2');
INSERT INTO `user_role` VALUES ('14', '2');
INSERT INTO `user_role` VALUES ('15', '2');
INSERT INTO `user_role` VALUES ('16', '2');
INSERT INTO `user_role` VALUES ('17', '2');
INSERT INTO `user_role` VALUES ('21', '2');
INSERT INTO `user_role` VALUES ('24', '2');
INSERT INTO `user_role` VALUES ('26', '2');
INSERT INTO `user_role` VALUES ('1', '5');
INSERT INTO `user_role` VALUES ('21', '5');
INSERT INTO `user_role` VALUES ('26', '5');
INSERT INTO `user_role` VALUES ('1', '7');
INSERT INTO `user_role` VALUES ('21', '7');
INSERT INTO `user_role` VALUES ('26', '7');
