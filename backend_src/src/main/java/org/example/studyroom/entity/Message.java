package org.example.studyroom.entity;

import java.time.LocalDateTime;

public class Message {
    private Integer id;
    private Integer userId;
    private Integer roomId;
    private String content;
    private Integer replyTo;
    private LocalDateTime createTime;
    // 关联字段（查询时填充）
    private String username;
    private String studentId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getReplyTo() { return replyTo; }
    public void setReplyTo(Integer replyTo) { this.replyTo = replyTo; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}
