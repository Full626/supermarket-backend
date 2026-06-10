package com.controller;

import com.annotation.RoleRequired;
import com.common.Result;
import com.constant.Constants;
import com.dto.request.PageRequestDTO;
import com.dto.request.TypeQueryDTO;
import com.dto.request.TypeRequestDTO;
import com.dto.response.PageResponseDTO;
import com.dto.response.TypeResponseDTO;
import com.domain.Type;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
     * 权限：所有登录用户
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
     * 权限：所有登录用户
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
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PostMapping("/add")
    public Result<Void> addType(@Valid @RequestBody TypeRequestDTO request) {
        typeService.insertType(request.getTypeId(), request.getTypeName());
        return Result.success("添加成功", null);
    }

    /**
     * 更新类别
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PutMapping("/update")
    public Result<Void> updateType(@Valid @RequestBody TypeRequestDTO request) {
        typeService.updateType(request.getTypeId(), request.getTypeName());
        return Result.success("更新成功", null);
    }

    /**
     * 删除类别
     * 权限：仅管理员(admin)
     */
    @RoleRequired(Constants.ROLE_ADMIN)
    @DeleteMapping("/{typeId}")
    public Result<Void> deleteType(@PathVariable String typeId) {
        typeService.deleteType(typeId);
        return Result.success("删除成功", null);
    }

    /**
     * 分页查询类别
     * 权限：所有登录用户
     */
    @GetMapping("/page")
    public Result<PageResponseDTO<TypeResponseDTO>> getTypesByPage(@Valid PageRequestDTO pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<Type> types = typeService.getAllTypes();
        PageInfo<Type> pageInfo = new PageInfo<>(types);

        List<TypeResponseDTO> dtoList = pageInfo.getList().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PageResponseDTO<TypeResponseDTO> response = new PageResponseDTO<>();
        response.setTotal(pageInfo.getTotal());
        response.setPageNum(pageInfo.getPageNum());
        response.setPageSize(pageInfo.getPageSize());
        response.setPages(pageInfo.getPages());
        response.setList(dtoList);

        return Result.success(response);
    }

    private TypeResponseDTO convertToDTO(Type type) {
        TypeResponseDTO dto = new TypeResponseDTO();
        dto.setTypeId(type.getTypeId());
        dto.setTypeName(type.getTypeName());
        return dto;
    }
}