-- ----------------------------
-- 社团管理系统数据库设计
-- 基于 BACKEND_API_REQUIREMENTS.md 接口需求
-- 兼容若依框架设计规范
-- ----------------------------

-- ----------------------------
-- 1、社团分类表
-- ----------------------------
drop table if exists club_category;
create table club_category (
  category_id       bigint(20)      not null auto_increment    comment '分类ID',
  category_code     varchar(50)     not null                   comment '分类编码（technology/art/sports/academic/volunteer）',
  category_name     varchar(50)     not null                   comment '分类名称',
  sort_order        int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (category_id),
  unique key uk_category_code (category_code)
) engine=innodb auto_increment=100 comment = '社团分类表';

-- ----------------------------
-- 初始化-社团分类数据
-- ----------------------------
insert into club_category values(1, 'technology', '科技类', 1, '0', '0', 'admin', sysdate(), '', null, '编程、AI、机器人等');
insert into club_category values(2, 'art', '艺术类', 2, '0', '0', 'admin', sysdate(), '', null, '音乐、绘画、摄影等');
insert into club_category values(3, 'sports', '体育类', 3, '0', '0', 'admin', sysdate(), '', null, '篮球、足球、跑步等');
insert into club_category values(4, 'academic', '学术类', 4, '0', '0', 'admin', sysdate(), '', null, '数学、物理、英语等');
insert into club_category values(5, 'volunteer', '志愿类', 5, '0', '0', 'admin', sysdate(), '', null, '公益、支教、环保等');


-- ----------------------------
-- 2、社团信息表
-- ----------------------------
drop table if exists club;
create table club (
  club_id           bigint(20)      not null auto_increment    comment '社团ID',
  club_name         varchar(100)    not null                   comment '社团名称',
  club_code         varchar(50)                                comment '社团编码（唯一标识）',
  category_id       bigint(20)      not null                   comment '分类ID',
  description       text                                       comment '社团简介',
  logo_url          varchar(255)    default ''                 comment '社团Logo图片地址',
  cover_url         varchar(255)    default ''                 comment '封面图片地址',
  president_id      bigint(20)                                 comment '社长用户ID',
  president_name    varchar(50)     default ''                 comment '社长姓名',
  vice_president    varchar(100)    default ''                 comment '副社长（多个用逗号分隔）',
  member_count      int(11)         default 0                  comment '成员数量',
  max_members       int(11)         default 0                  comment '最大成员数（0为不限制）',
  founded_date      date                                       comment '成立日期',
  location          varchar(200)    default ''                 comment '活动地点（如：304室）',
  contact_phone     varchar(20)     default ''                 comment '联系电话',
  contact_email     varchar(100)    default ''                 comment '联系邮箱',
  contact_qq        varchar(20)     default ''                 comment 'QQ群号',
  contact_wechat    varchar(50)     default ''                 comment '微信号/公众号',
  is_recruiting     char(1)         default '1'                comment '是否招新（0否 1是）',
  is_popular        char(1)         default '0'                comment '是否热门推荐（0否 1是）',
  view_count        int(11)         default 0                  comment '浏览次数',
  favorite_count    int(11)         default 0                  comment '收藏次数',
  sort_order        int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1停用 2已解散）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (club_id),
  unique key uk_club_code (club_code),
  key idx_club_category (category_id),
  key idx_club_status (status),
  key idx_club_popular (is_popular)
) engine=innodb auto_increment=1000 comment = '社团信息表';


-- ----------------------------
-- 3、社团成员表
-- ----------------------------
drop table if exists club_member;
create table club_member (
  member_id         bigint(20)      not null auto_increment    comment '成员ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  nick_name         varchar(30)     default ''                 comment '用户昵称',
  student_id        varchar(50)     default ''                 comment '学号',
  role_type         char(1)         default '3'                comment '角色类型（1社长 2副社长 3普通成员）',
  position_name     varchar(50)     default ''                 comment '职位名称',
  join_date         datetime                                   comment '加入日期',
  contribution      int(11)         default 0                  comment '贡献值',
  status            char(1)         default '0'                comment '状态（0正常 1已退出）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (member_id),
  unique key uk_club_user (club_id, user_id),
  key idx_member_club (club_id),
  key idx_member_user (user_id),
  key idx_member_status (status)
) engine=innodb auto_increment=10000 comment = '社团成员表';


-- ----------------------------
-- 4、社团收藏表
-- ----------------------------
drop table if exists club_favorite;
create table club_favorite (
  favorite_id       bigint(20)      not null auto_increment    comment '收藏ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  create_time       datetime                                   comment '收藏时间',
  primary key (favorite_id),
  unique key uk_favorite_club_user (club_id, user_id),
  key idx_favorite_user (user_id),
  key idx_favorite_time (create_time)
) engine=innodb auto_increment=10000 comment = '社团收藏表';


