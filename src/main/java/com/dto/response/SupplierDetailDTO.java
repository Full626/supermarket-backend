// dto/response/SupplierDetailDTO.java
package com.dto.response;

import lombok.Data;

/**
 * 供应商详情响应DTO（管理员可见，可以包含更多字段）
 */
@Data
public class SupplierDetailDTO {
    private String supplyId;      // 供应商号
    private String supplyName;    // 供应商名称
    private String phone;         // 联系电话
    private String address;       // 地址
    // 可以添加其他字段，如：合作时间、信用等级等
}