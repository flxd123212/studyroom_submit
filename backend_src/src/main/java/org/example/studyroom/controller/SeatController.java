package org.example.studyroom.controller;
import org.example.studyroom.entity.Seat;
import org.example.studyroom.mapper.SeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/seat")
public class SeatController {
    @Autowired
    private SeatMapper seatMapper;

    @GetMapping("/list/{roomId}")
    public Map<String,Object> list(@PathVariable Integer roomId){
        Map<String,Object> map = new HashMap<>();
        List<Seat> list = seatMapper.getByRoom(roomId);
        map.put("code",1);
        map.put("data",list);
        return map;
    }

    // ========== 管理员：座位管理 ==========
    @GetMapping("/listAll")
    public Map<String,Object> listAll(){
        Map<String,Object> map = new HashMap<>();
        map.put("code",1);
        map.put("data", seatMapper.listAll());
        return map;
    }

    @PostMapping("/add")
    public Map<String,Object> add(@RequestParam Integer roomId, @RequestParam String seatNo){
        Map<String,Object> map = new HashMap<>();
        try {
            seatMapper.add(roomId, seatNo);
            map.put("code",1);
            map.put("msg","座位添加成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","添加失败，座位号可能已存在");
        }
        return map;
    }

    @PostMapping("/batchAdd")
    public Map<String,Object> batchAdd(@RequestBody Map<String,Object> params){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer roomId = (Integer) params.get("roomId");
            List<String> seatNos = (List<String>) params.get("seatNos");
            seatMapper.batchInsert(roomId, seatNos);
            map.put("code",1);
            map.put("msg","批量添加成功，共"+seatNos.size()+"个座位");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","批量添加失败");
        }
        return map;
    }

    @PostMapping("/delete/{seatId}")
    public Map<String,Object> delete(@PathVariable Integer seatId){
        Map<String,Object> map = new HashMap<>();
        try {
            seatMapper.delete(seatId);
            map.put("code",1);
            map.put("msg","删除成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","删除失败");
        }
        return map;
    }

    @PostMapping("/updateStatus")
    public Map<String,Object> updateStatus(@RequestParam Integer seatId, @RequestParam Integer status){
        Map<String,Object> map = new HashMap<>();
        try {
            seatMapper.updateStatusDirectly(seatId, status);
            map.put("code",1);
            map.put("msg","状态更新成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","状态更新失败");
        }
        return map;
    }

    @GetMapping("/available-counts")
    public Map<String,Object> getAvailableCounts(){
        Map<String,Object> map = new HashMap<>();
        map.put("code",1);
        map.put("data", seatMapper.countAvailableByRoom());
        return map;
    }

    @PostMapping("/updateFeatures")
    public Map<String,Object> updateFeatures(@RequestBody Map<String,Object> params){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer seatId = (Integer) params.get("seatId");
            String features = (String) params.get("features");
            seatMapper.updateFeatures(seatId, features);
            map.put("code",1);
            map.put("msg","特征更新成功");
        } catch (Exception e) {
            map.put("code",0);
            map.put("msg","特征更新失败");
        }
        return map;
    }
}