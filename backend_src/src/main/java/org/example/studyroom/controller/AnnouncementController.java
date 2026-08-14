package org.example.studyroom.controller;

import org.example.studyroom.entity.Announcement;
import org.example.studyroom.mapper.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", announcementMapper.listPublished());
        return map;
    }

    @GetMapping("/listAll")
    public Map<String, Object> listAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("data", announcementMapper.listAll());
        return map;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Announcement announcement) {
        Map<String, Object> map = new HashMap<>();
        try {
            announcementMapper.add(announcement);
            map.put("code", 1);
            map.put("msg", "添加成功");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "添加失败");
        }
        return map;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Announcement announcement) {
        Map<String, Object> map = new HashMap<>();
        try {
            announcementMapper.update(announcement);
            map.put("code", 1);
            map.put("msg", "更新成功");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "更新失败");
        }
        return map;
    }

    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();
        try {
            announcementMapper.delete(id);
            map.put("code", 1);
            map.put("msg", "删除成功");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "删除失败");
        }
        return map;
    }
}
