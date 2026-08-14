package org.example.studyroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ShutdownController {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 安全关闭后端服务
     * 调用后延迟 500ms 执行退出，让响应先返回给前端
     */
    @PostMapping("/api/shutdown")
    public Map<String, Object> shutdown() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // ignore
            }
            int exitCode = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exitCode);
        }).start();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "服务正在关闭");
        return map;
    }
}
