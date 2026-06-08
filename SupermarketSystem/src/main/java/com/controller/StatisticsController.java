package com.controller;

import com.common.Result;
import com.dto.response.MonthlySaleStatDTO;
import com.dto.response.StatDTO;
import com.mapper.GoodsMapper;
import com.mapper.InStockMapper;
import com.mapper.SaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private InStockMapper inStockMapper;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/dashboard")
    public Result<StatDTO> getDashboardStat() {
        StatDTO stat = new StatDTO();

        // 商品总数
        stat.setTotalGoods(goodsMapper.getTotalGoodsCount());

        // 低库存商品数
        stat.setLowStockCount(goodsMapper.getLowStockCount());

        // 今日销售统计
        Map<String, Object> todaySale = saleMapper.getTodaySaleStat();
        if (todaySale != null) {
            stat.setTodaySaleCount(((Number) todaySale.getOrDefault("count", 0)).intValue());
            stat.setTodaySaleNum(((Number) todaySale.getOrDefault("totalNum", 0)).intValue());
            Object amount = todaySale.get("totalAmount");
            stat.setTodaySaleAmount(amount != null ? (BigDecimal) amount : BigDecimal.ZERO);
        } else {
            stat.setTodaySaleCount(0);
            stat.setTodaySaleNum(0);
            stat.setTodaySaleAmount(BigDecimal.ZERO);
        }

        // 今日进货统计
        Map<String, Object> todayInStock = inStockMapper.getTodayInStockStat();
        if (todayInStock != null) {
            stat.setTodayInStockCount(((Number) todayInStock.getOrDefault("count", 0)).intValue());
            stat.setTodayInStockNum(((Number) todayInStock.getOrDefault("totalNum", 0)).intValue());
            Object amount = todayInStock.get("totalAmount");
            stat.setTodayInStockAmount(amount != null ? (BigDecimal) amount : BigDecimal.ZERO);
        } else {
            stat.setTodayInStockCount(0);
            stat.setTodayInStockNum(0);
            stat.setTodayInStockAmount(BigDecimal.ZERO);
        }

        return Result.success(stat);
    }

    /**
     * 获取月度销售统计
     */
    @GetMapping("/monthlySale")
    public Result<List<MonthlySaleStatDTO>> getMonthlySaleStat() {
        List<Map<String, Object>> list = saleMapper.getMonthlySaleStat();
        List<MonthlySaleStatDTO> result = new ArrayList<>();

        if (list != null) {
            for (Map<String, Object> item : list) {
                MonthlySaleStatDTO dto = new MonthlySaleStatDTO();
                dto.setMonth((String) item.get("month"));
                dto.setTotalSales(((Number) item.getOrDefault("totalSales", 0)).intValue());
                dto.setTotalQuantity(((Number) item.getOrDefault("totalQuantity", 0)).intValue());
                Object amount = item.get("totalAmount");
                dto.setTotalAmount(amount != null ? (BigDecimal) amount : BigDecimal.ZERO);
                result.add(dto);
            }
        }

        return Result.success(result);
    }
}