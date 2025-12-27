-- Clear existing conflicting data first (to allow new IDs)
DELETE FROM `club_category` WHERE `category_code` IN ('technology', 'art', 'sports', 'academic', 'volunteer');

-- Insert Category Data
INSERT INTO `club_category` (`category_id`, `category_code`, `category_name`, `sort_order`, `create_time`, `create_by`) VALUES
(1872500000000000001, 'technology', '科技类', 1, NOW(), 'admin'),
(1872500000000000002, 'art', '艺术类', 2, NOW(), 'admin'),
(1872500000000000003, 'sports', '体育类', 3, NOW(), 'admin'),
(1872500000000000004, 'academic', '学术类', 4, NOW(), 'admin'),
(1872500000000000005, 'volunteer', '志愿类', 5, NOW(), 'admin');

-- Insert Club Data
INSERT INTO `club` (`club_id`, `club_name`, `club_code`, `category_id`, `description`, `logo_url`, `member_count`, `president_name`, `founded_date`, `location`, `remark`, `create_time`, `create_by`) VALUES
(1872600000000000001, '编程魔法师', 'coding_wizards', 1872500000000000001, '开发者分享知识、构建项目并参加黑客马拉松的社区。我们每周聚会一次，讨论最新的技术，开展开源项目，并为编程面试做准备。', 'https://images.unsplash.com/photo-1531482615713-2afd69097998?auto=format&fit=crop&q=80&w=800', 128, '爱丽丝·约翰逊', '2020-09-01', '科技楼 304室', '每周三 18:00', NOW(), 'admin'),
(1872600000000000002, '辩论协会', 'debate_society', 1872500000000000004, '通过每周对当前全球问题的辩论，磨练你的修辞和批判性思维能力。', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', 45, '鲍勃·史密斯', '2018-03-15', '学生活动中心 101', '每周二 17:00', NOW(), 'admin'),
(1872600000000000003, '摄影俱乐部', 'photo_club', 1872500000000000002, '捕捉你周围的世界。每周摄影采风、工作坊和展览。', 'https://images.unsplash.com/photo-1542038784456-1ea8e935640e?auto=format&fit=crop&q=80&w=800', 82, '查理·布朗', '2019-11-20', '艺术工作室 B', '每周四 16:30', NOW(), 'admin'),
(1872600000000000004, '绿色地球', 'green_earth', 1872500000000000005, '通过校园倡议促进可持续发展和环境意识。', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?auto=format&fit=crop&q=80&w=800', 200, '戴安娜', '2015-04-22', '社区花园', '每周五 15:00', NOW(), 'admin'),
(1872600000000000005, '乐器合奏团', 'music_ensemble', 1872500000000000002, '加入其他音乐家进行即兴演奏、表演和音乐鉴赏之夜。', 'https://images.unsplash.com/photo-1511379938547-c1f69419868d?auto=format&fit=crop&q=80&w=800', 65, '埃文', '2016-09-10', '音乐厅', '每周一 19:00', NOW(), 'admin'),
(1872600000000000006, '机器人俱乐部', 'robotics_club', 1872500000000000001, '建造和编程机器人。参加全国比赛并学习机电一体化。', 'https://images.unsplash.com/photo-1561557944-6e7860d1a7eb?auto=format&fit=crop&q=80&w=800', 50, '菲奥娜', '2021-01-15', '工程实验室', '每周六 10:00', NOW(), 'admin'),
(1872600000000000007, 'AI 研习社', 'ai_study_group', 1872500000000000001, '探索人工智能的前沿技术，包括深度学习、计算机视觉和自然语言处理。', 'https://plus.unsplash.com/premium_photo-1683121710572-7723bd2e235d?auto=format&fit=crop&q=80&w=800', 150, 'GPT-4', '2023-05-01', '创新中心 202', '每周五 14:00', NOW(), 'admin'),
(1872600000000000008, '篮球社', 'basketball_club', 1872500000000000003, '热爱篮球的同学集结地，每周组织训练和校内友谊赛。', 'https://images.unsplash.com/photo-1546519638-68e109498ee2?auto=format&fit=crop&q=80&w=800', 90, '科比·粉丝', '2010-09-01', '北区篮球场', '每周二、四 18:00', NOW(), 'admin');

-- Insert Activity Data
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `start_time`, `location`, `status`, `description`, `create_time`, `create_by`) VALUES
(1872700000000000001, 1872600000000000001, '黑客马拉松备战之夜', '2024-10-15 18:00:00', '304室', '1', '备战即将到来的黑客马拉松，分享创意和组队。', NOW(), 'admin'),
(1872700000000000002, 1872600000000000001, '特邀嘉宾：谷歌工程师', '2024-09-28 14:00:00', 'A号礼堂', '2', '来自谷歌的高级工程师分享职业发展经验。', NOW(), 'admin'),
(1872700000000000003, 1872600000000000001, 'React 进阶工作坊', '2024-11-05 19:00:00', '2号实验室', '0', '深入理解React Hooks和性能优化。', NOW(), 'admin');

-- Insert Notice Data
INSERT INTO `club_notice` (`notice_id`, `club_id`, `notice_title`, `notice_content`, `publish_time`, `status`, `create_time`, `create_by`) VALUES
(1872800000000000001, 1872600000000000001, '会费缴纳通知', '请在月底前缴纳会员费。', '2024-10-01 09:00:00', '1', NOW(), 'admin'),
(1872800000000000002, 1872600000000000001, '迎新会确认', '本周五的迎新会已确认！', '2024-09-15 10:00:00', '1', NOW(), 'admin');
  