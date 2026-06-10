package com.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private String userName;
    private String phone;
    private String idCard;
    private String pwd;
    private String role;  // 新增角色字段
}