package com.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID生成工具类
 * 用于自动生成进货单号和销售单号
 */
@Component
public class IdGenerator {

    //存储每天的序号计数器
    private static final ConcurrentHashMap<String, AtomicInteger> SEQUENCE_MAP = new ConcurrentHashMap<>();

    //进货单号前缀
    private static final String IN_STOCK_PREFIX = "IN";
    //销售单号前缀
    private static final String SALE_PREFIX = "SA";

    /**
     * 生成进货单号
     * 格式：IN + yyyyMMdd + 3位序号（如：IN20260301001）
     * @return 进货单号
     */
    public String generateInStockId() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = "IN_" + dateStr;

        // 修复：使用 ConcurrentHashMap 的原子操作，减少锁粒度
        AtomicInteger sequence = SEQUENCE_MAP.computeIfAbsent(key, k -> new AtomicInteger(0));
        int seq = sequence.incrementAndGet();

        return IN_STOCK_PREFIX + dateStr + String.format("%03d", seq);
    }

    /**
     * 生成销售单号
     * 格式：SA + yyyyMMdd + 3位序号（如：SA20260301001）
     * @return 销售单号
     */
    public String generateSaleId() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = "SA_" + dateStr;

        // 修复：使用 ConcurrentHashMap 的原子操作，减少锁粒度
        AtomicInteger sequence = SEQUENCE_MAP.computeIfAbsent(key, k -> new AtomicInteger(0));
        int seq = sequence.incrementAndGet();

        return SALE_PREFIX + dateStr + String.format("%03d", seq);
    }

    /**
     * 重置指定类型的序号（通常在跨天时自动处理）
     * @param type 类型（IN或SA）
     */
    public void resetSequence(String type) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = type + "_" + dateStr;
        SEQUENCE_MAP.put(key, new AtomicInteger(0));
    }
}