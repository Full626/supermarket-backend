package com.service;

import com.domain.Goods;
import com.domain.Sale;
import com.mapper.SaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {
    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private GoodsService goodsService;

    //查询商品的销售记录
    public List<Sale> getSaleRec(String goodsId){
        List<Sale> records = saleMapper.selectByGoodsId(goodsId);
        return records != null ? records : new ArrayList<>();
    }

    //添加销售记录
    public int setSaleRec(Sale sale){
        return saleMapper.insertSale(sale);
    }

    /**
     * 销售处理（事务处理）
     * 自动完成：检查库存是否充足、获取商品售价、保存销售记录、扣减库存
     * @param sale 销售记录对象（不包含售价）
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean sell(Sale sale){
        try {
            if (sale == null) {
                throw new RuntimeException("销售记录不能为空");
            }

            // 获取商品信息（包含售价）
            Goods goods = goodsService.getGoodsById(sale.getGoodsId());
            if (goods == null) {
                throw new RuntimeException("商品不存在：" + sale.getGoodsId());
            }

            // 设置售价为商品当前售价
            sale.setSalePrice(goods.getSalePrice());

            //1. 保存销售记录
            int result = saleMapper.insertSale(sale);
            if (result <= 0) {
                throw new RuntimeException("保存销售记录失败");
            }

            //2. 减少库存
            goodsService.reduceStockNum(sale.getGoodsId(), sale.getSaleNum());

            return true;
        } catch (Exception e) {
            throw new RuntimeException("销售失败：" + e.getMessage(), e);
        }
    }

    /**
     * 批量销售处理（事务处理）
     * 一次销售多个不同商品
     * @param sales 销售记录列表（不包含售价）
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSell(List<Sale> sales){
        try {
            if (sales == null || sales.isEmpty()) {
                throw new RuntimeException("销售记录列表不能为空");
            }

            for (Sale sale : sales) {
                // 获取商品信息（包含售价）
                Goods goods = goodsService.getGoodsById(sale.getGoodsId());
                if (goods == null) {
                    throw new RuntimeException("商品不存在：" + sale.getGoodsId());
                }

                // 设置售价为商品当前售价
                sale.setSalePrice(goods.getSalePrice());

                //保存销售记录
                int result = saleMapper.insertSale(sale);
                if (result <= 0) {
                    throw new RuntimeException("保存销售记录失败，商品：" + sale.getGoodsId());
                }

                //减少库存
                goodsService.reduceStockNum(sale.getGoodsId(), sale.getSaleNum());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("批量销售失败：" + e.getMessage(), e);
        }
    }
}