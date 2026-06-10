package com.mapper;

import com.domain.InStock;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface InStockMapper {

    /**
     * 插入进货记录
     */
    @Insert("INSERT INTO in_stock (inId, supplyId, goodsId, inNum, inPrice, inTime) " +
            "VALUES (#{inId}, #{supplyId}, #{goodsId}, #{inNum}, #{inPrice}, #{inTime})")
    int insertInStock(InStock inStock);

    /**
     * 查询进货记录
     */
    @Select("SELECT * FROM in_stock WHERE goodsId = #{goodsId} ORDER BY inTime DESC")
    List<InStock> selectByGoodsId(@Param("goodsId") String goodsId);

    /**
     * 获取今日进货统计
     */
    @Select("SELECT COUNT(*) as count, SUM(inNum) as totalNum, SUM(inNum * inPrice) as totalAmount " +
            "FROM in_stock WHERE DATE(inTime) = CURDATE()")
    Map<String, Object> getTodayInStockStat();

    /**
     * 条件查询进货记录
     */
    @Select("<script>" +
            "SELECT * FROM in_stock WHERE 1=1 " +
            "<if test='goodsId != null and goodsId != \"\"'>" +
            " AND goodsId = #{goodsId}" +
            "</if>" +
            "<if test='supplyId != null and supplyId != \"\"'>" +
            " AND supplyId = #{supplyId}" +
            "</if>" +
            "<if test='startDate != null and startDate != \"\"'>" +
            " AND DATE(inTime) &gt;= #{startDate}" +
            "</if>" +
            "<if test='endDate != null and endDate != \"\"'>" +
            " AND DATE(inTime) &lt;= #{endDate}" +
            "</if>" +
            " ORDER BY inTime DESC" +
            "</script>")
    List<InStock> selectByCondition(@Param("goodsId") String goodsId,
                                    @Param("supplyId") String supplyId,
                                    @Param("startDate") String startDate,
                                    @Param("endDate") String endDate);

    /**
     * 统计指定商品的进货记录数量（用于删除前检查）
     */
    @Select("SELECT COUNT(*) FROM in_stock WHERE goodsId = #{goodsId}")
    int countByGoodsId(@Param("goodsId") String goodsId);

    /**
     * 统计指定供应商的进货记录数量（用于删除前检查）
     */
    @Select("SELECT COUNT(*) FROM in_stock WHERE supplyId = #{supplyId}")
    int countBySupplyId(@Param("supplyId") String supplyId);
}