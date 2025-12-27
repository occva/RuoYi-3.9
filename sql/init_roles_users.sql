-- =====================================================
-- 社团管理系统 - 角色用户初始化 SQL
-- 
-- 当前状态：
--   用户: admin(1), ry(2), r1(100)
--   角色: 超级管理员(1), 普通角色(2)
-- 
-- 需要新增：
--   角色: 社团管理员(100), 社长(101), 副社长(102)
--   用户: 测试社长、测试副社长
-- =====================================================

-- =====================================================
-- 一、新增角色
-- =====================================================

-- 社团管理员（可管理所有社团）
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
VALUES (100, '社团管理员', 'club_admin', 3, '1', 1, 1, '0', '0', 'admin', NOW(), '可管理所有社团信息');

-- 社长（管理自己的社团）
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
VALUES (101, '社长', 'president', 4, '5', 1, 1, '0', '0', 'admin', NOW(), '管理自己的社团');

-- 副社长（协助管理社团）
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
VALUES (102, '副社长', 'vice_president', 5, '5', 1, 1, '0', '0', 'admin', NOW(), '协助管理社团');

-- =====================================================
-- 二、新增测试用户
-- 密码统一为: admin123 (加密后)
-- =====================================================

-- 测试社团管理员
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, remark)
VALUES (101, 100, 'clubadmin', '社团管理员', '00', 'clubadmin@example.com', '13800000001', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', NOW(), '社团管理员测试账号');

-- 测试社长（编程魔法社）
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, remark)
VALUES (102, 100, 'president1', '张明（社长）', '00', 'president1@example.com', '13800000002', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', NOW(), '编程魔法社社长');

-- 测试副社长（编程魔法社）
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, remark)
VALUES (103, 100, 'vicepresident1', '李华（副社长）', '00', 'vp1@example.com', '13800000003', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', NOW(), '编程魔法社副社长');

-- =====================================================
-- 三、用户角色关联
-- =====================================================

-- 社团管理员用户 -> 社团管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (101, 100);

-- 社长用户 -> 社长角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (102, 101);

-- 副社长用户 -> 副社长角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (103, 102);

-- =====================================================
-- 四、关联社团成员表（设置社长/副社长）
-- 假设编程魔法社的 club_id = 1
-- =====================================================

-- 更新编程魔法社的社长信息
UPDATE club SET president_id = 102, president_name = '张明' WHERE club_id = 1;

-- 更新编程魔法社的副社长信息
UPDATE club SET vice_president = '李华' WHERE club_id = 1;

-- 添加社长到成员表
INSERT INTO club_member (club_id, user_id, user_name, nick_name, role_type, position_name, join_date, status, del_flag, create_by, create_time)
VALUES (1, 102, 'president1', '张明', '1', '社长', NOW(), '0', '0', 'admin', NOW());

-- 添加副社长到成员表
INSERT INTO club_member (club_id, user_id, user_name, nick_name, role_type, position_name, join_date, status, del_flag, create_by, create_time)
VALUES (1, 103, 'vicepresident1', '李华', '2', '副社长', NOW(), '0', '0', 'admin', NOW());

-- =====================================================
-- 五、验证
-- =====================================================

-- 查看所有角色
SELECT role_id, role_name, role_key, role_sort FROM sys_role WHERE del_flag = '0' ORDER BY role_sort;

-- 查看所有用户
SELECT user_id, user_name, nick_name, status FROM sys_user WHERE del_flag = '0' ORDER BY user_id;

-- 查看用户角色关联
SELECT u.user_name, u.nick_name, r.role_name 
FROM sys_user u
JOIN sys_user_role ur ON u.user_id = ur.user_id
JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.del_flag = '0'
ORDER BY u.user_id;

-- =====================================================
-- 测试账号汇总
-- =====================================================
/*
账号              密码         角色          所属社团
---------------------------------------------------
admin           admin123    超级管理员      -
clubadmin       admin123    社团管理员      -
president1      admin123    社长           编程魔法社
vicepresident1  admin123    副社长         编程魔法社
*/
