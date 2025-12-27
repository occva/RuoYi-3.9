-- SQL for adding more mock activities for different clubs

-- 1872600000000000002: 辩论协会 (Debate Society)
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `activity_type`, `description`, `cover_url`, `location`, `start_time`, `end_time`, `status`, `is_public`, `create_time`, `create_by`) VALUES
(1872700000000000004, 1872600000000000002, '全校辩论大赛：网络社交是否取代了面对面交流', '比赛', '探讨现代科技对人类社交模式的影响。冠军将获得精美奖品！', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&q=80&w=800', '演讲厅', NOW() + INTERVAL 5 DAY, NOW() + INTERVAL 5 DAY + INTERVAL 3 HOUR, '0', '1', NOW(), 'admin'),
(1872700000000000005, 1872600000000000002, '辩论技巧工作坊', '培训', '学习如何构建有说服力的论点并提升临场反应能力。', 'https://images.unsplash.com/photo-1517048676732-d65bc937f952?auto=format&fit=crop&q=80&w=800', '101会议室', NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY + INTERVAL 2 HOUR, '2', '1', NOW(), 'admin');

-- 1872600000000000003: 摄影俱乐部 (Photo Club)
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `activity_type`, `description`, `cover_url`, `location`, `start_time`, `end_time`, `status`, `is_public`, `create_time`, `create_by`) VALUES
(1872700000000000006, 1872600000000000003, '秋季校园外拍：寻找光影', '外拍', '拿起相机，记录下秋天校园里最美的瞬间。', 'https://images.unsplash.com/photo-1542038784456-1ea8e935640e?auto=format&fit=crop&q=80&w=800', '博雅湖畔', NOW() + INTERVAL 2 DAY, NOW() + INTERVAL 2 DAY + INTERVAL 4 HOUR, '0', '1', NOW(), 'admin'),
(1872700000000000007, 1872600000000000003, '后期处理入门：Lightroom 实操', '培训', '由资深摄影师指导，教你如何修出电影感大片。', 'https://images.unsplash.com/photo-1554048612-b6a482bc67e5?auto=format&fit=crop&q=80&w=800', '机房501', NOW(), NOW() + INTERVAL 2 HOUR, '1', '1', NOW(), 'admin');

-- 1872600000000000004: 绿色地球 (Green Earth)
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `activity_type`, `description`, `cover_url`, `location`, `start_time`, `end_time`, `status`, `is_public`, `create_time`, `create_by`) VALUES
(1872700000000000008, 1872600000000000004, '“植”得期待：校园公益植树行动', '公益', '为了更绿色的校园，我们一起动手种下希望的树苗。', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?auto=format&fit=crop&q=80&w=800', '西区实验田', NOW() + INTERVAL 10 DAY, NOW() + INTERVAL 10 DAY + INTERVAL 5 HOUR, '0', '1', NOW(), 'admin');

-- 1872600000000000005: 乐器合奏团 (Music Ensemble)
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `activity_type`, `description`, `cover_url`, `location`, `start_time`, `end_time`, `status`, `is_public`, `create_time`, `create_by`) VALUES
(1872700000000000009, 1872600000000000005, '仲夏之夜：草坪草地音乐会', '演出', '在星空下，倾听古典与现代乐器的交织与融合。', 'https://images.unsplash.com/photo-1459749411177-042180ce673c?auto=format&fit=crop&q=80&w=800', '大操场草坪', NOW() + INTERVAL 7 DAY, NOW() + INTERVAL 7 DAY + INTERVAL 2 HOUR, '0', '1', NOW(), 'admin');

-- 1872600000000000006: 机器人俱乐部 (Robotics Club)
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `activity_type`, `description`, `cover_url`, `location`, `start_time`, `end_time`, `status`, `is_public`, `create_time`, `create_by`) VALUES
(1872700000000000010, 1872600000000000006, '格斗机器人组装体验日', '动手', '想亲手制作一个能战斗的机器人吗？快来加入我们！', 'https://images.unsplash.com/photo-1485827404703-89b55fcc595e?auto=format&fit=crop&q=80&w=800', '机器人实验室', NOW() + INTERVAL 1 DAY, NOW() + INTERVAL 1 DAY + INTERVAL 4 HOUR, '0', '1', NOW(), 'admin');

-- 1872600000000000007: AI 研习社 (AI Study Group)
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `activity_type`, `description`, `cover_url`, `location`, `start_time`, `end_time`, `status`, `is_public`, `create_time`, `create_by`) VALUES
(1872700000000000011, 1872600000000000007, '深度学习论文研讨会', '学术', '本周探讨最新的计算机视觉模型及其在工业界的落地。', 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?auto=format&fit=crop&q=80&w=800', '202教室', NOW() - INTERVAL 2 HOUR, NOW() + INTERVAL 1 HOUR, '1', '1', NOW(), 'admin');

-- 1872600000000000008: 篮球社 (Basketball Club)
INSERT INTO `club_activity` (`activity_id`, `club_id`, `activity_title`, `activity_type`, `description`, `cover_url`, `location`, `start_time`, `end_time`, `status`, `is_public`, `create_time`, `create_by`) VALUES
(1872700000000000012, 1872600000000000008, '三对三邀请战：谁是路人王？', '比赛', '热血篮球，就在此刻。期待全校各路高手前来挑战。', 'https://images.unsplash.com/photo-1519766304817-4f37bda74a26?auto=format&fit=crop&q=80&w=800', '室内篮球场', NOW() + INTERVAL 4 DAY, NOW() + INTERVAL 4 DAY + INTERVAL 6 HOUR, '0', '1', NOW(), 'admin');
