package com.service;

import com.domain.Goods;
import com.mapper.GoodsMapper;
import com.mapper.InStockMapper;
import com.mapper.SaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private InStockMapper inStockMapper;
    @Autowired
    private SaleMapper saleMapper;

    private static final BigDecimal SALE_PRICE_MULTIPLIER = new BigDecimal("1.1");

    public List<Goods> getGoodsList(String goodsId, String goodsName) {
        List<Goods> goodsList = goodsMapper.searchGoods(goodsId, goodsName);
        return goodsList != null ? goodsList : new ArrayList<>();
    }

    /**
     * 根据商品号精确查询
     */
    public Goods getGoodsById(String goodsId) {
        if (goodsId == null || goodsId.trim().isEmpty()) {
            return null;
        }
        return goodsMapper.findByGoodsId(goodsId);
    }

    /**
     * 根据商品名称查询所有匹配的商品（同名不同供应商）
     */
    public List<Goods> getGoodsByGoodsName(String goodsName) {
        if (goodsName == null || goodsName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Goods> goodsList = goodsMapper.findByGoodsName(goodsName);
        return goodsList != null ? goodsList : new ArrayList<>();
    }

    /**
     * 根据供应商和商品名称精确查询
     */
    public Goods getGoodsBySupplyIdAndGoodsName(String supplyId, String goodsName) {
        if (supplyId == null || goodsName == null) {
            return null;
        }
        return goodsMapper.findBySupplyIdAndGoodsName(supplyId, goodsName);
    }

    /**
     * 检查同一供应商下商品名称是否已存在
     */
    public boolean existsBySupplyIdAndGoodsName(String supplyId, String goodsName) {
        if (supplyId == null || goodsName == null) {
            return false;
        }
        return goodsMapper.countBySupplyIdAndGoodsName(supplyId, goodsName) > 0;
    }

    public int addStockNum(String goodsId, Integer addNum) {
        if (addNum == null || addNum <= 0) {
            throw new RuntimeException("增加数量必须大于0");
        }
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        int result = goodsMapper.addStock(goodsId, addNum);
        if (result <= 0) {
            throw new RuntimeException("增加库存失败");
        }
        return result;
    }

    public int reduceStockNum(String goodsId, Integer saleNum) {
        if (saleNum == null || saleNum <= 0) {
            throw new RuntimeException("减少数量必须大于0");
        }
        int result = goodsMapper.reduceStock(goodsId, saleNum);
        if (result <= 0) {
            if (!existsById(goodsId)) {
                throw new RuntimeException("商品不存在：" + goodsId);
            }
            throw new RuntimeException("库存不足，减少库存失败");
        }
        return result;
    }

    public int changeSalePrice(String goodsId, BigDecimal salePrice) {
        if (salePrice == null || salePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("售价必须大于0");
        }
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        return goodsMapper.updateSalePrice(goodsId, salePrice);
    }

    public int changeInPrice(String goodsId, BigDecimal inPrice) {
        if (inPrice == null || inPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("进价必须大于0");
        }
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        return goodsMapper.updateInPrice(goodsId, inPrice);
    }

    public int changeInPriceAndSalePrice(String goodsId, BigDecimal inPrice) {
        if (inPrice == null || inPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("进价必须大于0");
        }
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        BigDecimal salePrice = inPrice.multiply(SALE_PRICE_MULTIPLIER)
                .setScale(2, RoundingMode.HALF_UP);
        return goodsMapper.updateInPriceAndSalePrice(goodsId, inPrice, salePrice);
    }

    public int changeWarnNum(String goodsId, Integer warnNum) {
        if (warnNum == null || warnNum < 0) {
            throw new RuntimeException("预警数量不能为负数");
        }
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }
        return goodsMapper.updateWarnNum(goodsId, warnNum);
    }

    public boolean existsById(String goodsId) {
        return getGoodsById(goodsId) != null;
    }

    public int insertOrUpdateGoodsInfos(Goods goods) {
        if (goods == null || goods.getGoodsId() == null || goods.getGoodsId().trim().isEmpty()) {
            throw new RuntimeException("商品信息不完整");
        }
        return goodsMapper.insertOrUpdateGoodsInfo(goods);
    }

    public List<Goods> getLowStockGoods() {
        List<Goods> goodsList = goodsMapper.selectLowStockGoods();
        return goodsList != null ? goodsList : new ArrayList<>();
    }

    /**
     * 新增商品（检查同一供应商下商品名称是否重复）
     */
    public int insertGoods(Goods goods) {
        if (goods == null || goods.getGoodsId() == null || goods.getGoodsId().trim().isEmpty()) {
            throw new RuntimeException("商品信息不完整");
        }
        if (existsById(goods.getGoodsId())) {
            throw new RuntimeException("商品ID已存在：" + goods.getGoodsId());
        }
        if (existsBySupplyIdAndGoodsName(goods.getSupplyId(), goods.getGoodsName())) {
            throw new RuntimeException("该供应商下已存在商品名称「" + goods.getGoodsName() + "」，请使用正确的商品号进货");
        }
        if (goods.getStockNum() == null) goods.setStockNum(0);
        if (goods.getWarnNum() == null) goods.setWarnNum(10);
        if (goods.getSalePrice() == null && goods.getInPrice() != null) {
            goods.setSalePrice(goods.getInPrice().multiply(SALE_PRICE_MULTIPLIER)
                    .setScale(2, RoundingMode.HALF_UP));
        }
        return goodsMapper.insertOrUpdateGoodsInfo(goods);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGoods(String goodsId) {
        if (goodsId == null || goodsId.trim().isEmpty()) {
            throw new RuntimeException("商品ID不能为空");
        }
        if (!existsById(goodsId)) {
            throw new RuntimeException("商品不存在：" + goodsId);
        }

        int inStockCount = inStockMapper.countByGoodsId(goodsId);
        if (inStockCount > 0) {
            throw new RuntimeException("该商品存在" + inStockCount + "条进货记录，无法删除");
        }

        int saleCount = saleMapper.countByGoodsId(goodsId);
        if (saleCount > 0) {
            throw new RuntimeException("该商品存在" + saleCount + "条销售记录，无法删除");
        }

        int result = goodsMapper.deleteGoods(goodsId);
        return result > 0;
    }

    public boolean hasInStockRecords(String goodsId) {
        if (goodsId == null || goodsId.trim().isEmpty()) {
            return false;
        }
        return inStockMapper.countByGoodsId(goodsId) > 0;
    }

    public boolean hasSaleRecords(String goodsId) {
        if (goodsId == null || goodsId.trim().isEmpty()) {
            return false;
        }
        return saleMapper.countByGoodsId(goodsId) > 0;
    }
}