package com.imau.brms.mapper;

import com.imau.brms.entity.ReaderCard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface ReaderMapper {

    @Insert("INSERT INTO db_reader_card (reader_id,name,passwd) values ( #{readerId},#{name},#{passwd} )")
    void insertCard(ReaderCard readerCard);

    @Insert("INSERT INTO db_reader_info (reader_id,name) values ( #{readerId},#{name} )")
    void insertInfo(ReaderCard readerCard);

    @Update("DELETE FROM db_reader_card WHERE reader_id = #{id}")
    void deleteCard(Long id);

    @Update("DELETE FROM db_reader_info WHERE reader_id = #{id}")
    void deleteInfo(Long id);

    @Select("SELECT reader_id,name FROM db_reader_card")
    ArrayList<ReaderCard> selectAllReaderCard();


}
