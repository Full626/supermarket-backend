package com.mapper;

import com.domain.Sale;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SaleMapper {

    /**
     * 插入销售记录
     */
    @Insert("INSERT INTO sale (saleId, goodsId, saleNum, salePrice, saleTime, userId) " +
            "VALUES (#{saleId}, #{goodsId}, #{saleNum}, #{salePrice}, #{saleTime}, #{userId})")
    int insertSale(Sale sale);

    /**
     * 查询销售记录
     */
    @Select("SELECT * FROM sale WHERE goodsId = #{goodsId} ORDER BY saleTime DESC")
    List<Sale> selectByGoodsId(@Param("goodsId") String goodsId);

    /**
     * 获取今日销售统计
     */
    @Select("SELECT COUNT(*) as count, SUM(saleNum) as totalNum, SUM(saleNum * salePrice) as totalAmount " +
            "FROM sale WHERE DATE(saleTime) = CURDATE()")
    Map<String, Object> getTodaySaleStat();

    /**
     * 获取月度销售统计
     */
    @Select("SELECT DATE_FORMAT(saleTime, '%Y-%m') as month, " +
            "COUNT(*) as totalSales, " +
            "SUM(saleNum) as totalQuantity, " +
            "SUM(saleNum * salePrice) as totalAmount " +
            "FROM sale " +
            "GROUP BY DATE_FORMAT(saleTime, '%Y-%m') " +
            "ORDER BY month DESC")
    List<Map<String, Object>> getMonthlySaleStat();

    /**
     * 条件查询销售记录
     */
    @Select("<script>" +
            "SELECT * FROM sale WHERE 1=1 " +
            "<if test='goodsId != null and goodsId != \"\"'>" +
            " AND goodsId = #{goodsId}" +
            "</if>" +
            "<if test='startDate != null and startDate != \"\"'>" +
            " AND DATE(saleTime) &gt;= #{startDate}" +
            "</if>" +
            "<if test='endDate != null and endDate != \"\"'>" +
            " AND DATE(saleTime) &lt;= #{endDate}" +
            "</if>" +
            " ORDER BY saleTime DESC" +
            "</script>")
    List<Sale> selectByCondition(@Param("goodsId") String goodsId,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate);

    /**
     * 统计指定商品的销售记录数量（用于删除前检查）
     */
    @Select("SELECT COUNT(*) FROM sale WHERE goodsId = #{goodsId}")
    int countByGoodsId(@Param("goodsId") String goodsId);
}