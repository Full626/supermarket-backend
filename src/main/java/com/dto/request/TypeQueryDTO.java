// dto/request/TypeQueryDTO.java
package com.dto.request;

import lombok.Data;

/**
 * 类别查询请求DTO
 */
@Data
public class TypeQueryDTO {
    private String typeId;      // 类别编号（精确查询）
    private String typeName;    // 类别名称（模糊查询）
}