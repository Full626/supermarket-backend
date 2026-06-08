// dto/request/SaleRequestDTO.java
package com.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 销售请求DTO（单商品）
 */
@Data
public class SaleRequestDTO {

    @NotBlank(message = "商品号不能为空")
    private String goodsId;

    @NotNull(message = "销售数量不能为空")
    @Min(value = 1, message = "销售数量至少为1")
    private Integer saleNum;

    // 删除 salePrice 字段 - 售价由后端从商品表读取

    @NotBlank(message = "操作员用户名不能为空")
    private String userId;
}