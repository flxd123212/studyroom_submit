package org.example.studyroom.mapper;
import org.apache.ibatis.annotations.*;
import org.example.studyroom.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO `user`(student_id,username,password) VALUES(#{studentId},#{username},#{password})")
    void register(User user);

    @Select("SELECT user_id AS id, student_id AS studentId, username, password, role, email, status FROM `user` WHERE student_id=#{studentId}")
    User findByStudentId(@Param("studentId") String studentId);

    @Select("SELECT user_id AS id, student_id AS studentId, username, password, role, email, status FROM `user` WHERE user_id=#{userId}")
    User findByUserId(@Param("userId") Integer userId);

    @Select("SELECT status FROM `user` WHERE user_id=#{userId}")
    Integer checkStatus(@Param("userId") Integer userId);

    @Update("UPDATE `user` SET password=#{password} WHERE student_id=#{studentId}")
    void updatePassword(@Param("studentId") String studentId, @Param("password") String password);

    @Select("SELECT user_id AS id, student_id AS studentId, username, password, role, email, status FROM `user` WHERE email=#{email}")
    User findByEmail(@Param("email") String email);

    @Insert("INSERT INTO `user`(student_id,username,password,email) VALUES(#{studentId},#{username},#{password},#{email})")
    void registerWithEmail(User user);

    @Update("UPDATE `user` SET email=#{email} WHERE user_id=#{userId}")
    void updateEmail(@Param("userId") Integer userId, @Param("email") String email);

    @Select("SELECT user_id AS id, student_id AS studentId, username, password, role, email, status FROM `user` ORDER BY user_id DESC")
    List<User> listAll();

    @Update("UPDATE `user` SET status=1 WHERE user_id=#{userId}")
    void banUser(@Param("userId") Integer userId);

    @Update("UPDATE `user` SET status=0 WHERE user_id=#{userId}")
    void unbanUser(@Param("userId") Integer userId);
}