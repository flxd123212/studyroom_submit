package org.example.studyroom.mapper;

import org.apache.ibatis.annotations.*;
import org.example.studyroom.entity.Announcement;
import java.util.List;

@Mapper
public interface AnnouncementMapper {

    @Select("SELECT id, title, content, priority, status, create_time createTime " +
            "FROM announcement WHERE status=1 ORDER BY priority DESC, create_time DESC")
    List<Announcement> listPublished();

    @Select("SELECT id, title, content, priority, status, create_time createTime " +
            "FROM announcement ORDER BY priority DESC, create_time DESC")
    List<Announcement> listAll();

    @Insert("INSERT INTO announcement(title, content, priority, status) VALUES(#{title}, #{content}, #{priority}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Announcement announcement);

    @Update("UPDATE announcement SET title=#{title}, content=#{content}, priority=#{priority}, " +
            "status=#{status} WHERE id=#{id}")
    int update(Announcement announcement);

    @Delete("DELETE FROM announcement WHERE id=#{id}")
    int delete(@Param("id") Integer id);
}
