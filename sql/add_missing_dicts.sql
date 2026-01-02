-- ----------------------------
-- 补充数据字典：社团公告、成员状态、社团状态、活动签到状态
-- ----------------------------

-- 1. 社团公告类型
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('社团公告类型', 'club_notice_type', '0', 'admin', sysdate(), '社团公告类型');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES 
(1, '通知', '1', 'club_notice_type', '', 'info', 'Y', '0', 'admin', sysdate(), ''),
(2, '公告', '2', 'club_notice_type', '', 'primary', 'N', '0', 'admin', sysdate(), ''),
(3, '紧急', '3', 'club_notice_type', '', 'danger', 'N', '0', 'admin', sysdate(), '');

-- 2. 社团公告状态
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('社团公告状态', 'club_notice_status', '0', 'admin', sysdate(), '社团公告状态');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES 
(1, '草稿', '0', 'club_notice_status', '', 'info', 'Y', '0', 'admin', sysdate(), ''),
(2, '已发布', '1', 'club_notice_status', '', 'success', 'N', '0', 'admin', sysdate(), ''),
(3, '已撤回', '2', 'club_notice_status', '', 'warning', 'N', '0', 'admin', sysdate(), '');

-- 3. 社团成员状态
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('社团成员状态', 'club_member_status', '0', 'admin', sysdate(), '社团成员状态');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES 
(1, '正常', '0', 'club_member_status', '', 'success', 'Y', '0', 'admin', sysdate(), ''),
(2, '禁言', '1', 'club_member_status', '', 'warning', 'N', '0', 'admin', sysdate(), ''),
(3, '已退社', '2', 'club_member_status', '', 'danger', 'N', '0', 'admin', sysdate(), '');

-- 4. 社团状态
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('社团状态', 'club_status', '0', 'admin', sysdate(), '社团状态');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES 
(1, '正常', '0', 'club_status', '', 'success', 'Y', '0', 'admin', sysdate(), ''),
(2, '停用', '1', 'club_status', '', 'danger', 'N', '0', 'admin', sysdate(), ''),
(3, '已解散', '2', 'club_status', '', 'info', 'N', '0', 'admin', sysdate(), '');

-- 5. 活动签到状态
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('活动签到状态', 'activity_checkin_status', '0', 'admin', sysdate(), '活动签到状态');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES 
(1, '未签到', '0', 'activity_checkin_status', '', 'info', 'Y', '0', 'admin', sysdate(), ''),
(2, '已签到', '1', 'activity_checkin_status', '', 'success', 'N', '0', 'admin', sysdate(), '');
