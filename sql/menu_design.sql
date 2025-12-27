-- =====================================================
-- 社团管理系统 - 菜单权限设计 SQL（树形结构）
-- 
-- 设计原则：
--   1. 采用树形结构，按业务模块分组
--   2. 普通用户不进入后台，只在 user/home 页面操作
--   3. 管理员拥有社长/副社长的所有权限
--   4. 按钮权限在页面内通过 v-hasPermi 控制
-- =====================================================

-- 清理旧菜单（可选，谨慎执行）
-- DELETE FROM sys_menu WHERE menu_id >= 3000 AND menu_id < 4000;
-- DELETE FROM sys_role_menu WHERE menu_id >= 3000 AND menu_id < 4000;

-- =====================================================
-- 一、菜单定义（树形结构）
-- =====================================================

-- ==================== 1. 社团信息 ====================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3100, '社团信息', 0, 1, 'club-info', NULL, 'M', '0', '0', '', 'club', 'admin', NOW(), '社团信息管理目录');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3101, '社团列表', 3100, 1, 'list', 'club/list/index', 'C', '0', '0', 'system:club:list', 'peoples', 'admin', NOW(), '社团列表管理');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3102, '社团分类', 3100, 2, 'category', 'club/category/index', 'C', '0', '0', 'system:category:list', 'tree', 'admin', NOW(), '社团分类管理');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3103, '荣誉管理', 3100, 3, 'achievement', 'club/achievement/index', 'C', '0', '0', 'system:achievement:list', 'star', 'admin', NOW(), '社团荣誉管理');


-- ==================== 2. 社团运营 ====================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3200, '社团运营', 0, 2, 'club-operation', NULL, 'M', '0', '0', '', 'guide', 'admin', NOW(), '社团运营管理目录');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3201, '入社申请', 3200, 1, 'application', 'club/application/index', 'C', '0', '0', 'system:application:list', 'form', 'admin', NOW(), '入社申请审核');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3202, '成员管理', 3200, 2, 'member', 'club/member/index', 'C', '0', '0', 'system:member:list', 'user', 'admin', NOW(), '社团成员管理');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3203, '申请统计', 3200, 3, 'application-stat', 'club/application/statistics', 'C', '0', '0', 'system:application:statistics', 'chart', 'admin', NOW(), '申请数据统计');


-- ==================== 3. 社团宣传 ====================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3300, '社团宣传', 0, 3, 'club-promotion', NULL, 'M', '0', '0', '', 'message', 'admin', NOW(), '社团宣传管理目录');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3301, '活动管理', 3300, 1, 'activity', 'club/activity/index', 'C', '0', '0', 'system:activity:list', 'date', 'admin', NOW(), '社团活动管理');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3302, '公告管理', 3300, 2, 'notice', 'club/notice/index', 'C', '0', '0', 'system:notice:list', 'message', 'admin', NOW(), '社团公告管理');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3303, '相册管理', 3300, 3, 'album', 'club/album/index', 'C', '0', '0', 'system:album:list', 'image', 'admin', NOW(), '社团相册管理');


-- ==================== 4. 数据统计 ====================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3400, '数据统计', 0, 4, 'statistics', NULL, 'M', '0', '0', '', 'chart', 'admin', NOW(), '数据统计目录');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3401, '社团统计', 3400, 1, 'club-stat', 'statistics/club/index', 'C', '0', '0', 'system:statistics:club', 'peoples', 'admin', NOW(), '社团数据统计');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3402, '成员统计', 3400, 2, 'member-stat', 'statistics/member/index', 'C', '0', '0', 'system:statistics:member', 'user', 'admin', NOW(), '成员数据统计');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (3403, '活动统计', 3400, 3, 'activity-stat', 'statistics/activity/index', 'C', '0', '0', 'system:statistics:activity', 'date', 'admin', NOW(), '活动数据统计');


-- =====================================================
-- 二、角色定义
-- =====================================================

-- 社团管理员角色（管理所有社团）
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time, remark)
SELECT 100, '社团管理员', 'club_admin', 3, '1', '0', '0', 'admin', NOW(), '可管理所有社团信息'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'club_admin');

-- 社长角色（管理自己的社团）
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time, remark)
SELECT 101, '社长', 'president', 4, '5', '0', '0', 'admin', NOW(), '管理自己的社团'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'president');

-- 副社长角色
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time, remark)
SELECT 102, '副社长', 'vice_president', 5, '5', '0', '0', 'admin', NOW(), '协助管理社团'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'vice_president');


-- =====================================================
-- 三、为角色分配菜单权限
-- =====================================================

-- 管理员（role_id=1）拥有所有菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3101);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3102);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3103);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3200);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3201);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3202);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3203);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3300);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3301);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3302);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3303);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3400);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3401);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3402);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3403);

-- 社团管理员（role_id=100）拥有所有社团菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3101);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3102);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3103);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3200);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3201);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3202);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3203);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3300);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3301);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3302);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3303);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3400);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3401);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3402);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3403);

-- 社长（role_id=101）- 不含分类管理、不含统计
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3101);  -- 社团列表（仅本社团）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3103);  -- 荣誉管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3200);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3201);  -- 入社申请
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3202);  -- 成员管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3203);  -- 申请统计（仅本社团）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3300);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3301);  -- 活动管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3302);  -- 公告管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3303);  -- 相册管理

-- 副社长（role_id=102）- 不含分类管理、公告仅查看/新增
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3101);  -- 社团列表（仅查看）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3103);  -- 荣誉管理（查看/新增）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3200);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3201);  -- 入社申请
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3202);  -- 成员管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3300);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3301);  -- 活动管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3302);  -- 公告管理（查看/新增）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (102, 3303);  -- 相册管理


-- =====================================================
-- 四、权限标识说明
-- =====================================================
/*
社团信息模块:
  - system:club:list/query/add/edit/remove/export
  - system:category:list/add/edit/remove
  - system:achievement:list/add/edit/remove

社团运营模块:
  - system:application:list/query/approve/reject/statistics
  - system:member:list/query/edit/remove

社团宣传模块:
  - system:activity:list/query/add/edit/remove
  - system:notice:list/query/add/edit/remove
  - system:album:list/add/edit/remove

数据统计模块:
  - system:statistics:club
  - system:statistics:member
  - system:statistics:activity
*/


-- =====================================================
-- 五、验证菜单
-- =====================================================
SELECT menu_id, menu_name, parent_id, order_num, path, menu_type, perms
FROM sys_menu 
WHERE menu_id >= 3100 AND menu_id < 3500
ORDER BY parent_id, order_num;

-- 验证角色菜单关联
SELECT r.role_name, COUNT(rm.menu_id) AS menu_count
FROM sys_role_menu rm
JOIN sys_role r ON rm.role_id = r.role_id
WHERE rm.menu_id >= 3100 AND rm.menu_id < 3500
GROUP BY r.role_id, r.role_name
ORDER BY r.role_id;
