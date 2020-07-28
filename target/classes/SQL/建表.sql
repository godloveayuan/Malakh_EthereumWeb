-- =======================================    创建数据库   ==================================================
CREATE DATABASE Malakh;

use Malakh;
show tables;
-- =======================================    创建表   ==================================================
-- 设备信息表
CREATE TABLE `device_info`
(
    `id`                 INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `device_name`        VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备名称',
    `device_mac`         VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备出厂标识',
    `device_uid`         VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '服务器下发的设备标识',
    `device_type`        VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备类型',
    `manufacture`        VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '生产厂商',
    `attribute_type`     VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备类型属性',
    `attribute_position` VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备位置属性',
    `attribute_system`   VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '系统属性',
    `agent_device`       VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '代理设备',
    `status`             INT              NOT NULL DEFAULT 1 COMMENT '当前状态 1-可用 0-不可用',
    `create_time`        DATETIME        COMMENT '创建时间',
    `update_time`        DATETIME        COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_idx_device_uid` (`device_uid`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT ='设备信息表';

-- 代理设备信息表
CREATE TABLE `agent_info`
(
    `id`          INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `device_name` VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备名称',
    `device_uid`  VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '服务器下发的设备标识',
    `address`     VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '代理设备地址',
    `device_mac`  VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备出厂标识',
    `manufacture` VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '生产厂商',
    `status`      INT              NOT NULL DEFAULT 1 COMMENT '当前状态 1-可用 0-不可用',
    `create_time` DATETIME        COMMENT '创建时间',
    `update_time` DATETIME        COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_idx_device_uid` (`device_uid`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT ='代理设备信息表';

-- 合约信息表
CREATE TABLE `contract_info`
(
    `id`          INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '合约名称',
    `type`        VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '合约类型',
    `owner`       VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '合约拥有者',
    `address`     VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '合约地址',
    `status`      INT              NOT NULL DEFAULT 1 COMMENT '当前状态 1-可用 0-不可用',
    `create_time` DATETIME         COMMENT '创建时间',
    `update_time` DATETIME         COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_idx_address` (`address`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT ='合约信息表';

-- 设置的安全规则表
CREATE TABLE `security_rule`
(
    `id`                INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`              VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '规则名称',
    `type`              INT              NOT NULL DEFAULT 0 COMMENT '规则类型 0-其他 1-访问频率 2-身份认证 3-越权访问',
    `check_count`       INT              NOT NULL DEFAULT 0 COMMENT '检测时间内允许失败的次数',
    `check_time_number` INT              NOT NULL DEFAULT 1 COMMENT '检测时间：',
    `check_time_unit`   VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '检测的时间单位：second/minute/hour/day',
    `punish_number`     INT              NOT NULL DEFAULT 1 COMMENT '惩罚时间：unite_number+unite = 10秒/5分钟',
    `punish_unit`       VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '惩罚时间单位：second/minute/hour/day',
    `kick_out`          BOOLEAN          NOT NULL DEFAULT FALSE COMMENT '违反规则后是否直接驱逐',
    `status`            INT              NOT NULL DEFAULT 1 COMMENT '规则是否可用 1-可用 0-不可用',
    `create_time`       DATETIME         COMMENT '创建时间',
    `update_time`       DATETIME         COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_idx_punish` (`type`, `kick_out`, `status`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT ='安全规则表';

-- 违反安全规则惩罚表
CREATE TABLE `punish_info`
(
    `id`           INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `device_uid`   VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '设备Uid',
    `rule_name`    VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '违反的安全规则',
    `punish`       INT              NOT NULL DEFAULT 1 COMMENT '惩罚措施 1-隔离 2-驱逐',
    `punish_start` DATETIME         COMMENT '惩罚开始时间',
    `punish_end`   DATETIME         COMMENT '惩罚结束时间',
    `status`       INT              NOT NULL DEFAULT 1 COMMENT '是否处于惩罚状态 1-是 0-否',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT ='惩罚表';

-- 访问日志表
CREATE TABLE `access_log`
(
    `id`            INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `subject_uid`   VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '发送访问请求的主体uid',
    `object_uid`    VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '发送访问请求的客体uid',
    `operate_type`  VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '访问请求类型 read/control',
    `operate_data`  VARCHAR(64) COMMENT '访问请求数据 on/off',
    `access_allow`  BOOLEAN COMMENT '是否允许访问 true/false',
    `fail_reason`   INT COMMENT '访问失败原因:1-访问频率过高 2-身份认证不通过 3-访问权限不通过 4-设备惩罚中 5-未失败',
    `result_data`   VARCHAR(64) COMMENT '访问的结果数据',
    `request_time`  DATETIME         COMMENT '发送访问请求的时间',
    `response_time` DATETIME         COMMENT '响应访问请求的时间',
    `status`        INT NOT NULL DEFAULT 1 COMMENT '访问日志是否可以作为惩罚规则依据',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT ='访问日志表';

-- 用户表
CREATE TABLE `user_info`
(
    `id`          INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name`   VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '用户名',
    `user_pwd`    VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '密码',
    `status`      INT              NOT NULL DEFAULT 1 COMMENT '当前状态 1-可用 0-不可用',
    `create_time` DATETIME         COMMENT '创建时间',
    `update_time` DATETIME         COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_idx_user_name` (`user_name`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT ='用户表';