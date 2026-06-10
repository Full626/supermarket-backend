package com.controller;

import com.annotation.RoleRequired;
import com.common.Result;
import com.constant.Constants;
import com.dto.request.PageRequestDTO;
import com.dto.request.SupplierQueryDTO;
import com.dto.request.SupplierRequestDTO;
import com.dto.request.SupplierUpdateDTO;
import com.dto.response.PageResponseDTO;
import com.dto.response.SupplierResponseDTO;
import com.domain.Supplier;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 获取所有供应商列表
     * 权限：所有登录用户
     */
    @GetMapping("/list")
    public Result<List<SupplierResponseDTO>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<SupplierResponseDTO> dtoList = suppliers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    /**
     * 根据ID查询供应商
     * 权限：所有登录用户
     */
    @GetMapping("/{supplyId}")
    public Result<SupplierResponseDTO> getSupplierById(@PathVariable String supplyId) {
        Supplier supplier = supplierService.getSupplierById(supplyId);
        if (supplier == null) {
            return Result.error("供应商不存在");
        }
        return Result.success(convertToDTO(supplier));
    }

    /**
     * 新增供应商
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PostMapping("/add")
    public Result<Void> addSupplier(@Valid @RequestBody SupplierRequestDTO request) {
        Supplier supplier = new Supplier();
        supplier.setSupplyId(request.getSupplyId());
        supplier.setSupplyName(request.getSupplyName());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());

        supplierService.insertSupplier(supplier);
        return Result.success("添加成功", null);
    }

    /**
     * 更新供应商信息
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PutMapping("/update")
    public Result<Void> updateSupplier(@Valid @RequestBody SupplierUpdateDTO request) {
        if (request.getSupplyName() != null && !request.getSupplyName().isEmpty()) {
            supplierService.updateSupplyName(request.getSupplyId(), request.getSupplyName());
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            supplierService.updateSupplyPhone(request.getSupplyId(), request.getPhone());
        }
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            supplierService.updateSupplyAddress(request.getSupplyId(), request.getAddress());
        }
        return Result.success("更新成功", null);
    }

    /**
     * 删除供应商
     * 权限：仅管理员(admin)
     */
    @RoleRequired(Constants.ROLE_ADMIN)
    @DeleteMapping("/{supplyId}")
    public Result<Void> deleteSupplier(@PathVariable String supplyId) {
        supplierService.deleteSupplier(supplyId);
        return Result.success("删除成功", null);
    }

    /**
     * 分页查询供应商
     * 权限：所有登录用户
     */
    @GetMapping("/page")
    public Result<PageResponseDTO<SupplierResponseDTO>> getSuppliersByPage(
            @Valid SupplierQueryDTO query,
            @Valid PageRequestDTO pageRequest) {

        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<Supplier> suppliers = supplierService.getSuppliersByCondition(query);
        PageInfo<Supplier> pageInfo = new PageInfo<>(suppliers);

        List<SupplierResponseDTO> dtoList = pageInfo.getList().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PageResponseDTO<SupplierResponseDTO> response = new PageResponseDTO<>();
        response.setTotal(pageInfo.getTotal());
        response.setPageNum(pageInfo.getPageNum());
        response.setPageSize(pageInfo.getPageSize());
        response.setPages(pageInfo.getPages());
        response.setList(dtoList);

        return Result.success(response);
    }

    private SupplierResponseDTO convertToDTO(Supplier supplier) {
        SupplierResponseDTO dto = new SupplierResponseDTO();
        dto.setSupplyId(supplier.getSupplyId());
        dto.setSupplyName(supplier.getSupplyName());
        dto.setPhone(supplier.getPhone());
        dto.setAddress(supplier.getAddress());
        return dto;
    }
}