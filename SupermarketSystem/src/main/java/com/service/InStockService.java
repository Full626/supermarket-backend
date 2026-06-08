package com.service;


import com.domain.Goods;
import com.domain.InStock;
import com.mapper.InStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class InStockService {
    @Autowired
    private InStockMapper inStockMapper;

    @Autowired
    private GoodsService goodsService;

    //查询商品的进货记录
    public List<InStock> getInStockRec(String goodsId){
        List<InStock> records = inStockMapper.selectByGoodsId(goodsId);
        // 修复：防止 Mapper 返回 null 导致 NPE
        return records != null ? records : new ArrayList<>();
    }

    //添加进货记录
    public int setInStockRec(InStock inStock){
        return inStockMapper.insertInStock(inStock);
    }

    /**
     * 进货处理（完整版）
     * 自动完成：保存进货记录、更新或插入商品信息、自动计算售价（进价*1.1）、增加库存
     * @param inStock 进货记录对象
     * @param goods 商品对象（用于新商品创建或旧商品信息更新）
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean purchase(InStock inStock, Goods goods){
        try {
            // 修复：参数校验
            if (inStock == null || goods == null) {
                throw new RuntimeException("进货记录或商品信息不能为空");
            }

            //1. 保存进货记录
            int result = inStockMapper.insertInStock(inStock);
            if (result <= 0) {
                throw new RuntimeException("保存进货记录失败");
            }

            //2. 设置商品进价和售价（售价 = 进价 * 1.1，保留两位小数）
            goods.setInPrice(inStock.getInPrice());
            BigDecimal salePrice = inStock.getInPrice()
                    .multiply(new BigDecimal("1.1"))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            goods.setSalePrice(salePrice);

            //3. 更新或插入商品基础信息
            int goodsResult = goodsService.insertOrUpdateGoodsInfos(goods);
            // 修复：检查商品信息保存结果（INSERT/UPDATE 成功返回 1 或 2）
            if (goodsResult <= 0) {
                throw new RuntimeException("保存商品信息失败");
            }

            //4. 增加商品库存（GoodsService 内部已有检查和返回值判断）
            goodsService.addStockNum(inStock.getGoodsId(), inStock.getInNum());

            return true;
        } catch (Exception e) {
            throw new RuntimeException("进货失败：" + e.getMessage(), e);
        }
    }
}