-- ============================================
-- 1. 用户表 (user)
-- 用途: 存储用户基本信息
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '用户姓名',
    `zhanghao` VARCHAR(50) NOT NULL COMMENT '账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `creattime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `avatarurl` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_zhanghao` (`zhanghao`),
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 消息表 (message)
-- 用途: 存储用户之间的聊天消息记录
-- ============================================
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `sender_id` INT NOT NULL COMMENT '发送者ID (关联 user.id)',
    `receiver_id` INT NOT NULL COMMENT '接收者ID (关联 user.id)',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `message_type` VARCHAR(20) DEFAULT 'TEXT' COMMENT '消息类型 (TEXT/IMAGE/FILE等)',
    `timestamp` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '消息时间戳',
    PRIMARY KEY (`id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`),
    KEY `idx_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ============================================
-- 3. 地理位置查询结果表 (location_results)
-- 用途: 存储IP地址的地理位置查询历史记录
-- ============================================
DROP TABLE IF EXISTS `location_results`;
CREATE TABLE `location_results` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `client_ip` VARCHAR(50) NOT NULL COMMENT '客户端IP地址',
    `public_ip` VARCHAR(50) DEFAULT NULL COMMENT '外网IP地址（当客户端为内网IP时使用）',
    `city` VARCHAR(100) DEFAULT NULL COMMENT '城市名称',
    `province` VARCHAR(100) DEFAULT NULL COMMENT '省份/地区名称',
    `country` VARCHAR(100) DEFAULT NULL COMMENT '国家名称',
    `isp` VARCHAR(100) DEFAULT NULL COMMENT '网络服务提供商',
    `raw_location` VARCHAR(500) DEFAULT NULL COMMENT '原始位置信息字符串',
    `success` TINYINT DEFAULT 1 COMMENT '操作是否成功 (1:成功 0:失败)',
    `message` VARCHAR(500) DEFAULT NULL COMMENT '响应消息',
    `timestamp` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '查询时间戳（精确到毫秒）',
    PRIMARY KEY (`id`),
    KEY `idx_client_ip` (`client_ip`),
    KEY `idx_public_ip` (`public_ip`),
    KEY `idx_timestamp` (`timestamp`),
    KEY `idx_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='地理位置查询结果表';

-- ============================================
-- 插入测试数据（可选）
-- ============================================
-- 插入测试用户
INSERT INTO `user` (`name`, `zhanghao`, `password`, `creattime`, `avatarurl`) VALUES
('张三', 'zhangsan', '123456', NOW(), '/images/avatar1.jpg'),
('李四', 'lisi', '123456', NOW(), '/images/avatar2.jpg'),
('王五', 'wangwu', '123456', NOW(), '/images/avatar3.jpg');

-- 插入测试消息
INSERT INTO `message` (`sender_id`, `receiver_id`, `content`, `message_type`, `timestamp`) VALUES
(1, 2, '你好！', 'TEXT', NOW()),
(2, 1, '你好啊！最近怎么样？', 'TEXT', NOW()),
(1, 2, '还不错，你呢？', 'TEXT', NOW());

-- 插入测试地理位置记录
INSERT INTO `location_results` (`client_ip`, `public_ip`, `city`, `province`, `country`, `isp`, `raw_location`, `success`, `message`, `timestamp`) VALUES
('192.168.1.100', '123.123.123.123', '北京', '北京市', '中国', '中国联通', '中国 北京市 北京市 中国联通', 1, '查询成功', NOW()),
('10.0.0.50', '210.210.210.210', '上海', '上海市', '中国', '中国电信', '中国 上海市 上海市 中国电信', 1, '查询成功', NOW());

-- ============================================
-- 查看创建的表
-- ============================================
SHOW TABLES;
