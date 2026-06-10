package com.service;

import com.domain.Type;
import com.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;

    /**
     * 获取所有类别编号列表
     */
    public List<String> getAllTypeIds() {
        List<String> typeIds = typeMapper.searchAllTypes();
        return typeIds != null ? typeIds : new ArrayList<>();
    }

    /**
     * 获取所有类别名称列表
     */
    public List<String> getAllTypeNames() {
        List<String> typeNames = typeMapper.searchAllTypeNames();
        return typeNames != null ? typeNames : new ArrayList<>();
    }

    /**
     * 获取所有完整类别信息
     */
    public List<Type> getAllTypes() {
        List<Type> types = typeMapper.selectAllTypes();
        return types != null ? types : new ArrayList<>();
    }

    /**
     * 根据类别编号获取类别名称
     */
    public String getTypeNameById(String typeId) {
        if (typeId == null || typeId.trim().isEmpty()) {
            return null;
        }
        return typeMapper.searchTypeNameByTypeId(typeId);
    }

    /**
     * 根据类别名称获取类别编号
     */
    public String getTypeIdByName(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            return null;
        }
        return typeMapper.searchTypeIdByTypeName(typeName);
    }

    /**
     * 检查类别是否存在
     */
    public boolean existsById(String typeId) {
        if (typeId == null || typeId.trim().isEmpty()) {
            return false;
        }
        String typeName = typeMapper.searchTypeNameByTypeId(typeId);
        return typeName != null && !typeName.isEmpty();
    }

    /**
     * 检查类别名称是否存在
     */
    public boolean existsByName(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            return false;
        }
        String typeId = typeMapper.searchTypeIdByTypeName(typeName);
        return typeId != null && !typeId.isEmpty();
    }

    /**
     * 检查类别是否被商品引用
     */
    public boolean isReferencedByGoods(String typeId) {
        if (typeId == null || typeId.trim().isEmpty()) {
            return false;
        }
        return typeMapper.countGoodsByTypeId(typeId) > 0;
    }

    /**
     * 新增类别
     */
    public boolean insertType(String typeId, String typeName) {
        if (typeId == null || typeId.trim().isEmpty()) {
            throw new RuntimeException("类别编号不能为空");
        }
        if (typeName == null || typeName.trim().isEmpty()) {
            throw new RuntimeException("类别名称不能为空");
        }
        if (existsById(typeId)) {
            throw new RuntimeException("类别编号已存在：" + typeId);
        }
        if (existsByName(typeName)) {
            throw new RuntimeException("类别名称已存在：" + typeName);
        }
        typeMapper.insertType(typeId, typeName);
        return true;
    }

    /**
     * 新增类别（使用Type对象）
     */
    public boolean insertType(Type type) {
        if (type == null) {
            throw new RuntimeException("类别信息不能为空");
        }
        return insertType(type.getTypeId(), type.getTypeName());
    }

    // ========== 新增方法 ==========

    /**
     * 更新类别名称
     * @param typeId 类别编号
     * @param typeName 新类别名称
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateType(String typeId, String typeName) {
        if (typeId == null || typeId.trim().isEmpty()) {
            throw new RuntimeException("类别编号不能为空");
        }
        if (typeName == null || typeName.trim().isEmpty()) {
            throw new RuntimeException("类别名称不能为空");
        }
        if (!existsById(typeId)) {
            throw new RuntimeException("类别不存在：" + typeId);
        }
        // 检查新名称是否已被其他类别使用
        String existingTypeId = getTypeIdByName(typeName);
        if (existingTypeId != null && !existingTypeId.equals(typeId)) {
            throw new RuntimeException("类别名称已存在：" + typeName);
        }
        int result = typeMapper.updateType(typeId, typeName);
        return result > 0;
    }

    /**
     * 删除类别
     * @param typeId 类别编号
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteType(String typeId) {
        if (typeId == null || typeId.trim().isEmpty()) {
            throw new RuntimeException("类别编号不能为空");
        }
        if (!existsById(typeId)) {
            throw new RuntimeException("类别不存在：" + typeId);
        }
        // 检查是否被商品引用
        if (isReferencedByGoods(typeId)) {
            throw new RuntimeException("该类别下存在商品，无法删除。请先删除或转移该类别的商品。");
        }
        int result = typeMapper.deleteType(typeId);
        return result > 0;
    }
}