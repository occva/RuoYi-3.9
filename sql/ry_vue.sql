/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : ry_vue

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 20/03/2026 18:39:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `ai_chat_message`;
CREATE TABLE `ai_chat_message`  (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `session_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID（可为空，支持匿名）',
  `message_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型（0用户 1AI）',
  `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  `context_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '上下文信息（JSON格式）',
  `response_time` int NULL DEFAULT 0 COMMENT '响应耗时（毫秒）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `idx_chat_session`(`session_id` ASC) USING BTREE,
  INDEX `idx_chat_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_chat_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI聊天记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_chat_message
-- ----------------------------

-- ----------------------------
-- Table structure for club
-- ----------------------------
DROP TABLE IF EXISTS `club`;
CREATE TABLE `club`  (
  `club_id` bigint NOT NULL AUTO_INCREMENT COMMENT '社团ID',
  `club_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '社团名称',
  `club_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '社团编码（唯一标识）',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '社团简介',
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '社团Logo图片地址',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '封面图片地址',
  `president_id` bigint NULL DEFAULT NULL COMMENT '社长用户ID',
  `president_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '社长姓名',
  `vice_president` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '副社长（多个用逗号分隔）',
  `member_count` int NULL DEFAULT 0 COMMENT '成员数量',
  `max_members` int NULL DEFAULT 0 COMMENT '最大成员数（0为不限制）',
  `founded_date` date NULL DEFAULT NULL COMMENT '成立日期',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '活动地点（如：304室）',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '联系电话',
  `contact_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '联系邮箱',
  `contact_qq` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'QQ群号',
  `contact_wechat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '微信号/公众号',
  `is_recruiting` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否招新（0否 1是）',
  `is_popular` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否热门推荐（0否 1是）',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览次数',
  `favorite_count` int NULL DEFAULT 0 COMMENT '收藏次数',
  `sort_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用 2已解散）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`club_id`) USING BTREE,
  UNIQUE INDEX `uk_club_code`(`club_code` ASC) USING BTREE,
  INDEX `idx_club_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_club_status`(`status` ASC) USING BTREE,
  INDEX `idx_club_popular`(`is_popular` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1872600000000000011 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club
-- ----------------------------
INSERT INTO `club` VALUES (1872600000000000001, '编程魔法师', 'coding_wizards', 1, '开发者分享知识、构建项目并参加黑客马拉松的社区。我们每周聚会一次，讨论最新的技术，开展开源项目，并为编程面试做准备。', 'https://images.unsplash.com/photo-1531482615713-2afd69097998?auto=format&fit=crop&q=80&w=800', '', 102, '张明', '李华,若依', 128, 0, '2020-09-01', '科技楼 304室', '', '', '', '', '1', '0', 27, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', 'president1', '2026-03-02 17:50:22', '每周三 18:00');
INSERT INTO `club` VALUES (1872600000000000002, '辩论协会', 'debate_society', 4, '通过每周对当前全球问题的辩论，磨练你的修辞和批判性思维能力。', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '', NULL, '鲍勃·史密斯', '', 45, 0, '2018-03-15', '学生活动中心 101', '', '', '', '', '1', '0', 3, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '每周二 17:00');
INSERT INTO `club` VALUES (1872600000000000003, '摄影俱乐部', 'photo_club', 2, '捕捉你周围的世界。每周摄影采风、工作坊和展览。', 'https://images.unsplash.com/photo-1542038784456-1ea8e935640e?auto=format&fit=crop&q=80&w=800', '', NULL, '查理·布朗', '', 82, 0, '2019-11-20', '艺术工作室 B', '', '', '', '', '1', '0', 1, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '每周四 16:30');
INSERT INTO `club` VALUES (1872600000000000004, '绿色地球', 'green_earth', 5, '通过校园倡议促进可持续发展和环境意识。', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?auto=format&fit=crop&q=80&w=800', '', NULL, '戴安娜', '', 200, 0, '2015-04-22', '社区花园', '', '', '', '', '1', '0', 7, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '每周五 15:00');
INSERT INTO `club` VALUES (1872600000000000005, '乐器合奏团', 'music_ensemble', 2, '加入其他音乐家进行即兴演奏、表演和音乐鉴赏之夜。', 'https://images.unsplash.com/photo-1511379938547-c1f69419868d?auto=format&fit=crop&q=80&w=800', '', NULL, '埃文', '', 65, 0, '2016-09-10', '音乐厅', '', '', '', '', '1', '0', 2, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '每周一 19:00');
INSERT INTO `club` VALUES (1872600000000000006, '机器人俱乐部', 'robotics_club', 1, '建造和编程机器人。参加全国比赛并学习机电一体化。', 'https://images.unsplash.com/photo-1561557944-6e7860d1a7eb?auto=format&fit=crop&q=80&w=800', '', NULL, '菲奥娜', '', 50, 0, '2021-01-15', '工程实验室', '', '', '', '', '1', '0', 0, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '每周六 10:00');
INSERT INTO `club` VALUES (1872600000000000007, 'AI 研习社', 'ai_study_group', 1, '探索人工智能的前沿技术，包括深度学习、计算机视觉和自然语言处理。', 'https://plus.unsplash.com/premium_photo-1683121710572-7723bd2e235d?auto=format&fit=crop&q=80&w=800', '', NULL, 'GPT-4', '', 150, 0, '2023-05-01', '创新中心 202', '', '', '', '', '1', '0', 0, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '每周五 14:00');
INSERT INTO `club` VALUES (1872600000000000008, '篮球社', 'basketball_club', 3, '热爱篮球的同学集结地，每周组织训练和校内友谊赛。', 'https://plus.unsplash.com/premium_photo-1683121710572-7723bd2e235d?auto=format&fit=crop&q=80&w=800', '', NULL, '科比·粉丝', '', 90, 0, '2010-09-01', '北区篮球场', '', '', '', '', '1', '0', 3, 0, 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '每周二、四 18:00');
INSERT INTO `club` VALUES (1872600000000000009, '校园公益行动社', NULL, 5, '围绕社区服务、环保倡导与助学帮扶开展公益项目，打造持续性的校园志愿服务团队。', '/profile/upload/2026/02/27/retouch_2024050523352230_20260227170902A001.jpg', '', 102, '张明（社长）', '', 1, 0, NULL, '', '13900139000', '', '', '', '1', '0', 36, 1, 0, '0', '0', 'admin', '2026-03-02 10:55:01', 'president1', '2026-03-02 18:12:21', 'Created from application #10002');
INSERT INTO `club` VALUES (1872600000000000010, '英伟达显卡俱乐部', NULL, 1, '英伟达显卡俱乐部英伟达显卡俱乐部英伟达显卡俱乐部英伟达显卡俱乐部', '/profile/upload/2026/03/02/mariia-shalabaieva-0SqsTxWhgNU-unsplash_20260302122058A003.jpg', '', 1, '若依', '', 1, 0, NULL, '', '13101000000', '', '', '', '1', '0', 15, 0, 0, '0', '0', 'admin', '2026-03-02 14:24:29', '', NULL, 'Created from application #10003');

-- ----------------------------
-- Table structure for club_achievement
-- ----------------------------
DROP TABLE IF EXISTS `club_achievement`;
CREATE TABLE `club_achievement`  (
  `achievement_id` bigint NOT NULL AUTO_INCREMENT COMMENT '成就ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `achievement_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '成就标题',
  `achievement_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '成就类型（award/competition/activity）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详细描述',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '图片地址',
  `achieve_date` date NULL DEFAULT NULL COMMENT '获得日期',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '级别（校级/市级/省级/国家级）',
  `sort_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1隐藏）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`achievement_id`) USING BTREE,
  INDEX `idx_achievement_club`(`club_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团荣誉/成就表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_achievement
-- ----------------------------
INSERT INTO `club_achievement` VALUES (1001, 1872600000000000001, '全国大学生编程大赛金奖', 'competition', '在2024年全国大学生编程大赛中荣获团队金奖', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '2024-05-20', '国家级', 1, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_achievement` VALUES (1002, 1872600000000000001, '校内优秀社团', 'award', '2023年度校级优秀学生社团荣誉称号', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '2023-12-15', '校级', 2, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_achievement` VALUES (1003, 1872600000000000003, '省级摄影展一等奖', 'competition', '社团成员在全省高校联合摄影展中获得第一名', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '2024-03-10', '省级', 1, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);

-- ----------------------------
-- Table structure for club_activity
-- ----------------------------
DROP TABLE IF EXISTS `club_activity`;
CREATE TABLE `club_activity`  (
  `activity_id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `activity_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动标题',
  `activity_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '活动类型（meeting/competition/training/social/other）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '活动详情',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '活动封面图片',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '活动地点',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `registration_start` datetime NULL DEFAULT NULL COMMENT '报名开始时间',
  `registration_end` datetime NULL DEFAULT NULL COMMENT '报名截止时间',
  `max_participants` int NULL DEFAULT 0 COMMENT '最大参与人数（0为不限制）',
  `current_participants` int NULL DEFAULT 0 COMMENT '当前报名人数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0待开始 1进行中 2已结束 3已取消）',
  `organizer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '组织者',
  `contact_info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '联系方式',
  `is_public` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否公开（0仅社团成员 1所有人可见）',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览次数',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`activity_id`) USING BTREE,
  INDEX `idx_activity_club`(`club_id` ASC) USING BTREE,
  INDEX `idx_activity_status`(`status` ASC) USING BTREE,
  INDEX `idx_activity_time`(`start_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1872700000000000013 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_activity
-- ----------------------------
INSERT INTO `club_activity` VALUES (1872700000000000001, 1872600000000000001, '黑客马拉松备战之夜', '', '备战即将到来的黑客马拉松，分享创意和组队。', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '304室', '2024-10-15 18:00:00', NULL, NULL, NULL, 0, 1, '1', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000002, 1872600000000000001, '特邀嘉宾：谷歌工程师', '', '来自谷歌的高级工程师分享职业发展经验。', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', 'A号礼堂', '2024-09-28 14:00:00', NULL, NULL, NULL, 0, 1, '1', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000003, 1872600000000000001, 'React 进阶工作坊', '', '深入理解React Hooks和性能优化。', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '2号实验室', '2024-11-05 19:00:00', NULL, NULL, NULL, 0, 2, '1', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000004, 1872600000000000002, '全校辩论大赛：网络社交是否取代了面对面交流', '比赛', '探讨现代科技对人类社交模式的影响。冠军将获得精美奖品！', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '演讲厅', '2026-03-04 18:34:21', '2026-03-04 21:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000005, 1872600000000000002, '辩论技巧工作坊', '培训', '学习如何构建有说服力的论点并提升临场反应能力。', 'https://images.unsplash.com/photo-1517048676732-d65bc937f952?auto=format&fit=crop&q=80&w=800', '101会议室', '2026-02-26 18:34:21', '2026-02-26 20:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000006, 1872600000000000003, '秋季校园外拍：寻找光影', '外拍', '拿起相机，记录下秋天校园里最美的瞬间。', 'https://images.unsplash.com/photo-1542038784456-1ea8e935640e?auto=format&fit=crop&q=80&w=800', '博雅湖畔', '2026-03-01 18:34:21', '2026-03-01 22:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000007, 1872600000000000003, '后期处理入门：Lightroom 实操', '培训', '由资深摄影师指导，教你如何修出电影感大片。', 'https://images.unsplash.com/photo-1554048612-b6a482bc67e5?auto=format&fit=crop&q=80&w=800', '机房501', '2026-02-27 18:34:21', '2026-02-27 20:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000008, 1872600000000000004, '\"植\"得期待：校园公益植树行动', '公益', '为了更绿色的校园，我们一起动手种下希望的树苗。', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?auto=format&fit=crop&q=80&w=800', '西区实验田', '2026-03-09 18:34:21', '2026-03-09 23:34:21', NULL, NULL, 0, 2, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000009, 1872600000000000005, '仲夏之夜：草坪草地音乐会', '演出', '在星空下，倾听古典与现代乐器的交织与融合。', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '大操场草坪', '2026-03-06 18:34:21', '2026-03-06 20:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000010, 1872600000000000006, '格斗机器人组装体验日', '动手', '想亲手制作一个能战斗的机器人吗？快来加入我们！', 'https://images.unsplash.com/photo-1485827404703-89b55fcc595e?auto=format&fit=crop&q=80&w=800', '机器人实验室', '2026-02-28 18:34:21', '2026-02-28 22:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000011, 1872600000000000007, '深度学习论文研讨会', '学术', '本周探讨最新的计算机视觉模型及其在工业界的落地。', 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?auto=format&fit=crop&q=80&w=800', '202教室', '2026-02-27 16:34:21', '2026-02-27 19:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_activity` VALUES (1872700000000000012, 1872600000000000008, '三对三邀请战：谁是路人王？', '比赛', '热血篮球，就在此刻。期待全校各路高手前来挑战。', 'https://images.unsplash.com/photo-1519766304817-4f37bda74a26?auto=format&fit=crop&q=80&w=800', '室内篮球场', '2026-03-03 18:34:21', '2026-03-04 00:34:21', NULL, NULL, 0, 0, '2', '', '', '1', 0, '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);

-- ----------------------------
-- Table structure for club_activity_registration
-- ----------------------------
DROP TABLE IF EXISTS `club_activity_registration`;
CREATE TABLE `club_activity_registration`  (
  `registration_id` bigint NOT NULL AUTO_INCREMENT COMMENT '报名ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户昵称',
  `student_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '学号',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '联系电话',
  `registration_time` datetime NULL DEFAULT NULL COMMENT '报名时间',
  `check_in_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '签到状态（0未签到 1已签到）',
  `check_in_time` datetime NULL DEFAULT NULL COMMENT '签到时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0待参加 1已参加 2已取消）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`registration_id`) USING BTREE,
  UNIQUE INDEX `uk_registration_activity_user`(`activity_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_registration_activity`(`activity_id` ASC) USING BTREE,
  INDEX `idx_registration_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_registration_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10006 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动报名表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_activity_registration
-- ----------------------------
INSERT INTO `club_activity_registration` VALUES (10000, 1872700000000000003, 1872600000000000001, 1, 'admin', '若依', 'admin', '15888888888', '2026-03-01 15:05:39', '1', '2026-03-08 17:06:26', '1', '0', '', NULL, '', NULL, NULL);
INSERT INTO `club_activity_registration` VALUES (10001, 1872700000000000008, 1872600000000000004, 1, 'admin', '若依', 'admin', '15888888888', '2026-03-01 15:11:39', '0', NULL, '0', '0', '', NULL, '', NULL, NULL);
INSERT INTO `club_activity_registration` VALUES (10002, 1872700000000000002, 1872600000000000001, 1, 'admin', '若依', 'admin', '15888888888', '2026-03-01 15:15:13', '0', NULL, '0', '0', '', NULL, '', NULL, NULL);
INSERT INTO `club_activity_registration` VALUES (10003, 1872700000000000001, 1872600000000000001, 1, 'admin', '若依', 'admin', '15888888888', '2026-03-01 21:05:21', '0', NULL, '0', '0', '', NULL, '', NULL, NULL);
INSERT INTO `club_activity_registration` VALUES (10004, 1872700000000000008, 1872600000000000004, 2, 'ry', '普通的若依', 'ry', '15666666666', '2026-03-01 21:39:49', '0', NULL, '0', '0', '', NULL, '', NULL, NULL);
INSERT INTO `club_activity_registration` VALUES (10005, 1872700000000000003, 1872600000000000001, 2, 'ry', '普通的若依', 'ry', '15666666666', '2026-03-06 19:17:14', '0', NULL, '0', '0', '', NULL, '', NULL, NULL);

-- ----------------------------
-- Table structure for club_album
-- ----------------------------
DROP TABLE IF EXISTS `club_album`;
CREATE TABLE `club_album`  (
  `album_id` bigint NOT NULL AUTO_INCREMENT COMMENT '相册ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '关联活动ID（可为空）',
  `album_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册名称',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '封面图片',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '相册描述',
  `photo_count` int NULL DEFAULT 0 COMMENT '照片数量',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览次数',
  `sort_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1隐藏）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`album_id`) USING BTREE,
  INDEX `idx_album_club`(`club_id` ASC) USING BTREE,
  INDEX `idx_album_activity`(`activity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团相册表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_album
-- ----------------------------

-- ----------------------------
-- Table structure for club_application
-- ----------------------------
DROP TABLE IF EXISTS `club_application`;
CREATE TABLE `club_application`  (
  `application_id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `club_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '社团名称',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户昵称',
  `student_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学号',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '性别（0男 1女 2未知）',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '专业',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '年级',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '班级',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '邮箱',
  `self_introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '自我介绍',
  `apply_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '申请理由',
  `special_skills` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '特长',
  `application_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态（0待审核 1已通过 2已拒绝 3已撤回）',
  `reviewer_id` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  `reviewer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '审核人姓名',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '审核意见',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`application_id`) USING BTREE,
  INDEX `idx_application_club`(`club_id` ASC) USING BTREE,
  INDEX `idx_application_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_application_status`(`review_status` ASC) USING BTREE,
  INDEX `idx_application_time`(`application_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入社申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_application
-- ----------------------------
INSERT INTO `club_application` VALUES (10000, 1872600000000000001, '编程魔法师', 2, 'ry', '若依', '20230101', '张三', '0', '计算机科学与技术', '2023级', '1班', '13888888888', 'zhangsan@example.com', '我是一名大一新生，对编程有着极其浓厚的兴趣，自学过Python和Java。', '希望通过加入社团提升实战能力，并结交志同道合的朋友。', '熟练使用Git，参加过校级编程比赛。', '2026-02-27 18:34:21', '1', 1, '若依', '2026-03-06 19:16:55', '', '0', 'system', '2026-02-27 18:34:21', 'admin', '2026-03-06 19:16:55', NULL);
INSERT INTO `club_application` VALUES (10001, 1872600000000000008, '篮球社', 3, 'test', '测试用户', '20220505', '李四', '0', '体育教育', '2022级', '3班', '13999999999', 'lisi@example.com', '以前在高中是校篮球队的队长，司职控球后卫。', '想在大学继续挥洒汗水，为校争光。', '擅长三分投射和组织进攻。', '2026-02-27 18:34:21', '0', NULL, '', NULL, '', '0', 'system', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_application` VALUES (10002, 1872600000000000003, '摄影俱乐部', 103, 'vicepresident1', '光影追随者', '20230912', '王小红', '1', '广告学', '2023级', '2班', '13777777777', 'wangxh@example.com', '喜欢记录生活中的美好瞬间，有一台自己的单反相机。', '希望能学习更多摄影构图和后期修图技巧。', '熟悉PS和Lightroom。', '2026-02-27 18:34:21', '0', NULL, '', NULL, '', '0', 'system', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_application` VALUES (10003, 1872600000000000001, '编程魔法师', 104, 'ai_lover', '算法达人', '20210302', '赵六', '0', '人工智能', '2021级', 'S班', '13666666666', 'zhaoliu@example.com', '大三学长，对大模型和深度学习有较深研究。', '想带带学弟学妹，整理社团技术资料库。', '精通PyTorch和TensorFlow。', '2026-02-27 18:34:21', '0', NULL, '', NULL, '', '0', 'system', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_application` VALUES (10004, 1872600000000000000, '编程狂人社', 2, 'ry', '若依', '20230101', '张三', '0', '计算机科学与技术', '2023级', '1班', '13888888888', 'zhangsan@example.com', '我是一名大一新生，对编程有着极其浓厚的兴趣，自学过Python和Java。', '希望通过加入社团提升实战能力，并结交志同道合的朋友。', '熟练使用Git，参加过校级编程比赛。', '2026-02-27 18:34:21', '0', NULL, '', NULL, '', '0', 'system', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_application` VALUES (10005, 1872600000000000001, '编程魔法师', 1, 'admin', '若依', '123', 'ry', '0', '', '', '', '15888888888', 'ry@163.com', NULL, '123', '', '2026-03-01 14:58:24', '1', 1, '若依', '2026-03-01 15:05:04', '', '0', 'admin', '2026-03-01 14:58:24', 'admin', '2026-03-01 15:05:04', NULL);
INSERT INTO `club_application` VALUES (10006, 1872600000000000004, '绿色地球', 2, 'ry', '普通的若依', '用户ry', '用户ry', '0', '', '', '', '15666666666', 'ry@qq.com', NULL, '用户ry', '', '2026-03-01 21:38:59', '1', 1, '若依', '2026-03-01 21:39:38', '', '0', 'ry', '2026-03-01 21:38:59', 'admin', '2026-03-01 21:39:38', NULL);
INSERT INTO `club_application` VALUES (10007, 1872600000000000009, '校园公益行动社', 1, 'admin', '若依', '111', '111', '0', '', '', '', '15888888888', 'ry@163.com', NULL, '111', '', '2026-03-02 12:57:05', '1', 105, '若依', '2026-03-02 12:57:21', '', '0', 'admin', '2026-03-02 12:57:05', 'clubp10002', '2026-03-02 12:57:21', NULL);
INSERT INTO `club_application` VALUES (10008, 1872600000000000001, '编程魔法师', 105, 'clubp10002', '若依', '@clubp10002', '@clubp10002', '0', '', '', '', '', '', NULL, '@clubp10002', '', '2026-03-02 17:49:15', '1', 102, '张明（社长）', '2026-03-02 17:49:30', '', '0', 'clubp10002', '2026-03-02 17:49:15', 'president1', '2026-03-02 17:49:30', NULL);
INSERT INTO `club_application` VALUES (10009, 1872600000000000009, '校园公益行动社', 102, 'president1', '张明（社长）', '张明', '张明', '0', '', '', '', '13800000002', 'president1@example.com', NULL, '张明', '', '2026-03-02 18:07:07', '1', 105, '若依', '2026-03-02 18:07:20', '', '0', 'president1', '2026-03-02 18:07:07', 'clubp10002', '2026-03-02 18:07:20', NULL);

-- ----------------------------
-- Table structure for club_category
-- ----------------------------
DROP TABLE IF EXISTS `club_category`;
CREATE TABLE `club_category`  (
  `category_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类编码（technology/art/sports/academic/volunteer）',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `sort_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`category_id`) USING BTREE,
  UNIQUE INDEX `uk_category_code`(`category_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1872500000000000002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_category
-- ----------------------------
INSERT INTO `club_category` VALUES (1, 'technology', '科技类', 1, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '编程、AI、机器人等');
INSERT INTO `club_category` VALUES (2, 'art', '艺术类', 2, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '音乐、绘画、摄影等');
INSERT INTO `club_category` VALUES (3, 'sports', '体育类', 3, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '篮球、足球、跑步等');
INSERT INTO `club_category` VALUES (4, 'academic', '学术类', 4, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '数学、物理、英语等');
INSERT INTO `club_category` VALUES (5, 'volunteer', '志愿类', 5, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, '公益、支教、环保等');

-- ----------------------------
-- Table structure for club_create_application
-- ----------------------------
DROP TABLE IF EXISTS `club_create_application`;
CREATE TABLE `club_create_application`  (
  `apply_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'application id',
  `club_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'target club name',
  `category_id` bigint NOT NULL COMMENT 'club category id',
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'club logo',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'contact phone',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'club description',
  `apply_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'apply reason',
  `activity_plan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'activity plan',
  `core_members` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'core members plan',
  `advisor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'advisor name',
  `advisor_contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'advisor contact',
  `applicant_user_id` bigint NOT NULL COMMENT 'applicant user id',
  `applicant_user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'applicant username',
  `applicant_nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'applicant nickname',
  `applicant_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'applicant phone',
  `applicant_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'applicant email',
  `apply_time` datetime NULL DEFAULT NULL COMMENT 'application time',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT 'review status (0 pending 1 approved 2 rejected)',
  `reviewer_id` bigint NULL DEFAULT NULL COMMENT 'reviewer id',
  `reviewer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'reviewer name',
  `review_time` datetime NULL DEFAULT NULL COMMENT 'review time',
  `review_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'review comment',
  `approved_club_id` bigint NULL DEFAULT NULL COMMENT 'approved club id',
  `admin_user_id` bigint NULL DEFAULT NULL COMMENT 'generated admin user id',
  `admin_user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'generated admin username',
  `admin_init_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'generated admin init password',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT 'delete flag (0 exists 2 deleted)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'create by',
  `create_time` datetime NULL DEFAULT NULL COMMENT 'create time',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'update by',
  `update_time` datetime NULL DEFAULT NULL COMMENT 'update time',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'remark',
  PRIMARY KEY (`apply_id`) USING BTREE,
  INDEX `idx_cca_status`(`review_status` ASC) USING BTREE,
  INDEX `idx_cca_apply_time`(`apply_time` ASC) USING BTREE,
  INDEX `idx_cca_applicant`(`applicant_user_id` ASC) USING BTREE,
  INDEX `idx_cca_club_name`(`club_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10005 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'new club create application' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_create_application
-- ----------------------------
INSERT INTO `club_create_application` VALUES (10000, '校园公益行动社', 1872500000000000005, '/profile/upload/2026/02/27/retouch_2024050523352230_20260227170902A001.jpg', '13900139000', '围绕社区服务、环保倡导与助学帮扶开展公益项目，打造持续性的校园志愿服务团队。', '整合校内志愿力量，形成稳定项目机制，让更多同学在实践中提升社会责任感与组织能力。', '每月开展1次社区服务、每两月组织1次环保主题活动、每学期策划1次大型公益联动活动。每月开展1次社区服务、每两月组织1次环保主题活动、每学期策划1次大型公益联动活动。\",', '每月开展1次社区服务、每两月组织1次环保主题活动、每学期策划1次大型公益联动活动。每月开展1次社区服务、每两月组织1次环保主题活动、每学期策划1次大型公益联动活动。\",', '李老师', 'lilaoshi@univ.edu.cn', 1, 'admin', '若依', '15888888888', 'ry@163.com', '2026-02-27 17:10:46', '2', 1, '若依', '2026-03-01 11:11:37', '', NULL, NULL, '', '', '0', 'admin', '2026-02-27 17:10:46', 'admin', '2026-03-01 11:11:37', '已确认材料：社团章程草案、核心成员名单与分工、指导老师确认信息、学期活动计划、经费与资源说明\n补充材料链接：无');
INSERT INTO `club_create_application` VALUES (10001, '校园公益行动社', 1872500000000000005, '/profile/upload/2026/02/27/retouch_2024050523352230_20260227170902A001.jpg', '13900139000', '围绕社区服务、环保倡导与助学帮扶开展公益项目，打造持续性的校园志愿服务团队。', '整合校内志愿力量，形成稳定项目机制，让更多同学在实践中提升社会责任感与组织能力。', NULL, NULL, '李老师', 'lilaoshi@univ.edu.cn', 1, 'admin', '若依', '15888888888', 'ry@163.com', '2026-03-02 10:47:44', '3', NULL, '', NULL, '', NULL, NULL, '', '', '0', 'admin', '2026-03-02 10:47:44', 'admin', '2026-03-02 10:48:29', '已确认材料：社团章程草案、核心成员名单与分工、指导老师确认信息、学期活动计划、经费与资源说明\n补充材料链接：无');
INSERT INTO `club_create_application` VALUES (10002, '校园公益行动社', 1872500000000000005, '/profile/upload/2026/02/27/retouch_2024050523352230_20260227170902A001.jpg', '13900139000', '围绕社区服务、环保倡导与助学帮扶开展公益项目，打造持续性的校园志愿服务团队。', '整合校内志愿力量，形成稳定项目机制，让更多同学在实践中提升社会责任感与组织能力。', NULL, NULL, '李老师', 'lilaoshi@univ.edu.cn', 1, 'admin', '若依', '15888888888', 'ry@163.com', '2026-03-02 10:48:50', '1', 1, '若依', '2026-03-02 10:55:02', '审核通过，已创建社长后台账号：clubp10002，初始密码：123456', 1872600000000000009, 105, 'clubp10002', '123456', '0', 'admin', '2026-03-02 10:48:50', 'admin', '2026-03-02 10:55:02', '已确认材料：社团章程草案、核心成员名单与分工、指导老师确认信息、学期活动计划、经费与资源说明\n补充材料链接：无');
INSERT INTO `club_create_application` VALUES (10003, '英伟达显卡俱乐部', 1, '/profile/upload/2026/03/02/mariia-shalabaieva-0SqsTxWhgNU-unsplash_20260302122058A003.jpg', '13101000000', '英伟达显卡俱乐部英伟达显卡俱乐部英伟达显卡俱乐部英伟达显卡俱乐部', '英伟达显卡俱乐部英伟达显卡俱乐部英伟达显卡俱乐部英伟达显卡俱乐部', NULL, NULL, NULL, '', 1, 'admin', '若依', '15888888888', 'ry@163.com', '2026-03-02 12:21:05', '1', 1, '若依', '2026-03-02 14:24:30', '审核通过，已将申请人账号升级为社长后台账号：admin（密码保持不变）', 1872600000000000010, 1, 'admin', '', '0', 'admin', '2026-03-02 12:21:05', 'admin', '2026-03-02 14:24:30', '已确认材料：社团章程草案、核心成员名单与分工、指导老师确认信息、经费与资源说明\n补充材料链接：无');
INSERT INTO `club_create_application` VALUES (10004, '111', 1, '/profile/upload/2026/03/09/OIP-C_20260309213020A001.jpg', '18888888888', '111111111111111111111111111111111111', '11111111111111111111111111111111111111', NULL, NULL, NULL, '', 1, 'admin', '若依', '15888888888', 'ry@163.com', '2026-03-09 21:30:26', '0', NULL, '', NULL, '', NULL, NULL, '', '', '0', 'admin', '2026-03-09 21:30:26', '', NULL, '已确认材料：社团章程草案、核心成员名单与分工、指导老师确认信息、学期活动计划、经费与资源说明\n补充材料链接：无');

-- ----------------------------
-- Table structure for club_favorite
-- ----------------------------
DROP TABLE IF EXISTS `club_favorite`;
CREATE TABLE `club_favorite`  (
  `favorite_id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`favorite_id`) USING BTREE,
  UNIQUE INDEX `uk_favorite_club_user`(`club_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_favorite_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_favorite_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10013 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_favorite
-- ----------------------------
INSERT INTO `club_favorite` VALUES (10006, 1872600000000000001, 1, '2026-03-01 14:10:02');
INSERT INTO `club_favorite` VALUES (10011, 1872600000000000004, 1, '2026-03-11 20:41:34');
INSERT INTO `club_favorite` VALUES (10012, 1872600000000000009, 1, '2026-03-11 20:49:30');

-- ----------------------------
-- Table structure for club_member
-- ----------------------------
DROP TABLE IF EXISTS `club_member`;
CREATE TABLE `club_member`  (
  `member_id` bigint NOT NULL AUTO_INCREMENT COMMENT '成员ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户昵称',
  `student_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '学号',
  `role_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '角色类型（1社长 2副社长 3普通成员）',
  `position_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '职位名称',
  `join_date` datetime NULL DEFAULT NULL COMMENT '加入日期',
  `contribution` int NULL DEFAULT 0 COMMENT '贡献值',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1已退出）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`member_id`) USING BTREE,
  UNIQUE INDEX `uk_club_user`(`club_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_member_club`(`club_id` ASC) USING BTREE,
  INDEX `idx_member_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_member_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_member
-- ----------------------------
INSERT INTO `club_member` VALUES (10000, 1872600000000000001, 102, 'president1', '张明', '', '1', '社长', '2026-02-27 18:34:21', 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_member` VALUES (10001, 1872600000000000001, 103, 'vicepresident1', '李华', '', '2', '副社长', '2026-02-27 18:34:21', 0, '0', '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_member` VALUES (10002, 1872600000000000001, 1, 'admin', '若依', '123', '3', '', '2026-03-01 15:05:04', 0, '0', '0', 'admin', '2026-03-01 15:05:04', '', NULL, NULL);
INSERT INTO `club_member` VALUES (10003, 1872600000000000004, 2, 'ry', '普通的若依', '用户ry', '3', '', '2026-03-01 21:39:38', 0, '0', '0', 'admin', '2026-03-01 21:39:38', '', NULL, NULL);
INSERT INTO `club_member` VALUES (10004, 1872600000000000009, 105, 'clubp10002', '若依', '', '3', '', '2026-03-02 10:55:02', 0, '0', '0', 'admin', '2026-03-02 10:55:02', 'president1', '2026-03-02 18:12:21', NULL);
INSERT INTO `club_member` VALUES (10005, 1872600000000000009, 1, 'admin', '若依', '111', '3', '', '2026-03-02 12:57:21', 0, '0', '0', 'clubp10002', '2026-03-02 12:57:21', 'clubp10002', '2026-03-02 17:45:46', NULL);
INSERT INTO `club_member` VALUES (10006, 1872600000000000010, 1, 'admin', '若依', '', '1', '', '2026-03-02 14:24:30', 0, '0', '0', 'admin', '2026-03-02 14:24:30', '', NULL, NULL);
INSERT INTO `club_member` VALUES (10007, 1872600000000000001, 105, 'clubp10002', '若依', '@clubp10002', '2', '', '2026-03-02 17:49:30', 0, '0', '0', 'president1', '2026-03-02 17:49:30', 'president1', '2026-03-02 17:50:22', NULL);
INSERT INTO `club_member` VALUES (10008, 1872600000000000009, 102, 'president1', '张明（社长）', '张明', '1', '', '2026-03-02 18:07:20', 0, '0', '0', 'clubp10002', '2026-03-02 18:07:20', 'clubp10002', '2026-03-02 18:07:33', NULL);
INSERT INTO `club_member` VALUES (10009, 1872600000000000001, 2, 'ry', '若依', '20230101', '3', '', '2026-03-06 19:16:55', 0, '0', '0', 'admin', '2026-03-06 19:16:55', '', NULL, NULL);

-- ----------------------------
-- Table structure for club_notice
-- ----------------------------
DROP TABLE IF EXISTS `club_notice`;
CREATE TABLE `club_notice`  (
  `notice_id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `notice_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '公告类型（1通知 2公告 3紧急）',
  `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公告内容',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '封面图片',
  `is_top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否置顶（0否 1是）',
  `is_important` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否重要（0否 1是）',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览次数',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `publisher_id` bigint NULL DEFAULT NULL COMMENT '发布人ID',
  `publisher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '发布人姓名',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0草稿 1已发布 2已撤回）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE,
  INDEX `idx_notice_club`(`club_id` ASC) USING BTREE,
  INDEX `idx_notice_status`(`status` ASC) USING BTREE,
  INDEX `idx_notice_publish_time`(`publish_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1872800000000000003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_notice
-- ----------------------------
INSERT INTO `club_notice` VALUES (1872800000000000001, 1872600000000000001, '会费缴纳通知', '1', '请在月底前缴纳会员费。', '', '0', '0', 0, '2024-10-01 09:00:00', NULL, '', '1', '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);
INSERT INTO `club_notice` VALUES (1872800000000000002, 1872600000000000001, '迎新会确认', '1', '本周五的迎新会已确认！', '', '0', '0', 0, '2024-09-15 10:00:00', NULL, '', '1', '0', 'admin', '2026-02-27 18:34:21', '', NULL, NULL);

-- ----------------------------
-- Table structure for club_photo
-- ----------------------------
DROP TABLE IF EXISTS `club_photo`;
CREATE TABLE `club_photo`  (
  `photo_id` bigint NOT NULL AUTO_INCREMENT COMMENT '照片ID',
  `album_id` bigint NOT NULL COMMENT '相册ID',
  `photo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '照片地址',
  `photo_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '照片标题',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '照片描述',
  `upload_user_id` bigint NULL DEFAULT NULL COMMENT '上传人ID',
  `upload_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '上传人姓名',
  `sort_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1隐藏）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`photo_id`) USING BTREE,
  INDEX `idx_photo_album`(`album_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '相册照片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club_photo
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2025-06-13 22:38:46', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-06-13 22:38:46', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2025-06-13 22:38:46', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'false', 'Y', 'admin', '2025-06-13 22:38:46', 'admin', '2025-12-27 23:46:37', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '2025-06-13 22:38:46', 'admin', '2025-12-27 15:04:59', '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2025-06-13 22:38:46', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `sys_config` VALUES (7, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '1', 'Y', 'admin', '2025-06-13 22:38:46', '', NULL, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO `sys_config` VALUES (8, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', 'admin', '2025-06-13 22:38:46', '', NULL, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-06-13 22:38:43', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 1, '科技类', 'technology', 'club_category', '', 'primary', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (101, 2, '艺术类', 'art', 'club_category', '', 'success', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (102, 3, '体育类', 'sports', 'club_category', '', 'warning', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (103, 4, '学术类', 'academic', 'club_category', '', 'info', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (104, 5, '志愿类', 'volunteer', 'club_category', '', 'danger', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (105, 1, '会议', 'meeting', 'activity_type', '', '', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (106, 2, '比赛', 'competition', 'activity_type', '', '', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (107, 3, '培训', 'training', 'activity_type', '', '', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (108, 4, '社交', 'social', 'activity_type', '', '', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (109, 5, '其他', 'other', 'activity_type', '', '', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (110, 1, '待开始', '0', 'activity_status', '', 'info', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (111, 2, '进行中', '1', 'activity_status', '', 'primary', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (112, 3, '已结束', '2', 'activity_status', '', 'success', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (113, 4, '已取消', '3', 'activity_status', '', 'danger', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (114, 1, '待审核', '0', 'application_status', '', 'warning', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (115, 2, '已通过', '1', 'application_status', '', 'success', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (116, 3, '已拒绝', '2', 'application_status', '', 'danger', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (117, 4, '已撤回', '3', 'application_status', '', 'info', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (118, 1, '社长', '1', 'club_role_type', '', 'danger', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (119, 2, '副社长', '2', 'club_role_type', '', 'warning', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (120, 3, '普通成员', '3', 'club_role_type', '', 'primary', 'N', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (121, 1, '通知', '1', 'club_notice_type', '', 'info', 'Y', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (122, 2, '公告', '2', 'club_notice_type', '', 'primary', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (123, 3, '紧急', '3', 'club_notice_type', '', 'danger', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (124, 1, '草稿', '0', 'club_notice_status', '', 'info', 'Y', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (125, 2, '已发布', '1', 'club_notice_status', '', 'success', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (126, 3, '已撤回', '2', 'club_notice_status', '', 'warning', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (127, 1, '正常', '0', 'club_member_status', '', 'success', 'Y', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (128, 2, '禁言', '1', 'club_member_status', '', 'warning', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (129, 3, '已退社', '2', 'club_member_status', '', 'danger', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (130, 1, '正常', '0', 'club_status', '', 'success', 'Y', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (131, 2, '停用', '1', 'club_status', '', 'danger', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (132, 3, '已解散', '2', 'club_status', '', 'info', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (133, 1, '未签到', '0', 'activity_checkin_status', '', 'info', 'Y', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (134, 2, '已签到', '1', 'activity_checkin_status', '', 'success', 'N', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2025-06-13 22:38:45', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2025-06-13 22:38:46', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, '社团分类', 'club_category', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '社团分类列表');
INSERT INTO `sys_dict_type` VALUES (101, '活动类型', 'activity_type', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '社团活动类型列表');
INSERT INTO `sys_dict_type` VALUES (102, '活动状态', 'activity_status', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '社团活动状态列表');
INSERT INTO `sys_dict_type` VALUES (103, '申请状态', 'application_status', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '入社申请状态列表');
INSERT INTO `sys_dict_type` VALUES (104, '社团角色', 'club_role_type', '0', 'admin', '2025-12-21 15:17:14', '', NULL, '社团成员角色类型');
INSERT INTO `sys_dict_type` VALUES (105, '社团公告类型', 'club_notice_type', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '社团公告类型');
INSERT INTO `sys_dict_type` VALUES (106, '社团公告状态', 'club_notice_status', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '社团公告状态');
INSERT INTO `sys_dict_type` VALUES (107, '社团成员状态', 'club_member_status', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '社团成员状态');
INSERT INTO `sys_dict_type` VALUES (108, '社团状态', 'club_status', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '社团状态');
INSERT INTO `sys_dict_type` VALUES (109, '活动签到状态', 'activity_checkin_status', '0', 'admin', '2026-01-02 02:54:48', '', NULL, '活动签到状态');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2025-06-13 22:38:46', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2025-06-13 22:38:46', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2025-06-13 22:38:46', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 404 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3404 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 7, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2025-06-13 22:38:44', 'admin', '2025-10-21 18:32:48', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 9, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2025-06-13 22:38:44', 'admin', '2025-10-21 18:32:42', '系统监控目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2025-06-13 22:38:44', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2025-06-13 22:38:44', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2025-06-13 22:38:44', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2025-06-13 22:38:44', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2025-06-13 22:38:44', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2025-06-13 22:38:44', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2025-06-13 22:38:44', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2025-06-13 22:38:44', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2025-06-13 22:38:44', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2025-06-13 22:38:44', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2025-06-13 22:38:44', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2025-06-13 22:38:44', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2025-06-13 22:38:44', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2025-06-13 22:38:44', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2025-06-13 22:38:44', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2025-06-13 22:38:44', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-06-13 22:38:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3100, '社团信息', 0, 1, 'club-info', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'component', 'admin', '2025-12-28 01:02:59', 'admin', '2025-12-28 01:10:27', '社团信息管理目录');
INSERT INTO `sys_menu` VALUES (3101, '社团列表', 3100, 1, 'list', 'club/list/index', NULL, '', 1, 0, 'C', '0', '0', 'system:club:list', 'peoples', 'admin', '2025-12-28 01:02:59', '', NULL, '社团列表管理');
INSERT INTO `sys_menu` VALUES (3102, '社团分类', 3100, 2, 'category', 'club/category/index', NULL, '', 1, 0, 'C', '0', '0', 'system:category:list', 'tree', 'admin', '2025-12-28 01:02:59', '', NULL, '社团分类管理');
INSERT INTO `sys_menu` VALUES (3103, '荣誉管理', 3100, 3, 'achievement', 'club/achievement/index', NULL, '', 1, 0, 'C', '0', '0', 'system:achievement:list', 'star', 'admin', '2025-12-28 01:02:59', '', NULL, '社团荣誉管理');
INSERT INTO `sys_menu` VALUES (3111, '社团查询', 3101, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:club:query', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3112, '社团新增', 3101, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:club:add', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3113, '社团修改', 3101, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:club:edit', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3114, '社团删除', 3101, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:club:remove', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3115, '社团导出', 3101, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:club:export', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3121, '分类查询', 3102, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:category:query', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3122, '分类新增', 3102, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:category:add', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3123, '分类修改', 3102, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:category:edit', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3124, '分类删除', 3102, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:category:remove', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3131, '荣誉查询', 3103, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:achievement:query', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3132, '荣誉新增', 3103, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:achievement:add', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3133, '荣誉修改', 3103, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:achievement:edit', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3134, '荣誉删除', 3103, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:achievement:remove', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3135, '荣誉导出', 3103, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:achievement:export', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3200, '社团运营', 0, 2, 'club-operation', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'guide', 'admin', '2025-12-28 01:03:02', '', NULL, '社团运营管理目录');
INSERT INTO `sys_menu` VALUES (3201, '入社申请', 3200, 1, 'application', 'club/application/index', NULL, '', 1, 0, 'C', '0', '0', 'club:application:list', 'button', 'admin', '2025-12-28 01:03:02', 'admin', '2025-12-28 01:10:54', '入社申请审核');
INSERT INTO `sys_menu` VALUES (3202, '成员管理', 3200, 2, 'member', 'club/member/index', NULL, '', 1, 0, 'C', '0', '0', 'club:member:list', 'user', 'admin', '2025-12-28 01:03:02', '', NULL, '社团成员管理');
INSERT INTO `sys_menu` VALUES (3203, '申请统计', 3200, 3, 'application-stat', 'club/application/stat', NULL, '', 1, 0, 'C', '0', '0', 'club:application:list', 'chart', 'admin', '2025-12-28 01:03:02', '', NULL, '申请数据统计');
INSERT INTO `sys_menu` VALUES (3204, '新社团申请', 3100, 4, 'club-apply', 'club/clubApply/index', '', '', 1, 0, 'C', '0', '0', 'club:createApply:list', 'button', 'admin', '2026-02-27 16:30:36', 'admin', '2026-02-27 17:34:17', '新社团申请审核');
INSERT INTO `sys_menu` VALUES (3211, '申请查询', 3201, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:query', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3212, '申请审核', 3201, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:review', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3213, '申请删除', 3201, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:remove', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3214, '申请导出', 3201, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:export', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3221, '成员查询', 3202, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:query', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3222, '成员新增', 3202, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:add', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3223, '成员修改', 3202, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:edit', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3224, '成员删除', 3202, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:remove', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3225, '成员导出', 3202, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:export', '#', 'admin', '2026-02-25 19:15:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3231, '新社团申请查询', 3204, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:query', '#', 'admin', '2026-02-27 16:30:36', 'admin', '2026-02-27 17:33:39', '');
INSERT INTO `sys_menu` VALUES (3232, '新社团申请审核', 3204, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:review', '#', 'admin', '2026-02-27 16:30:36', 'admin', '2026-02-27 17:33:39', '');
INSERT INTO `sys_menu` VALUES (3233, '新社团申请删除', 3204, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:remove', '#', 'admin', '2026-02-27 16:30:36', 'admin', '2026-02-27 17:33:39', '');
INSERT INTO `sys_menu` VALUES (3234, '新社团申请导出', 3204, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:export', '#', 'admin', '2026-02-27 16:30:36', 'admin', '2026-02-27 17:33:39', '');
INSERT INTO `sys_menu` VALUES (3300, '社团宣传', 0, 3, 'club-promotion', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'message', 'admin', '2025-12-28 01:03:06', '', NULL, '社团宣传管理目录');
INSERT INTO `sys_menu` VALUES (3301, '活动管理', 3300, 1, 'activity', 'club/activity/index', NULL, '', 1, 0, 'C', '0', '0', 'system:activity:list', 'date', 'admin', '2025-12-28 01:03:07', '', NULL, '社团活动管理');
INSERT INTO `sys_menu` VALUES (3302, '公告管理', 3300, 2, 'notice', 'club/notice/index', NULL, '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2025-12-28 01:03:07', '', NULL, '社团公告管理');
INSERT INTO `sys_menu` VALUES (3303, '报名管理', 3300, 3, 'registration', 'club/registration/index', '', '', 1, 0, 'C', '0', '0', 'club:registration:list', 'checkbox', 'admin', '2025-12-28 01:03:07', 'admin', '2026-03-02 13:37:59', '活动报名管理');
INSERT INTO `sys_menu` VALUES (3311, '活动查询', 3301, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:activity:query', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3312, '活动新增', 3301, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:activity:add', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3313, '活动修改', 3301, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:activity:edit', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3314, '活动删除', 3301, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:activity:remove', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3331, '报名查询', 3303, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:registration:query', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3332, '报名签到', 3303, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:registration:edit', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3333, '报名移除', 3303, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:registration:remove', '#', 'admin', '2026-03-02 13:38:09', 'admin', '2026-03-02 13:38:09', '');
INSERT INTO `sys_menu` VALUES (3400, '数据统计', 0, 4, 'statistics', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', '2025-12-28 01:03:22', '', NULL, '数据统计目录');
INSERT INTO `sys_menu` VALUES (3401, '社团统计', 3400, 1, 'club-stat', 'statistics/club-stat/index', NULL, '', 1, 0, 'C', '0', '0', 'system:statistics:club', 'peoples', 'admin', '2025-12-28 01:03:22', '', NULL, '社团数据统计');
INSERT INTO `sys_menu` VALUES (3402, '成员统计', 3400, 2, 'member-stat', 'statistics/member-stat/index', NULL, '', 1, 0, 'C', '0', '0', 'system:statistics:member', 'user', 'admin', '2025-12-28 01:03:22', '', NULL, '成员数据统计');
INSERT INTO `sys_menu` VALUES (3403, '活动统计', 3400, 3, 'activity-stat', 'statistics/activity-stat/index', NULL, '', 1, 0, 'C', '0', '0', 'system:statistics:activity', 'date', 'admin', '2025-12-28 01:03:22', '', NULL, '活动数据统计');
INSERT INTO `sys_menu` VALUES (3404, '页脚配置', 1, 10, 'footer', 'system/footer/index', NULL, '', 1, 0, 'C', '0', '0', 'system:footer:query', 'guide', 'admin', '2026-04-21 00:00:00', '', NULL, '用户端页脚配置菜单');
INSERT INTO `sys_menu` VALUES (3405, '页脚查询', 3404, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:footer:query', '#', 'admin', '2026-04-21 00:00:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3406, '页脚修改', 3404, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:footer:edit', '#', 'admin', '2026-04-21 00:00:00', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2025-06-13 22:38:46', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2025-06-13 22:38:46', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 248 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------


-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2025-06-13 22:38:43', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2025-06-13 22:38:43', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2025-06-13 22:38:43', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2025-06-13 22:38:43', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-06-13 22:38:44', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2025-06-13 22:38:44', 'admin', '2025-12-27 15:12:26', '普通角色');
INSERT INTO `sys_role` VALUES (100, '社团管理员', 'club_admin', 3, '1', 1, 1, '0', '0', 'admin', '2025-12-28 00:58:04', '', NULL, '可管理所有社团信息');
INSERT INTO `sys_role` VALUES (101, '社长', 'president', 4, '5', 1, 1, '0', '0', 'admin', '2025-12-28 00:58:04', '', NULL, '管理自己的社团');
INSERT INTO `sys_role` VALUES (102, '副社长', 'vice_president', 5, '5', 1, 1, '0', '0', 'admin', '2025-12-28 00:58:04', '', NULL, '协助管理社团');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 3100);
INSERT INTO `sys_role_menu` VALUES (1, 3101);
INSERT INTO `sys_role_menu` VALUES (1, 3102);
INSERT INTO `sys_role_menu` VALUES (1, 3103);
INSERT INTO `sys_role_menu` VALUES (1, 3111);
INSERT INTO `sys_role_menu` VALUES (1, 3112);
INSERT INTO `sys_role_menu` VALUES (1, 3113);
INSERT INTO `sys_role_menu` VALUES (1, 3114);
INSERT INTO `sys_role_menu` VALUES (1, 3115);
INSERT INTO `sys_role_menu` VALUES (1, 3121);
INSERT INTO `sys_role_menu` VALUES (1, 3122);
INSERT INTO `sys_role_menu` VALUES (1, 3123);
INSERT INTO `sys_role_menu` VALUES (1, 3124);
INSERT INTO `sys_role_menu` VALUES (1, 3131);
INSERT INTO `sys_role_menu` VALUES (1, 3132);
INSERT INTO `sys_role_menu` VALUES (1, 3133);
INSERT INTO `sys_role_menu` VALUES (1, 3134);
INSERT INTO `sys_role_menu` VALUES (1, 3135);
INSERT INTO `sys_role_menu` VALUES (1, 3200);
INSERT INTO `sys_role_menu` VALUES (1, 3201);
INSERT INTO `sys_role_menu` VALUES (1, 3202);
INSERT INTO `sys_role_menu` VALUES (1, 3203);
INSERT INTO `sys_role_menu` VALUES (1, 3204);
INSERT INTO `sys_role_menu` VALUES (1, 3231);
INSERT INTO `sys_role_menu` VALUES (1, 3232);
INSERT INTO `sys_role_menu` VALUES (1, 3233);
INSERT INTO `sys_role_menu` VALUES (1, 3234);
INSERT INTO `sys_role_menu` VALUES (1, 3300);
INSERT INTO `sys_role_menu` VALUES (1, 3301);
INSERT INTO `sys_role_menu` VALUES (1, 3302);
INSERT INTO `sys_role_menu` VALUES (1, 3303);
INSERT INTO `sys_role_menu` VALUES (1, 3311);
INSERT INTO `sys_role_menu` VALUES (1, 3312);
INSERT INTO `sys_role_menu` VALUES (1, 3313);
INSERT INTO `sys_role_menu` VALUES (1, 3314);
INSERT INTO `sys_role_menu` VALUES (1, 3331);
INSERT INTO `sys_role_menu` VALUES (1, 3332);
INSERT INTO `sys_role_menu` VALUES (1, 3333);
INSERT INTO `sys_role_menu` VALUES (1, 3400);
INSERT INTO `sys_role_menu` VALUES (1, 3401);
INSERT INTO `sys_role_menu` VALUES (1, 3402);
INSERT INTO `sys_role_menu` VALUES (1, 3403);
INSERT INTO `sys_role_menu` VALUES (1, 3404);
INSERT INTO `sys_role_menu` VALUES (1, 3405);
INSERT INTO `sys_role_menu` VALUES (1, 3406);
INSERT INTO `sys_role_menu` VALUES (100, 3100);
INSERT INTO `sys_role_menu` VALUES (100, 3101);
INSERT INTO `sys_role_menu` VALUES (100, 3102);
INSERT INTO `sys_role_menu` VALUES (100, 3103);
INSERT INTO `sys_role_menu` VALUES (100, 3111);
INSERT INTO `sys_role_menu` VALUES (100, 3112);
INSERT INTO `sys_role_menu` VALUES (100, 3113);
INSERT INTO `sys_role_menu` VALUES (100, 3114);
INSERT INTO `sys_role_menu` VALUES (100, 3115);
INSERT INTO `sys_role_menu` VALUES (100, 3121);
INSERT INTO `sys_role_menu` VALUES (100, 3122);
INSERT INTO `sys_role_menu` VALUES (100, 3123);
INSERT INTO `sys_role_menu` VALUES (100, 3124);
INSERT INTO `sys_role_menu` VALUES (100, 3131);
INSERT INTO `sys_role_menu` VALUES (100, 3132);
INSERT INTO `sys_role_menu` VALUES (100, 3133);
INSERT INTO `sys_role_menu` VALUES (100, 3134);
INSERT INTO `sys_role_menu` VALUES (100, 3135);
INSERT INTO `sys_role_menu` VALUES (100, 3200);
INSERT INTO `sys_role_menu` VALUES (100, 3201);
INSERT INTO `sys_role_menu` VALUES (100, 3202);
INSERT INTO `sys_role_menu` VALUES (100, 3203);
INSERT INTO `sys_role_menu` VALUES (100, 3204);
INSERT INTO `sys_role_menu` VALUES (100, 3211);
INSERT INTO `sys_role_menu` VALUES (100, 3212);
INSERT INTO `sys_role_menu` VALUES (100, 3213);
INSERT INTO `sys_role_menu` VALUES (100, 3214);
INSERT INTO `sys_role_menu` VALUES (100, 3221);
INSERT INTO `sys_role_menu` VALUES (100, 3222);
INSERT INTO `sys_role_menu` VALUES (100, 3223);
INSERT INTO `sys_role_menu` VALUES (100, 3224);
INSERT INTO `sys_role_menu` VALUES (100, 3225);
INSERT INTO `sys_role_menu` VALUES (100, 3231);
INSERT INTO `sys_role_menu` VALUES (100, 3232);
INSERT INTO `sys_role_menu` VALUES (100, 3233);
INSERT INTO `sys_role_menu` VALUES (100, 3234);
INSERT INTO `sys_role_menu` VALUES (100, 3300);
INSERT INTO `sys_role_menu` VALUES (100, 3301);
INSERT INTO `sys_role_menu` VALUES (100, 3302);
INSERT INTO `sys_role_menu` VALUES (100, 3303);
INSERT INTO `sys_role_menu` VALUES (100, 3311);
INSERT INTO `sys_role_menu` VALUES (100, 3312);
INSERT INTO `sys_role_menu` VALUES (100, 3313);
INSERT INTO `sys_role_menu` VALUES (100, 3314);
INSERT INTO `sys_role_menu` VALUES (100, 3331);
INSERT INTO `sys_role_menu` VALUES (100, 3332);
INSERT INTO `sys_role_menu` VALUES (100, 3333);
INSERT INTO `sys_role_menu` VALUES (100, 3400);
INSERT INTO `sys_role_menu` VALUES (100, 3401);
INSERT INTO `sys_role_menu` VALUES (100, 3402);
INSERT INTO `sys_role_menu` VALUES (100, 3403);
INSERT INTO `sys_role_menu` VALUES (101, 3100);
INSERT INTO `sys_role_menu` VALUES (101, 3101);
INSERT INTO `sys_role_menu` VALUES (101, 3103);
INSERT INTO `sys_role_menu` VALUES (101, 3111);
INSERT INTO `sys_role_menu` VALUES (101, 3113);
INSERT INTO `sys_role_menu` VALUES (101, 3131);
INSERT INTO `sys_role_menu` VALUES (101, 3132);
INSERT INTO `sys_role_menu` VALUES (101, 3133);
INSERT INTO `sys_role_menu` VALUES (101, 3134);
INSERT INTO `sys_role_menu` VALUES (101, 3200);
INSERT INTO `sys_role_menu` VALUES (101, 3201);
INSERT INTO `sys_role_menu` VALUES (101, 3202);
INSERT INTO `sys_role_menu` VALUES (101, 3203);
INSERT INTO `sys_role_menu` VALUES (101, 3211);
INSERT INTO `sys_role_menu` VALUES (101, 3212);
INSERT INTO `sys_role_menu` VALUES (101, 3213);
INSERT INTO `sys_role_menu` VALUES (101, 3221);
INSERT INTO `sys_role_menu` VALUES (101, 3223);
INSERT INTO `sys_role_menu` VALUES (101, 3224);
INSERT INTO `sys_role_menu` VALUES (101, 3300);
INSERT INTO `sys_role_menu` VALUES (101, 3301);
INSERT INTO `sys_role_menu` VALUES (101, 3302);
INSERT INTO `sys_role_menu` VALUES (101, 3303);
INSERT INTO `sys_role_menu` VALUES (101, 3311);
INSERT INTO `sys_role_menu` VALUES (101, 3312);
INSERT INTO `sys_role_menu` VALUES (101, 3313);
INSERT INTO `sys_role_menu` VALUES (101, 3314);
INSERT INTO `sys_role_menu` VALUES (101, 3331);
INSERT INTO `sys_role_menu` VALUES (101, 3332);
INSERT INTO `sys_role_menu` VALUES (101, 3333);
INSERT INTO `sys_role_menu` VALUES (102, 3100);
INSERT INTO `sys_role_menu` VALUES (102, 3101);
INSERT INTO `sys_role_menu` VALUES (102, 3103);
INSERT INTO `sys_role_menu` VALUES (102, 3131);
INSERT INTO `sys_role_menu` VALUES (102, 3132);
INSERT INTO `sys_role_menu` VALUES (102, 3200);
INSERT INTO `sys_role_menu` VALUES (102, 3201);
INSERT INTO `sys_role_menu` VALUES (102, 3202);
INSERT INTO `sys_role_menu` VALUES (102, 3211);
INSERT INTO `sys_role_menu` VALUES (102, 3212);
INSERT INTO `sys_role_menu` VALUES (102, 3221);
INSERT INTO `sys_role_menu` VALUES (102, 3300);
INSERT INTO `sys_role_menu` VALUES (102, 3301);
INSERT INTO `sys_role_menu` VALUES (102, 3302);
INSERT INTO `sys_role_menu` VALUES (102, 3303);
INSERT INTO `sys_role_menu` VALUES (102, 3311);
INSERT INTO `sys_role_menu` VALUES (102, 3312);
INSERT INTO `sys_role_menu` VALUES (102, 3313);
INSERT INTO `sys_role_menu` VALUES (102, 3331);
INSERT INTO `sys_role_menu` VALUES (102, 3332);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `student_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '学号',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '班级',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '', '', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '', '', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '测试员');
INSERT INTO `sys_user` VALUES (100, NULL, 'r1', 'r1', '00', '', '', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '测试用户');
INSERT INTO `sys_user` VALUES (101, 100, 'clubadmin', '社团管理员', '00', 'clubadmin@example.com', '13800000001', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '社团管理员测试账号');
INSERT INTO `sys_user` VALUES (102, 100, 'president1', '张明（社长）', '00', 'president1@example.com', '13800000002', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '编程魔法社社长');
INSERT INTO `sys_user` VALUES (103, 100, 'vicepresident1', '李华（副社长）', '00', 'vp1@example.com', '13800000003', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '编程魔法社副社长');
INSERT INTO `sys_user` VALUES (104, NULL, 'test_user_004', 'test_user_004', '00', '', '', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '测试用户');
INSERT INTO `sys_user` VALUES (105, 100, 'clubp10002', '若依', '00', '', '', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, SYSDATE(), 'admin', SYSDATE(), '', NULL, '社团测试账号');
-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1, 101);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (100, 2);
INSERT INTO `sys_user_role` VALUES (101, 100);
INSERT INTO `sys_user_role` VALUES (102, 101);
INSERT INTO `sys_user_role` VALUES (103, 102);
INSERT INTO `sys_user_role` VALUES (104, 2);
INSERT INTO `sys_user_role` VALUES (105, 102);

SET FOREIGN_KEY_CHECKS = 1;
