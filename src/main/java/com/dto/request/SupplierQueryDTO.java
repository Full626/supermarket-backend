// dto/request/SupplierQueryDTO.java
package com.dto.request;

import lombok.Data;

/**
 * 供应商查询请求DTO
 */
@Data
public class SupplierQueryDTO {
    private String supplyId;      // 供应商号（精确查询）
    private String supplyName;    // 供应商名称（模糊查询）
    private String phone;         // 联系电话（精确查询）
}