package com.mapper;


import com.domain.Supplier;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SupplierMapper {

    /**
     * 获取所有的供应商id
     */
    @Select("SELECT supplyId FROM supplier")
    List<Integer> searchSupplierIds();

    /**
     * 获取所有供应商完整信息
     */
    @Select("SELECT supplyId,supplyName,phone,address FROM supplier")
    List<Supplier> searchSuppliers();

    /**
     * 插入一个新的供应商
     */
    @Insert("<script>"+
            "INSERT supplier (supplyId,supplyName,phone,address)"+
            " VALUES (#{supplyId},#{supplyName},#{phone},#{address}) "+
            "</script>"
    )
    void insertSupplier(Supplier supplier);

    /**
     * 更新（修改）供应商名字，根据supplyId
     */
    @Update("UPDATE supplier SET supplyName = #{supplyName} WHERE supplyId = #{supplyId}")
    void updateSupplyName(@Param("supplyName") String supplyName,
                          @Param("supplyId") String supplyId
    );

    /**
     * 更新（修改）供应商电话，根据supplyId
     */
    @Update("UPDATE supplier SET phone = #{phone} WHERE supplyId = #{supplyId}")
    void updateSupplyPhone(@Param("phone") String phone,
                          @Param("supplyId") String supplyId
    );

    /**
     * 更新（修改）供应商地址，根据supplyId
     */
    @Update("UPDATE supplier SET address = #{address} WHERE supplyId = #{supplyId}")
    void updateSupplyAddress(@Param("address") String address,
                          @Param("supplyId") String supplyId
    );

}
