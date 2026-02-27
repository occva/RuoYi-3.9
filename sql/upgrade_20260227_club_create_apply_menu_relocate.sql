-- ============================================================
-- 新社团申请菜单修正（增量，幂等）
-- 1) menu_id=3204 名称改为“新社团申请”
-- 2) 将 3204 从“社团运营(3200)”迁移到“社团信息(3100)”下
-- ============================================================

START TRANSACTION;

UPDATE sys_menu
SET menu_name = '新社团申请',
    parent_id = 3100,
    order_num = 4,
    path = 'club-apply',
    component = 'club/clubApply/index',
    perms = 'club:createApply:list',
    icon = 'edit-pen',
    remark = '新社团申请审核',
    update_by = 'admin',
    update_time = NOW()
WHERE menu_id = 3204;

UPDATE sys_menu
SET menu_name = '新社团申请查询',
    perms = 'club:createApply:query',
    update_by = 'admin',
    update_time = NOW()
WHERE menu_id = 3231;

UPDATE sys_menu
SET menu_name = '新社团申请审核',
    perms = 'club:createApply:review',
    update_by = 'admin',
    update_time = NOW()
WHERE menu_id = 3232;

UPDATE sys_menu
SET menu_name = '新社团申请删除',
    perms = 'club:createApply:remove',
    update_by = 'admin',
    update_time = NOW()
WHERE menu_id = 3233;

UPDATE sys_menu
SET menu_name = '新社团申请导出',
    perms = 'club:createApply:export',
    update_by = 'admin',
    update_time = NOW()
WHERE menu_id = 3234;

COMMIT;
