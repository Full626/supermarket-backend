// dto/response/SaleRecordDTO.java
package com.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售记录响应DTO
 */
@Data
public class SaleRecordDTO {
    private String saleId;       // 销售单号
    private String goodsId;      // 商品号
    private Integer saleNum;     // 销售数量
    private BigDecimal salePrice;// 售价
    private LocalDateTime saleTime;// 销售时间
    private String userId;       // 操作员用户名
}