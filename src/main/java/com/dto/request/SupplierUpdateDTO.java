package com.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 供应商更新请求DTO（支持部分更新）
 */
@Data
public class SupplierUpdateDTO {

    @NotBlank(message = "供应商号不能为空")
    @Pattern(regexp = "^S\\d{3}$", message = "供应商号格式：S+3位数字")  // 修复：S开头，不是G
    private String supplyId;

    private String supplyName;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String address;
}