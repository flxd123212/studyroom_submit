package org.example.studyroom.controller;

import org.example.studyroom.mapper.ReserveMapper;
import org.example.studyroom.mapper.RoomMapper;
import org.example.studyroom.mapper.SeatMapper;
import org.example.studyroom.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private ReserveMapper reserveMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> map = new HashMap<>();

        // 总自习室数
        try { map.put("totalRooms", roomMapper.list().size()); } catch (Exception e) { map.put("totalRooms", 0); }

        // 总座位数
        try { map.put("totalSeats", seatMapper.listAll().size()); } catch (Exception e) { map.put("totalSeats", 0); }

        // 总用户数
        try { map.put("totalUsers", userMapper.listAll().size()); } catch (Exception e) { map.put("totalUsers", 0); }

        // 今日预约数（按预约时间统计）
        try { map.put("todayReserves", reserveMapper.listAll().size()); } catch (Exception e) { map.put("todayReserves", 0); }

        // 当前签到人数（status=1 已签到）
        try { map.put("checkedIn", reserveMapper.countCheckedIn()); } catch (Exception e) { map.put("checkedIn", 0); }

        // 可用座位数
        try {
            var counts = seatMapper.countAvailableByRoom();
            int avail = 0;
            for (var c : counts) {
                Object val = c.get("availableCount");
                if (val instanceof Number) avail += ((Number) val).intValue();
            }
            map.put("availableSeats", avail);
        } catch (Exception e) { map.put("availableSeats", 0); }

        map.put("code", 1);
        return map;
    }

    @GetMapping("/peak-hours")
    public Map<String, Object> peakHours() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> raw = reserveMapper.peakHours();
            // 转换为前端需要的格式
            List<int[]> result = new ArrayList<>();
            for (Map<String, Object> row : raw) {
                int dayOfWeek = ((Number) row.get("dayOfWeek")).intValue();
                int hour = ((Number) row.get("hour")).intValue();
                int cnt = ((Number) row.get("cnt")).intValue();
                // MySQL DAYOFWEEK: 1=Sunday, 2=Monday ... 7=Saturday
                // 前端期望 0=Monday...6=Sunday
                int d = (dayOfWeek + 5) % 7;
                if (hour >= 8 && hour <= 21) {
                    result.add(new int[]{d, hour - 8, cnt});
                }
            }
            map.put("code", 1);
            map.put("data", result);
        } catch (Exception e) {
            map.put("code", 0);
            map.put("data", new ArrayList<>());
        }
        return map;
    }
}
