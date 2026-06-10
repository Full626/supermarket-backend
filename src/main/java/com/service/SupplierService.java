package com.service;

import com.domain.Supplier;
import com.dto.request.SupplierQueryDTO;
import com.mapper.GoodsMapper;
import com.mapper.InStockMapper;
import com.mapper.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private InStockMapper inStockMapper;  // 用于检查进货记录

    /**
     * 获取所有供应商ID列表
     */
    public List<String> getAllSupplierIds() {
        List<String> ids = supplierMapper.searchSupplierIds();
        return ids != null ? ids : new ArrayList<>();
    }

    /**
     * 获取所有供应商完整信息
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = supplierMapper.searchSuppliers();
        return suppliers != null ? suppliers : new ArrayList<>();
    }

    /**
     * 根据供应商ID获取供应商信息
     */
    public Supplier getSupplierById(String supplyId) {
        if (supplyId == null || supplyId.trim().isEmpty()) {
            return null;
        }
        return supplierMapper.selectBySupplyId(supplyId);
    }

    /**
     * 检查供应商是否存在
     */
    public boolean existsById(String supplyId) {
        return getSupplierById(supplyId) != null;
    }

    /**
     * 检查供应商是否被商品引用（通过 goods 表）
     */
    public boolean isReferencedByGoods(String supplyId) {
        if (supplyId == null || supplyId.trim().isEmpty()) {
            return false;
        }
        // 查询是否有商品的 supplyId 等于当前供应商ID
        List<com.domain.Goods> goodsList = goodsMapper.searchGoods(null, null);
        if (goodsList != null) {
            return goodsList.stream().anyMatch(g -> supplyId.equals(g.getSupplyId()));
        }
        return false;
    }

    /**
     * 检查供应商是否有进货记录（使用 countBySupplyId 方法）
     */
    public boolean hasInStockRecords(String supplyId) {
        if (supplyId == null || supplyId.trim().isEmpty()) {
            return false;
        }
        // 使用 InStockMapper 的 countBySupplyId 方法
        int count = inStockMapper.countBySupplyId(supplyId);
        return count > 0;
    }

    /**
     * 新增供应商
     */
    public boolean insertSupplier(Supplier supplier) {
        if (supplier == null || supplier.getSupplyId() == null || supplier.getSupplyId().trim().isEmpty()) {
            throw new RuntimeException("供应商信息不完整");
        }
        if (existsById(supplier.getSupplyId())) {
            throw new RuntimeException("供应商ID已存在：" + supplier.getSupplyId());
        }
        supplierMapper.insertSupplier(supplier);
        return true;
    }

    /**
     * 修改供应商名称
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

    /**
     * 删除供应商（使用 countBySupplyId 检查进货记录）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSupplier(String supplyId) {
        if (supplyId == null || supplyId.trim().isEmpty()) {
            throw new RuntimeException("供应商ID不能为空");
        }
        if (!existsById(supplyId)) {
            throw new RuntimeException("供应商不存在：" + supplyId);
        }

        // 1. 检查是否被商品引用
        if (isReferencedByGoods(supplyId)) {
            throw new RuntimeException("该供应商存在关联商品，无法删除。请先删除或转移该供应商的商品。");
        }

        // 2. 检查是否有进货记录（这里使用 countBySupplyId）
        if (hasInStockRecords(supplyId)) {
            throw new RuntimeException("该供应商存在进货记录，无法删除。");
        }

        supplierMapper.deleteSupplier(supplyId);
        return true;
    }

    /**
     * 条件查询供应商
     */
    public List<Supplier> getSuppliersByCondition(SupplierQueryDTO query) {
        if (query == null) {
            return getAllSuppliers();
        }
        return supplierMapper.searchSuppliersByCondition(query);
    }
}