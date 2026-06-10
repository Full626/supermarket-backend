package com.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 系统常量类
 */
public class Constants {

    // ========== 价格相关 ==========
    public static final int PRICE_SCALE = 2;

    // 修复：使用 RoundingMode.HALF_UP 替代过时的 BigDecimal.ROUND_HALF_UP
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static final BigDecimal SALE_PRICE_MULTIPLIER = new BigDecimal("1.1");

    // ========== 默认值 ==========
    public static final int DEFAULT_WARN_NUM = 10;
    public static final int DEFAULT_STOCK_NUM = 0;

    // ========== 用户角色 ==========
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_CASHIER = "cashier";

    // ========== JWT相关 ==========
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // ========== 单号前缀 ==========
    public static final String IN_STOCK_PREFIX = "IN";
    public static final String SALE_PREFIX = "SA";

    // ========== ID生成器Key前缀 ==========
    public static final String IN_STOCK_SEQ_KEY = "IN_";
    public static final String SALE_SEQ_KEY = "SA_";
}