-- ----------------------------
-- 5、社团活动表
-- ----------------------------
drop table if exists club_activity;
create table club_activity (
  activity_id       bigint(20)      not null auto_increment    comment '活动ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  activity_title    varchar(200)    not null                   comment '活动标题',
  activity_type     varchar(50)     default ''                 comment '活动类型（meeting/competition/training/social/other）',
  description       text                                       comment '活动详情',
  cover_url         varchar(255)    default ''                 comment '活动封面图片',
  location          varchar(200)    default ''                 comment '活动地点',
  start_time        datetime                                   comment '开始时间',
  end_time          datetime                                   comment '结束时间',
  registration_start datetime                                  comment '报名开始时间',
  registration_end  datetime                                   comment '报名截止时间',
  max_participants  int(11)         default 0                  comment '最大参与人数（0为不限制）',
  current_participants int(11)      default 0                  comment '当前报名人数',
  status            char(1)         default '0'                comment '状态（0待开始 1进行中 2已结束 3已取消）',
  organizer         varchar(100)    default ''                 comment '组织者',
  contact_info      varchar(200)    default ''                 comment '联系方式',
  is_public         char(1)         default '1'                comment '是否公开（0仅社团成员 1所有人可见）',
  view_count        int(11)         default 0                  comment '浏览次数',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (activity_id),
  key idx_activity_club (club_id),
  key idx_activity_status (status),
  key idx_activity_time (start_time)
) engine=innodb auto_increment=10000 comment = '社团活动表';


-- ----------------------------
-- 6、活动报名表
-- ----------------------------
drop table if exists club_activity_registration;
create table club_activity_registration (
  registration_id   bigint(20)      not null auto_increment    comment '报名ID',
  activity_id       bigint(20)      not null                   comment '活动ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  nick_name         varchar(30)     default ''                 comment '用户昵称',
  student_id        varchar(50)     default ''                 comment '学号',
  phone             varchar(20)     default ''                 comment '联系电话',
  registration_time datetime                                   comment '报名时间',
  check_in_status   char(1)         default '0'                comment '签到状态（0未签到 1已签到）',
  check_in_time     datetime                                   comment '签到时间',
  status            char(1)         default '0'                comment '状态（0待参加 1已参加 2已取消）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (registration_id),
  unique key uk_registration_activity_user (activity_id, user_id),
  key idx_registration_activity (activity_id),
  key idx_registration_user (user_id),
  key idx_registration_status (status)
) engine=innodb auto_increment=10000 comment = '活动报名表';


-- ----------------------------
-- 7、入社申请表
-- ----------------------------
drop table if exists club_application;
create table club_application (
  application_id    bigint(20)      not null auto_increment    comment '申请ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  club_name         varchar(100)    default ''                 comment '社团名称',
  user_id           bigint(20)      not null                   comment '用户ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  nick_name         varchar(30)     default ''                 comment '用户昵称',
  student_id        varchar(50)     not null                   comment '学号',
  real_name         varchar(50)     not null                   comment '真实姓名',
  gender            char(1)         default '0'                comment '性别（0男 1女 2未知）',
  major             varchar(100)    default ''                 comment '专业',
  grade             varchar(20)     default ''                 comment '年级',
  class_name        varchar(50)     default ''                 comment '班级',
  phone             varchar(20)     default ''                 comment '联系电话',
  email             varchar(100)    default ''                 comment '邮箱',
  self_introduction text                                       comment '自我介绍',
  apply_reason      text                                       comment '申请理由',
  special_skills    varchar(500)    default ''                 comment '特长',
  application_time  datetime                                   comment '申请时间',
  review_status     char(1)         default '0'                comment '审核状态（0待审核 1已通过 2已拒绝 3已撤回）',
  reviewer_id       bigint(20)                                 comment '审核人ID',
  reviewer_name     varchar(50)     default ''                 comment '审核人姓名',
  review_time       datetime                                   comment '审核时间',
  review_comment    varchar(500)    default ''                 comment '审核意见',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (application_id),
  key idx_application_club (club_id),
  key idx_application_user (user_id),
  key idx_application_status (review_status),
  key idx_application_time (application_time)
) engine=innodb auto_increment=10000 comment = '入社申请表';


-- ----------------------------
-- 8、社团公告表
-- ----------------------------
drop table if exists club_notice;
create table club_notice (
  notice_id         bigint(20)      not null auto_increment    comment '公告ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  notice_title      varchar(200)    not null                   comment '公告标题',
  notice_type       char(1)         default '1'                comment '公告类型（1通知 2公告 3紧急）',
  notice_content    text                                       comment '公告内容',
  cover_url         varchar(255)    default ''                 comment '封面图片',
  is_top            char(1)         default '0'                comment '是否置顶（0否 1是）',
  is_important      char(1)         default '0'                comment '是否重要（0否 1是）',
  view_count        int(11)         default 0                  comment '浏览次数',
  publish_time      datetime                                   comment '发布时间',
  publisher_id      bigint(20)                                 comment '发布人ID',
  publisher_name    varchar(50)     default ''                 comment '发布人姓名',
  status            char(1)         default '0'                comment '状态（0草稿 1已发布 2已撤回）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (notice_id),
  key idx_notice_club (club_id),
  key idx_notice_status (status),
  key idx_notice_publish_time (publish_time)
) engine=innodb auto_increment=10000 comment = '社团公告表';


