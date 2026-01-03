-- ------------------------------------------------------------
-- 数据库创建
-- ------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `solver_platform` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `solver_platform`;

-- ------------------------------------------------------------
-- 1. 用户与权限表
-- ------------------------------------------------------------
CREATE TABLE `users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password_hash` VARCHAR(255) NOT NULL COMMENT '密码哈希',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `full_name` VARCHAR(100) DEFAULT NULL COMMENT '全名',
  `role` ENUM('ADMIN', 'OPERATOR', 'VIEWER') NOT NULL DEFAULT 'VIEWER' COMMENT '角色',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否激活',
  `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_role` (`role`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ------------------------------------------------------------
-- 2. 计算服务器表
-- ------------------------------------------------------------
CREATE TABLE `compute_servers` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '服务器ID',
  `name` VARCHAR(100) NOT NULL COMMENT '服务器名称',
  `host` VARCHAR(100) NOT NULL COMMENT '服务器主机/IP',
  `port` INT(11) NOT NULL DEFAULT 22 COMMENT 'SSH端口',
  `ssh_username` VARCHAR(50) NOT NULL COMMENT 'SSH用户名',
  `ssh_password` VARCHAR(255) DEFAULT NULL COMMENT 'SSH密码（加密存储）',
  `ssh_private_key_path` VARCHAR(500) DEFAULT NULL COMMENT 'SSH私钥路径',
  `description` TEXT COMMENT '服务器描述',
  `status` ENUM('ONLINE', 'OFFLINE', 'MAINTENANCE', 'ERROR') NOT NULL DEFAULT 'ONLINE' COMMENT '服务器状态',
  `gpu_count` INT(11) NOT NULL DEFAULT 0 COMMENT 'GPU数量',
  `max_concurrent_tasks` INT(11) NOT NULL DEFAULT 4 COMMENT '最大并发任务数',
  `tags` JSON DEFAULT NULL COMMENT '服务器标签（JSON数组）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_heartbeat` DATETIME DEFAULT NULL COMMENT '最后心跳时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_host_port` (`host`, `port`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_status` (`status`),
  KEY `idx_gpu_count` (`gpu_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='计算服务器表';

-- ------------------------------------------------------------
-- 3. GPU资源表
-- ------------------------------------------------------------
CREATE TABLE `gpu_resources` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'GPU资源ID',
  `server_id` BIGINT(20) NOT NULL COMMENT '所属服务器ID',
  `gpu_index` INT(11) NOT NULL COMMENT 'GPU索引（从0开始）',
  `name` VARCHAR(100) DEFAULT NULL COMMENT 'GPU名称（如NVIDIA A100）',
  `memory_mb` INT(11) DEFAULT NULL COMMENT '显存大小（MB）',
  `status` ENUM('AVAILABLE', 'BUSY', 'RESERVED', 'ERROR', 'MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE' COMMENT 'GPU状态',
  `current_task_id` BIGINT(20) DEFAULT NULL COMMENT '当前运行的任务ID',
  `utilization` DECIMAL(5,2) DEFAULT 0.00 COMMENT 'GPU利用率（0-100）',
  `temperature` DECIMAL(5,2) DEFAULT NULL COMMENT 'GPU温度',
  `last_checked` DATETIME DEFAULT NULL COMMENT '最后检查时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_server_gpu` (`server_id`, `gpu_index`),
  KEY `idx_status` (`status`),
  KEY `idx_current_task` (`current_task_id`),
  CONSTRAINT `fk_gpu_server` FOREIGN KEY (`server_id`) 
    REFERENCES `compute_servers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GPU资源表';

-- ------------------------------------------------------------
-- 4. 求解器版本表
-- ------------------------------------------------------------
CREATE TABLE `solver_versions` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '求解器版本ID',
  `name` VARCHAR(100) NOT NULL COMMENT '求解器名称',
  `version` VARCHAR(50) NOT NULL COMMENT '版本号',
  `full_version` VARCHAR(200) NOT NULL COMMENT '完整版本标识',
  `file_path` VARCHAR(500) NOT NULL COMMENT '求解器文件路径（共享存储）',
  `description` TEXT COMMENT '版本描述',
  `uploaded_by` BIGINT(20) NOT NULL COMMENT '上传用户ID',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `total_runs` INT(11) NOT NULL DEFAULT 0 COMMENT '总运行次数',
  `success_count` INT(11) NOT NULL DEFAULT 0 COMMENT '成功次数',
  `failure_count` INT(11) NOT NULL DEFAULT 0 COMMENT '失败次数',
  `avg_execution_time` DECIMAL(10,2) DEFAULT NULL COMMENT '平均执行时间（秒）',
  `success_rate` DECIMAL(5,2) DEFAULT NULL COMMENT '成功率（%）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_version` (`name`, `version`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_uploaded_by` (`uploaded_by`),
  KEY `idx_success_rate` (`success_rate`),
  CONSTRAINT `fk_solver_uploader` FOREIGN KEY (`uploaded_by`) 
    REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求解器版本表';

-- ------------------------------------------------------------
-- 5. 求解文件表
-- ------------------------------------------------------------
CREATE TABLE `solver_files` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `name` VARCHAR(200) NOT NULL COMMENT '文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径（共享存储）',
  `description` TEXT COMMENT '文件描述',
  `file_size` BIGINT(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `file_type` VARCHAR(50) DEFAULT NULL COMMENT '文件类型（如CASE, CONFIG等）',
  `tags` JSON DEFAULT NULL COMMENT '文件标签（JSON数组）',
  `uploaded_by` BIGINT(20) NOT NULL COMMENT '上传用户ID',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `usage_count` INT(11) NOT NULL DEFAULT 0 COMMENT '使用次数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_file_path` (`file_path`),
  KEY `idx_name` (`name`),
  KEY `idx_file_type` (`file_type`),
  KEY `idx_uploaded_by` (`uploaded_by`),
  KEY `idx_is_active` (`is_active`),
  CONSTRAINT `fk_file_uploader` FOREIGN KEY (`uploaded_by`) 
    REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求解文件表';

-- ------------------------------------------------------------
-- 6. 求解文件组表（支持多选文件）
-- ------------------------------------------------------------
CREATE TABLE `solver_file_groups` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '文件组ID',
  `name` VARCHAR(200) NOT NULL COMMENT '文件组名称',
  `description` TEXT COMMENT '文件组描述',
  `created_by` BIGINT(20) NOT NULL COMMENT '创建用户ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_created_by` (`created_by`),
  CONSTRAINT `fk_file_group_creator` FOREIGN KEY (`created_by`) 
    REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求解文件组表';

-- 文件组与文件的关系表
CREATE TABLE `solver_file_group_items` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `group_id` BIGINT(20) NOT NULL COMMENT '文件组ID',
  `file_id` BIGINT(20) NOT NULL COMMENT '文件ID',
  `order_index` INT(11) NOT NULL DEFAULT 0 COMMENT '文件在组中的顺序',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_file` (`group_id`, `file_id`),
  KEY `idx_file_id` (`file_id`),
  CONSTRAINT `fk_group_item_group` FOREIGN KEY (`group_id`) 
    REFERENCES `solver_file_groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_group_item_file` FOREIGN KEY (`file_id`) 
    REFERENCES `solver_files` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件组关系表';

-- ------------------------------------------------------------
-- 7. 计算任务主表
-- ------------------------------------------------------------
CREATE TABLE `compute_tasks` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_name` VARCHAR(200) NOT NULL COMMENT '任务名称',
  `task_description` TEXT COMMENT '任务描述',
  `solver_version_id` BIGINT(20) NOT NULL COMMENT '求解器版本ID',
  
  -- 文件支持：单个文件或文件组
  `solver_file_id` BIGINT(20) DEFAULT NULL COMMENT '单个求解文件ID',
  `solver_file_group_id` BIGINT(20) DEFAULT NULL COMMENT '求解文件组ID',
  
  `server_id` BIGINT(20) DEFAULT NULL COMMENT '指定服务器ID（为空则系统分配）',
  `gpu_id` BIGINT(20) DEFAULT NULL COMMENT '分配的GPU ID',
  `gpu_index` INT(11) DEFAULT NULL COMMENT '分配的GPU索引',
  
  -- 任务执行参数
  `parameters` JSON DEFAULT NULL COMMENT '求解参数（JSON格式）',
  `priority` ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT') NOT NULL DEFAULT 'NORMAL' COMMENT '任务优先级',
  `status` ENUM(
    'PENDING',      -- 待处理
    'QUEUED',       -- 排队中（已分配到GPU队列）
    'RUNNING',      -- 运行中
    'PAUSED',       -- 已暂停
    'COMPLETED',    -- 已完成
    'FAILED',       -- 已失败
    'CANCELLED',    -- 已取消
    'TIMEOUT'       -- 超时
  ) NOT NULL DEFAULT 'PENDING' COMMENT '任务状态',
  
  -- 执行相关信息
  `remote_pid` VARCHAR(50) DEFAULT NULL COMMENT '远程进程PID',
  `queue_position` INT(11) DEFAULT NULL COMMENT '在队列中的位置',
  `estimated_duration` INT(11) DEFAULT NULL COMMENT '预估持续时间（秒）',
  
  -- 时间戳
  `submitted_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `queued_at` DATETIME DEFAULT NULL COMMENT '进入队列时间',
  `started_at` DATETIME DEFAULT NULL COMMENT '开始执行时间',
  `finished_at` DATETIME DEFAULT NULL COMMENT '完成时间',
  `last_heartbeat` DATETIME DEFAULT NULL COMMENT '最后心跳时间',
  `timeout_at` DATETIME DEFAULT NULL COMMENT '超时时间',
  
  -- 结果信息
  `exit_code` INT(11) DEFAULT NULL COMMENT '退出代码',
  `error_message` TEXT COMMENT '错误信息',
  `log_file_path` VARCHAR(500) DEFAULT NULL COMMENT '日志文件路径',
  `result_file_path` VARCHAR(500) DEFAULT NULL COMMENT '结果文件路径',
  
  -- 性能指标
  `execution_time_seconds` DECIMAL(10,2) DEFAULT NULL COMMENT '执行时间（秒）',
  `peak_gpu_memory_mb` INT(11) DEFAULT NULL COMMENT '峰值GPU显存使用（MB）',
  `avg_gpu_utilization` DECIMAL(5,2) DEFAULT NULL COMMENT '平均GPU利用率（%）',
  
  -- 提交信息
  `submitted_by` BIGINT(20) NOT NULL COMMENT '提交用户ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  KEY `idx_solver_version` (`solver_version_id`),
  KEY `idx_solver_file` (`solver_file_id`),
  KEY `idx_file_group` (`solver_file_group_id`),
  KEY `idx_server` (`server_id`),
  KEY `idx_gpu` (`gpu_id`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_submitted_by` (`submitted_by`),
  KEY `idx_submitted_at` (`submitted_at`),
  KEY `idx_finished_at` (`finished_at`),
  KEY `idx_status_priority` (`status`, `priority`),
  
  CONSTRAINT `fk_task_solver_version` FOREIGN KEY (`solver_version_id`) 
    REFERENCES `solver_versions` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_task_solver_file` FOREIGN KEY (`solver_file_id`) 
    REFERENCES `solver_files` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_task_file_group` FOREIGN KEY (`solver_file_group_id`) 
    REFERENCES `solver_file_groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_task_server` FOREIGN KEY (`server_id`) 
    REFERENCES `compute_servers` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_task_gpu` FOREIGN KEY (`gpu_id`) 
    REFERENCES `gpu_resources` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_task_submitter` FOREIGN KEY (`submitted_by`) 
    REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='计算任务表';

-- ------------------------------------------------------------
-- 8. GPU任务队列表（用于手动调整顺序）
-- ------------------------------------------------------------
CREATE TABLE `gpu_task_queues` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '队列项ID',
  `gpu_id` BIGINT(20) NOT NULL COMMENT 'GPU ID',
  `task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
  `queue_position` INT(11) NOT NULL COMMENT '在队列中的位置',
  `status` ENUM('QUEUED', 'PROCESSING', 'REMOVED') NOT NULL DEFAULT 'QUEUED' COMMENT '队列状态',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gpu_position` (`gpu_id`, `queue_position`),
  UNIQUE KEY `uk_task` (`task_id`),
  KEY `idx_gpu_status` (`gpu_id`, `status`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_queue_gpu` FOREIGN KEY (`gpu_id`) 
    REFERENCES `gpu_resources` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_queue_task` FOREIGN KEY (`task_id`) 
    REFERENCES `compute_tasks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GPU任务队列表';

-- ------------------------------------------------------------
-- 9. 任务执行日志表（详细记录）
-- ------------------------------------------------------------
CREATE TABLE `task_execution_logs` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
  `log_level` ENUM('DEBUG', 'INFO', 'WARN', 'ERROR', 'CRITICAL') NOT NULL DEFAULT 'INFO' COMMENT '日志级别',
  `log_message` TEXT NOT NULL COMMENT '日志消息',
  `log_timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志时间',
  `source` VARCHAR(100) DEFAULT NULL COMMENT '日志来源（如SSH, Monitor, Scheduler等）',
  `metadata` JSON DEFAULT NULL COMMENT '额外元数据',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_log_level` (`log_level`),
  KEY `idx_timestamp` (`log_timestamp`),
  KEY `idx_task_timestamp` (`task_id`, `log_timestamp`),
  CONSTRAINT `fk_log_task` FOREIGN KEY (`task_id`) 
    REFERENCES `compute_tasks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务执行日志表';

-- ------------------------------------------------------------
-- 10. 性能比较结果表
-- ------------------------------------------------------------
CREATE TABLE `solver_performance_comparisons` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '比较ID',
  `base_solver_id` BIGINT(20) NOT NULL COMMENT '基准求解器ID',
  `compare_solver_id` BIGINT(20) NOT NULL COMMENT '对比求解器ID',
  `task_count` INT(11) NOT NULL COMMENT '对比任务数量',
  
  -- 性能指标
  `avg_speedup` DECIMAL(10,4) DEFAULT NULL COMMENT '平均加速比（对比/基准）',
  `avg_execution_time_diff` DECIMAL(10,2) DEFAULT NULL COMMENT '平均执行时间差异（秒）',
  `success_rate_diff` DECIMAL(5,2) DEFAULT NULL COMMENT '成功率差异（%）',
  `result_accuracy` DECIMAL(5,2) DEFAULT NULL COMMENT '结果准确度（%）',
  
  -- 统计信息
  `faster_count` INT(11) DEFAULT 0 COMMENT '更快次数',
  `slower_count` INT(11) DEFAULT 0 COMMENT '更慢次数',
  `more_stable_count` INT(11) DEFAULT 0 COMMENT '更稳定次数',
  
  `compared_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '比较时间',
  `created_by` BIGINT(20) NOT NULL COMMENT '创建用户ID',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_solver_pair` (`base_solver_id`, `compare_solver_id`),
  KEY `idx_compared_at` (`compared_at`),
  KEY `idx_created_by` (`created_by`),
  CONSTRAINT `fk_comparison_base_solver` FOREIGN KEY (`base_solver_id`) 
    REFERENCES `solver_versions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comparison_compare_solver` FOREIGN KEY (`compare_solver_id`) 
    REFERENCES `solver_versions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comparison_creator` FOREIGN KEY (`created_by`) 
    REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求解器性能比较表';

-- ------------------------------------------------------------
-- 11. 系统配置表
-- ------------------------------------------------------------
CREATE TABLE `system_configurations` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT COMMENT '配置值',
  `config_type` ENUM('STRING', 'INTEGER', 'BOOLEAN', 'JSON', 'FLOAT') NOT NULL DEFAULT 'STRING' COMMENT '配置值类型',
  `description` TEXT COMMENT '配置描述',
  `category` VARCHAR(50) DEFAULT 'GENERAL' COMMENT '配置类别',
  `is_encrypted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否加密存储',
  `is_system` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为系统配置',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` BIGINT(20) DEFAULT NULL COMMENT '最后更新用户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_category` (`category`),
  KEY `idx_is_system` (`is_system`),
  CONSTRAINT `fk_config_updater` FOREIGN KEY (`updated_by`) 
    REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ------------------------------------------------------------
-- 初始化数据
-- ------------------------------------------------------------

-- 插入默认管理员用户（密码需在应用层加密）
INSERT INTO `users` (`username`, `password_hash`, `email`, `full_name`, `role`) 
VALUES 
('admin', '$2a$10$YourHashedPasswordHere', 'admin@solver-platform.com', '系统管理员', 'ADMIN');

-- 插入默认系统配置
INSERT INTO `system_configurations` (`config_key`, `config_value`, `config_type`, `description`, `category`) VALUES
('task.default_timeout', '86400', 'INTEGER', '任务默认超时时间（秒）', 'TASK'),
('task.max_retry_count', '3', 'INTEGER', '任务最大重试次数', 'TASK'),
('monitor.heartbeat_interval', '30', 'INTEGER', '心跳检查间隔（秒）', 'MONITOR'),
('monitor.gpu_check_interval', '60', 'INTEGER', 'GPU状态检查间隔（秒）', 'MONITOR'),
('ssh.connection_timeout', '30000', 'INTEGER', 'SSH连接超时（毫秒）', 'SSH'),
('ssh.socket_timeout', '60000', 'INTEGER', 'SSH Socket超时（毫秒）', 'SSH'),
('scheduler.queue_check_interval', '10', 'INTEGER', '队列检查间隔（秒）', 'SCHEDULER'),
('file.shared_storage_root', '/mnt/solver_storage', 'STRING', '共享存储根路径', 'STORAGE');

-- ------------------------------------------------------------
-- 索引优化
-- ------------------------------------------------------------
-- 为compute_tasks表添加复合索引以提高查询性能
CREATE INDEX `idx_task_status_time` ON `compute_tasks` (`status`, `priority`, `submitted_at`);
CREATE INDEX `idx_task_gpu_status` ON `compute_tasks` (`gpu_id`, `status`, `queue_position`);
CREATE INDEX `idx_task_server_status` ON `compute_tasks` (`server_id`, `status`, `submitted_at`);

-- 为gpu_resources表添加索引
CREATE INDEX `idx_gpu_server_status` ON `gpu_resources` (`server_id`, `status`, `gpu_index`);
CREATE INDEX `idx_gpu_utilization` ON `gpu_resources` (`utilization`);

-- ------------------------------------------------------------
-- 视图：GPU状态概览
-- ------------------------------------------------------------
CREATE VIEW `gpu_status_overview` AS
SELECT 
  cs.`name` AS server_name,
  cs.`host`,
  gr.`gpu_index`,
  gr.`name` AS gpu_name,
  gr.`status`,
  gr.`utilization`,
  gr.`temperature`,
  gr.`last_checked`,
  ct.`task_name` AS current_task,
  ct.`status` AS task_status
FROM `gpu_resources` gr
LEFT JOIN `compute_servers` cs ON gr.`server_id` = cs.`id`
LEFT JOIN `compute_tasks` ct ON gr.`current_task_id` = ct.`id`
WHERE cs.`status` = 'ONLINE';

-- ------------------------------------------------------------
-- 视图：求解器性能统计
-- ------------------------------------------------------------
CREATE VIEW `solver_performance_stats` AS
SELECT 
  sv.`id`,
  sv.`name`,
  sv.`version`,
  sv.`full_version`,
  sv.`total_runs`,
  sv.`success_count`,
  sv.`failure_count`,
  sv.`avg_execution_time`,
  sv.`success_rate`,
  COUNT(DISTINCT ct.`id`) AS active_tasks,
  AVG(ct.`execution_time_seconds`) AS recent_avg_time
FROM `solver_versions` sv
LEFT JOIN `compute_tasks` ct ON sv.`id` = ct.`solver_version_id`
  AND ct.`finished_at` >= DATE_SUB(NOW(), INTERVAL 7 DAY)
WHERE sv.`is_active` = 1
GROUP BY sv.`id`, sv.`name`, sv.`version`, sv.`full_version`;

-- ------------------------------------------------------------
-- 存储过程：更新GPU任务队列位置
-- ------------------------------------------------------------
DELIMITER //

CREATE PROCEDURE `update_gpu_queue_position`(
  IN p_gpu_id BIGINT,
  IN p_task_id BIGINT,
  IN p_new_position INT
)
BEGIN
  DECLARE old_position INT;
  DECLARE v_current_max_position INT;
  
  START TRANSACTION;
  
  -- 获取任务当前在队列中的位置
  SELECT `queue_position` INTO old_position 
  FROM `gpu_task_queues` 
  WHERE `gpu_id` = p_gpu_id AND `task_id` = p_task_id;
  
  IF old_position IS NULL THEN
    ROLLBACK;
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '任务不在指定GPU的队列中';
  END IF;
  
  IF old_position = p_new_position THEN
    COMMIT;
    RETURN;
  END IF;
  
  -- 获取当前队列的最大位置
  SELECT MAX(`queue_position`) INTO v_current_max_position 
  FROM `gpu_task_queues` 
  WHERE `gpu_id` = p_gpu_id;
  
  -- 确保新位置有效
  IF p_new_position < 1 OR p_new_position > v_current_max_position THEN
    ROLLBACK;
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '新位置超出有效范围';
  END IF;
  
  -- 调整队列位置
  IF old_position < p_new_position THEN
    -- 向后移动：将中间的任务前移
    UPDATE `gpu_task_queues`
    SET `queue_position` = `queue_position` - 1
    WHERE `gpu_id` = p_gpu_id 
      AND `queue_position` > old_position 
      AND `queue_position` <= p_new_position;
  ELSE
    -- 向前移动：将中间的任务后移
    UPDATE `gpu_task_queues`
    SET `queue_position` = `queue_position` + 1
    WHERE `gpu_id` = p_gpu_id 
      AND `queue_position` >= p_new_position 
      AND `queue_position` < old_position;
  END IF;
  
  -- 更新目标任务的位置
  UPDATE `gpu_task_queues`
  SET `queue_position` = p_new_position
  WHERE `gpu_id` = p_gpu_id AND `task_id` = p_task_id;
  
  COMMIT;
END//

DELIMITER ;

-- ------------------------------------------------------------
-- 触发器：自动更新求解器统计数据
-- ------------------------------------------------------------
DELIMITER //

CREATE TRIGGER `trg_update_solver_stats_after_task`
AFTER UPDATE ON `compute_tasks`
FOR EACH ROW
BEGIN
  DECLARE total_runs INT;
  DECLARE success_count INT;
  DECLARE failure_count INT;
  DECLARE avg_time DECIMAL(10,2);
  
  -- 只有任务状态变为完成或失败时才更新
  IF (OLD.status != 'COMPLETED' AND NEW.status = 'COMPLETED') OR
     (OLD.status != 'FAILED' AND NEW.status = 'FAILED') THEN
    
    -- 计算统计数据
    SELECT 
      COUNT(*),
      SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END),
      SUM(CASE WHEN status = 'FAILED' THEN 1 ELSE 0 END),
      AVG(execution_time_seconds)
    INTO total_runs, success_count, failure_count, avg_time
    FROM `compute_tasks`
    WHERE `solver_version_id` = NEW.solver_version_id
      AND (`status` = 'COMPLETED' OR `status` = 'FAILED');
    
    -- 更新求解器版本表
    UPDATE `solver_versions`
    SET 
      `total_runs` = total_runs,
      `success_count` = success_count,
      `failure_count` = failure_count,
      `avg_execution_time` = avg_time,
      `success_rate` = CASE 
        WHEN total_runs > 0 THEN (success_count * 100.0 / total_runs)
        ELSE NULL
      END,
      `updated_at` = NOW()
    WHERE `id` = NEW.solver_version_id;
  END IF;
END//

DELIMITER ;