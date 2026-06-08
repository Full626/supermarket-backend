// dto/response/GoodsListDTO.java
package com.dto.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品列表响应DTO（不包含进价等敏感信息）
 */
@Data
public class GoodsListDTO {
    private String goodsId;      // 商品号
    private String goodsName;    // 商品名称
    private BigDecimal salePrice;// 售价
    private Integer stockNum;    // 库存数量
    private Integer warnNum;     // 预警数量
}