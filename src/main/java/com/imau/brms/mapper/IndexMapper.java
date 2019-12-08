package com.imau.brms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IndexMapper {

    @Select("SELECT COUNT(*) FROM db_book_info")
    int getBookCount();

    @Select("SELECT COUNT(*) FROM db_reader_card")
    int getReaderCount();

    @Select("SELECT IFNULL(SUM(down_sum), 0) FROM db_book_resourse")
    int getDownCount();

    @Select("SELECT COUNT(*) FROM db_notice")
    int getNoticeCount();
}
