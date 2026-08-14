package org.example.studyroom.entity;

public class User {
    private Integer id;
    private String studentId;
    private String username;
    private String password;
    private Integer role;   // 0=学生 1=管理员
    private String email;
    private Integer status; // 0=正常 1=拉黑

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}