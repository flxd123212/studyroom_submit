package org.example.studyroom.mapper;
import org.apache.ibatis.annotations.*;
import org.example.studyroom.entity.Room;
import java.util.List;

@Mapper
public interface RoomMapper {
    @Select("SELECT room_id roomId, room_name roomName, total_seat totalSeat, open_time openTime, close_time closeTime FROM room")
    List<Room> list();

    @Select("SELECT room_id roomId, room_name roomName, total_seat totalSeat, open_time openTime, close_time closeTime FROM room WHERE room_id=#{roomId}")
    Room getById(@Param("roomId") Integer roomId);

    @Insert("INSERT INTO room(room_name,total_seat,open_time,close_time) VALUES(#{roomName},#{totalSeat},#{openTime},#{closeTime})")
    @Options(useGeneratedKeys = true, keyProperty = "roomId")
    void add(Room room);

    @Update("UPDATE room SET room_name=#{roomName}, total_seat=#{totalSeat}, open_time=#{openTime}, close_time=#{closeTime} WHERE room_id=#{roomId}")
    void update(Room room);

    @Delete("DELETE FROM room WHERE room_id=#{roomId}")
    void delete(@Param("roomId") Integer roomId);
}