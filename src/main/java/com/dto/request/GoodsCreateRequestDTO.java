package com.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsCreateRequestDTO {
    @NotBlank(message = "商品号不能为空")
    private String goodsId;

    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @NotBlank(message = "类别编号不能为空")
    private String typeId;

    @NotBlank(message = "供应商号不能为空")
    private String supplyId;

    @DecimalMin(value = "0.01", message = "进价必须大于0")
    private BigDecimal inPrice;

    @DecimalMin(value = "0.01", message = "售价必须大于0")
    private BigDecimal salePrice;

    @Min(value = 0, message = "库存不能为负数")
    private Integer stockNum;

    @Min(value = 0, message = "预警数量不能为负数")
    private Integer warnNum;
}