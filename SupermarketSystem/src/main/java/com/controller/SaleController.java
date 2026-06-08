package com.controller;

import com.common.Result;
import com.domain.Goods;
import com.domain.Sale;
import com.dto.request.BatchSaleRequestDTO;
import com.dto.request.SaleRequestDTO;
import com.dto.response.SaleRecordDTO;
import com.service.GoodsService;
import com.service.SaleService;
import com.util.IdGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 单商品销售接口
     */
    @PostMapping("/sell")
    public Result<Map<String, Object>> sell(@Valid @RequestBody SaleRequestDTO request) {
        // 1. 获取商品信息（用于返回售价信息）
        Goods goods = goodsService.getGoodsById(request.getGoodsId());
        if (goods == null) {
            return Result.error("商品不存在");
        }

        // 2. 自动生成销售单号
        String saleId = idGenerator.generateSaleId();

        // 3. 创建销售记录对象（售价由Service层从商品表获取）
        Sale sale = new Sale();
        sale.setSaleId(saleId);
        sale.setGoodsId(request.getGoodsId());
        sale.setSaleNum(request.getSaleNum());
        sale.setSaleTime(LocalDateTime.now());
        sale.setUserId(request.getUserId());

        // 4. 执行销售
        saleService.sell(sale);

        // 5. 获取更新后的库存
        Goods updatedGoods = goodsService.getGoodsById(request.getGoodsId());

        Map<String, Object> data = new HashMap<>();
        data.put("saleId", saleId);
        data.put("saleTime", sale.getSaleTime());
        data.put("salePrice", goods.getSalePrice());  // 返回销售时使用的售价
        data.put("remainingStock", updatedGoods != null ? updatedGoods.getStockNum() : 0);

        return Result.success("销售成功", data);
    }

    /**
     * 批量销售接口
     */
    @PostMapping("/batchSell")
    public Result<Map<String, Object>> batchSell(@Valid @RequestBody BatchSaleRequestDTO request) {
        List<Sale> sales = new ArrayList<>();
        List<Map<String, Object>> saleDetails = new ArrayList<>();

        // 为每个商品生成销售单号并创建销售记录
        for (BatchSaleRequestDTO.SaleItemDTO item : request.getItems()) {
            // 获取商品信息（验证商品是否存在并获取售价）
            Goods goods = goodsService.getGoodsById(item.getGoodsId());
            if (goods == null) {
                return Result.error("商品不存在：" + item.getGoodsId());
            }

            String saleId = idGenerator.generateSaleId();
            Sale sale = new Sale();
            sale.setSaleId(saleId);
            sale.setGoodsId(item.getGoodsId());
            sale.setSaleNum(item.getSaleNum());
            sale.setSaleTime(LocalDateTime.now());
            sale.setUserId(request.getUserId());
            // 售价由Service层设置，这里不设置
            sales.add(sale);

            // 记录详情用于返回
            Map<String, Object> detail = new HashMap<>();
            detail.put("goodsId", item.getGoodsId());
            detail.put("salePrice", goods.getSalePrice());
            detail.put("saleNum", item.getSaleNum());
            saleDetails.add(detail);
        }

        // 执行批量销售
        saleService.batchSell(sales);

        // 计算总金额
        double totalAmount = saleDetails.stream()
                .mapToDouble(d -> ((Number) d.get("salePrice")).doubleValue() * ((Number) d.get("saleNum")).intValue())
                .sum();

        Map<String, Object> data = new HashMap<>();
        data.put("count", sales.size());
        data.put("totalAmount", totalAmount);
        data.put("details", saleDetails);

        return Result.success("批量销售成功", data);
    }

    /**
     * 获取商品售价（用于前端销售时显示）
     */
    @GetMapping("/price/{goodsId}")
    public Result<Map<String, Object>> getGoodsSalePrice(@PathVariable String goodsId) {
        Goods goods = goodsService.getGoodsById(goodsId);
        if (goods == null) {
            return Result.error("商品不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("goodsId", goods.getGoodsId());
        data.put("goodsName", goods.getGoodsName());
        data.put("salePrice", goods.getSalePrice());
        data.put("stockNum", goods.getStockNum());

        return Result.success(data);
    }

    /**
     * 查询商品的销售记录
     */
    @GetMapping("/records/{goodsId}")
    public Result<List<SaleRecordDTO>> getSaleRecords(@PathVariable String goodsId) {
        List<Sale> records = saleService.getSaleRec(goodsId);
        List<SaleRecordDTO> dtoList = records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    private SaleRecordDTO convertToDTO(Sale record) {
        SaleRecordDTO dto = new SaleRecordDTO();
        dto.setSaleId(record.getSaleId());
        dto.setGoodsId(record.getGoodsId());
        dto.setSaleNum(record.getSaleNum());
        dto.setSalePrice(record.getSalePrice());
        dto.setSaleTime(record.getSaleTime());
        dto.setUserId(record.getUserId());
        return dto;
    }
}