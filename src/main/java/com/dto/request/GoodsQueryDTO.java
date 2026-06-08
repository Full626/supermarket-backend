// dto/request/GoodsQueryDTO.java
package com.dto.request;

import lombok.Data;

/**
 * 商品查询请求DTO
 */
@Data
public class GoodsQueryDTO {
    private String goodsId;     // 商品号（模糊查询）
    private String goodsName;   // 商品名称（模糊查询）
}