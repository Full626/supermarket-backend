package com.mapper;

import com.domain.Goods;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper

public interface GoodsMapper {


    /**
     * 查询商品信息
     * 通过goodsId或goodsName模糊查询
     */

    //<script> 标签是 MyBatis 中用于支持复杂动态 SQL 的解决方案
    //不支持多行
    @Select("<script>" +
            "SELECT goodsId, goodsName, typeId, supplyId, stockNum, warnNum, inPrice, salePrice " +
            "FROM goods " +
            "WHERE 1=1 " +
            "<if test='goodsId != null and goodsId != \"\"'>" +
            " AND goodsId LIKE CONCAT('%', #{goodsId}, '%')" +
            "</if>" +
            "<if test='goodsName != null and goodsName != \"\"'>" +
            " AND goodsName LIKE CONCAT('%', #{goodsName}, '%')" +
            "</if>" +
            "</script>")
    public List<Goods> searchGoods(@Param("goodsId") String goodsId,
                                   @Param("goodsName") String goodsName);


    /**
     * 进货时：增加库存
     */
    @Update("UPDATE goods SET stockNum = stockNum + #{addNum} WHERE goodsId = #{goodsId}")
    int addStock(@Param("goodsId") String goodsId, @Param("addNum") Integer addNum);

    /**
     * 销售时：减少库存
     * 修复：WHERE 条件中的 stockNum >= saleNum 保证了原子性和库存充足检查
     * 返回影响行数：1表示成功，0表示库存不足或商品不存在
     */
    @Update("UPDATE goods SET stockNum = stockNum - #{saleNum} WHERE goodsId = #{goodsId} AND stockNum >= #{saleNum}")
    int reduceStock(@Param("goodsId") String goodsId, @Param("saleNum") Integer saleNum);

    /**
     * 新增或更新商品基础信息（不包含库存）
     * 其中goodsId由进货时确定
     * 同时库存要在进货或者销售时更新
     * 此处为在进货时或者销售自动调用此方法，在别处使用事务来完成
     * 先判断商品是否存在，存在则在原数据上更改，否则添加
     */
    // 多行格式，需要用 script 包裹
    //ON DUPLICATE KEY UPDATE
    //这是 MySQL 数据库特有的语法，意思是：如果主键或唯一索引冲突就更新，否则插入。
    @Insert("<script>" +
            "INSERT INTO goods (goodsId, goodsName, typeId, supplyId, inPrice, salePrice, warnNum) " +
            "VALUES (#{goodsId}, #{goodsName}, #{typeId}, #{supplyId}, #{inPrice}, #{salePrice}, #{warnNum}) " +
            "ON DUPLICATE KEY UPDATE " +
            "goodsName = #{goodsName}, " +
            "typeId = #{typeId}, " +
            "supplyId = #{supplyId}, " +
            "inPrice = #{inPrice}, " +
            "salePrice = #{salePrice}, " +
            "warnNum = #{warnNum}" +
            "</script>")
    int insertOrUpdateGoodsInfo(Goods goods);

    /**
     * 修改售价，默认是进价*1.1
     * 支持手动修改售价
     * @param goodsId 商品编号
     * @param salePrice 新售价
     * @return 影响行数
     */
    @Update("UPDATE goods SET salePrice = #{salePrice} WHERE goodsId = #{goodsId}")
    int updateSalePrice(@Param("goodsId") String goodsId, @Param("salePrice") BigDecimal salePrice);

    /**
     * 修改进价
     * 注意：修改进价不会自动修改售价，如需同步更新售价请调用 updatePriceWithSale
     */
    @Update("UPDATE goods SET inPrice = #{inPrice} WHERE goodsId = #{goodsId}")
    int updateInPrice(@Param("goodsId") String goodsId, @Param("inPrice") BigDecimal inPrice);

    /**
     * 修改进价，并自动按进价*1.1更新售价
     * 注：售价由 Java 层计算后传入，避免 SQL 精度问题
     */
    @Update("UPDATE goods SET inPrice = #{inPrice}, salePrice = #{salePrice} WHERE goodsId = #{goodsId}")
    int updateInPriceAndSalePrice(@Param("goodsId") String goodsId,
                                  @Param("inPrice") BigDecimal inPrice,
                                  @Param("salePrice") BigDecimal salePrice);

    /**
     * 修改预警数量
     */
    @Update("UPDATE goods SET warnNum = #{warnNum} WHERE goodsId = #{goodsId}")
    int updateWarnNum(@Param("goodsId") String goodsId, @Param("warnNum") Integer warnNum);

    /**
     * 查预警的库存商品信息
     */
    @Select("SELECT goodsId, goodsName, typeId, supplyId, inPrice, salePrice, stockNum, warnNum " +
            "FROM goods WHERE stockNum < warnNum")
    List<Goods> selectLowStockGoods();

    /**
     * 获取商品总数
     */
    @Select("SELECT COUNT(*) FROM goods")
    int getTotalGoodsCount();

    /**
     * 获取低库存商品数
     */
    @Select("SELECT COUNT(*) FROM goods WHERE stockNum < warnNum")
    int getLowStockCount();
}