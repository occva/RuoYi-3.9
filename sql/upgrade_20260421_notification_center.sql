CREATE TABLE IF NOT EXISTS `app_notice_read` (
  `read_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `notice_source` varchar(16) NOT NULL COMMENT '通知来源(system/club)',
  `notice_id` bigint NOT NULL COMMENT '通知ID',
  `read_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '已读时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`read_id`),
  UNIQUE KEY `uk_user_notice` (`user_id`,`notice_source`,`notice_id`),
  KEY `idx_notice_lookup` (`notice_source`,`notice_id`),
  KEY `idx_user_read_time` (`user_id`,`read_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户通知已读记录';
