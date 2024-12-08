/*
 Navicat Premium Dump SQL

 Source Server         : 1
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : shop

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 08/12/2024 03:07:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (70, 11, 33);
INSERT INTO `cart` VALUES (71, 11, 34);

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_items
-- ----------------------------
INSERT INTO `order_items` VALUES (43, 29, 32, 999.99);
INSERT INTO `order_items` VALUES (44, 29, 33, 899.99);
INSERT INTO `order_items` VALUES (45, 30, 34, 349.99);
INSERT INTO `order_items` VALUES (46, 30, 32, 999.99);
INSERT INTO `order_items` VALUES (47, 30, 33, 899.99);
INSERT INTO `order_items` VALUES (48, 31, 32, 999.99);
INSERT INTO `order_items` VALUES (49, 31, 33, 899.99);
INSERT INTO `order_items` VALUES (50, 32, 32, 999.99);
INSERT INTO `order_items` VALUES (51, 32, 33, 899.99);
INSERT INTO `order_items` VALUES (52, 33, 32, 999.99);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `total_price` decimal(10, 2) NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '未发货',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (29, 9, 1899.98, '已发货');
INSERT INTO `orders` VALUES (30, 9, 2249.97, '已发货');
INSERT INTO `orders` VALUES (31, 11, 1899.98, '已发货');
INSERT INTO `orders` VALUES (32, 11, 1899.98, '未发货');
INSERT INTO `orders` VALUES (33, 13, 999.99, '已发货');

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `price` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (32, '苹果 iPhone 15', '苹果最新型号，电池性能更强。', 999.99);
INSERT INTO `products` VALUES (33, '三星 Galaxy S23', '高分辨率摄像头，续航持久。', 899.99);
INSERT INTO `products` VALUES (34, '索尼耳机 WH1000XM4', '行业领先的降噪功能。', 349.99);
INSERT INTO `products` VALUES (35, '戴尔 XPS 15', '配备第十代英特尔处理器。', 1499.99);
INSERT INTO `products` VALUES (36, '任天堂 Switch', '可家用或携带的游戏机。', 299.99);
INSERT INTO `products` VALUES (39, '4090显卡', '阉割版', 19999.00);

-- ----------------------------
-- Table structure for user_logs
-- ----------------------------
DROP TABLE IF EXISTS `user_logs`;
CREATE TABLE `user_logs`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `action_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `action_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作详情',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_logs
-- ----------------------------
INSERT INTO `user_logs` VALUES (11, 9, '2024-12-07 11:12:26', '浏览', '搜索关键字: 三星, 结果数量: 1');
INSERT INTO `user_logs` VALUES (12, 9, '2024-12-07 11:12:34', '购买', '购买商品ID: [32, 33]');
INSERT INTO `user_logs` VALUES (13, 9, '2024-12-07 17:08:13', '购买', '购买商品ID: [34, 32, 33]');
INSERT INTO `user_logs` VALUES (14, 11, '2024-12-07 17:18:00', '购买', '购买商品ID: [32, 33]');
INSERT INTO `user_logs` VALUES (15, 11, '2024-12-07 17:28:03', '浏览', '搜索关键字: 耳机, 结果数量: 1');
INSERT INTO `user_logs` VALUES (16, 11, '2024-12-07 17:28:25', '购买', '购买商品ID: [32, 33]');
INSERT INTO `user_logs` VALUES (17, 13, '2024-12-08 01:03:39', '浏览', '搜索关键字: 耳机, 结果数量: 1');
INSERT INTO `user_logs` VALUES (18, 13, '2024-12-08 01:05:02', '购买', '购买商品ID: [32]');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'USER',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (2, 'admin', '123', '979983817@qq.com', 'ADMIN');
INSERT INTO `users` VALUES (13, 'test1', 'test1', 'bedguy_zz@163.com', 'USER');

SET FOREIGN_KEY_CHECKS = 1;
