package org.example.studyroom.entity;

import java.time.LocalDateTime;

public class Reserve {
    private Integer resId;
    private Integer userId;
    private Integer roomId;
    private Integer seatId;
    private LocalDateTime resTime;
    private String roomName;
    private String seatNo;
    private String studentId;
    private String username;
    private Integer status;
    private String checkinTime;
    private String checkoutTime;

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCheckinTime() { return checkinTime; }
    public void setCheckinTime(String checkinTime) { this.checkinTime = checkinTime; }
    public String getCheckoutTime() { return checkoutTime; }
    public void setCheckoutTime(String checkoutTime) { this.checkoutTime = checkoutTime; }

    public Integer getResId() { return resId; }
    public void setResId(Integer resId) { this.resId = resId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public Integer getSeatId() { return seatId; }
    public void setSeatId(Integer seatId) { this.seatId = seatId; }

    public LocalDateTime getResTime() { return resTime; }
    public void setResTime(LocalDateTime resTime) { this.resTime = resTime; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getSeatNo() { return seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}