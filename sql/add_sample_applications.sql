-- ----------------------------
-- 新增加几条入社申请演示数据
-- ----------------------------

INSERT INTO club_application (
    club_id, club_name, user_id, user_name, nick_name, 
    student_id, real_name, gender, major, grade, 
    class_name, phone, email, self_introduction, apply_reason, 
    special_skills, application_time, review_status, create_by, create_time
) VALUES (
    1000, '编程狂人社', 2, 'ry', '若依', 
    '20230101', '张三', '0', '计算机科学与技术', '2023级', 
    '1班', '13888888888', 'zhangsan@example.com', '我是一名大一新生，对编程有着极其浓厚的兴趣，自学过Python和Java。', '希望通过加入社团提升实战能力，并结交志同道合的朋友。', 
    '熟练使用Git，参加过校级编程比赛。', NOW(), '0', 'system', NOW()
);

INSERT INTO club_application (
    club_id, club_name, user_id, user_name, nick_name, 
    student_id, real_name, gender, major, grade, 
    class_name, phone, email, self_introduction, apply_reason, 
    special_skills, application_time, review_status, create_by, create_time
) VALUES (
    1001, '篮球协会', 3, 'test', '测试用户', 
    '20220505', '李四', '0', '体育教育', '2022级', 
    '3班', '13999999999', 'lisi@example.com', '以前在高中是校篮球队的队长，司职控球后卫。', '想在大学继续挥洒汗水，为校争光。', 
    '擅长三分投射和组织进攻。', NOW(), '0', 'system', NOW()
);

INSERT INTO club_application (
    club_id, club_name, user_id, user_name, nick_name, 
    student_id, real_name, gender, major, grade, 
    class_name, phone, email, self_introduction, apply_reason, 
    special_skills, application_time, review_status, create_by, create_time
) VALUES (
    1002, '摄影协会', 103, 'student01', '光影追随者', 
    '20230912', '王小红', '1', '广告学', '2023级', 
    '2班', '13777777777', 'wangxh@example.com', '喜欢记录生活中的美好瞬间，有一台自己的单反相机。', '希望能学习更多摄影构图和后期修图技巧。', 
    '熟悉PS和Lightroom。', NOW(), '0', 'system', NOW()
);

INSERT INTO club_application (
    club_id, club_name, user_id, user_name, nick_name, 
    student_id, real_name, gender, major, grade, 
    class_name, phone, email, self_introduction, apply_reason, 
    special_skills, application_time, review_status, create_by, create_time
) VALUES (
    1000, '编程狂人社', 104, 'ai_lover', '算法达人', 
    '20210302', '赵六', '0', '人工智能', '2021级', 
    'S班', '13666666666', 'zhaoliu@example.com', '大三学长，对大模型和深度学习有较深研究。', '想带带学弟学妹，整理社团技术资料库。', 
    '精通PyTorch和TensorFlow。', NOW(), '0', 'system', NOW()
);
