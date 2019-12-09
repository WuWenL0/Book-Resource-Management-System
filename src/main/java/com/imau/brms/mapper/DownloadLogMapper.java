package com.imau.brms.mapper;

import com.imau.brms.entity.DownloadLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DownloadLogMapper {

    @Insert("INSERT INTO db_download_log(reader_id, ip, book_id, book_name, res_name, download_time) VALUES (#{readerId},#{ip},#{bookId},#{bookName},#{resName},#{downloadTime})")
    void insert(DownloadLog downloadLog);

    @Select("SELECT * FROM db_download_log")
    ArrayList<DownloadLog> selectAllLog();

}
