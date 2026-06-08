// dto/request/GoodsPriceDTO.java
package com.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品价格修改请求DTO
 */
@Data
public class GoodsPriceDTO {

    @NotNull(message = "售价不能为空")
    @DecimalMin(value = "0.01", message = "售价必须大于0")
    private BigDecimal salePrice;
}