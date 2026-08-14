package org.example.studyroom.entity;

public class Seat {
    private Integer seatId;
    private Integer roomId;
    private String seatNo;
    private Integer status;
    private Integer reserveUser;
    private String roomName;
    private String features;

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public Integer getSeatId() { return seatId; }
    public void setSeatId(Integer seatId) { this.seatId = seatId; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public String getSeatNo() { return seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getReserveUser() { return reserveUser; }
    public void setReserveUser(Integer reserveUser) { this.reserveUser = reserveUser; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
}