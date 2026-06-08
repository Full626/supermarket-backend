// dto/response/TypeResponseDTO.java
package com.dto.response;

import lombok.Data;

/**
 * 商品类别响应DTO
 */
@Data
public class TypeResponseDTO {
    private String typeId;      // 类别编号
    private String typeName;    // 类别名称
}