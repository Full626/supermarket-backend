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
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "userId", length = 20)
    private String userId;      // 用户名，主键，登录账号

    @Column(name = "pwd", length = 60)  // BCrypt 加密后长度 60
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;         // 密码，BCrypt 加密存储

    @Column(name = "userName", length = 20)
    private String userName;    // 姓名

    @Column(name = "phone", length = 11)
    private String phone;       // 电话

    @Column(name = "idCard", length = 18)
    private String idCard;      // 身份证号

    @Column(name = "role", length = 20)
    private String role;        // 角色：admin/manager/cashier
}