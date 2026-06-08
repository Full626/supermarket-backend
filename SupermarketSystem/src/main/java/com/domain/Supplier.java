package com.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="supplier")
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @Column(name="supplyId",length=20)
    private String supplyId;//供应商号，主键

    @Column(name="supplyName",length=50)
    private String supplyName;//供应商名称

    @Column(name="phone",length = 11)
    private String phone;//联系电话,考虑是否为固定数位

    @Column(name="address",length = 500)
    private String address;//地址

}
