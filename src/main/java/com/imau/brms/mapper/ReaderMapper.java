package com.imau.brms.mapper;

import com.imau.brms.entity.ReaderCard;
import com.imau.brms.entity.ReaderInfo;
import lombok.experimental.Delegate;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ReaderMapper {

    @Insert("INSERT INTO db_reader_card (reader_id,name,passwd) values ( #{readerId},#{name},#{passwd} )")
    Integer insertCard(ReaderCard readerCard);

    @Insert("INSERT INTO db_reader_info (reader_id,name,department,major,grade,class_name) values ( #{readerId},#{name},#{department},#{major},#{grade},#{className} )")
    Integer insertInfo(ReaderInfo readerInfo);

    @Delete("DELETE FROM db_reader_card WHERE reader_id = #{id}")
    void deleteCard(Long id);

    @Delete("DELETE FROM db_reader_info WHERE reader_id = #{id}")
    void deleteInfo(Long id);

    @Update("UPDATE db_reader_info SET name = #{name} , department = #{department} , major = #{major} , grade = #{grade} , class_name = #{className} WHERE reader_id = #{readerId}")
    Integer updateReaderInfo(ReaderInfo readerInfo);

    @Update("UPDATE db_reader_card SET passwd = #{passwd} WHERE reader_id = #{readerId}")
    void changeReaderPassword(Long readerId , String passwd);

    @Select("SELECT reader_id,name FROM db_reader_card")
    ArrayList<ReaderCard> selectAllReaderCard();

    @Select("SELECT * FROM db_reader_info")
    ArrayList<ReaderInfo> selectAllReaderInfo();

    @Select("SELECT COUNT(*) FROM db_reader_card WHERE reader_id = #{readerId} and passwd = #{passwd}")
    Integer hasMatchReader(Long readerId , String passwd);

    @Select("SELECT * FROM db_reader_card WHERE reader_id = #{readerId}")
    ReaderCard findReaderCardByUserId(Long id);

    @Select("SELECT * FROM db_reader_info WHERE reader_id = #{readerId}")
    ReaderInfo findReaderInfoByReaderId(Long readerId);

    @Update("UPDATE db_reader_card SET name = #{name} WHERE reader_id = #{readerId}")
    void updateReaderCardName(ReaderCard reader);

    @Select("SELECT COUNT(*) FROM db_reader_card WHERE reader_id = #{readerId}")
    int selectReaderCard(Long readerId);

    @Select("SELECT COUNT(*) FROM db_reader_info WHERE reader_id = #{readerId}")
    int selectReaderInfo(Long readerId);

    @Update("UPDATE db_reader_card SET name = #{name} , passwd = #{passwd} WHERE reader_id = #{readerId}")
    int updateReaderCard(ReaderCard readerCard);

    @Delete({
            "<script>",
            "DELETE FROM ${delName} WHERE reader_id in (",
            "<foreach collection='readerIds' item='item' index='index' separator=','>",
            "#{item}",
            "</foreach>", ")</script>"
    })
    int batchDelete(String delName , String[] readerIds);

}
