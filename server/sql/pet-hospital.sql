/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80044
 Source Host           : localhost:3306
 Source Schema         : pet-hospital

 Target Server Type    : MySQL
 Target Server Version : 80044
 File Encoding         : 65001

 Date: 30/10/2025 22:19:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `banner_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '轮播图ID',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图片',
  `sort` int NOT NULL COMMENT '排序',
  PRIMARY KEY (`banner_id`) USING BTREE,
  UNIQUE INDEX `banner_id`(`banner_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '轮播图' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES ('20d78cb805ce420096c0866ad2690686', '/profile/upload/2025/10/26/banner2_20251026232314A003.png', 2);
INSERT INTO `banner` VALUES ('bb0fd3c32b83423f9fd997439abd7d92', '/profile/upload/2025/10/26/banner3_20251026232318A004.png', 3);
INSERT INTO `banner` VALUES ('c617e55b424d472bb713cb5dc01da78f', '/profile/upload/2025/10/26/banner1_20251026232309A002.jpg', 1);

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `doctor_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '医生ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '姓名',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '职称',
  `specialty` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '擅长',
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '照片',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`doctor_id`) USING BTREE,
  UNIQUE INDEX `doctor_id`(`doctor_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '医生' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctor
-- ----------------------------
INSERT INTO `doctor` VALUES ('8e178306e68e4dc39b1ac6621e491ee6', '李医生', '副主任医师', '皮肤科、眼科', '/profile/upload/2025/10/27/医生2_20251027165012A003.png', '2025-10-27 16:50:14');
INSERT INTO `doctor` VALUES ('c10891a2a7dd4629814dbc8fd86a7907', '张医生', '主治医生', '内科、外科手术', '/profile/upload/2025/10/27/医生1_20251027164945A002.png', '2025-10-27 16:49:47');
INSERT INTO `doctor` VALUES ('f385cfec5d1343f1ad55ec37e210db2e', '王医生', '主任医师', '牙科', '/profile/upload/2025/10/27/医生3_20251027165036A004.png', '2025-10-27 16:50:37');

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (13, 'knowledge', '养宠知识', NULL, NULL, 'Knowledge', 'crud', 'element-plus', 'com.huacai.hospital', 'hospital', 'knowledge', '养宠知识', 'huacai', '0', '/', '{\"parentMenuId\":0}', 'admin', '2025-10-29 15:26:47', '', '2025-10-29 15:27:35', NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (86, 13, 'knowledge_id', '养宠知识ID', 'varchar(255)', 'String', 'knowledgeId', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-10-29 15:26:47', '', '2025-10-29 15:27:35');
INSERT INTO `gen_table_column` VALUES (87, 13, 'title', '标题', 'varchar(100)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-10-29 15:26:47', '', '2025-10-29 15:27:35');
INSERT INTO `gen_table_column` VALUES (88, 13, 'content', '内容', 'longtext', 'String', 'content', '0', '0', '1', '1', '1', '1', '0', 'EQ', 'editor', '', 3, 'admin', '2025-10-29 15:26:47', '', '2025-10-29 15:27:35');
INSERT INTO `gen_table_column` VALUES (89, 13, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 4, 'admin', '2025-10-29 15:26:47', '', '2025-10-29 15:27:35');

-- ----------------------------
-- Table structure for knowledge
-- ----------------------------
DROP TABLE IF EXISTS `knowledge`;
CREATE TABLE `knowledge`  (
  `knowledge_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '养宠知识ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`knowledge_id`) USING BTREE,
  UNIQUE INDEX `knowledge_id`(`knowledge_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '养宠知识' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of knowledge
-- ----------------------------
INSERT INTO `knowledge` VALUES ('81ac20561a214ce68a21598647a42342', '如何正确给狗狗洗澡', '<p>给狗狗洗澡是宠物护理中非常重要的一部分，但许多宠物主人在洗澡过程中存在一些误区。正确的洗澡方法不仅能保持狗狗的清洁，还能促进它们的健康。</p>\r\n\r\n<h3>洗澡频率</h3>\r\n<p>狗狗的洗澡频率需要根据品种、毛发类型和生活环境来决定：</p>\r\n<ul>\r\n    <li>短毛犬：通常每2-3个月洗一次</li>\r\n    <li>长毛犬：建议每个月洗1-2次</li>\r\n    <li>皮肤敏感的狗狗：可以适当延长洗澡间隔</li>\r\n</ul>\r\n\r\n<h3>用品选择</h3>\r\n<p>选择适合的洗浴用品对狗狗的皮肤健康至关重要：</p>\r\n<ul>\r\n    <li>使用专用的狗狗洗发水，避免使用人用洗发水</li>\r\n    <li>根据狗狗皮肤类型选择相应的护理产品</li>\r\n    <li>准备专用的毛巾和吹风机</li>\r\n</ul>\r\n\r\n<h3>洗澡步骤</h3>\r\n<ol>\r\n    <li>先用温水将狗狗全身毛发打湿</li>\r\n    <li>涂抹洗发水并轻轻按摩，注意避开眼部</li>\r\n    <li>彻底冲洗干净，确保无残留</li>\r\n    <li>使用护毛素（如有需要）</li>\r\n    <li>用毛巾擦干并用吹风机吹干毛发</li>\r\n</ol>\r\n\r\n<h3>注意事项</h3>\r\n<ul>\r\n    <li>洗澡前先梳理毛发，避免打结</li>\r\n    <li>水温控制在38-40度为宜</li>\r\n    <li>洗澡过程中注意保护狗狗的眼睛和耳朵</li>\r\n    <li>洗澡后要及时吹干毛发，防止感冒</li>\r\n    <li>幼犬未完成疫苗接种前不建议洗澡</li>\r\n</ul>\r\n\r\n<p>定期给狗狗洗澡不仅能保持清洁，还能及时发现皮肤问题，是宠物护理中不可缺少的一环。</p>', '2025-10-23 23:06:28');
INSERT INTO `knowledge` VALUES ('a491b7de04d54c271202ccae998efd7', '宠物训练基本方法', '<p>科学的训练方法能帮助宠物养成良好习惯，建立与主人的良好关系，提升生活质量。</p>\r\n\r\n<h3>训练原则</h3>\r\n<ul>\r\n    <li><strong>正向激励</strong>：通过奖励强化正确行为</li>\r\n    <li><strong>一致性</strong>：所有家庭成员使用相同指令和规则</li>\r\n    <li><strong>循序渐进</strong>：从简单到复杂，逐步提高要求</li>\r\n    <li><strong>耐心坚持</strong>：训练需要时间和重复，避免急躁</li>\r\n</ul>\r\n\r\n<h3>基础指令训练</h3>\r\n<h4>坐下指令</h4>\r\n<ol>\r\n    <li>手持食物吸引狗狗注意力</li>\r\n    <li>将食物从狗狗鼻子前方向后上方移动</li>\r\n    <li>当狗狗自然坐下时，立即说\"坐下\"并给予奖励</li>\r\n    <li>重复练习，逐渐减少食物引导</li>\r\n</ol>\r\n\r\n<h4>握手指令</h4>\r\n<ol>\r\n    <li>先让狗狗坐下</li>\r\n    <li>轻触狗狗前爪，当它抬起爪子时立即握住</li>\r\n    <li>同时说\"握手\"并给予奖励</li>\r\n    <li>反复练习直到狗狗主动伸出爪子</li>\r\n</ol>\r\n\r\n<h3>行为纠正训练</h3>\r\n<h4>制止乱叫</h4>\r\n<ul>\r\n    <li>了解狗狗叫的原因（警戒、焦虑、兴奋等）</li>\r\n    <li>在狗狗乱叫时保持冷静，不要大声呵斥</li>\r\n    <li>使用\"安静\"指令并引导其注意力</li>\r\n    <li>当狗狗停止吠叫时立即奖励</li>\r\n</ul>\r\n\r\n<h4>定点排便</h4>\r\n<ul>\r\n    <li>确定固定排便地点</li>\r\n    <li>观察排便时间规律，及时引导</li>\r\n    <li>排便正确时立即奖励</li>\r\n    <li>意外排便时不惩罚，及时清理</li>\r\n</ul>\r\n\r\n<h3>社会化训练</h3>\r\n<p>帮助宠物适应各种环境和情况：</p>\r\n<ul>\r\n    <li>从小接触不同的人、动物和环境</li>\r\n    <li>逐步增加刺激强度，避免过度惊吓</li>\r\n    <li>正面体验，避免负面刺激</li>\r\n    <li>培养宠物的自信心和适应能力</li>\r\n</ul>', '2025-10-23 23:06:35');
INSERT INTO `knowledge` VALUES ('a491b7de04d54c2781002ccae998efd7', '猫咪疫苗接种全攻略', '<p>疫苗接种是保护猫咪免受严重疾病侵害的重要措施。正确的疫苗接种计划能有效预防多种致命疾病，保障猫咪的健康。</p>\r\n\r\n<h3>核心疫苗</h3>\r\n<p>所有猫咪都应该接种的核心疫苗包括：</p>\r\n<ul>\r\n    <li><strong>猫瘟疫苗（FPV）</strong>：预防猫泛白细胞减少症，致死率极高</li>\r\n    <li><strong>猫鼻支疫苗（FHV-1）</strong>：预防猫疱疹病毒感染</li>\r\n    <li><strong>猫杯状病毒疫苗（FCV）</strong>：预防猫杯状病毒感染</li>\r\n    <li><strong>狂犬病疫苗</strong>：法律要求，预防狂犬病</li>\r\n</ul>\r\n\r\n<h3>非核心疫苗</h3>\r\n<p>根据猫咪生活环境和风险评估选择性接种：</p>\r\n<ul>\r\n    <li><strong>猫白血病疫苗（FeLV）</strong>：适用于户外活动的猫咪</li>\r\n    <li><strong>衣原体疫苗</strong>：预防猫衣原体感染</li>\r\n    <li><strong>猫免疫缺陷病毒疫苗（FIV）</strong>：根据地区和猫咪情况选择</li>\r\n</ul>\r\n\r\n<h3>接种时间表</h3>\r\n<p>标准疫苗接种时间安排：</p>\r\n<ul>\r\n    <li><strong>6-8周龄</strong>：首次接种核心疫苗（FPV、FHV-1、FCV）</li>\r\n    <li><strong>10-12周龄</strong>：第二次接种核心疫苗</li>\r\n    <li><strong>14-16周龄</strong>：第三次接种核心疫苗，首次接种狂犬病疫苗</li>\r\n    <li><strong>12-16月龄</strong>：加强接种所有核心疫苗</li>\r\n    <li><strong>之后每1-3年</strong>：根据疫苗类型和兽医建议定期加强</li>\r\n</ul>\r\n\r\n<h3>接种注意事项</h3>\r\n<ul>\r\n    <li>确保猫咪健康状况良好再接种疫苗</li>\r\n    <li>接种前咨询兽医，制定个性化接种计划</li>\r\n    <li>接种后观察猫咪反应，如有异常及时就医</li>\r\n    <li>保持接种记录，按时进行加强接种</li>\r\n</ul>', '2025-10-23 23:06:42');
INSERT INTO `knowledge` VALUES ('cd96cf12970a4a5ba9ae9c2ad5782c5a', '狗狗常见疾病预防', '<p>了解狗狗常见疾病的症状和预防措施有助于早期发现和治疗，提高治愈率，保障狗狗健康。</p>\r\n\r\n<h3>消化系统疾病</h3>\r\n<h4>肠胃炎</h4>\r\n<p><strong>症状：</strong>呕吐、腹泻、食欲不振、精神萎靡</p>\r\n<p><strong>预防：</strong>避免食用变质食物，定时定量喂食，不随意更换狗粮</p>\r\n\r\n<h4>肠梗阻</h4>\r\n<p><strong>症状：</strong>频繁呕吐、腹痛、便秘、食欲废绝</p>\r\n<p><strong>预防：</strong>避免狗狗吞食异物，如骨头、玩具等</p>\r\n\r\n<h3>呼吸系统疾病</h3>\r\n<h4>感冒</h4>\r\n<p><strong>症状：</strong>流鼻涕、打喷嚏、咳嗽、发热</p>\r\n<p><strong>预防：</strong>避免受凉，保持环境干燥通风</p>\r\n\r\n<h4>犬瘟热</h4>\r\n<p><strong>症状：</strong>高热、咳嗽、流脓性鼻涕、神经症状</p>\r\n<p><strong>预防：</strong>按时接种疫苗，避免接触病犬</p>\r\n\r\n<h3>皮肤疾病</h3>\r\n<h4>湿疹</h4>\r\n<p><strong>症状：</strong>皮肤红肿、瘙痒、脱毛</p>\r\n<p><strong>预防：</strong>保持皮肤清洁干燥，定期洗澡护理</p>\r\n\r\n<h4>真菌感染</h4>\r\n<p><strong>症状：</strong>局部脱毛、皮肤发红、有异味</p>\r\n<p><strong>预防：</strong>保持环境清洁，避免潮湿</p>\r\n\r\n<h3>预防措施</h3>\r\n<ul>\r\n    <li>定期接种疫苗和驱虫</li>\r\n    <li>保持良好的生活环境</li>\r\n    <li>定期体检，早发现早治疗</li>\r\n    <li>合理饮食，增强免疫力</li>\r\n    <li>适量运动，增强体质</li>\r\n</ul>', '2025-10-23 23:06:44');
INSERT INTO `knowledge` VALUES ('d950fad518ed47ae892a97187a444929', '宠物营养饮食搭配', '<p>科学的营养饮食搭配是宠物健康长寿的基础。不同年龄、体型和健康状况的宠物对营养的需求各不相同，需要个性化定制饮食方案。</p>\r\n\r\n<h3>营养要素</h3>\r\n<p>宠物日常饮食需要包含以下基本营养要素：</p>\r\n<ul>\r\n    <li><strong>蛋白质</strong>：构建和修复组织，提供必需氨基酸</li>\r\n    <li><strong>脂肪</strong>：提供能量，维持皮肤和毛发健康</li>\r\n    <li><strong>碳水化合物</strong>：提供能量来源，促进肠道健康</li>\r\n    <li><strong>维生素</strong>：维持各种生理功能，增强免疫力</li>\r\n    <li><strong>矿物质</strong>：构建骨骼和牙齿，维持神经和肌肉功能</li>\r\n    <li><strong>水分</strong>：维持生命活动，调节体温</li>\r\n</ul>\r\n\r\n<h3>不同生命阶段的营养需求</h3>\r\n<h4>幼犬/幼猫期</h4>\r\n<ul>\r\n    <li>高蛋白质和高热量需求</li>\r\n    <li>丰富的钙质和磷质促进骨骼发育</li>\r\n    <li>适量的DHA促进大脑和视力发育</li>\r\n</ul>\r\n\r\n<h4>成犬/成猫期</h4>\r\n<ul>\r\n    <li>均衡的营养配比</li>\r\n    <li>适量的纤维促进消化</li>\r\n    <li>控制热量摄入，防止肥胖</li>\r\n</ul>\r\n\r\n<h4>老年期</h4>\r\n<ul>\r\n    <li>易消化的蛋白质</li>\r\n    <li>关节保护成分如葡萄糖胺</li>\r\n    <li>抗氧化成分延缓衰老</li>\r\n    <li>低磷饮食保护肾脏功能</li>\r\n</ul>\r\n\r\n<h3>饮食搭配原则</h3>\r\n<ul>\r\n    <li>选择高质量的商业宠物食品或科学配制的自制食品</li>\r\n    <li>定时定量喂食，避免暴饮暴食</li>\r\n    <li>提供充足清洁的饮水</li>\r\n    <li>避免喂食对宠物有害的人类食物</li>\r\n    <li>根据宠物活动量调整喂食量</li>\r\n</ul>', '2025-10-23 23:06:48');
INSERT INTO `knowledge` VALUES ('df58a9b89593414abe804e1c94f21c67', '猫咪日常护理要点', '<p>科学的日常护理能够有效预防疾病，延长猫咪寿命，提升生活质量。</p>\r\n\r\n<h3>毛发护理</h3>\r\n<p>定期梳理毛发的好处：</p>\r\n<ul>\r\n    <li>去除死毛和污垢，防止毛发打结</li>\r\n    <li>促进血液循环，增强皮肤健康</li>\r\n    <li>减少毛球形成，预防毛球症</li>\r\n    <li>增进与猫咪的感情交流</li>\r\n</ul>\r\n\r\n<h3>眼部护理</h3>\r\n<p>眼部清洁步骤：</p>\r\n<ol>\r\n    <li>使用专用眼部清洁液或生理盐水</li>\r\n    <li>用棉球或纱布轻轻擦拭眼角分泌物</li>\r\n    <li>从内眼角向外眼角擦拭</li>\r\n    <li>每天清洁1-2次，保持眼部清洁</li>\r\n</ol>\r\n\r\n<h3>耳部护理</h3>\r\n<p>耳部清洁方法：</p>\r\n<ul>\r\n    <li>每周检查耳朵是否有异味、红肿或分泌物</li>\r\n    <li>使用专用耳部清洁液</li>\r\n    <li>轻柔地清洁外耳道，避免深入</li>\r\n    <li>发现异常及时就医</li>\r\n</ul>\r\n\r\n<h3>口腔护理</h3>\r\n<p>牙齿健康的重要性：</p>\r\n<ul>\r\n    <li>定期刷牙，使用猫咪专用牙刷和牙膏</li>\r\n    <li>提供洁牙玩具或洁牙零食</li>\r\n    <li>定期检查牙龈和牙齿健康状况</li>\r\n    <li>预防牙结石和牙龈疾病</li>\r\n</ul>\r\n\r\n<h3>爪部护理</h3>\r\n<p>修剪指甲注意事项：</p>\r\n<ul>\r\n    <li>使用专用猫咪指甲剪</li>\r\n    <li>避免剪到血管丰富的粉红色部分</li>\r\n    <li>定期修剪，一般每2-3周一次</li>\r\n    <li>注意安全，避免猫咪挣扎时受伤</li>\r\n</ul>', '2025-10-23 23:06:46');

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`  (
  `reservation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '预约ID',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '预约类型',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '待就诊' COMMENT '预约状态',
  `pet_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '宠物名字',
  `pet_bread` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '宠物品种',
  `pet_age` int NOT NULL COMMENT '宠物年龄',
  `symptom` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '症状',
  `date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '预约日期',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '预约时间',
  `doctor_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '医生ID',
  `vaccine_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '疫苗ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`reservation_id`) USING BTREE,
  UNIQUE INDEX `reservation_id`(`reservation_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '预约' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation
-- ----------------------------
INSERT INTO `reservation` VALUES ('OR202510280010481', '普通预约', '已取消', '丫丫', '哈士奇', 3, '牙齿有蛀牙', '2025-10-29', '13:00', 'f385cfec5d1343f1ad55ec37e210db2e', NULL, 1, '2025-10-28 00:10:49');
INSERT INTO `reservation` VALUES ('OR202510281607411', '疫苗预约', '已完成', '丫丫', '哈士奇', 3, '疫苗接种预约', '2025-10-29', '15:00', NULL, 'ec48ad6932f4472fbaa70dba5480131f', 1, '2025-10-28 16:07:42');
INSERT INTO `reservation` VALUES ('OR20251028230542104', '普通预约', '已完成', '哈哈', '哈士奇', 2, '皮疹', '2025-10-29', '15:00', '8e178306e68e4dc39b1ac6621e491ee6', NULL, 104, '2025-10-28 23:05:43');
INSERT INTO `reservation` VALUES ('OR20251028230604104', '疫苗预约', '待就诊', '哈哈', '哈士奇', 2, '疫苗接种预约', '2025-10-30', '13:00', NULL, '0597f090ce9e4afd9fe5e9693a993121', 104, '2025-10-28 23:06:04');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-08-30 08:59:54', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (2, '验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-11 13:30:28', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (3, '是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-11 13:31:46', '是否开启注册用户功能（true开启，false关闭）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '花菜编程', 0, 'huacai', '13800000000', 'demo@example.com', '0', '0', 'admin', '2025-08-30 08:59:53', 'admin', '2025-08-30 10:36:00');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (9, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (100, 1, '疫苗预约', '疫苗预约', 'reservation_type', NULL, 'primary', 'N', '0', 'admin', '2025-10-27 20:24:50', 'admin', '2025-10-27 20:24:57', NULL);
INSERT INTO `sys_dict_data` VALUES (101, 2, '普通预约', '普通预约', 'reservation_type', NULL, 'primary', 'N', '0', 'admin', '2025-10-27 20:25:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (102, 1, '待就诊', '待就诊', 'reservation_status', NULL, 'warning', 'N', '0', 'admin', '2025-10-27 20:25:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (103, 2, '已取消', '已取消', 'reservation_status', NULL, 'danger', 'N', '0', 'admin', '2025-10-27 20:25:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (104, 3, '已完成', '已完成', 'reservation_status', NULL, 'success', 'N', '0', 'admin', '2025-10-27 20:26:07', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '系统是否', 'sys_yes_no', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (100, '预约类型', 'reservation_type', '0', 'admin', '2025-10-27 20:24:30', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (101, '预约状态', 'reservation_status', '0', 'admin', '2025-10-27 20:25:31', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 1 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2043 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 99, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', '系统管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-10-26 23:03:25', '系统管理目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 1, 'C', '0', '0', 'system:user:list', '用户管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:20:55', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 1, 'C', '0', '0', 'system:role:list', '角色管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:22', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 1, 'C', '0', '0', 'system:menu:list', '菜单管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:28', '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 1, 'C', '0', '0', 'system:dept:list', '部门管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:33', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 1, 'C', '0', '0', 'system:dict:list', '字典管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:38', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 1, 'C', '0', '0', 'system:config:list', '参数设置', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:44', '参数设置菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 1, 8, 'gen', 'tool/gen/index', '', '', 1, 1, 'C', '0', '0', 'tool:gen:list', '代码生成', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:49', '代码生成菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '宠物医院管理', 0, 1, 'hospital', NULL, NULL, '', 1, 1, 'M', '0', '0', '', '宠物医院管理', 'admin', '2025-10-26 23:04:08', 'admin', '2025-10-26 23:07:29', '');
INSERT INTO `sys_menu` VALUES (2013, '轮播图管理', 2012, 1, 'banner', 'hospital/banner/index', NULL, '', 1, 1, 'C', '0', '0', 'hospital:banner:list', '轮播图管理', 'admin', '2025-10-26 23:14:17', 'admin', '2025-10-26 23:15:25', '轮播图菜单');
INSERT INTO `sys_menu` VALUES (2014, '轮播图查询', 2013, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:banner:query', '#', 'admin', '2025-10-26 23:14:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2015, '轮播图新增', 2013, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:banner:add', '#', 'admin', '2025-10-26 23:14:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '轮播图修改', 2013, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:banner:edit', '#', 'admin', '2025-10-26 23:14:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '轮播图删除', 2013, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:banner:remove', '#', 'admin', '2025-10-26 23:14:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '轮播图导出', 2013, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:banner:export', '#', 'admin', '2025-10-26 23:14:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2019, '医生管理', 2012, 2, 'doctor', 'hospital/doctor/index', NULL, '', 1, 1, 'C', '0', '0', 'hospital:doctor:list', '医生管理', 'admin', '2025-10-27 16:42:51', 'admin', '2025-10-27 16:47:45', '医生菜单');
INSERT INTO `sys_menu` VALUES (2020, '医生查询', 2019, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:doctor:query', '#', 'admin', '2025-10-27 16:42:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2021, '医生新增', 2019, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:doctor:add', '#', 'admin', '2025-10-27 16:42:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '医生修改', 2019, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:doctor:edit', '#', 'admin', '2025-10-27 16:42:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '医生删除', 2019, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:doctor:remove', '#', 'admin', '2025-10-27 16:42:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '医生导出', 2019, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:doctor:export', '#', 'admin', '2025-10-27 16:42:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2025, '疫苗管理', 2012, 3, 'vaccine', 'hospital/vaccine/index', NULL, '', 1, 1, 'C', '0', '0', 'hospital:vaccine:list', '疫苗管理', 'admin', '2025-10-27 19:52:57', 'admin', '2025-10-27 19:56:07', '疫苗菜单');
INSERT INTO `sys_menu` VALUES (2026, '疫苗查询', 2025, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:vaccine:query', '#', 'admin', '2025-10-27 19:52:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2027, '疫苗新增', 2025, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:vaccine:add', '#', 'admin', '2025-10-27 19:52:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2028, '疫苗修改', 2025, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:vaccine:edit', '#', 'admin', '2025-10-27 19:52:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2029, '疫苗删除', 2025, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:vaccine:remove', '#', 'admin', '2025-10-27 19:52:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2030, '疫苗导出', 2025, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:vaccine:export', '#', 'admin', '2025-10-27 19:52:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2031, '预约管理', 2012, 4, 'reservation', 'hospital/reservation/index', NULL, '', 1, 1, 'C', '0', '0', 'hospital:reservation:list', '预约管理', 'admin', '2025-10-27 20:28:58', 'admin', '2025-10-27 20:32:09', '预约菜单');
INSERT INTO `sys_menu` VALUES (2032, '预约查询', 2031, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:reservation:query', '#', 'admin', '2025-10-27 20:28:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2033, '预约新增', 2031, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:reservation:add', '#', 'admin', '2025-10-27 20:28:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2034, '预约修改', 2031, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:reservation:edit', '#', 'admin', '2025-10-27 20:28:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2035, '预约删除', 2031, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:reservation:remove', '#', 'admin', '2025-10-27 20:28:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '预约导出', 2031, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:reservation:export', '#', 'admin', '2025-10-27 20:28:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2037, '养宠知识管理', 2012, 5, 'knowledge', 'hospital/knowledge/index', NULL, '', 1, 1, 'C', '0', '0', 'hospital:knowledge:list', '养宠知识管理', 'admin', '2025-10-29 15:28:16', 'admin', '2025-10-29 15:32:00', '养宠知识菜单');
INSERT INTO `sys_menu` VALUES (2038, '养宠知识查询', 2037, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:knowledge:query', '#', 'admin', '2025-10-29 15:28:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2039, '养宠知识新增', 2037, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:knowledge:add', '#', 'admin', '2025-10-29 15:28:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '养宠知识修改', 2037, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:knowledge:edit', '#', 'admin', '2025-10-29 15:28:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '养宠知识删除', 2037, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:knowledge:remove', '#', 'admin', '2025-10-29 15:28:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '养宠知识导出', 2037, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hospital:knowledge:export', '#', 'admin', '2025-10-29 15:28:17', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '2', 'admin', '2025-08-30 08:59:54', 'admin', '2025-08-30 10:34:10', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '花菜', '00', 'demo@example.com', '13800000000', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-10-30 11:47:50', 'admin', '2025-08-30 08:59:53', '', '2025-10-30 11:47:49', '管理员');
INSERT INTO `sys_user` VALUES (104, NULL, '张三', '张三', '00', '', '', '0', '', '$2a$10$qYjznCEboJx10MpixiTowOMWBPznXoYhlTlRZZL52/QtkuaivuGA2', '0', '0', '127.0.0.1', '2025-10-28 23:03:40', '', '2025-10-28 21:16:32', '', '2025-10-28 23:03:40', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

-- ----------------------------
-- Table structure for vaccine
-- ----------------------------
DROP TABLE IF EXISTS `vaccine`;
CREATE TABLE `vaccine`  (
  `vaccine_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '疫苗ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '疫苗名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '描述',
  PRIMARY KEY (`vaccine_id`) USING BTREE,
  UNIQUE INDEX `vaccine_id`(`vaccine_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '疫苗' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vaccine
-- ----------------------------
INSERT INTO `vaccine` VALUES ('0597f090ce9e4afd9fe5e9693a993121', '犬八联疫苗', '预防犬瘟热、腺病毒、副流感等八种疾病');
INSERT INTO `vaccine` VALUES ('238721c8a6be4358a28462ac5e33cad8', '犬五联疫苗', '预防犬瘟热、细小病毒等五种疾病');
INSERT INTO `vaccine` VALUES ('362f6028d8ca42459173803871252f56', '猫白血病疫苗', '预防猫白血病病毒感染');
INSERT INTO `vaccine` VALUES ('e32e066bfd6c44898bdf1cb7872472e1', '猫三联疫苗', '预防猫瘟、猫鼻支、杯状病毒');
INSERT INTO `vaccine` VALUES ('ec48ad6932f4472fbaa70dba5480131f', '狂犬疫苗', '预防狂犬病，每年需要接种一次');

SET FOREIGN_KEY_CHECKS = 1;
