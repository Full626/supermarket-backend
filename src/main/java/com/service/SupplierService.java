package com.service;

import com.domain.Supplier;
import com.mapper.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 获取所有供应商ID列表
     * @return 供应商ID列表
     */
    public List<Integer> getAllSupplierIds() {
        List<Integer> ids = supplierMapper.searchSupplierIds();
        return ids != null ? ids : new ArrayList<>();
    }

    /**
     * 获取所有供应商完整信息
     * @return 供应商列表
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = supplierMapper.searchSuppliers();
        return suppliers != null ? suppliers : new ArrayList<>();
    }

    /**
     * 根据供应商ID获取供应商信息
     * @param supplyId 供应商ID
     * @return 供应商对象，不存在则返回null
     */
    public Supplier getSupplierById(String supplyId) {
        List<Supplier> suppliers = supplierMapper.searchSuppliers();
        if (suppliers != null && supplyId != null) {
            return suppliers.stream()
                    .filter(s -> supplyId.equals(s.getSupplyId()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * 检查供应商是否存在
     * @param supplyId 供应商ID
     * @return 是否存在
     */
    public boolean existsById(String supplyId) {
        return getSupplierById(supplyId) != null;
    }

    /**
     * 新增供应商
     * @param supplier 供应商对象
     * @return 是否成功
     */
    public boolean insertSupplier(Supplier supplier) {
        if (supplier == null || supplier.getSupplyId() == null || supplier.getSupplyId().trim().isEmpty()) {
            throw new RuntimeException("供应商信息不完整");
        }
        // 检查是否已存在
        if (existsById(supplier.getSupplyId())) {
            throw new RuntimeException("供应商ID已存在：" + supplier.getSupplyId());
        }
        supplierMapper.insertSupplier(supplier);
        return true;
    }

    /**
     * 修改供应商名称
     * @param supplyId 供应商ID
     * @param supplyName 新名称
     * @return 是否成功
     */
    public boolean updateSupplyName(String supplyId, String supplyName) {
        if (supplyId == null || supplyId.trim().isEmpty()) {
            throw new RuntimeException("供应商ID不能为空");
        }
        if (supplyName == null || supplyName.trim().isEmpty()) {
            throw new RuntimeException("供应商名称不能为空");
        }
        if (!existsById(supplyId)) {
            throw new RuntimeException("供应商不存在：" + supplyId);
        }
        supplierMapper.updateSupplyName(supplyName, supplyId);
        return true;
    }

    /**
     * 修改供应商电话
     * @param supplyId 供应商ID
     * @param phone 新电话
     * @return 是否成功
     */
    public boolean updateSupplyPhone(String supplyId, String phone) {
        if (supplyId == null || supplyId.trim().isEmpty()) {
            throw new RuntimeException("供应商ID不能为空");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("电话不能为空");
        }
        if (!existsById(supplyId)) {
            throw new RuntimeException("供应商不存在：" + supplyId);
        }
        supplierMapper.updateSupplyPhone(phone, supplyId);
        return true;
    }

    /**
     * 修改供应商地址
     * @param supplyId 供应商ID
     * @param address 新地址
     * @return 是否成功
     */
    public boolean updateSupplyAddress(String supplyId, String address) {
        if (supplyId == null || supplyId.trim().isEmpty()) {
            throw new RuntimeException("供应商ID不能为空");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new RuntimeException("地址不能为空");
        }
        if (!existsById(supplyId)) {
            throw new RuntimeException("供应商不存在：" + supplyId);
        }
        supplierMapper.updateSupplyAddress(address, supplyId);
        return true;
    }
}