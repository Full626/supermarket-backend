package com.service;


import com.domain.Goods;
import com.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    //获取商品信息
    public List<Goods> getGoodsList(String goodsId, String goodsName){
        List<Goods> goodsList = goodsMapper.searchGoods(goodsId,goodsName);
        // 修复：防止 Mapper 返回 null 导致 NPE
        return goodsList != null ? goodsList : new ArrayList<>();
    }

    //增加商品库存，数量由进货数决定
    public int addStockNum(String goodsId, Integer addNum){
        // 参数校验
        if (addNum == null || addNum <= 0) {
            throw new RuntimeException("增加数量必须大于0");
        }
        // 检查商品是否存在
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        int result = goodsMapper.addStock(goodsId,addNum);
        // 修复：检查更新是否成功
        if (result <= 0) {
            throw new RuntimeException("增加库存失败，商品可能不存在");
        }
        return result;
    }

    //减少商品库存，数量由销售数决定，要库存首先大于销售数
    public int reduceStockNum(String goodsId, Integer saleNum){
        // 参数校验
        if (saleNum == null || saleNum <= 0) {
            throw new RuntimeException("减少数量必须大于0");
        }
        // 修复：直接使用 Mapper 的条件更新，避免并发问题
        // MySQL 的 UPDATE 语句中 WHERE stockNum >= saleNum 保证了原子性
        int result = goodsMapper.reduceStock(goodsId, saleNum);
        if (result <= 0) {
            // 进一步判断是商品不存在还是库存不足
            if (!existsById(goodsId)) {
                throw new RuntimeException("商品不存在：" + goodsId);
            }
            throw new RuntimeException("库存不足，减少库存失败");
        }
        return result;
    }

    //改变商品售价
    public int changeSalePrice(String goodsId, BigDecimal salePrice){
        if (salePrice == null || salePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("售价必须大于0");
        }
        // 修复：检查商品是否存在
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        return goodsMapper.updateSalePrice(goodsId,salePrice);
    }

    //改变商品进价
    public int changeInPrice(String goodsId, BigDecimal inPrice){
        if (inPrice == null || inPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("进价必须大于0");
        }
        // 修复：检查商品是否存在
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        return goodsMapper.updateInPrice(goodsId,inPrice);
    }

    //改变商品进价和售价，此处售价是默认进价*1.1
    public int changeInPriceAndSalePrice(String goodsId, BigDecimal inPrice){
        if (inPrice == null || inPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("进价必须大于0");
        }
        // 修复：检查商品是否存在
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        // 使用 Java 的 BigDecimal 计算，避免 SQL 精度问题
        BigDecimal salePrice = inPrice.multiply(new BigDecimal("1.1"))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        return goodsMapper.updateInPriceAndSalePrice(goodsId, inPrice, salePrice);
    }

    //修改预警数
    public int changeWarnNum(String goodsId, Integer warnNum){
        if (warnNum == null || warnNum < 0) {
            throw new RuntimeException("预警数量不能为负数");
        }
        // 修复：检查商品是否存在
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        return goodsMapper.updateWarnNum(goodsId,warnNum);
    }

    /**
     * 根据商品号获取商品完整信息
     * @param goodsId 商品号
     * @return 商品对象，不存在则返回null
     */
    public Goods getGoodsById(String goodsId){
        List<Goods> goodsList = goodsMapper.searchGoods(goodsId, null);
        if (goodsList != null && !goodsList.isEmpty()) {
            return goodsList.get(0);
        }
        return null;
    }

    /**
     * 检查商品是否存在
     * @param goodsId 商品号
     * @return 是否存在
     */
    public boolean existsById(String goodsId){
        return getGoodsById(goodsId) != null;
    }

    /**
     * 商品信息新增或更新
     * @param goods 商品号
     * @return 略
     */
    public int insertOrUpdateGoodsInfos(Goods goods){
        // 修复：添加基础参数校验
        if (goods == null || goods.getGoodsId() == null || goods.getGoodsId().trim().isEmpty()) {
            throw new RuntimeException("商品信息不完整");
        }
        return goodsMapper.insertOrUpdateGoodsInfo(goods);
    }

    /**
     * 查询低库存商品（库存低于预警值
     * @return
     */
    public List<Goods> getLowStockGoods(){
        List<Goods> goodsList = goodsMapper.selectLowStockGoods();
        // 修复：防止 Mapper 返回 null 导致 NPE
        return goodsList != null ? goodsList : new ArrayList<>();
    }
}