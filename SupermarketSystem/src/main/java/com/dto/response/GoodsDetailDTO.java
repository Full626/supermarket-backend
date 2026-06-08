// dto/response/GoodsDetailDTO.java
package com.dto.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品详情响应DTO
 */
@Data
public class GoodsDetailDTO {
    private String goodsId;      // 商品号
    private String goodsName;    // 商品名称
    private String typeId;       // 类别编号
    private String supplyId;     // 供应商号
    private BigDecimal salePrice;// 售价
    private Integer stockNum;    // 库存数量
    private Integer warnNum;     // 预警数量
    // 注意：不包含 inPrice（进价）
}