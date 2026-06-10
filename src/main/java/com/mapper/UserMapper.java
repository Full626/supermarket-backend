package com.mapper;

import com.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 根据用户ID查询用户（包含密码用于MD5验证）
     */
    @Select("SELECT userId, pwd, userName, phone, idCard, role FROM user WHERE userId = #{userId}")
    User findByUserId(@Param("userId") String userId);

    /**
     * 查询所有用户（不包含密码）
     */
    @Select("SELECT userId, userName, phone, idCard, role FROM user")
    List<User> findAll();

    /**
     * 新增用户
     */
    @Insert("INSERT INTO user (userId, pwd, userName, phone, idCard, role) " +
            "VALUES (#{userId}, #{pwd}, #{userName}, #{phone}, #{idCard}, #{role})")
    int insertUser(User user);

    /**
     * 更新用户信息
     */
    @Update("<script>" +
            "UPDATE user SET " +
            "<if test='userName != null'>userName = #{userName},</if>" +
            "<if test='phone != null'>phone = #{phone},</if>" +
            "<if test='idCard != null'>idCard = #{idCard},</if>" +
            "<if test='pwd != null'>pwd = #{pwd},</if>" +
            "<if test='role != null'>role = #{role},</if>" +
            "userId = #{userId} " +
            "WHERE userId = #{userId}" +
            "</script>")
    int updateUser(User user);

    /**
     * 删除用户
     */
    @Delete("DELETE FROM user WHERE userId = #{userId}")
    int deleteUser(@Param("userId") String userId);

    /**
     * 检查用户是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE userId = #{userId}")
    int countByUserId(@Param("userId") String userId);
}