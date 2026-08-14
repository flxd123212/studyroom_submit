package org.example.studyroom.controller;

import org.example.studyroom.entity.Reserve;
import org.example.studyroom.entity.Seat;
import org.example.studyroom.entity.User;
import org.example.studyroom.mapper.ReserveMapper;
import org.example.studyroom.mapper.SeatMapper;
import org.example.studyroom.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private ReserveMapper reserveMapper;

    /**
     * 获取所有用户
     */
    @GetMapping("/users")
    public Map<String, Object> listUsers() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", userMapper.listAll());
        return map;
    }

    /**
     * 拉黑用户
     */
    @PostMapping("/user/ban/{userId}")
    public Map<String, Object> banUser(@PathVariable Integer userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            userMapper.banUser(userId);
            map.put("code", 1);
            map.put("msg", "已拉黑该用户");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "操作失败");
        }
        return map;
    }

    /**
     * 解封用户
     */
    @PostMapping("/user/unban/{userId}")
    public Map<String, Object> unbanUser(@PathVariable Integer userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            userMapper.unbanUser(userId);
            map.put("code", 1);
            map.put("msg", "已解封该用户");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "操作失败");
        }
        return map;
    }

    /**
     * 管理员代预约：按学号为指定学生预约座位
     */
    @Transactional
    @PostMapping("/reserve")
    public Map<String, Object> adminReserve(@RequestParam Integer seatId, @RequestParam String studentId) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 1. 查找学生
            User user = userMapper.findByStudentId(studentId);
            if (user == null) {
                map.put("code", 0);
                map.put("msg", "未找到该学号的用户");
                return map;
            }
            // 2. 检查座位
            Seat seat = seatMapper.getBySeatId(seatId);
            if (seat == null) {
                map.put("code", 0);
                map.put("msg", "座位不存在");
                return map;
            }
            if (seat.getStatus() != 0) {
                map.put("code", 0);
                map.put("msg", "该座位当前不可预约（状态：" + getStatusText(seat.getStatus()) + "）");
                return map;
            }
            // 3. 检查学生是否已有预约
            int count = reserveMapper.countByUser(user.getId());
            if (count > 0) {
                map.put("code", 0);
                map.put("msg", "该学生已有预约，不可重复预约");
                return map;
            }
            // 4. 创建预约记录并更新座位
            Reserve reserve = new Reserve();
            reserve.setUserId(user.getId());
            reserve.setRoomId(seat.getRoomId());
            reserve.setSeatId(seatId);
            reserveMapper.add(reserve);
            seatMapper.updateStatus(seatId, user.getId());

            map.put("code", 1);
            map.put("msg", "管理员代预约成功，学生：" + user.getUsername() + "（" + studentId + "）");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "代预约失败：" + e.getMessage());
        }
        return map;
    }

    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "空闲";
            case 1: return "已预约";
            case 2: return "损坏";
            case 3: return "维护中";
            default: return "其他";
        }
    }
}
