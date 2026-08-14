package org.example.studyroom.controller;

import org.example.studyroom.entity.ReservationRule;
import org.example.studyroom.mapper.ReservationRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/rule")
public class RuleController {

    @Autowired
    private ReservationRuleMapper ruleMapper;

    @GetMapping("/get")
    public Map<String, Object> getRule() {
        Map<String, Object> map = new HashMap<>();
        ReservationRule rule = ruleMapper.getRule();
        if (rule == null) {
            map.put("code", 0);
            map.put("msg", "未设置预约规则");
        } else {
            map.put("code", 1);
            map.put("data", rule);
        }
        return map;
    }

    @PostMapping("/update")
    public Map<String, Object> updateRule(@RequestBody ReservationRule rule) {
        Map<String, Object> map = new HashMap<>();
        try {
            int ok = ruleMapper.updateRule(rule);
            map.put("code", ok > 0 ? 1 : 0);
            map.put("msg", ok > 0 ? "保存成功" : "保存失败");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "保存失败：" + e.getMessage());
        }
        return map;
    }
}
