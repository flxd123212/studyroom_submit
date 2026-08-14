-- ============================================================
-- 自习室预约系统 - 示例数据
-- ============================================================

-- ---------- 管理员 ----------
INSERT INTO `user`(student_id, username, password, role, status)
SELECT 'admin', '管理员', 'admin123', 1, 0
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE student_id='admin');

-- ---------- 学生测试账号 ----------
INSERT INTO `user`(student_id, username, password, role, status)
SELECT '2024001', '张三', '123456', 0, 0
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE student_id='2024001');

INSERT INTO `user`(student_id, username, password, role, status)
SELECT '2024002', '李四', '123456', 0, 0
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE student_id='2024002');

INSERT INTO `user`(student_id, username, password, role, status)
SELECT '2024003', '王五', '123456', 0, 0
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE student_id='2024003');

-- ---------- 自习室 ----------
INSERT INTO room(room_name, total_seat, open_time, close_time)
SELECT 'A区 静思阁', 24, '08:00', '22:00'
WHERE NOT EXISTS (SELECT 1 FROM room WHERE room_name='A区 静思阁');

INSERT INTO room(room_name, total_seat, open_time, close_time)
SELECT 'B区 明辨轩', 18, '08:00', '21:30'
WHERE NOT EXISTS (SELECT 1 FROM room WHERE room_name='B区 明辨轩');

INSERT INTO room(room_name, total_seat, open_time, close_time)
SELECT 'C区 致远厅', 30, '07:30', '23:00'
WHERE NOT EXISTS (SELECT 1 FROM room WHERE room_name='C区 致远厅');

-- ---------- 座位（A区 24个，编号 A01~A24） ----------
INSERT INTO seat(seat_id, room_id, seat_no, status, features)
SELECT x.id, 1, x.sn, 0, 'window,quiet' FROM (
  SELECT 101, 'A01' UNION ALL SELECT 102, 'A02' UNION ALL SELECT 103, 'A03'
  UNION ALL SELECT 104, 'A04' UNION ALL SELECT 105, 'A05' UNION ALL SELECT 106, 'A06'
  UNION ALL SELECT 107, 'A07' UNION ALL SELECT 108, 'A08' UNION ALL SELECT 109, 'A09'
  UNION ALL SELECT 110, 'A10' UNION ALL SELECT 111, 'A11' UNION ALL SELECT 112, 'A12'
  UNION ALL SELECT 113, 'A13' UNION ALL SELECT 114, 'A14' UNION ALL SELECT 115, 'A15'
  UNION ALL SELECT 116, 'A16' UNION ALL SELECT 117, 'A17' UNION ALL SELECT 118, 'A18'
  UNION ALL SELECT 119, 'A19' UNION ALL SELECT 120, 'A20' UNION ALL SELECT 121, 'A21'
  UNION ALL SELECT 122, 'A22' UNION ALL SELECT 123, 'A23' UNION ALL SELECT 124, 'A24'
) AS x(id, sn)
WHERE NOT EXISTS (SELECT 1 FROM seat WHERE room_id=1 AND seat_no='A01');

-- ---------- 座位（B区 18个，编号 B01~B18） ----------
INSERT INTO seat(seat_id, room_id, seat_no, status, features)
SELECT x.id, 2, x.sn, 0, x.feat FROM (
  SELECT 201, 'B01', 'window' UNION ALL SELECT 202, 'B02', 'power'
  UNION ALL SELECT 203, 'B03', 'window' UNION ALL SELECT 204, 'B04', 'power'
  UNION ALL SELECT 205, 'B05', 'window' UNION ALL SELECT 206, 'B06', 'power'
  UNION ALL SELECT 207, 'B07', 'window' UNION ALL SELECT 208, 'B08', 'power'
  UNION ALL SELECT 209, 'B09', 'window' UNION ALL SELECT 210, 'B10', 'power'
  UNION ALL SELECT 211, 'B11', 'window' UNION ALL SELECT 212, 'B12', 'power'
  UNION ALL SELECT 213, 'B13', 'window' UNION ALL SELECT 214, 'B14', 'power'
  UNION ALL SELECT 215, 'B15', 'window' UNION ALL SELECT 216, 'B16', 'power'
  UNION ALL SELECT 217, 'B17', 'window' UNION ALL SELECT 218, 'B18', 'power'
) AS x(id, sn, feat)
WHERE NOT EXISTS (SELECT 1 FROM seat WHERE room_id=2 AND seat_no='B01');

