package com.mapper;

import com.domain.InStock;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface InStockMapper {

    /**
     *插入进货记录
     */
    @Insert("<script>"+
            "INSERT INTO in_stock (inId,supplyId,goodsId,inNum,inPrice,inTime)"+
            "VALUES (#{inId},#{supplyId},#{goodsId},#{inNum},#{inPrice},#{inTime})"+
            "</script>"
    )
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
}
