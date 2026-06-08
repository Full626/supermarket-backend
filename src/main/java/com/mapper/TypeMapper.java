package com.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    @Insert("<script>"+
            "INSERT type (typeId,typeName)"+
            " VALUES (#{typeId},#{typeName}) "+
            "</script>")
    void insertType(@Param("typeId") String typeId, @Param("typeName") String typeName);
}
