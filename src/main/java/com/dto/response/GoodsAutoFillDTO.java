package com.dto.response;

import lombok.Data;

/**
 * 商品自动填充响应DTO
 */
@Data
public class GoodsAutoFillDTO {
    private String goodsId;
    private String goodsName;
    private String typeId;
    private String supplyId;
    private Integer warnNum;
}