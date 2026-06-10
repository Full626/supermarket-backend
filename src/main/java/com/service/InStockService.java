package com.service;

import com.domain.Goods;
import com.domain.InStock;
import com.mapper.InStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class InStockService {
    @Autowired
    private InStockMapper inStockMapper;

    @Autowired
    private GoodsService goodsService;

    public List<InStock> getInStockRec(String goodsId){
        List<InStock> records = inStockMapper.selectByGoodsId(goodsId);
        return records != null ? records : new ArrayList<>();
    }

    public int setInStockRec(InStock inStock){
        return inStockMapper.insertInStock(inStock);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean purchase(InStock inStock, Goods goods){
        try {
            if (inStock == null || goods == null) {
                throw new RuntimeException("进货记录或商品信息不能为空");
            }

            // 1. 保存进货记录
            int result = inStockMapper.insertInStock(inStock);
            if (result <= 0) {
                throw new RuntimeException("保存进货记录失败");
            }

            // 2. 检查商品是否存在（按商品号）
            Goods existingGoods = goodsService.getGoodsById(inStock.getGoodsId());

            if (existingGoods != null) {
                // ===== 情况1：商品号已存在 =====

                // 校验商品名称是否一致
                if (!existingGoods.getGoodsName().equals(goods.getGoodsName())) {
                    throw new RuntimeException("商品号 " + inStock.getGoodsId() + " 已存在，但商品名称不一致："
                            + "数据库中的名称为「" + existingGoods.getGoodsName() + "」，"
                            + "你输入的名称为「" + goods.getGoodsName() + "」");
                }

                // 校验供应商是否一致
                if (!existingGoods.getSupplyId().equals(goods.getSupplyId())) {
                    throw new RuntimeException("商品号 " + inStock.getGoodsId() + " 已存在，但供应商不一致："
                            + "数据库中的供应商为「" + existingGoods.getSupplyId() + "」，"
                            + "你输入的供应商为「" + goods.getSupplyId() + "」");
                }

                // 只增加库存
                goodsService.addStockNum(inStock.getGoodsId(), inStock.getInNum());

            } else {
                // ===== 情况2：商品号不存在 =====

                // 检查同一供应商下是否已有同名商品
                Goods sameNameGoods = goodsService.getGoodsBySupplyIdAndGoodsName(
                        goods.getSupplyId(), goods.getGoodsName());

                if (sameNameGoods != null) {
                    // 同供应商同名：拒绝
                    throw new RuntimeException("供应商「" + goods.getSupplyId() + "」下已存在商品「"
                            + goods.getGoodsName() + "」（商品号：" + sameNameGoods.getGoodsId() + "），"
                            + "请使用正确的商品号进货，或修改商品名称");
                }

                // 不同供应商可以同名，直接新增商品
                goods.setInPrice(inStock.getInPrice());
                BigDecimal salePrice = inStock.getInPrice()
                        .multiply(new BigDecimal("1.1"))
                        .setScale(2, RoundingMode.HALF_UP);
                goods.setSalePrice(salePrice);
                goods.setStockNum(inStock.getInNum());

                int goodsResult = goodsService.insertGoods(goods);
                if (goodsResult <= 0) {
                    throw new RuntimeException("新增商品失败");
                }
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("进货失败：" + e.getMessage(), e);
        }
    }
}