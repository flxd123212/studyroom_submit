package org.example.studyroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public class Room {
    private Integer roomId;
    private String roomName;
    private Integer totalSeat;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public Integer getTotalSeat() { return totalSeat; }
    public void setTotalSeat(Integer totalSeat) { this.totalSeat = totalSeat; }

    public LocalTime getOpenTime() { return openTime; }
    public void setOpenTime(LocalTime openTime) { this.openTime = openTime; }

    public LocalTime getCloseTime() { return closeTime; }
    public void setCloseTime(LocalTime closeTime) { this.closeTime = closeTime; }
}