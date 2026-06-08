package com.controller;

import com.common.Result;
import com.domain.Goods;
import com.domain.InStock;
import com.dto.request.PurchaseRequestDTO;
import com.dto.response.InStockRecordDTO;
import com.service.InStockService;
import com.util.IdGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 进货管理控制器
 */
@RestController
@RequestMapping("/api/inStock")
public class InStockController {

    @Autowired
    private InStockService inStockService;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 进货接口
     */
    @PostMapping("/purchase")
    public Result<String> purchase(@Valid @RequestBody PurchaseRequestDTO request) {
        // 1. 自动生成进货单号
        String inId = idGenerator.generateInStockId();

        // 2. 创建进货记录对象
        InStock inStock = new InStock();
        inStock.setInId(inId);
        inStock.setSupplyId(request.getSupplyId());
        inStock.setGoodsId(request.getGoodsId());
        inStock.setInNum(request.getInNum());
        inStock.setInPrice(request.getInPrice());
        inStock.setInTime(LocalDateTime.now());

        // 3. 构建商品对象
        Goods goods = new Goods();
        goods.setGoodsId(request.getGoodsId());
        goods.setGoodsName(request.getGoodsName());
        goods.setTypeId(request.getTypeId());
        goods.setSupplyId(request.getSupplyId());
        goods.setInPrice(request.getInPrice());
        goods.setWarnNum(request.getWarnNum() != null ? request.getWarnNum() : 10);

        // 4. 执行进货
        inStockService.purchase(inStock, goods);

        return Result.success("进货成功", inId);
    }

    /**
     * 查询商品的进货记录
     */
    @GetMapping("/records/{goodsId}")
    public Result<List<InStockRecordDTO>> getInStockRecords(@PathVariable String goodsId) {
        List<InStock> records = inStockService.getInStockRec(goodsId);
        List<InStockRecordDTO> dtoList = records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    private InStockRecordDTO convertToDTO(InStock record) {
        InStockRecordDTO dto = new InStockRecordDTO();
        dto.setInId(record.getInId());
        dto.setSupplyId(record.getSupplyId());
        dto.setGoodsId(record.getGoodsId());
        dto.setInNum(record.getInNum());
        dto.setInPrice(record.getInPrice());
        dto.setInTime(record.getInTime());
        return dto;
    }
}