-- ----------------------------
-- 9、AI聊天记录表（可选扩展）
-- ----------------------------
drop table if exists ai_chat_message;
create table ai_chat_message (
  message_id        bigint(20)      not null auto_increment    comment '消息ID',
  session_id        varchar(100)    not null                   comment '会话ID',
  user_id           bigint(20)                                 comment '用户ID（可为空，支持匿名）',
  message_type      char(1)         not null                   comment '消息类型（0用户 1AI）',
  message_content   text            not null                   comment '消息内容',
  context_info      text                                       comment '上下文信息（JSON格式）',
  response_time     int(11)         default 0                  comment '响应耗时（毫秒）',
  create_time       datetime                                   comment '创建时间',
  primary key (message_id),
  key idx_chat_session (session_id),
  key idx_chat_user (user_id),
  key idx_chat_time (create_time)
) engine=innodb auto_increment=10000 comment = 'AI聊天记录表';


-- ----------------------------
-- 10、社团荣誉/成就表（扩展）
-- ----------------------------
drop table if exists club_achievement;
create table club_achievement (
  achievement_id    bigint(20)      not null auto_increment    comment '成就ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  achievement_title varchar(200)    not null                   comment '成就标题',
  achievement_type  varchar(50)     default ''                 comment '成就类型（award/competition/activity）',
  description       text                                       comment '详细描述',
  image_url         varchar(255)    default ''                 comment '图片地址',
  achieve_date      date                                       comment '获得日期',
  level             varchar(20)     default ''                 comment '级别（校级/市级/省级/国家级）',
  sort_order        int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1隐藏）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (achievement_id),
  key idx_achievement_club (club_id)
) engine=innodb auto_increment=1000 comment = '社团荣誉/成就表';


-- ----------------------------
-- 11、社团相册表（扩展）
-- ----------------------------
drop table if exists club_album;
create table club_album (
  album_id          bigint(20)      not null auto_increment    comment '相册ID',
  club_id           bigint(20)      not null                   comment '社团ID',
  activity_id       bigint(20)                                 comment '关联活动ID（可为空）',
  album_name        varchar(100)    not null                   comment '相册名称',
  cover_url         varchar(255)    default ''                 comment '封面图片',
  description       varchar(500)    default ''                 comment '相册描述',
  photo_count       int(11)         default 0                  comment '照片数量',
  view_count        int(11)         default 0                  comment '浏览次数',
  sort_order        int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1隐藏）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (album_id),
  key idx_album_club (club_id),
  key idx_album_activity (activity_id)
) engine=innodb auto_increment=1000 comment = '社团相册表';


-- ----------------------------
-- 12、相册照片表（扩展）
-- ----------------------------
drop table if exists club_photo;
create table club_photo (
  photo_id          bigint(20)      not null auto_increment    comment '照片ID',
  album_id          bigint(20)      not null                   comment '相册ID',
  photo_url         varchar(255)    not null                   comment '照片地址',
  photo_title       varchar(100)    default ''                 comment '照片标题',
  description       varchar(500)    default ''                 comment '照片描述',
  upload_user_id    bigint(20)                                 comment '上传人ID',
  upload_user_name  varchar(50)     default ''                 comment '上传人姓名',
  sort_order        int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1隐藏）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  primary key (photo_id),
  key idx_photo_album (album_id)
) engine=innodb auto_increment=10000 comment = '相册照片表';


