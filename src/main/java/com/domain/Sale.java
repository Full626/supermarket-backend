package com.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name="sale")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @Column(name="saleId",length = 20)
    private String saleId;//进货单号，主键，SA+时间+编号（最好设置为自动编号），升序

    @Column(name="goodsId",length = 20)
    private String goodsId;//商品号，外键，进货时确定（如果存在则继用，没有则额外添加）

    @Column(name="saleNum")
    private Integer saleNum;//销售数量

    @Column(name="salePrice", precision = 10, scale = 2)
    private BigDecimal salePrice;//售价（销售时的单价）默认进价*1.1，可手动修改

    @Column(name = "saleTime", nullable = false)
    private LocalDateTime saleTime;//销售时间，格式****-**-** **:**:**

    @Column(name="userId", length = 20)
    private String userId;//操作员用户名，外键

}
