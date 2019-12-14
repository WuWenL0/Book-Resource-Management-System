package com.imau.brms.mapper;

import com.imau.brms.entity.Horse;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Mapper
@Repository
public interface HorseMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO db_sports_horse(name, author, grade, content, time) VALUES (#{name},#{author},#{grade},#{content},#{time})")
    void insertInfo(Horse horse);

    @Insert("UPDATE db_sports_horse SET file1_src = #{file1Src}, file2_src = #{file2Src}, file3_src = #{file3Src} WHERE id = #{id}")
    void insertFile(Horse horse);

    @Select("SELECT * FROM db_sports_horse")
    ArrayList<Horse> getAllhorse();

    @Select("SELECT * FROM db_sports_horse WHERE id = #{id}")
    Horse findHorseById(Integer id);

    @Update("UPDATE db_sports_horse SET name = #{name}, author = #{author}, grade = #{grade}, content = #{content}, time = #{time} WHERE id = #{id}")
    void updateInfo(Horse horse);

    @Insert("UPDATE db_sports_horse SET ${fileName} = #{fileSrc} WHERE id = #{id}")
    void updateFile(String fileName , String fileSrc , Integer id);

    @Select("SELECT * FROM db_sports_horse WHERE ${searchType} LIKE #{searchWordUp}")
    ArrayList<Horse> queryHorse(String searchType, String searchWordUp);

    @Select("SELECT COUNT(*) FROM db_sports_horse WHERE ${searchType} LIKE #{searchWordUp}")
    Integer queryHorseSum(String searchType, String searchWordUp);
}
