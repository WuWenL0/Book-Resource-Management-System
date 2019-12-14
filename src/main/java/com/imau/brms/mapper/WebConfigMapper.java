package com.imau.brms.mapper;

import com.imau.brms.entity.Horse;
import com.imau.brms.entity.WebConfig;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WebConfigMapper {
    @Insert({"insert into db_web_config (" +
            "web_name,title,logo_img,bg_img,copyright,icp,file1_name,file2_name,file3_name) values(" +
            " #{webName},#{title},#{logoImg,jdbcType=BLOB},#{bgImg,jdbcType=BLOB},#{copyright},#{icp},#{file1Name},#{file2Name},#{file3Name} )"})
    void insertWebConfig(WebConfig webConfig);

    @Update("UPDATE db_web_config SET web_name = #{webName} , title = #{title} , " +
            "copyright = #{copyright} , icp = #{icp} ,file1_name = #{file1Name} , file2_name = #{file2Name} , file3_name = #{file3Name} WHERE web_name = #{webName} ")
    void updateWebConfig(WebConfig webConfig);

    @Update("UPDATE db_web_config SET logo_img = #{logoImg,jdbcType=BLOB} WHERE web_name = #{webName} ")
    void updateWebLogoImg(WebConfig webConfig);

    @Update("UPDATE db_web_config SET bg_img = #{bgImg,jdbcType=BLOB} WHERE web_name = #{webName} ")
    void updateWebbgImg(WebConfig webConfig);

    @Select("SELECT COUNT(*) FROM db_web_config WHERE web_name = #{webName}")
    Integer hasWebConfig(String webName);

    @Select("SELECT * FROM db_web_config WHERE web_name = #{webName}")
    WebConfig findWebConfigByWebName(String webName);

}
