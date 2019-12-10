package com.imau.brms.mapper;

import com.imau.brms.entity.Book;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface BookMapper {

    @Insert("INSERT INTO db_book_info(name,author,publish,pubdate,introduction,ISBN,class_id,pressmark,update_time) values ( #{name},#{author},#{publish},#{pubdate},#{introduction},#{isbn},#{classId},#{pressmark},#{updateTime} )")
    void insert(Book book);

    @Delete("DELETE FROM db_book_info WHERE book_id = #{id}")
    void delete(Integer id);

    @Update("UPDATE db_book_info SET name = #{name} , author = #{author} , publish = #{publish} , pubdate = #{pubdate} , introduction = #{introduction} , ISBN = #{isbn} , class_id = #{classId} , pressmark = #{pressmark}   WHERE book_id = #{bookId}")
    void update(Book book);

    @Select("SELECT * FROM db_book_info")
    ArrayList<Book> getAllBooks();

    @Select("SELECT * FROM db_book_info WHERE book_id = #{id}")
    Book findBookById(Integer id);

    @Select("SELECT * FROM `db_book_info` WHERE book_id >= ( SELECT FLOOR( RAND() * (SELECT MAX(book_id) FROM `db_book_info`))) ORDER BY book_id LIMIT 6")
    ArrayList<Book> getRecommendBooks();

    @Select("SELECT * FROM db_book_info WHERE ${searchType} LIKE #{searchWord}")
    ArrayList<Book> queryBook(String searchType , String searchWord);

    @Select("SELECT COUNT(*) FROM db_book_info WHERE ${searchType} LIKE #{searchWord}")
    Integer queryBookSum(String searchType , String searchWord);
}
