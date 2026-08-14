DELIMITER //
-- 工具存储过程：安全添加列（兼容 MySQL 5.7+）
DROP PROCEDURE IF EXISTS add_col //
CREATE PROCEDURE add_col(tbl VARCHAR(64), col VARCHAR(64), def VARCHAR(512))
BEGIN
  SET @s = CONCAT('ALTER TABLE ', tbl, ' ADD COLUMN ', col, ' ', def);
  SELECT COUNT(*) INTO @cnt FROM information_schema.columns
    WHERE table_schema=DATABASE() AND table_name=REPLACE(tbl,'`','') AND column_name=col;
  IF @cnt = 0 THEN
    PREPARE stmt FROM @s; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  END IF;
END //
-- 工具存储过程：安全创建表
DROP PROCEDURE IF EXISTS create_if_not //
CREATE PROCEDURE create_if_not(tbl VARCHAR(64), def TEXT)
BEGIN
  SELECT COUNT(*) INTO @cnt FROM information_schema.tables
    WHERE table_schema=DATABASE() AND table_name=tbl;
  IF @cnt = 0 THEN
    SET @s = CONCAT('CREATE TABLE ', tbl, ' ', def);
    PREPARE stmt FROM @s; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  END IF;
END //
DELIMITER ;

-- 1. user表新增 status 字段（0=正常, 1=拉黑）
CALL add_col('user', 'status', 'INT DEFAULT 0 COMMENT ''0=正常 1=拉黑''');

-- 2. room表新增开放时间字段
CALL add_col('room', 'open_time', 'TIME DEFAULT ''08:00:00''');
CALL add_col('room', 'close_time', 'TIME DEFAULT ''22:00:00''');

-- 3. 更新已有自习室的开放时间
UPDATE room SET open_time='08:00:00', close_time='22:00:00' WHERE open_time IS NULL;

-- 4. password列扩展为varchar(100)，支持BCrypt加密（60字符）
ALTER TABLE user MODIFY COLUMN password VARCHAR(100) NOT NULL COMMENT 'BCrypt加密密码';

-- 5. 插入默认管理员账号（学号: admin, 密码: admin123, role=1）
INSERT INTO user(student_id, username, password, role, status)
SELECT 'admin', '管理员', 'admin123', 1, 0
WHERE NOT EXISTS (SELECT 1 FROM user WHERE student_id='admin');

-- ========== 以下为 v2.0 新增功能：签到签退 + 规则 + 公告 + 座位特征 ==========

-- 6. reservation 表新增状态和签到时间
CALL add_col('reservation', 'v_status', 'INT DEFAULT 0 COMMENT ''0=已预约 1=已签到 2=已完成 3=已取消''');
CALL add_col('reservation', 'checkin_time', 'DATETIME DEFAULT NULL COMMENT ''签到时间''');
CALL add_col('reservation', 'checkout_time', 'DATETIME DEFAULT NULL COMMENT ''签退时间''');

-- 7. 预约规则表
CALL create_if_not('reservation_rule', '(
  rule_id INT AUTO_INCREMENT PRIMARY KEY,
  max_per_user INT DEFAULT 1 COMMENT ''每人最多可同时预约几个座位'',
  max_hours DECIMAL(4,1) DEFAULT 4.0 COMMENT ''每次最长使用小时数'',
  allow_cancel_minutes INT DEFAULT 30 COMMENT ''预约后多少分钟内可取消（0=随时可取消）'',
  auto_checkout_minutes INT DEFAULT 0 COMMENT ''签到后超时未签退自动完成（0=不自动）'',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)');

-- 插入默认规则
INSERT INTO reservation_rule(max_per_user, max_hours, allow_cancel_minutes)
SELECT 1, 4.0, 30
WHERE NOT EXISTS (SELECT 1 FROM reservation_rule);

-- 8. 公告表
CALL create_if_not('announcement', '(
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL COMMENT ''公告标题'',
  content TEXT COMMENT ''公告内容'',
  priority INT DEFAULT 0 COMMENT ''优先级（越大越靠前）'',
  status INT DEFAULT 1 COMMENT ''1=发布 0=草稿'',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)');

-- 9. seat 表新增特征字段
CALL add_col('seat', 'features', 'VARCHAR(200) DEFAULT '''' COMMENT ''座位特征,逗号分隔: window,power,quiet''');

-- 10. 留言表
CALL create_if_not('message', '(
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL COMMENT ''留言用户ID'',
  room_id INT DEFAULT NULL COMMENT ''关联自习室ID（null=全局留言）'',
  content TEXT NOT NULL COMMENT ''留言内容'',
  reply_to INT DEFAULT NULL COMMENT ''回复的留言ID（null=原始留言）'',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
)');

-- 清理存储过程
DROP PROCEDURE IF EXISTS add_col;
DROP PROCEDURE IF EXISTS create_if_not;
