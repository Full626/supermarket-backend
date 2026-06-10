package com.controller;

import com.annotation.RoleRequired;
import com.common.Result;
import com.constant.Constants;
import com.domain.InStock;
import com.dto.request.PageRequestDTO;
import com.dto.response.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.GoodsMapper;
import com.mapper.InStockMapper;
import com.mapper.SaleMapper;
import com.domain.Sale;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 权限：所有登录用户（收银员、经理、管理员）
     */
    @GetMapping("/dashboard")
    public Result<StatDTO> getDashboardStat() {
        StatDTO stat = new StatDTO();

        stat.setTotalGoods(goodsMapper.getTotalGoodsCount());
        stat.setLowStockCount(goodsMapper.getLowStockCount());

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
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
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

    /**
     * 分页查询销售记录
     * 权限：所有登录用户
     */
    @GetMapping("/saleRecords")
    public Result<PageResponseDTO<SaleRecordDTO>> getSaleRecordsByPage(
            @RequestParam(required = false) String goodsId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @Valid PageRequestDTO pageRequest) {

        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<Sale> records = saleMapper.selectByCondition(goodsId, startDate, endDate);
        PageInfo<Sale> pageInfo = new PageInfo<>(records);

        List<SaleRecordDTO> dtoList = pageInfo.getList().stream()
                .map(this::convertSaleToDTO)
                .collect(Collectors.toList());

        PageResponseDTO<SaleRecordDTO> response = new PageResponseDTO<>();
        response.setTotal(pageInfo.getTotal());
        response.setPageNum(pageInfo.getPageNum());
        response.setPageSize(pageInfo.getPageSize());
        response.setPages(pageInfo.getPages());
        response.setList(dtoList);

        return Result.success(response);
    }

    /**
     * 分页查询进货记录
     * 权限：管理员(admin)、经理(manager)
     * 说明：进货记录包含进价等敏感信息，仅管理人员可见
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @GetMapping("/inStockRecords")
    public Result<PageResponseDTO<InStockRecordDTO>> getInStockRecordsByPage(
            @RequestParam(required = false) String goodsId,
            @RequestParam(required = false) String supplyId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @Valid PageRequestDTO pageRequest) {

        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<InStock> records = inStockMapper.selectByCondition(goodsId, supplyId, startDate, endDate);
        PageInfo<InStock> pageInfo = new PageInfo<>(records);

        List<InStockRecordDTO> dtoList = pageInfo.getList().stream()
                .map(this::convertInStockToDTO)
                .collect(Collectors.toList());

        PageResponseDTO<InStockRecordDTO> response = new PageResponseDTO<>();
        response.setTotal(pageInfo.getTotal());
        response.setPageNum(pageInfo.getPageNum());
        response.setPageSize(pageInfo.getPageSize());
        response.setPages(pageInfo.getPages());
        response.setList(dtoList);

        return Result.success(response);
    }

    private SaleRecordDTO convertSaleToDTO(Sale sale) {
        SaleRecordDTO dto = new SaleRecordDTO();
        dto.setSaleId(sale.getSaleId());
        dto.setGoodsId(sale.getGoodsId());
        dto.setSaleNum(sale.getSaleNum());
        dto.setSalePrice(sale.getSalePrice());
        dto.setSaleTime(sale.getSaleTime());
        dto.setUserId(sale.getUserId());
        return dto;
    }

    private InStockRecordDTO convertInStockToDTO(InStock inStock) {
        InStockRecordDTO dto = new InStockRecordDTO();
        dto.setInId(inStock.getInId());
        dto.setSupplyId(inStock.getSupplyId());
        dto.setGoodsId(inStock.getGoodsId());
        dto.setInNum(inStock.getInNum());
        dto.setInPrice(inStock.getInPrice());
        dto.setInTime(inStock.getInTime());
        return dto;
    }
}