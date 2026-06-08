package com.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * '商品表数据 (goods)'
 */
@Data
@Entity
@Table(name = "goods")
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    @Id
    @Column(name="goodsId",length = 20)
    private String goodsId;//商品号，主键，进货时确定（如果存在则继用，没有则额外添加）

    @Column(name="goodsName",length = 50)
    private String goodsName;//商品名称

    @Column(name="typeId",length = 20)
    private String typeId;//类别编号，外键

    @Column(name="supplyId",length = 20)
    private String supplyId;//供应商号，外键

    @Column(name = "inPrice", precision = 10, scale = 2)
    private BigDecimal inPrice;//进价

    @Column(name = "salePrice", precision = 10, scale = 2)
    private BigDecimal salePrice;//售价，默认进价*1.1，可手动修改

    @Column(name="stockNum")
    private Integer stockNum;//库存数量，实时更新

    @Column(name="warnNum")
    private Integer warnNum;//预警数量，自定义设置


}
