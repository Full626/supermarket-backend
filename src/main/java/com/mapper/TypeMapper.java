package com.mapper;

import com.domain.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TypeMapper {

    /**
     * 获取商品类别编号
     */
    @Select("SELECT typeId FROM type")
    List<String> searchAllTypes();

    /**
     * 获取商品类别名称
     */
    @Select("SELECT typeName FROM type")
    List<String> searchAllTypeNames();

    /**
     * 获取所有完整类别信息
     */
    @Select("SELECT typeId, typeName FROM type")
    List<Type> selectAllTypes();

    /**
     * 依据商品类别编号找类别名称
     */
    @Select("SELECT typeName FROM type WHERE typeId = #{typeId}")
    String searchTypeNameByTypeId(@Param("typeId") String typeId);

    /**
     * 依据商品名称找对应的商品编号
     */
    @Select("SELECT typeId FROM type WHERE typeName = #{typeName}")
    String searchTypeIdByTypeName(@Param("typeName") String typeName);

    /**
     * 新增商品编号和对应的名称
     */
    @Insert("INSERT INTO type (typeId, typeName) VALUES (#{typeId}, #{typeName})")
    void insertType(@Param("typeId") String typeId, @Param("typeName") String typeName);

    // ========== 新增方法 ==========

    /**
     * 更新类别名称
     */
    @Update("UPDATE type SET typeName = #{typeName} WHERE typeId = #{typeId}")
    int updateType(@Param("typeId") String typeId, @Param("typeName") String typeName);

    /**
     * 删除类别
     */
    @Delete("DELETE FROM type WHERE typeId = #{typeId}")
    int deleteType(@Param("typeId") String typeId);

    /**
     * 检查类别是否被商品引用
     */
    @Select("SELECT COUNT(*) FROM goods WHERE typeId = #{typeId}")
    int countGoodsByTypeId(@Param("typeId") String typeId);
}