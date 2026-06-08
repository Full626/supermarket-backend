// dto/response/InStockRecordDTO.java
package com.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 进货记录响应DTO
 */
@Data
public class InStockRecordDTO {
    private String inId;         // 进货单号
    private String supplyId;     // 供应商号
    private String goodsId;      // 商品号
    private Integer inNum;       // 进货数量
    private BigDecimal inPrice;  // 进价（管理员可见）
    private LocalDateTime inTime;// 进货时间
}