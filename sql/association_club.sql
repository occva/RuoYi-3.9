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
  审核_status        char(1)         default '0'                comment '审核状态（0待审核 1已通过 2已拒绝 3已撤回）',
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
  key idx_application_status (审核_status),
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
