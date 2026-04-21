INSERT INTO `sys_menu` VALUES (3404, '页脚配置', 1, 10, 'footer', 'system/footer/index', NULL, '', 1, 0, 'C', '0', '0', 'system:footer:query', 'guide', 'admin', '2026-04-21 00:00:00', '', NULL, '用户端页脚配置菜单');
INSERT INTO `sys_menu` VALUES (3405, '页脚查询', 3404, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:footer:query', '#', 'admin', '2026-04-21 00:00:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (3406, '页脚修改', 3404, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:footer:edit', '#', 'admin', '2026-04-21 00:00:00', '', NULL, '');

INSERT INTO `sys_role_menu` VALUES (1, 3404);
INSERT INTO `sys_role_menu` VALUES (1, 3405);
INSERT INTO `sys_role_menu` VALUES (1, 3406);
