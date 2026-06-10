package com.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String userId;
    private String userName;
    private String phone;
    private String idCard;
    private String role;  // 新增角色字段
}