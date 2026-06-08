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

@Data
@Entity
@Table(name="in_stock")
@NoArgsConstructor
@AllArgsConstructor
public class InStock {
    @Id
    @Column(name="inId",length= 20)
    private String inId;//进货单号，主键，in+时间+编号（最好设置为自动编号），升序

    @Column(name="supplyId",length= 20)
    private String supplyId;//供应商号，外键

    @Column(name="goodsId",length= 20)
    private String goodsId;//商品号，外键，进货时确定（如果存在则继用，没有则额外添加）

    @Column(name="inNum")
    private Integer inNum;//进货数量

    @Column(name="inPrice",precision = 10,scale = 2)
    private BigDecimal inPrice;//进价

    @Column(name="inTime",nullable = false)
    private LocalDateTime inTime;//进货时间，格式****-**-**

}
