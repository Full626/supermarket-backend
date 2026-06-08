package com.mapper;


import com.domain.Supplier;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
            " VALUES (supplyId,supplyName,phone,address) "+
            "</script>"
    )
    void insertSupplier(Supplier supplier);

}
