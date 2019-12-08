package com.imau.brms.mapper;

import com.imau.brms.entity.Resource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface ResourseMapper {

    @Insert("INSERT INTO db_book_resourse ( book_id,res_name,res_src,res_type ) VALUES ( #{bookId},#{resName},#{resSrc},#{resType} )")
    void insert(Resource resource);

    @Select("SELECT * FROM db_book_resourse WHERE book_id = #{id}")
    ArrayList<Resource> findResourcesByBookId(Integer id);

    @Select("SELECT * FROM db_book_resourse WHERE res_id = #{resId}")
    Resource findResourceByResId(Integer resId);

    @Delete("DELETE FROM db_book_resourse WHERE res_id = #{resId}")
    void delete(Integer resId);

    @Select("SELECT * FROM db_book_resourse Order By down_sum Desc limit 0,6")
    ArrayList<Resource> getResourceDownSumSort();
}
