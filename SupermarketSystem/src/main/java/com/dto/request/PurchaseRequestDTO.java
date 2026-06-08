// dto/request/PurchaseRequestDTO.java
package com.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 进货请求DTO
 */
@Data
public class PurchaseRequestDTO {

    @NotBlank(message = "供应商号不能为空")
    private String supplyId;

    @NotBlank(message = "商品号不能为空")
    private String goodsId;

    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @NotBlank(message = "类别编号不能为空")
    private String typeId;

    @NotNull(message = "进货数量不能为空")
    @Min(value = 1, message = "进货数量至少为1")
    private Integer inNum;

    @NotNull(message = "进价不能为空")
    @DecimalMin(value = "0.01", message = "进价必须大于0")
    private BigDecimal inPrice;

    @Min(value = 0, message = "预警数量不能小于0")
    private Integer warnNum;  // 可选，默认为10
}