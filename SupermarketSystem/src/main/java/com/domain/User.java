package com.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name="userId",length = 20)
    private String userId;//用户名，主键，登录账号

    @Column(name="pwd",length = 32)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//  只允许写入，不允许读取
    private String pwd;//密码，加密存储

    @Column(name="userName",length = 20)
    private String userName;//姓名

    @Column(name="phone",length = 11)
    private String phone;//电话，固定位数

    @Column(name="idCard",length=18)
    private String idCard;//身份证号，唯一，位数固定
}
