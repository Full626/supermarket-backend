package com.mapper;

import com.domain.Goods;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GoodsMapper {

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
    List<Goods> searchGoods(@Param("goodsId") String goodsId,
                            @Param("goodsName") String goodsName);

    /**
     * 根据商品号精确查询
     */
    @Select("SELECT goodsId, goodsName, typeId, supplyId, inPrice, salePrice, stockNum, warnNum " +
            "FROM goods WHERE goodsId = #{goodsId}")
    Goods findByGoodsId(@Param("goodsId") String goodsId);

    /**
     * 根据供应商ID和商品名称精确查询
     */
    @Select("SELECT goodsId, goodsName, typeId, supplyId, inPrice, salePrice, stockNum, warnNum " +
            "FROM goods WHERE supplyId = #{supplyId} AND goodsName = #{goodsName}")
    Goods findBySupplyIdAndGoodsName(@Param("supplyId") String supplyId,
                                     @Param("goodsName") String goodsName);

    /**
     * 根据商品名称查询所有匹配的商品（用于处理同名不同供应商）
     */
    @Select("SELECT goodsId, goodsName, typeId, supplyId, inPrice, salePrice, stockNum, warnNum " +
            "FROM goods WHERE goodsName = #{goodsName}")
    List<Goods> findByGoodsName(@Param("goodsName") String goodsName);

    /**
     * 检查同一供应商下商品名称是否已存在
     */
    @Select("SELECT COUNT(*) FROM goods WHERE supplyId = #{supplyId} AND goodsName = #{goodsName}")
    int countBySupplyIdAndGoodsName(@Param("supplyId") String supplyId,
                                    @Param("goodsName") String goodsName);

    @Update("UPDATE goods SET stockNum = stockNum + #{addNum} WHERE goodsId = #{goodsId}")
    int addStock(@Param("goodsId") String goodsId, @Param("addNum") Integer addNum);

    @Update("UPDATE goods SET stockNum = stockNum - #{saleNum} WHERE goodsId = #{goodsId} AND stockNum >= #{saleNum}")
    int reduceStock(@Param("goodsId") String goodsId, @Param("saleNum") Integer saleNum);

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

    @Update("UPDATE goods SET salePrice = #{salePrice} WHERE goodsId = #{goodsId}")
    int updateSalePrice(@Param("goodsId") String goodsId, @Param("salePrice") BigDecimal salePrice);

    @Update("UPDATE goods SET inPrice = #{inPrice} WHERE goodsId = #{goodsId}")
    int updateInPrice(@Param("goodsId") String goodsId, @Param("inPrice") BigDecimal inPrice);

    @Update("UPDATE goods SET inPrice = #{inPrice}, salePrice = #{salePrice} WHERE goodsId = #{goodsId}")
    int updateInPriceAndSalePrice(@Param("goodsId") String goodsId,
                                  @Param("inPrice") BigDecimal inPrice,
                                  @Param("salePrice") BigDecimal salePrice);

    @Update("UPDATE goods SET warnNum = #{warnNum} WHERE goodsId = #{goodsId}")
    int updateWarnNum(@Param("goodsId") String goodsId, @Param("warnNum") Integer warnNum);

    @Select("SELECT goodsId, goodsName, typeId, supplyId, inPrice, salePrice, stockNum, warnNum " +
            "FROM goods WHERE stockNum < warnNum")
    List<Goods> selectLowStockGoods();

    @Select("SELECT COUNT(*) FROM goods")
    int getTotalGoodsCount();

    @Select("SELECT COUNT(*) FROM goods WHERE stockNum < warnNum")
    int getLowStockCount();

    @Delete("DELETE FROM goods WHERE goodsId = #{goodsId}")
    int deleteGoods(@Param("goodsId") String goodsId);
}