package com.controller;

import com.common.Result;
import com.domain.Goods;
import com.dto.request.GoodsPriceDTO;
import com.dto.request.GoodsQueryDTO;
import com.dto.response.GoodsDetailDTO;
import com.dto.response.GoodsListDTO;
import com.service.GoodsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理控制器
 */
@RestController
@RequestMapping("/api/goods")
@Validated
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 查询商品列表
     */
    @GetMapping("/list")
    public Result<List<GoodsListDTO>> getGoodsList(@Valid GoodsQueryDTO query) {
        List<Goods> goodsList = goodsService.getGoodsList(query.getGoodsId(), query.getGoodsName());
        List<GoodsListDTO> dtoList = goodsList.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    /**
     * 根据商品号查询商品详情
     */
    @GetMapping("/{goodsId}")
    public Result<GoodsDetailDTO> getGoodsById(@PathVariable @NotBlank String goodsId) {
        Goods goods = goodsService.getGoodsById(goodsId);
        if (goods == null) {
            return Result.error("商品不存在");
        }
        return Result.success(convertToDetailDTO(goods));
    }

    /**
     * 修改商品售价
     */
    @PutMapping("/{goodsId}/salePrice")
    public Result<Void> updateSalePrice(
            @PathVariable @NotBlank String goodsId,
            @Valid @RequestBody GoodsPriceDTO priceDTO) {
        goodsService.changeSalePrice(goodsId, priceDTO.getSalePrice());
        return Result.success("修改售价成功", null);
    }

    /**
     * 修改商品进价（售价不会自动更新）
     */
    @PutMapping("/{goodsId}/inPrice")
    public Result<Void> updateInPrice(
            @PathVariable @NotBlank String goodsId,
            @RequestParam @DecimalMin(value = "0.01", message = "进价必须大于0") BigDecimal inPrice) {
        goodsService.changeInPrice(goodsId, inPrice);
        return Result.success("修改进价成功", null);
    }

    /**
     * 修改商品进价并自动同步售价（售价 = 进价 * 1.1）
     */
    @PutMapping("/{goodsId}/inPriceAndSalePrice")
    public Result<Void> updateInPriceAndSalePrice(
            @PathVariable @NotBlank String goodsId,
            @RequestParam @DecimalMin(value = "0.01", message = "进价必须大于0") BigDecimal inPrice) {
        goodsService.changeInPriceAndSalePrice(goodsId, inPrice);
        return Result.success("修改进价并同步售价成功", null);
    }

    /**
     * 修改商品预警数量
     */
    @PutMapping("/{goodsId}/warnNum")
    public Result<Void> updateWarnNum(
            @PathVariable @NotBlank String goodsId,
            @RequestParam @jakarta.validation.constraints.Min(value = 0, message = "预警数量不能小于0") Integer warnNum) {
        goodsService.changeWarnNum(goodsId, warnNum);
        return Result.success("修改预警数量成功", null);
    }

    /**
     * 查询低库存商品（库存低于预警值）
     */
    @GetMapping("/lowStock")
    public Result<List<GoodsListDTO>> getLowStockGoods() {
        List<Goods> goodsList = goodsService.getLowStockGoods();
        List<GoodsListDTO> dtoList = goodsList.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    // ========== 转换方法 ==========

    private GoodsListDTO convertToListDTO(Goods goods) {
        GoodsListDTO dto = new GoodsListDTO();
        dto.setGoodsId(goods.getGoodsId());
        dto.setGoodsName(goods.getGoodsName());
        dto.setSalePrice(goods.getSalePrice());
        dto.setStockNum(goods.getStockNum());
        dto.setWarnNum(goods.getWarnNum());
        return dto;
    }

    private GoodsDetailDTO convertToDetailDTO(Goods goods) {
        GoodsDetailDTO dto = new GoodsDetailDTO();
        dto.setGoodsId(goods.getGoodsId());
        dto.setGoodsName(goods.getGoodsName());
        dto.setTypeId(goods.getTypeId());
        dto.setSupplyId(goods.getSupplyId());
        dto.setSalePrice(goods.getSalePrice());
        dto.setStockNum(goods.getStockNum());
        dto.setWarnNum(goods.getWarnNum());
        return dto;
    }
}