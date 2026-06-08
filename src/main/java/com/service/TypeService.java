package com.service;

import com.domain.Type;
import com.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;

    /**
     * 获取所有类别编号列表
     * @return 类别编号列表
     */
    public List<String> getAllTypeIds() {
        List<String> typeIds = typeMapper.searchAllTypes();
        return typeIds != null ? typeIds : new ArrayList<>();
    }

    /**
     * 获取所有类别名称列表
     * @return 类别名称列表
     */
    public List<String> getAllTypeNames() {
        List<String> typeNames = typeMapper.searchAllTypeNames();
        return typeNames != null ? typeNames : new ArrayList<>();
    }

    /**
     * 获取所有完整类别信息
     * @return 类别列表
     */
    public List<Type> getAllTypes() {
        List<String> typeIds = getAllTypeIds();
        List<Type> types = new ArrayList<>();
        for (String typeId : typeIds) {
            String typeName = typeMapper.searchTypeNameByTypeId(typeId);
            types.add(new Type(typeId, typeName));
        }
        return types;
    }

    /**
     * 根据类别编号获取类别名称
     * @param typeId 类别编号
     * @return 类别名称
     */
    public String getTypeNameById(String typeId) {
        if (typeId == null || typeId.trim().isEmpty()) {
            return null;
        }
        return typeMapper.searchTypeNameByTypeId(typeId);
    }

    /**
     * 根据类别名称获取类别编号
     * @param typeName 类别名称
     * @return 类别编号
     */
    public String getTypeIdByName(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            return null;
        }
        return typeMapper.searchTypeIdByTypeName(typeName);
    }

    /**
     * 检查类别是否存在
     * @param typeId 类别编号
     * @return 是否存在
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
     * @param typeName 类别名称
     * @return 是否存在
     */
    public boolean existsByName(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            return false;
        }
        String typeId = typeMapper.searchTypeIdByTypeName(typeName);
        return typeId != null && !typeId.isEmpty();
    }

    /**
     * 新增类别
     * @param typeId 类别编号
     * @param typeName 类别名称
     * @return 是否成功
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
     * @param type 类别对象
     * @return 是否成功
     */
    public boolean insertType(Type type) {
        if (type == null) {
            throw new RuntimeException("类别信息不能为空");
        }
        return insertType(type.getTypeId(), type.getTypeName());
    }
}