package org.example.studyroom.controller;
import org.example.studyroom.entity.Room;
import org.example.studyroom.mapper.RoomMapper;
import org.example.studyroom.mapper.SeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SeatMapper seatMapper;

    @GetMapping("/list")
    public Map<String,Object> list(){
        Map<String,Object> map = new HashMap<>();
        List<Room> list = roomMapper.list();
        map.put("code",1);
        map.put("data",list);
        return map;
    }

    // ========== 管理员：自习室管理 ==========

    @PostMapping("/add")
    public Map<String,Object> add(@RequestBody Room room){
        Map<String,Object> map = new HashMap<>();
        try {
            roomMapper.add(room);
            map.put("code",1);
            map.put("msg","添加成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","添加失败");
        }
        return map;
    }

    @PostMapping("/update")
    public Map<String,Object> update(@RequestBody Room room){
        Map<String,Object> map = new HashMap<>();
        try {
            roomMapper.update(room);
            map.put("code",1);
            map.put("msg","更新成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","更新失败");
        }
        return map;
    }

    @PostMapping("/delete/{roomId}")
    public Map<String,Object> delete(@PathVariable Integer roomId){
        Map<String,Object> map = new HashMap<>();
        try {
            seatMapper.deleteByRoom(roomId);
            roomMapper.delete(roomId);
            map.put("code",1);
            map.put("msg","删除成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","删除失败");
        }
        return map;
    }
}