package com.imau.brms.mapper;

import com.imau.brms.entity.ReaderCard;
import com.imau.brms.entity.ReaderInfo;
import lombok.experimental.Delegate;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface ReaderMapper {

    @Insert("INSERT INTO db_reader_card (reader_id,name,passwd) values ( #{readerId},#{name},#{passwd} )")
    void insertCard(ReaderCard readerCard);

    @Insert("INSERT INTO db_reader_info (reader_id,name) values ( #{readerId},#{name} )")
    void insertInfo(ReaderCard readerCard);

    @Delete("DELETE FROM db_reader_card WHERE reader_id = #{id}")
    void deleteCard(Long id);

    @Delete("DELETE FROM db_reader_info WHERE reader_id = #{id}")
    void deleteInfo(Long id);

    @Update("UPDATE db_reader_info SET name = #{name} , sex = #{sex} , birth = #{birth} , address = #{address} , telcode = #{telcode} WHERE reader_id = #{readerId}")
    void updateReaderInfo(ReaderInfo readerInfo);

    @Update("UPDATE db_reader_card SET passwd = #{passwd} WHERE reader_id = #{readerId}")
    void changeReaderPassword(Long readerId , String passwd);

    @Select("SELECT reader_id,name FROM db_reader_card")
    ArrayList<ReaderCard> selectAllReaderCard();

    @Select("SELECT COUNT(*) FROM db_reader_card WHERE reader_id = #{readerId} and passwd = #{passwd}")
    Integer hasMatchReader(Long readerId , String passwd);

    @Select("SELECT * FROM db_reader_card WHERE reader_id = #{readerId}")
    ReaderCard findReaderCardByUserId(Long id);

    @Select("SELECT * FROM db_reader_info WHERE reader_id = #{readerId}")
    ReaderInfo findReaderInfoByReaderId(Long readerId);
}
