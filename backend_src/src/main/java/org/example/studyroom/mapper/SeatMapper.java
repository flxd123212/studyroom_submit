package org.example.studyroom.mapper;
import org.apache.ibatis.annotations.*;
import org.example.studyroom.entity.Seat;
import java.util.List;
import java.util.Map;

@Mapper
public interface SeatMapper {
    // 必须查出 reserve_user（预约人ID）
    @Select("SELECT seat_id seatId, seat_no seatNo, status, reserve_user reserveUser, features FROM seat WHERE room_id=#{roomId}")
    List<Seat> getByRoom(@Param("roomId") Integer roomId);

    @Select("SELECT seat_id seatId, seat_no seatNo, status, reserve_user reserveUser, room_id roomId, features FROM seat WHERE seat_id=#{seatId}")
    Seat getBySeatId(@Param("seatId") Integer seatId);

    // 预约：绑定用户ID，状态=1
    @Update("UPDATE seat SET status=1, reserve_user=#{userId} WHERE seat_id=#{seatId}")
    void updateStatus(@Param("seatId") Integer seatId, @Param("userId") Integer userId);

    // 取消：解绑，状态=0
    @Update("UPDATE seat SET status=0, reserve_user=NULL WHERE seat_id=#{seatId}")
    void updateStatus0(@Param("seatId") Integer seatId);

    // 管理员：获取所有座位（含房间信息）
    @Select("SELECT s.seat_id seatId, s.room_id roomId, s.seat_no seatNo, s.status, s.reserve_user reserveUser, r.room_name roomName FROM seat s LEFT JOIN room r ON s.room_id=r.room_id ORDER BY s.room_id, s.seat_no")
    List<Seat> listAll();

    // 管理员：添加座位
    @Insert("INSERT INTO seat(room_id, seat_no) VALUES(#{roomId}, #{seatNo})")
    void add(@Param("roomId") Integer roomId, @Param("seatNo") String seatNo);

    // 管理员：批量添加座位
    @Insert("<script>INSERT INTO seat(room_id, seat_no) VALUES " +
            "<foreach collection='seatNos' item='sn' separator=','>(#{roomId}, #{sn})</foreach></script>")
    void batchInsert(@Param("roomId") Integer roomId, @Param("seatNos") List<String> seatNos);

    // 管理员：删除座位
    @Delete("DELETE FROM seat WHERE seat_id=#{seatId}")
    void delete(@Param("seatId") Integer seatId);

    // 管理员：直接更新座位状态（不涉及预约），可设多种状态
    @Update("UPDATE seat SET status=#{status} WHERE seat_id=#{seatId}")
    void updateStatusOnly(@Param("seatId") Integer seatId, @Param("status") Integer status);

    // 管理员：直接更新座位状态并解绑用户
    @Update("UPDATE seat SET status=#{status}, reserve_user=NULL WHERE seat_id=#{seatId}")
    void updateStatusDirectly(@Param("seatId") Integer seatId, @Param("status") Integer status);

    // 根据房间ID删除所有座位
    @Delete("DELETE FROM seat WHERE room_id=#{roomId}")
    void deleteByRoom(@Param("roomId") Integer roomId);

    // 统计每个房间可用座位数（status=0 的）
    @Select("SELECT room_id roomId, COUNT(*) availableCount FROM seat WHERE status=0 GROUP BY room_id")
    List<Map<String, Object>> countAvailableByRoom();

    // 更新座位特征
    @Update("UPDATE seat SET features=#{features} WHERE seat_id=#{seatId}")
    void updateFeatures(@Param("seatId") Integer seatId, @Param("features") String features);
}