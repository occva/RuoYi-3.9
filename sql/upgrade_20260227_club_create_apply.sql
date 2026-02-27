-- ============================================================
-- New club create application flow (idempotent patch)
-- 1) add club_create_application table
-- 2) add menu/button permissions for review page
-- ============================================================

START TRANSACTION;

CREATE TABLE IF NOT EXISTS club_create_application (
  apply_id             bigint(20)      NOT NULL AUTO_INCREMENT COMMENT 'application id',
  club_name            varchar(100)    NOT NULL                COMMENT 'target club name',
  category_id          bigint(20)      NOT NULL                COMMENT 'club category id',
  logo_url             varchar(255)    DEFAULT ''              COMMENT 'club logo',
  contact_phone        varchar(20)     DEFAULT ''              COMMENT 'contact phone',
  description          text                                     COMMENT 'club description',
  apply_reason         text                                     COMMENT 'apply reason',
  activity_plan        text                                     COMMENT 'activity plan',
  core_members         text                                     COMMENT 'core members plan',
  advisor_name         varchar(50)     DEFAULT ''              COMMENT 'advisor name',
  advisor_contact      varchar(100)    DEFAULT ''              COMMENT 'advisor contact',
  applicant_user_id    bigint(20)      NOT NULL                COMMENT 'applicant user id',
  applicant_user_name  varchar(30)     NOT NULL                COMMENT 'applicant username',
  applicant_nick_name  varchar(30)     DEFAULT ''              COMMENT 'applicant nickname',
  applicant_phone      varchar(20)     DEFAULT ''              COMMENT 'applicant phone',
  applicant_email      varchar(100)    DEFAULT ''              COMMENT 'applicant email',
  apply_time           datetime                                  COMMENT 'application time',
  review_status        char(1)         DEFAULT '0'             COMMENT 'review status (0 pending 1 approved 2 rejected)',
  reviewer_id          bigint(20)                                COMMENT 'reviewer id',
  reviewer_name        varchar(50)     DEFAULT ''              COMMENT 'reviewer name',
  review_time          datetime                                  COMMENT 'review time',
  review_comment       varchar(500)    DEFAULT ''              COMMENT 'review comment',
  approved_club_id     bigint(20)                                COMMENT 'approved club id',
  admin_user_id        bigint(20)                                COMMENT 'generated admin user id',
  admin_user_name      varchar(30)     DEFAULT ''              COMMENT 'generated admin username',
  admin_init_password  varchar(100)    DEFAULT ''              COMMENT 'generated admin init password',
  del_flag             char(1)         DEFAULT '0'             COMMENT 'delete flag (0 exists 2 deleted)',
  create_by            varchar(64)     DEFAULT ''              COMMENT 'create by',
  create_time          datetime                                  COMMENT 'create time',
  update_by            varchar(64)     DEFAULT ''              COMMENT 'update by',
  update_time          datetime                                  COMMENT 'update time',
  remark               varchar(500)    DEFAULT NULL            COMMENT 'remark',
  PRIMARY KEY (apply_id),
  KEY idx_cca_status (review_status),
  KEY idx_cca_apply_time (apply_time),
  KEY idx_cca_applicant (applicant_user_id),
  KEY idx_cca_club_name (club_name)
) ENGINE=InnoDB AUTO_INCREMENT=10000 COMMENT='new club create application';

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3204, '新社团申请', 3100, 4, 'club-apply', 'club/clubApply/index', '', '', 1, 0, 'C', '0', '0', 'club:createApply:list', 'edit-pen', 'admin', NOW(), '', NULL, '新社团申请审核'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3204);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3231, '新社团申请查询', 3204, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:query', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3231);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3232, '新社团申请审核', 3204, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:review', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3232);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3233, '新社团申请删除', 3204, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:remove', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3233);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3234, '新社团申请导出', 3204, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:export', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3234);

UPDATE sys_menu
SET menu_name = '新社团申请',
    parent_id = 3100,
    order_num = 4,
    perms = 'club:createApply:list',
    component = 'club/clubApply/index',
    path = 'club-apply',
    remark = '新社团申请审核'
WHERE menu_id = 3204;
UPDATE sys_menu SET menu_name = '新社团申请查询', perms = 'club:createApply:query' WHERE menu_id = 3231;
UPDATE sys_menu SET menu_name = '新社团申请审核', perms = 'club:createApply:review' WHERE menu_id = 3232;
UPDATE sys_menu SET menu_name = '新社团申请删除', perms = 'club:createApply:remove' WHERE menu_id = 3233;
UPDATE sys_menu SET menu_name = '新社团申请导出', perms = 'club:createApply:export' WHERE menu_id = 3234;

INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES
(1, 3204), (100, 3204),
(1, 3231), (1, 3232), (1, 3233), (1, 3234),
(100, 3231), (100, 3232), (100, 3233), (100, 3234);

COMMIT;
