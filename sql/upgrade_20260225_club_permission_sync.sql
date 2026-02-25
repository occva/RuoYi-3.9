-- ============================================================
-- 社团权限同步补丁（增量，幂等）
-- 适用场景：
-- 1) 已有数据库未重建，仍使用旧权限 system:application:* / system:member:*
-- 2) 前后端已切到 club:* 权限码与 /system/application、/system/member 接口
-- 执行后请让相关账号重新登录（刷新 Redis 中缓存的权限集合）
-- ============================================================

START TRANSACTION;

-- 一级菜单权限码与统计组件路径同步
UPDATE sys_menu
SET perms = 'club:application:list', component = 'club/application/index', path = 'application'
WHERE menu_id = 3201;

UPDATE sys_menu
SET perms = 'club:member:list', component = 'club/member/index', path = 'member'
WHERE menu_id = 3202;

UPDATE sys_menu
SET perms = 'club:application:list', component = 'club/application/stat', path = 'application-stat'
WHERE menu_id = 3203;

-- 申请按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3211, '申请查询', 3201, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:query', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3211);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3212, '申请审核', 3201, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:review', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3212);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3213, '申请删除', 3201, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:remove', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3213);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3214, '申请导出', 3201, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:export', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3214);

-- 成员按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3221, '成员查询', 3202, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:query', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3221);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3222, '成员新增', 3202, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:add', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3222);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3223, '成员修改', 3202, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:edit', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3223);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3224, '成员删除', 3202, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:remove', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3224);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 3225, '成员导出', 3202, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:export', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 3225);

-- 已存在按钮统一为最新权限码
UPDATE sys_menu SET perms = 'club:application:query'  WHERE menu_id = 3211;
UPDATE sys_menu SET perms = 'club:application:review' WHERE menu_id = 3212;
UPDATE sys_menu SET perms = 'club:application:remove' WHERE menu_id = 3213;
UPDATE sys_menu SET perms = 'club:application:export' WHERE menu_id = 3214;
UPDATE sys_menu SET perms = 'club:member:query'       WHERE menu_id = 3221;
UPDATE sys_menu SET perms = 'club:member:add'         WHERE menu_id = 3222;
UPDATE sys_menu SET perms = 'club:member:edit'        WHERE menu_id = 3223;
UPDATE sys_menu SET perms = 'club:member:remove'      WHERE menu_id = 3224;
UPDATE sys_menu SET perms = 'club:member:export'      WHERE menu_id = 3225;

-- 角色授权补齐
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES
(100, 3211), (100, 3212), (100, 3213), (100, 3214), (100, 3221), (100, 3222), (100, 3223), (100, 3224), (100, 3225),
(101, 3211), (101, 3212), (101, 3213), (101, 3221), (101, 3223), (101, 3224),
(102, 3211), (102, 3212), (102, 3221);

COMMIT;
