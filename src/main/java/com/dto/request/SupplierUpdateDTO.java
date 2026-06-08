// dto/request/SupplierUpdateDTO.java
package com.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 供应商更新请求DTO（支持部分更新）
 */
@Data
public class SupplierUpdateDTO {

    @NotBlank(message = "供应商号不能为空")
    private String supplyId;

    private String supplyName;    // 可选
    private String phone;         // 可选
    private String address;       // 可选
}