-- ----------------------------
-- 数据字典扩展 - 添加社团相关字典
-- ----------------------------
-- 社团分类
insert into sys_dict_type values(100, '社团分类', 'club_category', '0', 'admin', sysdate(), '', null, '社团分类列表');
insert into sys_dict_data values(100, 1, '科技类', 'technology', 'club_category', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(101, 2, '艺术类', 'art', 'club_category', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(102, 3, '体育类', 'sports', 'club_category', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(103, 4, '学术类', 'academic', 'club_category', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(104, 5, '志愿类', 'volunteer', 'club_category', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');

-- 活动类型
insert into sys_dict_type values(101, '活动类型', 'activity_type', '0', 'admin', sysdate(), '', null, '社团活动类型列表');
insert into sys_dict_data values(105, 1, '会议', 'meeting', 'activity_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(106, 2, '比赛', 'competition', 'activity_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(107, 3, '培训', 'training', 'activity_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(108, 4, '社交', 'social', 'activity_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(109, 5, '其他', 'other', 'activity_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');

-- 活动状态
insert into sys_dict_type values(102, '活动状态', 'activity_status', '0', 'admin', sysdate(), '', null, '社团活动状态列表');
insert into sys_dict_data values(110, 1, '待开始', '0', 'activity_status', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(111, 2, '进行中', '1', 'activity_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(112, 3, '已结束', '2', 'activity_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(113, 4, '已取消', '3', 'activity_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');

-- 申请状态
insert into sys_dict_type values(103, '申请状态', 'application_status', '0', 'admin', sysdate(), '', null, '入社申请状态列表');
insert into sys_dict_data values(114, 1, '待审核', '0', 'application_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(115, 2, '已通过', '1', 'application_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(116, 3, '已拒绝', '2', 'application_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(117, 4, '已撤回', '3', 'application_status', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '');

-- 成员角色类型
insert into sys_dict_type values(104, '社团角色', 'club_role_type', '0', 'admin', sysdate(), '', null, '社团成员角色类型');
insert into sys_dict_data values(118, 1, '社长', '1', 'club_role_type', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(119, 2, '副社长', '2', 'club_role_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(120, 3, '普通成员', '3', 'club_role_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');

-- 社团公告类型
insert into sys_dict_type values(105, '社团公告类型', 'club_notice_type', '0', 'admin', sysdate(), '', null, '社团公告类型');
insert into sys_dict_data values(121, 1, '通知', '1', 'club_notice_type', '', 'info', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(122, 2, '公告', '2', 'club_notice_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(123, 3, '紧急', '3', 'club_notice_type', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');

-- 社团公告状态
insert into sys_dict_type values(106, '社团公告状态', 'club_notice_status', '0', 'admin', sysdate(), '', null, '社团公告状态');
insert into sys_dict_data values(124, 1, '草稿', '0', 'club_notice_status', '', 'info', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(125, 2, '已发布', '1', 'club_notice_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(126, 3, '已撤回', '2', 'club_notice_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');

-- 社团成员状态
insert into sys_dict_type values(107, '社团成员状态', 'club_member_status', '0', 'admin', sysdate(), '', null, '社团成员状态');
insert into sys_dict_data values(127, 1, '正常', '0', 'club_member_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(128, 2, '禁言', '1', 'club_member_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(129, 3, '已退社', '2', 'club_member_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');

-- 社团状态
insert into sys_dict_type values(108, '社团状态', 'club_status', '0', 'admin', sysdate(), '', null, '社团状态');
insert into sys_dict_data values(130, 1, '正常', '0', 'club_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(131, 2, '停用', '1', 'club_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(132, 3, '已解散', '2', 'club_status', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '');

-- 活动签到状态
insert into sys_dict_type values(109, '活动签到状态', 'activity_checkin_status', '0', 'admin', sysdate(), '', null, '活动签到状态');
insert into sys_dict_data values(133, 1, '未签到', '0', 'activity_checkin_status', '', 'info', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(134, 2, '已签到', '1', 'activity_checkin_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');


-- =====================================================
-- 初始化-演示数据
-- =====================================================

-- ----------------------------
-- 社团分类演示数据
-- ----------------------------
INSERT INTO club_category (category_id, category_code, category_name, sort_order, create_time, create_by) VALUES
(1872500000000000001, 'technology', '科技类', 1, NOW(), 'admin'),
(1872500000000000002, 'art', '艺术类', 2, NOW(), 'admin'),
(1872500000000000003, 'sports', '体育类', 3, NOW(), 'admin'),
(1872500000000000004, 'academic', '学术类', 4, NOW(), 'admin'),
(1872500000000000005, 'volunteer', '志愿类', 5, NOW(), 'admin'),
(1872500000000000006, 'test_234811', 'test_234811', 999, NOW(), 'admin');

-- ----------------------------
-- 社团演示数据
-- ----------------------------
INSERT INTO club (club_id, club_name, club_code, category_id, description, logo_url, member_count, president_id, president_name, vice_president, founded_date, location, remark, create_time, create_by) VALUES
(1872600000000000001, '编程魔法师', 'coding_wizards', 1872500000000000001, '开发者分享知识、构建项目并参加黑客马拉松的社区。我们每周聚会一次，讨论最新的技术，开展开源项目，并为编程面试做准备。', 'https://images.unsplash.com/photo-1531482615713-2afd69097998?auto=format&fit=crop&q=80&w=800', 128, 102, '张明', '李华', '2020-09-01', '科技楼 304室', '每周三 18:00', NOW(), 'admin'),
(1872600000000000002, '辩论协会', 'debate_society', 1872500000000000004, '通过每周对当前全球问题的辩论，磨练你的修辞和批判性思维能力。', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', 45, NULL, '鲍勃·史密斯', '', '2018-03-15', '学生活动中心 101', '每周二 17:00', NOW(), 'admin'),
(1872600000000000003, '摄影俱乐部', 'photo_club', 1872500000000000002, '捕捉你周围的世界。每周摄影采风、工作坊和展览。', 'https://images.unsplash.com/photo-1542038784456-1ea8e935640e?auto=format&fit=crop&q=80&w=800', 82, NULL, '查理·布朗', '', '2019-11-20', '艺术工作室 B', '每周四 16:30', NOW(), 'admin'),
(1872600000000000004, '绿色地球', 'green_earth', 1872500000000000005, '通过校园倡议促进可持续发展和环境意识。', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?auto=format&fit=crop&q=80&w=800', 200, NULL, '戴安娜', '', '2015-04-22', '社区花园', '每周五 15:00', NOW(), 'admin'),
(1872600000000000005, '乐器合奏团', 'music_ensemble', 1872500000000000002, '加入其他音乐家进行即兴演奏、表演和音乐鉴赏之夜。', 'https://images.unsplash.com/photo-1511379938547-c1f69419868d?auto=format&fit=crop&q=80&w=800', 65, NULL, '埃文', '', '2016-09-10', '音乐厅', '每周一 19:00', NOW(), 'admin'),
(1872600000000000006, '机器人俱乐部', 'robotics_club', 1872500000000000001, '建造和编程机器人。参加全国比赛并学习机电一体化。', 'https://images.unsplash.com/photo-1561557944-6e7860d1a7eb?auto=format&fit=crop&q=80&w=800', 50, NULL, '菲奥娜', '', '2021-01-15', '工程实验室', '每周六 10:00', NOW(), 'admin'),
(1872600000000000007, 'AI 研习社', 'ai_study_group', 1872500000000000001, '探索人工智能的前沿技术，包括深度学习、计算机视觉和自然语言处理。', 'https://plus.unsplash.com/premium_photo-1683121710572-7723bd2e235d?auto=format&fit=crop&q=80&w=800', 150, NULL, 'GPT-4', '', '2023-05-01', '创新中心 202', '每周五 14:00', NOW(), 'admin'),
(1872600000000000008, '篮球社', 'basketball_club', 1872500000000000003, '热爱篮球的同学集结地，每周组织训练和校内友谊赛。', 'https://images.unsplash.com/photo-1546519638-68e109498ee2?auto=format&fit=crop&q=80&w=800', 90, NULL, '科比·粉丝', '', '2010-09-01', '北区篮球场', '每周二、四 18:00', NOW(), 'admin');

-- ----------------------------
-- 社团成员演示数据（社长/副社长）
-- ----------------------------
INSERT INTO club_member (club_id, user_id, user_name, nick_name, role_type, position_name, join_date, status, del_flag, create_by, create_time)
VALUES (1872600000000000001, 102, 'president1', '张明', '1', '社长', NOW(), '0', '0', 'admin', NOW());
INSERT INTO club_member (club_id, user_id, user_name, nick_name, role_type, position_name, join_date, status, del_flag, create_by, create_time)
VALUES (1872600000000000001, 103, 'vicepresident1', '李华', '2', '副社长', NOW(), '0', '0', 'admin', NOW());

-- ----------------------------
-- 活动演示数据
-- ----------------------------
INSERT INTO club_activity (activity_id, club_id, activity_title, start_time, location, status, description, create_time, create_by) VALUES
(1872700000000000001, 1872600000000000001, '黑客马拉松备战之夜', '2024-10-15 18:00:00', '304室', '1', '备战即将到来的黑客马拉松，分享创意和组队。', NOW(), 'admin'),
(1872700000000000002, 1872600000000000001, '特邀嘉宾：谷歌工程师', '2024-09-28 14:00:00', 'A号礼堂', '2', '来自谷歌的高级工程师分享职业发展经验。', NOW(), 'admin'),
(1872700000000000003, 1872600000000000001, 'React 进阶工作坊', '2024-11-05 19:00:00', '2号实验室', '0', '深入理解React Hooks和性能优化。', NOW(), 'admin');

INSERT INTO club_activity (activity_id, club_id, activity_title, activity_type, description, cover_url, location, start_time, end_time, status, is_public, create_time, create_by) VALUES
(1872700000000000004, 1872600000000000002, '全校辩论大赛：网络社交是否取代了面对面交流', '比赛', '探讨现代科技对人类社交模式的影响。冠军将获得精美奖品！', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '演讲厅', NOW() + INTERVAL 5 DAY, NOW() + INTERVAL 5 DAY + INTERVAL 3 HOUR, '0', '1', NOW(), 'admin'),
(1872700000000000005, 1872600000000000002, '辩论技巧工作坊', '培训', '学习如何构建有说服力的论点并提升临场反应能力。', 'https://images.unsplash.com/photo-1517048676732-d65bc937f952?auto=format&fit=crop&q=80&w=800', '101会议室', NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY + INTERVAL 2 HOUR, '2', '1', NOW(), 'admin'),
(1872700000000000006, 1872600000000000003, '秋季校园外拍：寻找光影', '外拍', '拿起相机，记录下秋天校园里最美的瞬间。', 'https://images.unsplash.com/photo-1542038784456-1ea8e935640e?auto=format&fit=crop&q=80&w=800', '博雅湖畔', NOW() + INTERVAL 2 DAY, NOW() + INTERVAL 2 DAY + INTERVAL 4 HOUR, '0', '1', NOW(), 'admin'),
(1872700000000000007, 1872600000000000003, '后期处理入门：Lightroom 实操', '培训', '由资深摄影师指导，教你如何修出电影感大片。', 'https://images.unsplash.com/photo-1554048612-b6a482bc67e5?auto=format&fit=crop&q=80&w=800', '机房501', NOW(), NOW() + INTERVAL 2 HOUR, '1', '1', NOW(), 'admin'),
(1872700000000000008, 1872600000000000004, '"植"得期待：校园公益植树行动', '公益', '为了更绿色的校园，我们一起动手种下希望的树苗。', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?auto=format&fit=crop&q=80&w=800', '西区实验田', NOW() + INTERVAL 10 DAY, NOW() + INTERVAL 10 DAY + INTERVAL 5 HOUR, '0', '1', NOW(), 'admin'),
(1872700000000000009, 1872600000000000005, '仲夏之夜：草坪草地音乐会', '演出', '在星空下，倾听古典与现代乐器的交织与融合。', 'https://images.unsplash.com/photo-1459749411177-042180ce673c?auto=format&fit=crop&q=80&w=800', '大操场草坪', NOW() + INTERVAL 7 DAY, NOW() + INTERVAL 7 DAY + INTERVAL 2 HOUR, '0', '1', NOW(), 'admin'),
(1872700000000000010, 1872600000000000006, '格斗机器人组装体验日', '动手', '想亲手制作一个能战斗的机器人吗？快来加入我们！', 'https://images.unsplash.com/photo-1485827404703-89b55fcc595e?auto=format&fit=crop&q=80&w=800', '机器人实验室', NOW() + INTERVAL 1 DAY, NOW() + INTERVAL 1 DAY + INTERVAL 4 HOUR, '0', '1', NOW(), 'admin'),
(1872700000000000011, 1872600000000000007, '深度学习论文研讨会', '学术', '本周探讨最新的计算机视觉模型及其在工业界的落地。', 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?auto=format&fit=crop&q=80&w=800', '202教室', NOW() - INTERVAL 2 HOUR, NOW() + INTERVAL 1 HOUR, '1', '1', NOW(), 'admin'),
(1872700000000000012, 1872600000000000008, '三对三邀请战：谁是路人王？', '比赛', '热血篮球，就在此刻。期待全校各路高手前来挑战。', 'https://images.unsplash.com/photo-1519766304817-4f37bda74a26?auto=format&fit=crop&q=80&w=800', '室内篮球场', NOW() + INTERVAL 4 DAY, NOW() + INTERVAL 4 DAY + INTERVAL 6 HOUR, '0', '1', NOW(), 'admin');

-- ----------------------------
-- 公告演示数据
-- ----------------------------
INSERT INTO club_notice (notice_id, club_id, notice_title, notice_content, publish_time, status, create_time, create_by) VALUES
(1872800000000000001, 1872600000000000001, '会费缴纳通知', '请在月底前缴纳会员费。', '2024-10-01 09:00:00', '1', NOW(), 'admin'),
(1872800000000000002, 1872600000000000001, '迎新会确认', '本周五的迎新会已确认！', '2024-09-15 10:00:00', '1', NOW(), 'admin');

-- ----------------------------
-- 荣誉演示数据
-- ----------------------------
INSERT INTO club_achievement (achievement_id, club_id, achievement_title, achievement_type, description, image_url, achieve_date, level, sort_order, status, create_time, create_by) VALUES
(1001, 1872600000000000001, '全国大学生编程大赛金奖', 'competition', '在2024年全国大学生编程大赛中荣获团队金奖', 'https://images.unsplash.com/photo-1578357078586-491adc1c4aa0?q=80&w=400', '2024-05-20', '国家级', 1, '0', NOW(), 'admin'),
(1002, 1872600000000000001, '校内优秀社团', 'award', '2023年度校级优秀学生社团荣誉称号', '', '2023-12-15', '校级', 2, '0', NOW(), 'admin'),
(1003, 1872600000000000003, '省级摄影展一等奖', 'competition', '社团成员在全省高校联合摄影展中获得第一名', 'https://images.unsplash.com/photo-1620662831351-9f693521fd1f?q=80&w=400', '2024-03-10', '省级', 1, '0', NOW(), 'admin');

-- ----------------------------
-- 入社申请演示数据
-- ----------------------------
INSERT INTO club_application (club_id, club_name, user_id, user_name, nick_name, student_id, real_name, gender, major, grade, class_name, phone, email, self_introduction, apply_reason, special_skills, application_time, review_status, create_by, create_time)
VALUES (1872600000000000001, '编程魔法师', 2, 'ry', '若依', '20230101', '张三', '0', '计算机科学与技术', '2023级', '1班', '13888888888', 'zhangsan@example.com', '我是一名大一新生，对编程有着极其浓厚的兴趣，自学过Python和Java。', '希望通过加入社团提升实战能力，并结交志同道合的朋友。', '熟练使用Git，参加过校级编程比赛。', NOW(), '0', 'system', NOW());
INSERT INTO club_application (club_id, club_name, user_id, user_name, nick_name, student_id, real_name, gender, major, grade, class_name, phone, email, self_introduction, apply_reason, special_skills, application_time, review_status, create_by, create_time)
VALUES (1872600000000000008, '篮球社', 3, 'test', '测试用户', '20220505', '李四', '0', '体育教育', '2022级', '3班', '13999999999', 'lisi@example.com', '以前在高中是校篮球队的队长，司职控球后卫。', '想在大学继续挥洒汗水，为校争光。', '擅长三分投射和组织进攻。', NOW(), '0', 'system', NOW());
INSERT INTO club_application (club_id, club_name, user_id, user_name, nick_name, student_id, real_name, gender, major, grade, class_name, phone, email, self_introduction, apply_reason, special_skills, application_time, review_status, create_by, create_time)
VALUES (1872600000000000003, '摄影俱乐部', 103, 'vicepresident1', '光影追随者', '20230912', '王小红', '1', '广告学', '2023级', '2班', '13777777777', 'wangxh@example.com', '喜欢记录生活中的美好瞬间，有一台自己的单反相机。', '希望能学习更多摄影构图和后期修图技巧。', '熟悉PS和Lightroom。', NOW(), '0', 'system', NOW());
INSERT INTO club_application (club_id, club_name, user_id, user_name, nick_name, student_id, real_name, gender, major, grade, class_name, phone, email, self_introduction, apply_reason, special_skills, application_time, review_status, create_by, create_time)
VALUES (1872600000000000001, '编程魔法师', 104, 'ai_lover', '算法达人', '20210302', '赵六', '0', '人工智能', '2021级', 'S班', '13666666666', 'zhaoliu@example.com', '大三学长，对大模型和深度学习有较深研究。', '想带带学弟学妹，整理社团技术资料库。', '精通PyTorch和TensorFlow。', NOW(), '0', 'system', NOW());
INSERT INTO club_application (club_id, club_name, user_id, user_name, nick_name, student_id, real_name, gender, major, grade, class_name, phone, email, self_introduction, apply_reason, special_skills, application_time, review_status, create_by, create_time)
VALUES (1872600000000000000, '编程狂人社', 2, 'ry', '若依', '20230101', '张三', '0', '计算机科学与技术', '2023级', '1班', '13888888888', 'zhangsan@example.com', '我是一名大一新生，对编程有着极其浓厚的兴趣，自学过Python和Java。', '希望通过加入社团提升实战能力，并结交志同道合的朋友。', '熟练使用Git，参加过校级编程比赛。', NOW(), '0', 'system', NOW());

-- ----------------------------
-- 收藏演示数据
-- ----------------------------
INSERT INTO club_favorite (favorite_id, club_id, user_id, create_time)
VALUES (10004, 1872600000000000001, 1, NOW());


-- =====================================================
-- 13、新社团创建申请表（同步 upgrade_20260227_*）
-- =====================================================
drop table if exists club_create_application;
create table club_create_application (
  apply_id             bigint(20)      not null auto_increment comment '申请ID',
  club_name            varchar(100)    not null               comment '目标社团名称',
  category_id          bigint(20)      not null               comment '社团分类ID',
  logo_url             varchar(255)    default ''             comment '社团Logo',
  contact_phone        varchar(20)     default ''             comment '联系电话',
  description          text                                    comment '社团简介',
  apply_reason         text                                    comment '申请理由',
  activity_plan        text                                    comment '活动计划（可选）',
  core_members         text                                    comment '核心成员规划（可选）',
  advisor_name         varchar(50)     default null           comment '指导老师姓名（可选）',
  advisor_contact      varchar(100)    default ''             comment '指导老师联系方式',
  applicant_user_id    bigint(20)      not null               comment '申请人用户ID',
  applicant_user_name  varchar(30)     not null               comment '申请人账号',
  applicant_nick_name  varchar(30)     default ''             comment '申请人昵称',
  applicant_phone      varchar(20)     default ''             comment '申请人电话',
  applicant_email      varchar(100)    default ''             comment '申请人邮箱',
  apply_time           datetime                                 comment '申请时间',
  review_status        char(1)         default '0'            comment '审核状态（0待审核 1通过 2拒绝）',
  reviewer_id          bigint(20)                               comment '审核人ID',
  reviewer_name        varchar(50)     default ''             comment '审核人姓名',
  review_time          datetime                                 comment '审核时间',
  review_comment       varchar(500)    default ''             comment '审核意见',
  approved_club_id     bigint(20)                               comment '通过后社团ID',
  admin_user_id        bigint(20)                               comment '自动创建管理员ID',
  admin_user_name      varchar(30)     default ''             comment '自动创建管理员账号',
  admin_init_password  varchar(100)    default ''             comment '自动创建管理员初始密码',
  del_flag             char(1)         default '0'            comment '删除标志（0存在 2删除）',
  create_by            varchar(64)     default ''             comment '创建者',
  create_time          datetime                                 comment '创建时间',
  update_by            varchar(64)     default ''             comment '更新者',
  update_time          datetime                                 comment '更新时间',
  remark               varchar(500)    default null           comment '备注',
  primary key (apply_id),
  key idx_cca_status (review_status),
  key idx_cca_apply_time (apply_time),
  key idx_cca_applicant (applicant_user_id),
  key idx_cca_club_name (club_name)
) engine=innodb auto_increment=10000 comment = '新建社团申请表';

-- 兼容旧数据：空字符串统一转为 NULL
update club_create_application
set advisor_name = null
where advisor_name = '';


-- =====================================================
-- 14、菜单与权限同步（同步 upgrade_20260225_*、upgrade_20260227_*）
-- =====================================================
start transaction;

-- 一级菜单权限码与组件路径同步
update sys_menu
set perms = 'club:application:list', component = 'club/application/index', path = 'application'
where menu_id = 3201;

update sys_menu
set perms = 'club:member:list', component = 'club/member/index', path = 'member'
where menu_id = 3202;

update sys_menu
set perms = 'club:application:list', component = 'club/application/stat', path = 'application-stat'
where menu_id = 3203;

-- 申请按钮权限
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3211, '申请查询', 3201, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:query', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3211);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3212, '申请审核', 3201, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:review', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3212);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3213, '申请删除', 3201, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:remove', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3213);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3214, '申请导出', 3201, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:application:export', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3214);

-- 成员按钮权限
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3221, '成员查询', 3202, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:query', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3221);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3222, '成员新增', 3202, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:add', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3222);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3223, '成员修改', 3202, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:edit', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3223);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3224, '成员删除', 3202, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:remove', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3224);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3225, '成员导出', 3202, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:member:export', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3225);

-- 按钮权限最终值统一
update sys_menu set perms = 'club:application:query'  where menu_id = 3211;
update sys_menu set perms = 'club:application:review' where menu_id = 3212;
update sys_menu set perms = 'club:application:remove' where menu_id = 3213;
update sys_menu set perms = 'club:application:export' where menu_id = 3214;
update sys_menu set perms = 'club:member:query'       where menu_id = 3221;
update sys_menu set perms = 'club:member:add'         where menu_id = 3222;
update sys_menu set perms = 'club:member:edit'        where menu_id = 3223;
update sys_menu set perms = 'club:member:remove'      where menu_id = 3224;
update sys_menu set perms = 'club:member:export'      where menu_id = 3225;

-- 新社团申请菜单与按钮
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3204, '新社团申请', 3100, 4, 'club-apply', 'club/clubApply/index', '', '', 1, 0, 'C', '0', '0', 'club:createApply:list', 'edit-pen', 'admin', now(), '', null, '新社团申请审核'
where not exists (select 1 from sys_menu where menu_id = 3204);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3231, '新社团申请查询', 3204, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:query', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3231);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3232, '新社团申请审核', 3204, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:review', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3232);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3233, '新社团申请删除', 3204, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:remove', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3233);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 3234, '新社团申请导出', 3204, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'club:createApply:export', '#', 'admin', now(), '', null, ''
where not exists (select 1 from sys_menu where menu_id = 3234);

update sys_menu
set menu_name = '新社团申请',
    parent_id = 3100,
    order_num = 4,
    perms = 'club:createApply:list',
    component = 'club/clubApply/index',
    path = 'club-apply',
    icon = 'edit-pen',
    remark = '新社团申请审核',
    update_by = 'admin',
    update_time = now()
where menu_id = 3204;

update sys_menu set menu_name = '新社团申请查询', perms = 'club:createApply:query',  update_by = 'admin', update_time = now() where menu_id = 3231;
update sys_menu set menu_name = '新社团申请审核', perms = 'club:createApply:review', update_by = 'admin', update_time = now() where menu_id = 3232;
update sys_menu set menu_name = '新社团申请删除', perms = 'club:createApply:remove', update_by = 'admin', update_time = now() where menu_id = 3233;
update sys_menu set menu_name = '新社团申请导出', perms = 'club:createApply:export', update_by = 'admin', update_time = now() where menu_id = 3234;

-- 角色授权补齐
insert ignore into sys_role_menu (role_id, menu_id) values
(100, 3211), (100, 3212), (100, 3213), (100, 3214), (100, 3221), (100, 3222), (100, 3223), (100, 3224), (100, 3225),
(101, 3211), (101, 3212), (101, 3213), (101, 3221), (101, 3223), (101, 3224),
(102, 3211), (102, 3212), (102, 3221),
(1, 3204), (100, 3204),
(1, 3231), (1, 3232), (1, 3233), (1, 3234),
(100, 3231), (100, 3232), (100, 3233), (100, 3234);

commit;

