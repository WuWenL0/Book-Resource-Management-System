package com.imau.brms.mapper;

import com.imau.brms.entity.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface NoticeMapper {

    @Insert("INSERT INTO db_notice ( name,context,creat_time ) VALUES ( #{name},#{context},#{creatTime} )")
    void insert(Notice notice);

    @Insert("UPDATE db_notice SET name = #{name} , context = #{context} WHERE id = ${id}")
    void update(Notice notice);

    @Delete("DELETE FROM db_notice WHERE id = #{id}")
    void delete(Integer id);

    @Select("SELECT * FROM db_notice")
    ArrayList<Notice> getAllNotice();

    @Select("SELECT * FROM db_notice WHERE id = #{id}")
    Notice findNoticeById(Integer id);

}
