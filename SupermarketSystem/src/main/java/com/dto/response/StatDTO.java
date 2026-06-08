package com.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StatDTO {
    private Integer totalGoods;        // 商品总数
    private Integer lowStockCount;     // 低库存商品数
    private Integer todaySaleCount;    // 今日销售笔数
    private Integer todaySaleNum;      // 今日销售总数量
    private BigDecimal todaySaleAmount;// 今日销售总金额
    private Integer todayInStockCount; // 今日进货笔数
    private Integer todayInStockNum;   // 今日进货总数量
    private BigDecimal todayInStockAmount; // 今日进货总金额
}