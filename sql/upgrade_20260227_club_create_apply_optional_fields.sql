-- ============================================================
-- club_create_application optional fields patch
-- 1) activity_plan optional
-- 2) core_members optional
-- 3) advisor_name optional
-- ============================================================

START TRANSACTION;

ALTER TABLE club_create_application
  MODIFY activity_plan text NULL COMMENT 'activity plan',
  MODIFY core_members text NULL COMMENT 'core members plan',
  MODIFY advisor_name varchar(50) NULL DEFAULT NULL COMMENT 'advisor name';

UPDATE club_create_application
SET advisor_name = NULL
WHERE advisor_name = '';

COMMIT;