-- ---------- 座位（C区 30个，编号 C01~C30） ----------
INSERT INTO seat(seat_id, room_id, seat_no, status, features)
SELECT x.id, 3, x.sn, 0, 'power,quiet' FROM (
  SELECT 301, 'C01' UNION ALL SELECT 302, 'C02' UNION ALL SELECT 303, 'C03'
  UNION ALL SELECT 304, 'C04' UNION ALL SELECT 305, 'C05' UNION ALL SELECT 306, 'C06'
  UNION ALL SELECT 307, 'C07' UNION ALL SELECT 308, 'C08' UNION ALL SELECT 309, 'C09'
  UNION ALL SELECT 310, 'C10' UNION ALL SELECT 311, 'C11' UNION ALL SELECT 312, 'C12'
  UNION ALL SELECT 313, 'C13' UNION ALL SELECT 314, 'C14' UNION ALL SELECT 315, 'C15'
  UNION ALL SELECT 316, 'C16' UNION ALL SELECT 317, 'C17' UNION ALL SELECT 318, 'C18'
  UNION ALL SELECT 319, 'C19' UNION ALL SELECT 320, 'C20' UNION ALL SELECT 321, 'C21'
  UNION ALL SELECT 322, 'C22' UNION ALL SELECT 323, 'C23' UNION ALL SELECT 324, 'C24'
  UNION ALL SELECT 325, 'C25' UNION ALL SELECT 326, 'C26' UNION ALL SELECT 327, 'C27'
  UNION ALL SELECT 328, 'C28' UNION ALL SELECT 329, 'C29' UNION ALL SELECT 330, 'C30'
) AS x(id, sn)
WHERE NOT EXISTS (SELECT 1 FROM seat WHERE room_id=3 AND seat_no='C01');

-- ---------- 示例预约记录（用于热力图等统计） ----------
-- 插入少量示例预约
INSERT INTO reservation(user_id, room_id, seat_id, res_time, v_status)
SELECT 2, 1, 101, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 3
WHERE NOT EXISTS (SELECT 1 FROM reservation WHERE res_id > 0);

INSERT INTO reservation(user_id, room_id, seat_id, res_time, v_status)
SELECT 3, 2, 201, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 3
WHERE NOT EXISTS (SELECT 1 FROM reservation WHERE res_id > 0);

INSERT INTO reservation(user_id, room_id, seat_id, res_time, v_status)
SELECT 4, 3, 301, DATEADD('DAY', -1, CURRENT_TIMESTAMP), 3
WHERE NOT EXISTS (SELECT 1 FROM reservation WHERE res_id > 0);

-- ---------- 预约规则 ----------
INSERT INTO reservation_rule(max_per_user, max_hours, allow_cancel_minutes)
SELECT 1, 4.0, 30
WHERE NOT EXISTS (SELECT 1 FROM reservation_rule);

-- ---------- 公告 ----------
INSERT INTO announcement(title, content, priority, status)
SELECT '📢 自习室系统上线通知', '欢迎使用自习室预约系统！请先使用学号注册，然后即可预约座位。', 3, 1
WHERE NOT EXISTS (SELECT 1 FROM announcement WHERE title='📢 自习室系统上线通知');

INSERT INTO announcement(title, content, priority, status)
SELECT '📌 预约规则说明', '每人最多同时预约1个座位，每次最长使用4小时。预约后请按时签到，超时将自动取消。', 2, 1
WHERE NOT EXISTS (SELECT 1 FROM announcement WHERE title='📌 预约规则说明');
