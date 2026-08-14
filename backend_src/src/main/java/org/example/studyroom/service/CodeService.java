package org.example.studyroom.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码服务：生成、发送、校验
 * 未配置真实邮箱时，验证码会打印到控制台，方便开发调试
 */
@Service
public class CodeService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    // 存储验证码: email -> { code, expireTime }
    private final Map<String, CodeEntry> codeMap = new ConcurrentHashMap<>();
    private final Random random = new Random();

    private static final long CODE_TTL = 5 * 60 * 1000; // 5分钟

    @PostConstruct
    public void init() {
        if (mailSender == null || mailFrom == null || mailFrom.isEmpty() || "your_email@qq.com".equals(mailFrom)) {
            System.out.println("══════════════════════════════════════════════");
            System.out.println("【邮箱未配置】验证码将打印到控制台，请查看后端日志");
            System.out.println("══════════════════════════════════════════════");
        } else {
            System.out.println("✅ 邮件服务已就绪");
        }
    }

    /**
     * 生成并发送验证码
     * @param email 目标邮箱
     * @return 验证码（用于调试）
     */
    public String generateAndSend(String email) {
        // 生成6位验证码
        String code = String.format("%06d", random.nextInt(999999));

        // 存储
        codeMap.put(email, new CodeEntry(code, System.currentTimeMillis() + CODE_TTL));

        // 尝试发送邮件
        if (mailSender != null && mailFrom != null && !mailFrom.isEmpty() && !"your_email@qq.com".equals(mailFrom)) {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(mailFrom);
                msg.setTo(email);
                msg.setSubject("自习室预约系统 - 验证码");
                msg.setText("您的验证码是：" + code + "，有效期5分钟。如非本人操作，请忽略。");
                mailSender.send(msg);
                System.out.println("✅ 验证码已发送至 " + email);
            } catch (Exception e) {
                System.out.println("⚠️ 邮件发送失败（" + e.getMessage() + "），验证码已打印到控制台");
                System.out.println("📧 验证码 for " + email + " : " + code);
            }
        } else {
            // 打印到控制台
            System.out.println("══════════════════════════════════════════════");
            System.out.println("📧 验证码 for " + email + " : " + code);
            System.out.println("══════════════════════════════════════════════");
        }

        return code;
    }

    /**
     * 验证码校验（仅校验，不删除）
     * @param email 邮箱
     * @param code 验证码
     * @return true=通过
     */
    public boolean verify(String email, String code) {
        CodeEntry entry = codeMap.get(email);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expireTime) {
            codeMap.remove(email);
            return false;
        }
        return entry.code.equals(code);
    }

    /**
     * 验证通过后消费验证码（删除）
     */
    public void consumeCode(String email) {
        codeMap.remove(email);
    }

    private record CodeEntry(String code, long expireTime) {}
}
