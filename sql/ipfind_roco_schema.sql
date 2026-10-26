-- ============================================================
-- ipfind 项目 MySQL 建表脚本（目标库：roco）
-- 依据 com.nsc.ipfind.pojos 下的 MyBatis-Plus 实体类生成
-- 字符集：utf8mb4，引擎：InnoDB
-- ============================================================

USE roco;

-- ------------------------------------------------------------
-- 1. 用户表 (对应 User.java)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`         INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`       VARCHAR(64)  DEFAULT NULL COMMENT '用户昵称',
    `zhanghao`   VARCHAR(64)  DEFAULT NULL COMMENT '登录账号',
    `password`   VARCHAR(128) DEFAULT NULL COMMENT '登录密码',
    `creattime`  DATETIME     DEFAULT NULL COMMENT '创建时间',
    `avatarurl`  VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_zhanghao` (`zhanghao`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- ------------------------------------------------------------
-- 2. 聊天消息表 (对应 Message.java)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `sender_id`   INT          DEFAULT NULL COMMENT '发送者ID (关联 user.id)',
    `receiver_id` INT          DEFAULT NULL COMMENT '接收者ID (关联 user.id)',
    `content`     TEXT         DEFAULT NULL COMMENT '消息内容',
    `message_type` VARCHAR(32) DEFAULT 'TEXT' COMMENT '消息类型：TEXT/IMAGE等',
    `timestamp`   DATETIME     DEFAULT NULL COMMENT '消息时间戳',
    PRIMARY KEY (`id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '聊天消息表';

-- ------------------------------------------------------------
-- 3. 地理位置查询结果表 (对应 LocationResults.java)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `location_results`;
CREATE TABLE `location_results` (
    `id`            BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `client_ip`     VARCHAR(64)  DEFAULT NULL COMMENT '客户端IP地址',
    `public_ip`     VARCHAR(64)  DEFAULT NULL COMMENT '外网IP地址（客户端为内网IP时使用）',
    `city`          VARCHAR(64)  DEFAULT NULL COMMENT '城市名称',
    `province`      VARCHAR(64)  DEFAULT NULL COMMENT '省份/地区名称',
    `country`       VARCHAR(64)  DEFAULT NULL COMMENT '国家名称',
    `isp`           VARCHAR(64)  DEFAULT NULL COMMENT '网络服务提供商',
    `raw_location`  VARCHAR(255) DEFAULT NULL COMMENT '原始位置信息字符串',
    `success`       INT          DEFAULT NULL COMMENT '操作是否成功：1=成功 0=失败',
    `message`       VARCHAR(255) DEFAULT NULL COMMENT '响应消息',
    `timestamp`     DATETIME(3)  DEFAULT NULL COMMENT '查询时间戳（精确到毫秒）',
    PRIMARY KEY (`id`),
    KEY `idx_client_ip` (`client_ip`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '地理位置查询结果表';

-- ------------------------------------------------------------
-- 4. 宠物主表 (对应 Pet.java)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet` (
    `pet_id`           INT NOT NULL COMMENT '宠物ID (接口主键)',
    `name`             VARCHAR(64)    DEFAULT NULL COMMENT '宠物名称',
    `handbook_no`      VARCHAR(16)    DEFAULT NULL COMMENT '图鉴编号，如 002',
    `quality`          INT            DEFAULT NULL COMMENT '品质/稀有度: 1=普通 2=稀有 3=史诗 4=传说',
    `stage`            INT            DEFAULT NULL COMMENT '进化阶段: 1=一阶 2=二阶 3=最终形态',
    `season_id`        INT            DEFAULT NULL COMMENT '赛季ID, 0=常驻精灵',
    `has_ride_talent`  INT            DEFAULT NULL COMMENT '是否有骑乘天赋: 0=否 1=是',
    `has_shiny`        INT            DEFAULT NULL COMMENT '是否有闪光/异色形态: 0=否 1=是',
    `height_min_cm`    DECIMAL(10, 2) DEFAULT NULL COMMENT '最小身高(cm)',
    `height_max_cm`    DECIMAL(10, 2) DEFAULT NULL COMMENT '最大身高(cm)',
    `weight_min_g`     INT            DEFAULT NULL COMMENT '最小体重(g)',
    `weight_max_g`     INT            DEFAULT NULL COMMENT '最大体重(g)',
    `icon`             VARCHAR(512)   DEFAULT NULL COMMENT '图标URL',
    `small_icon`       VARCHAR(512)   DEFAULT NULL COMMENT '小图标URL',
    `created_at`       DATETIME       DEFAULT NULL COMMENT '创建时间',
    `updated_at`       DATETIME       DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`pet_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '宠物主表';

-- ------------------------------------------------------------
-- 5. 宠物属性/系别字典表 (对应 PetType.java)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `pet_type`;
CREATE TABLE `pet_type` (
    `id`         INT NOT NULL COMMENT '属性ID (对应接口 type_ids)',
    `name`       VARCHAR(32) DEFAULT NULL COMMENT '属性名称，如 草/火/水',
    `created_at` DATETIME    DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '宠物属性/系别字典表';

-- ------------------------------------------------------------
-- 6. 宠物-属性关联表 (对应 PetTypeRel.java，无外键)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `pet_type_rel`;
CREATE TABLE `pet_type_rel` (
    `id`       BIGINT NOT NULL AUTO_INCREMENT,
    `pet_id`   INT DEFAULT NULL COMMENT '宠物ID',
    `type_id`  INT DEFAULT NULL COMMENT '属性ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pet_type` (`pet_id`, `type_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '宠物-属性多对多关联表';

-- ------------------------------------------------------------
-- 7. 蛋组字典表 (对应 EggGroup.java)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `egg_group`;
CREATE TABLE `egg_group` (
    `id`         INT NOT NULL COMMENT '蛋组ID (对应接口 egg_group_ids)',
    `name`       VARCHAR(32) DEFAULT NULL COMMENT '蛋组名称，如 动物组/魔力组',
    `created_at` DATETIME    DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '蛋组字典表';

-- ------------------------------------------------------------
-- 8. 宠物-蛋组关联表 (对应 PetEggGroupRel.java，无外键)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `pet_egg_group_rel`;
CREATE TABLE `pet_egg_group_rel` (
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `pet_id`       INT DEFAULT NULL COMMENT '宠物ID',
    `egg_group_id` INT DEFAULT NULL COMMENT '蛋组ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pet_egg_group` (`pet_id`, `egg_group_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '宠物-蛋组多对多关联表';
