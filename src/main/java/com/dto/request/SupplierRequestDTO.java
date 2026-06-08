// dto/request/SupplierRequestDTO.java
package com.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 供应商请求DTO（创建/更新）
 */
@Data
public class SupplierRequestDTO {

    @NotBlank(message = "供应商号不能为空")
    @Pattern(regexp = "^S\\d{3}$", message = "供应商号格式：S+3位数字")
    private String supplyId;

    @NotBlank(message = "供应商名称不能为空")
    private String supplyName;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String address;  // 地址可以为空
}