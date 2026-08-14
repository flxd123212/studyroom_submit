package org.example.studyroom.mapper;
import org.apache.ibatis.annotations.*;
import org.example.studyroom.entity.Reserve;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReserveMapper {
    @Insert("INSERT INTO reservation(user_id,room_id,seat_id) VALUES(#{userId},#{roomId},#{seatId})")
    void add(Reserve reserve);

    // 严格校验：当前登录用户有没有预约
    @Select("SELECT COUNT(*) FROM reservation WHERE user_id=#{userId} AND v_status IN (0,1)")
    int countByUser(@Param("userId") Integer userId);

    @Select("""
            SELECT r.res_id resId, r.user_id userId, r.room_id roomId, r.seat_id seatId,
                   r.res_time resTime, r.v_status status, r.checkin_time checkinTime, r.checkout_time checkoutTime,
                   room.room_name roomName, seat.seat_no seatNo
            FROM reservation r
            LEFT JOIN room ON r.room_id=room.room_id
            LEFT JOIN seat ON r.seat_id=seat.seat_id
            WHERE r.user_id=#{userId} AND r.v_status IN (0,1)
            """)
    List<Reserve> myList(@Param("userId") Integer userId);

    @Select("""
            SELECT r.res_id resId, r.user_id userId, r.room_id roomId, r.seat_id seatId,
                   r.res_time resTime, r.v_status status, r.checkin_time checkinTime, r.checkout_time checkoutTime,
                   room.room_name roomName, seat.seat_no seatNo, u.student_id studentId, u.username
            FROM reservation r
            LEFT JOIN room ON r.room_id=room.room_id
            LEFT JOIN seat ON r.seat_id=seat.seat_id
            LEFT JOIN `user` u ON r.user_id=u.user_id
            WHERE r.v_status IN (0,1)
            ORDER BY r.res_time DESC
            """)
    List<Reserve> listAll();

    @Select("""
            SELECT r.res_id resId, r.user_id userId, r.room_id roomId, r.seat_id seatId,
                   r.res_time resTime, r.v_status status, r.checkin_time checkinTime, r.checkout_time checkoutTime,
                   room.room_name roomName, seat.seat_no seatNo, u.student_id studentId, u.username
            FROM reservation r
            LEFT JOIN room ON r.room_id=room.room_id
            LEFT JOIN seat ON r.seat_id=seat.seat_id
            LEFT JOIN `user` u ON r.user_id=u.user_id
            ORDER BY r.res_time DESC
            """)
    List<Reserve> listAllWithHistory();

    @Delete("DELETE FROM reservation WHERE res_id=#{resId} AND user_id=#{userId}")
    int cancel(@Param("resId") Integer resId, @Param("userId") Integer userId);

    // 签到
    @Update("UPDATE reservation SET v_status=1, checkin_time=NOW() WHERE res_id=#{resId} AND user_id=#{userId} AND v_status=0")
    int checkin(@Param("resId") Integer resId, @Param("userId") Integer userId);

    // 签退
    @Update("UPDATE reservation SET v_status=2, checkout_time=NOW() WHERE res_id=#{resId} AND user_id=#{userId} AND v_status=1")
    int checkout(@Param("resId") Integer resId, @Param("userId") Integer userId);

    // 一键续约：将checkout_time延长maxHours（仅针对已签到状态）
    @Update("UPDATE reservation SET checkout_time=DATE_ADD(NOW(), INTERVAL #{hours} HOUR) WHERE res_id=#{resId} AND user_id=#{userId} AND v_status=1")
    int renew(@Param("resId") Integer resId, @Param("userId") Integer userId, @Param("hours") Integer hours);

    // 获取当前有效的预约ID（未签到/已签到）
    @Select("SELECT res_id FROM reservation WHERE user_id=#{userId} AND v_status IN (0,1) LIMIT 1")
    Integer getActiveResId(@Param("userId") Integer userId);

    // 统计当前已签到人数
    @Select("SELECT COUNT(*) FROM reservation WHERE v_status=1")
    int countCheckedIn();

    // 峰值时段统计：按星期几×小时分组
    @Select("SELECT DAYOFWEEK(res_time) AS dayOfWeek, HOUR(res_time) AS hour, COUNT(*) AS cnt " +
            "FROM reservation " +
            "WHERE res_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
            "GROUP BY DAYOFWEEK(res_time), HOUR(res_time) " +
            "ORDER BY dayOfWeek, hour")
    List<Map<String, Object>> peakHours();

    @Select("""
            SELECT r.res_id resId, r.user_id userId, r.room_id roomId, r.seat_id seatId,
                   r.res_time resTime, r.v_status status, r.checkin_time checkinTime, r.checkout_time checkoutTime,
                   room.room_name roomName, seat.seat_no seatNo
            FROM reservation r
            LEFT JOIN room ON r.room_id=room.room_id
            LEFT JOIN seat ON r.seat_id=seat.seat_id
            WHERE r.seat_id=#{seatId} AND r.v_status IN (0,1)
            LIMIT 1
            """)
    Reserve findBySeatId(@Param("seatId") Integer seatId);
}