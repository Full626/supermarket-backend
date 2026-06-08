package com.controller;

import com.common.Result;
import com.dto.request.TypeQueryDTO;
import com.dto.request.TypeRequestDTO;
import com.dto.response.TypeResponseDTO;
import com.domain.Type;
import com.service.TypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 获取所有类别列表
     */
    @GetMapping("/list")
    public Result<List<TypeResponseDTO>> getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        List<TypeResponseDTO> dtoList = types.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    /**
     * 根据编号查询类别
     */
    @GetMapping("/{typeId}")
    public Result<TypeResponseDTO> getTypeById(@PathVariable String typeId) {
        String typeName = typeService.getTypeNameById(typeId);
        if (typeName == null) {
            return Result.error("类别不存在");
        }
        TypeResponseDTO dto = new TypeResponseDTO();
        dto.setTypeId(typeId);
        dto.setTypeName(typeName);
        return Result.success(dto);
    }

    /**
     * 新增类别
     */
    @PostMapping("/add")
    public Result<Void> addType(@Valid @RequestBody TypeRequestDTO request) {
        typeService.insertType(request.getTypeId(), request.getTypeName());
        return Result.success("添加成功", null);
    }

    private TypeResponseDTO convertToDTO(Type type) {
        TypeResponseDTO dto = new TypeResponseDTO();
        dto.setTypeId(type.getTypeId());
        dto.setTypeName(type.getTypeName());
        return dto;
    }
}