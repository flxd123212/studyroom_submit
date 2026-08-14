-- ============================================================
-- 自习室预约系统 - H2 数据库初始化脚本（兼容 MySQL 模式）
-- ============================================================

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    user_id     INT          AUTO_INCREMENT PRIMARY KEY,
    student_id  VARCHAR(50)  NOT NULL UNIQUE,
    username    VARCHAR(50)  NOT NULL,
    password    VARCHAR(200) NOT NULL,
    role        INT          DEFAULT 0,
    email       VARCHAR(100) DEFAULT NULL,
    status      INT          DEFAULT 0
);

-- 自习室表
CREATE TABLE IF NOT EXISTS `room` (
    room_id     INT          AUTO_INCREMENT PRIMARY KEY,
    room_name   VARCHAR(100) NOT NULL,
    total_seat  INT          DEFAULT 0,
    open_time   TIME         DEFAULT '08:00:00',
    close_time  TIME         DEFAULT '22:00:00'
);

-- 座位表
CREATE TABLE IF NOT EXISTS `seat` (
    seat_id      INT         AUTO_INCREMENT PRIMARY KEY,
    room_id      INT         NOT NULL,
    seat_no      VARCHAR(20) NOT NULL,
    status       INT         DEFAULT 0,
    reserve_user INT         DEFAULT NULL,
    features     VARCHAR(200) DEFAULT '',
    UNIQUE KEY uk_room_seat (room_id, seat_no)
);

-- 预约记录表
CREATE TABLE IF NOT EXISTS `reservation` (
    res_id        INT          AUTO_INCREMENT PRIMARY KEY,
    user_id       INT          NOT NULL,
    room_id       INT          NOT NULL,
    seat_id       INT          NOT NULL,
    res_time      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    v_status      INT          DEFAULT 0,
    checkin_time  DATETIME     DEFAULT NULL,
    checkout_time DATETIME     DEFAULT NULL
);

-- 预约规则表
CREATE TABLE IF NOT EXISTS `reservation_rule` (
    rule_id               INT AUTO_INCREMENT PRIMARY KEY,
    max_per_user          INT DEFAULT 1,
    max_hours             DECIMAL(4,1) DEFAULT 4.0,
    allow_cancel_minutes  INT DEFAULT 30,
    auto_checkout_minutes INT DEFAULT 0,
    create_time           DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time           DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 公告表
CREATE TABLE IF NOT EXISTS `announcement` (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    content     TEXT,
    priority    INT DEFAULT 0,
    status      INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 留言表
CREATE TABLE IF NOT EXISTS `message` (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    room_id     INT DEFAULT NULL,
    content     TEXT NOT NULL,
    reply_to    INT DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
