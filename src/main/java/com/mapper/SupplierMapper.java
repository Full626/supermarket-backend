package com.mapper;

import com.domain.Supplier;
import com.dto.request.SupplierQueryDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SupplierMapper {

    /**
     * 获取所有的供应商id（修复：String 类型）
     */
    @Select("SELECT supplyId FROM supplier")
    List<String> searchSupplierIds();  // 修复：Integer → String

    /**
     * 获取所有供应商完整信息
     */
    @Select("SELECT supplyId, supplyName, phone, address FROM supplier")
    List<Supplier> searchSuppliers();

    /**
     * 根据供应商ID查询单个供应商
     */
    @Select("SELECT supplyId, supplyName, phone, address FROM supplier WHERE supplyId = #{supplyId}")
    Supplier selectBySupplyId(@Param("supplyId") String supplyId);

    /**
     * 插入一个新的供应商
     */
    @Insert("INSERT INTO supplier (supplyId, supplyName, phone, address) " +
            "VALUES (#{supplyId}, #{supplyName}, #{phone}, #{address})")
    void insertSupplier(Supplier supplier);

    /**
     * 更新供应商名字
     */
    @Update("UPDATE supplier SET supplyName = #{supplyName} WHERE supplyId = #{supplyId}")
    void updateSupplyName(@Param("supplyName") String supplyName,
                          @Param("supplyId") String supplyId);

    /**
     * 更新供应商电话
     */
    @Update("UPDATE supplier SET phone = #{phone} WHERE supplyId = #{supplyId}")
    void updateSupplyPhone(@Param("phone") String phone,
                           @Param("supplyId") String supplyId);

    /**
     * 更新供应商地址
     */
    @Update("UPDATE supplier SET address = #{address} WHERE supplyId = #{supplyId}")
    void updateSupplyAddress(@Param("address") String address,
                             @Param("supplyId") String supplyId);

    /**
     * 删除供应商
     */
    @Delete("DELETE FROM supplier WHERE supplyId = #{supplyId}")
    void deleteSupplier(@Param("supplyId") String supplyId);

    /**
     * 条件查询供应商
     */
    @Select("<script>" +
            "SELECT supplyId, supplyName, phone, address FROM supplier WHERE 1=1 " +
            "<if test='supplyId != null and supplyId != \"\"'>" +
            " AND supplyId LIKE CONCAT('%', #{supplyId}, '%')" +
            "</if>" +
            "<if test='supplyName != null and supplyName != \"\"'>" +
            " AND supplyName LIKE CONCAT('%', #{supplyName}, '%')" +
            "</if>" +
            "<if test='phone != null and phone != \"\"'>" +
            " AND phone LIKE CONCAT('%', #{phone}, '%')" +
            "</if>" +
            "</script>")
    List<Supplier> searchSuppliersByCondition(SupplierQueryDTO query);
}