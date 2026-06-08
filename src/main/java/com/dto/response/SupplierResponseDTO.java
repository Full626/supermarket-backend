// dto/response/SupplierResponseDTO.java
package com.dto.response;

import lombok.Data;

/**
 * 供应商响应DTO（普通用户可见）
 */
@Data
public class SupplierResponseDTO {
    private String supplyId;      // 供应商号
    private String supplyName;    // 供应商名称
    private String phone;         // 联系电话
    private String address;       // 地址
}