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
@Table(name="type")
@NoArgsConstructor
@AllArgsConstructor
public class Type {
    @Id
    @Column(name="typeId",length=20)
    private String typeId;//类别编号，主键

    @Column(name="typeName",length=30)
    private String typeName;//类别名称
}
