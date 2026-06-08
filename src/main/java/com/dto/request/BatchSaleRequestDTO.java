// dto/request/BatchSaleRequestDTO.java
package com.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量销售请求DTO
 */
@Data
public class BatchSaleRequestDTO {

    @NotBlank(message = "操作员用户名不能为空")
    private String userId;

    @NotNull(message = "销售商品列表不能为空")
    private List<@Valid SaleItemDTO> items;

    /**
     * 销售商品项
     */
    @Data
    public static class SaleItemDTO {
        @NotBlank(message = "商品号不能为空")
        private String goodsId;

        @NotNull(message = "销售数量不能为空")
        @Min(value = 1, message = "销售数量至少为1")
        private Integer saleNum;
    }
}