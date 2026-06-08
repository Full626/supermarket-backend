package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一响应结果类
 */
@Data
@AllArgsConstructor
public class Result<T> {

    /** 状态码：200成功，其他失败 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 返回数据 */
    private T data;

    private Result() {}

    //  成功响应

    public static <T> Result<T> success() {
        return new Result<T>(200,"成功",null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(200,"成功",data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(200,message,data);
    }

    // 失败响应

    public static <T> Result<T> error(String message) {
        return new Result<T>(500,message,null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<T>(code, message,null);
    }
}