package org.example.studyroom.mapper;

import org.apache.ibatis.annotations.*;
import org.example.studyroom.entity.Message;
import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("SELECT m.id, m.user_id userId, m.room_id roomId, m.content, m.reply_to replyTo, " +
            "m.create_time createTime, u.username, u.student_id studentId " +
            "FROM `message` m LEFT JOIN `user` u ON m.user_id = u.user_id " +
            "WHERE m.room_id IS NULL " +
            "ORDER BY m.create_time DESC")
    List<Message> listGlobal();

    @Select("SELECT m.id, m.user_id userId, m.room_id roomId, m.content, m.reply_to replyTo, " +
            "m.create_time createTime, u.username, u.student_id studentId " +
            "FROM `message` m LEFT JOIN `user` u ON m.user_id = u.user_id " +
            "WHERE m.room_id = #{roomId} " +
            "ORDER BY m.create_time DESC")
    List<Message> listByRoom(@Param("roomId") Integer roomId);

    @Insert("INSERT INTO `message`(user_id, room_id, content, reply_to) VALUES(#{userId}, #{roomId}, #{content}, #{replyTo})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Message message);

    @Delete("DELETE FROM `message` WHERE id=#{id} AND user_id=#{userId}")
    int deleteByIdAndUser(@Param("id") Integer id, @Param("userId") Integer userId);

    @Delete("DELETE FROM `message` WHERE id=#{id}")
    int delete(@Param("id") Integer id);

    @Select("SELECT m.id, m.user_id userId, m.room_id roomId, m.content, m.reply_to replyTo, " +
            "m.create_time createTime, u.username, u.student_id studentId " +
            "FROM `message` m LEFT JOIN `user` u ON m.user_id = u.user_id " +
            "WHERE m.reply_to = #{parentId} " +
            "ORDER BY m.create_time ASC")
    List<Message> listReplies(@Param("parentId") Integer parentId);
}
