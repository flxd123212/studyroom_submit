package org.example.studyroom.controller;
import org.example.studyroom.entity.User;
import org.example.studyroom.mapper.UserMapper;
import org.example.studyroom.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CodeService codeService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        User loginUser = userMapper.findByStudentId(user.getStudentId());
        if (loginUser == null || !checkPassword(user.getPassword(), loginUser.getPassword())) {
            map.put("code", 0);
            map.put("msg", "学号或密码错误");
        } else if (loginUser.getStatus() != null && loginUser.getStatus() == 1) {
            map.put("code", 0);
            map.put("msg", "您的账号已被拉黑，无法登录");
        } else {
            map.put("code", 1);
            map.put("data", loginUser);
        }
        return map;
    }

    @PostMapping("/admin-login")
    public Map<String, Object> adminLogin(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        User loginUser = userMapper.findByStudentId(user.getStudentId());
        if (loginUser == null || loginUser.getRole() != 1 || !checkPassword(user.getPassword(), loginUser.getPassword())) {
            map.put("code", 0);
            map.put("msg", "管理员账号或密码错误");
        } else {
            map.put("code", 1);
            map.put("data", loginUser);
        }
        return map;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        try {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userMapper.register(user);
            map.put("code", 1);
            map.put("msg", "注册成功");
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "学号已存在");
        }
        return map;
    }

    /**
     * 发送注册验证码（验证邮箱是否已被使用）
     */
    @PostMapping("/register-send-code")
    public Map<String, Object> registerSendCode(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String email = params.get("email");

        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            map.put("code", 0);
            map.put("msg", "邮箱格式不正确");
            return map;
        }

        // 检查邮箱是否已被绑定
        User existing = userMapper.findByEmail(email);
        if (existing != null) {
            map.put("code", 0);
            map.put("msg", "该邮箱已被其他账号绑定");
            return map;
        }

        codeService.generateAndSend(email);
        map.put("code", 1);
        map.put("msg", "验证码已发送");
        return map;
    }

    /**
     * 注册（带邮箱和验证码校验）
     */
    @PostMapping("/register-with-email")
    public Map<String, Object> registerWithEmail(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String studentId = params.get("studentId");
        String username = params.get("username");
        String password = params.get("password");
        String email = params.get("email");
        String code = params.get("code");

        // 校验验证码
        if (!codeService.verify(email, code)) {
            map.put("code", 0);
            map.put("msg", "验证码错误或已过期");
            return map;
        }

        try {
            User user = new User();
            user.setStudentId(studentId);
            user.setUsername(username);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setEmail(email);
            userMapper.registerWithEmail(user);
            codeService.consumeCode(email);
            map.put("code", 1);
            map.put("msg", "注册成功");
        } catch (Exception e) {
            // 学号或邮箱重复
            String msg = e.getMessage();
            if (msg != null && msg.contains("student_id")) {
                map.put("code", 0);
                map.put("msg", "学号已存在");
            } else if (msg != null && msg.contains("email")) {
                map.put("code", 0);
                map.put("msg", "该邮箱已被绑定");
            } else {
                map.put("code", 0);
                map.put("msg", "注册失败，请重试");
            }
        }
        return map;
    }

    /**
     * 发送重置密码验证码
     */
    @PostMapping("/send-code")
    public Map<String, Object> sendCode(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String studentId = params.get("studentId");
        String email = params.get("email");

        // 校验学号是否存在
        User user = userMapper.findByStudentId(studentId);
        if (user == null) {
            map.put("code", 0);
            map.put("msg", "该学号不存在");
            return map;
        }

        // 校验邮箱是否匹配
        if (user.getEmail() == null || !user.getEmail().equals(email)) {
            map.put("code", 0);
            map.put("msg", "绑定的邮箱不匹配");
            return map;
        }

        codeService.generateAndSend(email);
        map.put("code", 1);
        map.put("msg", "验证码已发送");
        return map;
    }

    /**
     * 重置密码
     */
    @Transactional
    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String studentId = params.get("studentId");
        String email = params.get("email");
        String code = params.get("code");
        String newPassword = params.get("newPassword");

        // 校验验证码（仅校验，不删除）
        if (!codeService.verify(email, code)) {
            map.put("code", 0);
            map.put("msg", "验证码错误或已过期");
            return map;
        }

        try {
            // 更新密码
            userMapper.updatePassword(studentId, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            codeService.consumeCode(email);
            map.put("code", 1);
            map.put("msg", "密码重置成功");
        } catch (Exception e) {
            log.error("重置密码失败, studentId={}", studentId, e);
            map.put("code", 0);
            map.put("msg", "系统错误，重置失败，请重新获取验证码后重试");
        }
        return map;
    }

    /**
     * 发送绑定邮箱验证码
     */
    @PostMapping("/send-bind-code")
    public Map<String, Object> sendBindCode(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String email = params.get("email");
        String userIdStr = params.get("userId");

        // 检查邮箱是否已被其他账号绑定
        // 简单检查：从user表查，如果有相同email的用户则拒绝
        // 这里简化处理，直接发码
        codeService.generateAndSend(email);
        map.put("code", 1);
        map.put("msg", "验证码已发送");
        return map;
    }

    /**
     * 绑定邮箱
     */
    @PostMapping("/bind-email")
    public Map<String, Object> bindEmail(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String email = params.get("email");
        String code = params.get("code");
        Integer userId = Integer.valueOf(params.get("userId"));

        // 校验验证码（仅校验，不删除）
        if (!codeService.verify(email, code)) {
            map.put("code", 0);
            map.put("msg", "验证码错误或已过期");
            return map;
        }

        try {
            // 绑定邮箱
            userMapper.updateEmail(userId, email);
            codeService.consumeCode(email);
            map.put("code", 1);
            map.put("msg", "邮箱绑定成功");
        } catch (Exception e) {
            log.error("绑定邮箱失败, userId={}", userId, e);
            map.put("code", 0);
            map.put("msg", "系统错误，绑定失败，请重新获取验证码后重试");
        }
        return map;
    }

    /**
     * 检查用户是否被拉黑（学生端轮询用）
     */
    @GetMapping("/check-status/{userId}")
    public Map<String, Object> checkUserStatus(@PathVariable Integer userId) {
        Map<String, Object> map = new HashMap<>();
        Integer status = userMapper.checkStatus(userId);
        if (status != null && status == 1) {
            map.put("code", 0);
            map.put("msg", "您的账号已被拉黑");
            map.put("status", 1);
        } else {
            map.put("code", 1);
            map.put("status", 0);
        }
        return map;
    }

    /**
     * 获取用户信息（检查邮箱状态）
     */
    @GetMapping("/info/{userId}")
    public Map<String, Object> getUserInfo(@PathVariable Integer userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            map.put("code", 0);
            map.put("msg", "用户不存在");
        } else {
            map.put("code", 1);
            map.put("data", user);
        }
        return map;
    }

    /**
     * 密码校验（兼容 BCrypt 哈希和旧版明文）
     */
    private boolean checkPassword(String rawPassword, String storedPassword) {
        if (storedPassword != null && storedPassword.startsWith("$")) {
            return BCrypt.checkpw(rawPassword, storedPassword);
        }
        return rawPassword.equals(storedPassword);
    }
}