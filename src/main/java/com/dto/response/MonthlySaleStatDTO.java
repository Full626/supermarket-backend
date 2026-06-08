package com.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlySaleStatDTO {
    private String month;              // 月份 (yyyy-MM)
    private Integer totalSales;        // 总销售笔数
    private Integer totalQuantity;     // 总销售数量
    private BigDecimal totalAmount;    // 总销售金额
}