package org.example.studyroom.controller;

import org.example.studyroom.entity.Reserve;
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
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private ReserveMapper reserveMapper;
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Reserve reserve) {
        Map<String, Object> map = new HashMap<>();

        // 防null报错
        if (reserve.getUserId() == null) {
            map.put("code", 0);
            map.put("msg", "请登录");
            return map;
        }

        // === 拉黑校验 ===
        Integer status = userMapper.checkStatus(reserve.getUserId());
        if (status != null && status == 1) {
            map.put("code", 0);
            map.put("msg", "您的账号已被拉黑，无法预约");
            return map;
        }

        // 一人一座
        int count = reserveMapper.countByUser(reserve.getUserId());
        if (count > 0) {
            map.put("code", 0);
            map.put("msg", "您已预约座位，不可重复预约！");
            return map;
        }

        reserveMapper.add(reserve);
        seatMapper.updateStatus(reserve.getSeatId(), reserve.getUserId());

        map.put("code", 1);
        map.put("msg", "预约成功");
        return map;
    }

    @GetMapping("/myList")
    public Map<String, Object> myList(@RequestParam Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", reserveMapper.myList(userId));
        return map;
    }

    @GetMapping("/myHistory")
    public Map<String, Object> myHistory(@RequestParam Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", reserveMapper.listAllWithHistory());
        return map;
    }

    @GetMapping("/listAll")
    public Map<String, Object> listAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", reserveMapper.listAll());
        return map;
    }

    @GetMapping("/listAllWithHistory")
    public Map<String, Object> listAllWithHistory() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", reserveMapper.listAllWithHistory());
        return map;
    }

    @PostMapping("/cancel")
    public Map<String, Object> cancel(
            @RequestParam Integer resId,
            @RequestParam Integer userId,
            @RequestParam Integer seatId
    ) {
        Map<String, Object> map = new HashMap<>();
        int ok = reserveMapper.cancel(resId, userId);
        if (ok > 0) {
            seatMapper.updateStatus0(seatId);
            map.put("code", 1);
            map.put("msg", "取消成功");
        } else {
            map.put("code", 0);
            map.put("msg", "取消失败");
        }
        return map;
    }

    // ========== 签到 ==========
    @PostMapping("/checkin")
    public Map<String, Object> checkin(@RequestParam Integer resId, @RequestParam Integer userId) {
        Map<String, Object> map = new HashMap<>();
        int ok = reserveMapper.checkin(resId, userId);
        if (ok > 0) {
            map.put("code", 1);
            map.put("msg", "签到成功！");
        } else {
            map.put("code", 0);
            map.put("msg", "签到失败，请确认该预约处于未签到状态");
        }
        return map;
    }

    // ========== 签退 ==========
    @PostMapping("/checkout")
    public Map<String, Object> checkout(@RequestParam Integer resId, @RequestParam Integer userId, @RequestParam Integer seatId) {
        Map<String, Object> map = new HashMap<>();
        int ok = reserveMapper.checkout(resId, userId);
        if (ok > 0) {
            seatMapper.updateStatus0(seatId);
            map.put("code", 1);
            map.put("msg", "签退成功，座位已释放");
        } else {
            map.put("code", 0);
            map.put("msg", "签退失败，请确认已签到");
        }
        return map;
    }

    // ========== 一键续约 ==========
    @PostMapping("/renew")
    public Map<String, Object> renew(@RequestParam Integer resId, @RequestParam Integer userId, @RequestParam Integer hours) {
        Map<String, Object> map = new HashMap<>();
        int ok = reserveMapper.renew(resId, userId, hours);
        if (ok > 0) {
            map.put("code", 1);
            map.put("msg", "续约成功，延长" + hours + "小时");
        } else {
            map.put("code", 0);
            map.put("msg", "续约失败，请确认已签到");
        }
        return map;
    }
}