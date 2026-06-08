// dto/request/TypeRequestDTO.java
package com.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 商品类别请求DTO
 */
@Data
public class TypeRequestDTO {

    @NotBlank(message = "类别编号不能为空")
    private String typeId;

    @NotBlank(message = "类别名称不能为空")
    private String typeName;
}