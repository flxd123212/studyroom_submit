package org.example.studyroom.controller;

import org.example.studyroom.entity.Message;
import org.example.studyroom.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageMapper messageMapper;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) Integer roomId) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        if (roomId != null) {
            map.put("data", messageMapper.listByRoom(roomId));
        } else {
            map.put("data", messageMapper.listGlobal());
        }
        return map;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Message message) {
        Map<String, Object> map = new HashMap<>();
        try {
            messageMapper.add(message);
            map.put("code", 1);
            map.put("msg", "留言成功");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "留言失败");
        }
        return map;
    }

    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id, @RequestParam Integer userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            int ok = messageMapper.deleteByIdAndUser(id, userId);
            if (ok > 0) {
                map.put("code", 1);
                map.put("msg", "删除成功");
            } else {
                map.put("code", 0);
                map.put("msg", "无权删除或留言不存在");
            }
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "删除失败");
        }
        return map;
    }

    @GetMapping("/replies/{parentId}")
    public Map<String, Object> replies(@PathVariable Integer parentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", messageMapper.listReplies(parentId));
        return map;
    }

    @PostMapping("/admin-delete/{id}")
    public Map<String, Object> adminDelete(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();
        try {
            messageMapper.delete(id);
            map.put("code", 1);
            map.put("msg", "已删除");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "删除失败");
        }
        return map;
    }
}
