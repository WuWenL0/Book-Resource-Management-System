package com.imau.brms.mapper;

import com.imau.brms.entity.Admin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface AdminMapper {

    @Insert("insert into db_admin(username,password,perms) values (#{username},#{password},#{perms})")
    void insert(Admin admin);

    @Delete("delete from db_admin where id = #{id}")
    void delete(Integer id);

    @Update("update db_admin set password = #{password} where id = #{id}")
    void changePassword(Integer id , String password);

    @Select("select * from db_admin")
    ArrayList<Admin> getAllAdmin();

    @Select("select * from db_admin where username = #{username}")
    Admin findByUserName(String username);

    @Select("select * from db_admin where id = #{id}")
    Admin findById(Integer id);
}
