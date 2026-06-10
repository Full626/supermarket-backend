package com.controller;

import com.common.Result;
import com.domain.Goods;
import com.dto.response.GoodsAutoFillDTO;
import com.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品自动填充接口
 */
@RestController
@RequestMapping("/api/goods")
public class GoodsQueryController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据商品号查询商品信息（用于自动填充）
     */
    @GetMapping("/query/byId")
    public Result<GoodsAutoFillDTO> queryByGoodsId(@RequestParam String goodsId) {
        Goods goods = goodsService.getGoodsById(goodsId);
        if (goods == null) {
            return Result.success(null);
        }
        return Result.success(convertToAutoFillDTO(goods));
    }

    /**
     * 根据商品名称查询商品列表（用于自动填充，可能返回多条）
     */
    @GetMapping("/query/byName")
    public Result<List<GoodsAutoFillDTO>> queryByGoodsName(@RequestParam String goodsName) {
        List<Goods> goodsList = goodsService.getGoodsByGoodsName(goodsName);
        List<GoodsAutoFillDTO> dtoList = goodsList.stream()
                .map(this::convertToAutoFillDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    private GoodsAutoFillDTO convertToAutoFillDTO(Goods goods) {
        GoodsAutoFillDTO dto = new GoodsAutoFillDTO();
        dto.setGoodsId(goods.getGoodsId());
        dto.setGoodsName(goods.getGoodsName());
        dto.setTypeId(goods.getTypeId());
        dto.setSupplyId(goods.getSupplyId());
        dto.setWarnNum(goods.getWarnNum());
        return dto;
    }
}