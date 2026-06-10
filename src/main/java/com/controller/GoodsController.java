package com.controller;

import com.annotation.RoleRequired;
import com.common.Result;
import com.constant.Constants;
import com.domain.Goods;
import com.dto.request.GoodsCreateRequestDTO;
import com.dto.request.GoodsPriceDTO;
import com.dto.request.GoodsQueryDTO;
import com.dto.request.PageRequestDTO;
import com.dto.response.GoodsDetailDTO;
import com.dto.response.GoodsListDTO;
import com.dto.response.PageResponseDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.GoodsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理控制器
 */
@RestController
@RequestMapping("/api/goods")
@Validated
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 查询商品列表（支持分页）
     * 权限：所有登录用户
     */
    @GetMapping("/list")
    public Result<PageResponseDTO<GoodsListDTO>> getGoodsList(
            @Valid GoodsQueryDTO query,
            @Valid PageRequestDTO pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<Goods> goodsList = goodsService.getGoodsList(query.getGoodsId(), query.getGoodsName());
        PageInfo<Goods> pageInfo = new PageInfo<>(goodsList);

        List<GoodsListDTO> dtoList = pageInfo.getList().stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());

        PageResponseDTO<GoodsListDTO> response = new PageResponseDTO<>();
        response.setTotal(pageInfo.getTotal());
        response.setPageNum(pageInfo.getPageNum());
        response.setPageSize(pageInfo.getPageSize());
        response.setPages(pageInfo.getPages());
        response.setList(dtoList);

        return Result.success(response);
    }

    /**
     * 根据商品号查询商品详情
     * 权限：所有登录用户
     */
    @GetMapping("/{goodsId}")
    public Result<GoodsDetailDTO> getGoodsById(@PathVariable @NotBlank String goodsId) {
        Goods goods = goodsService.getGoodsById(goodsId);
        if (goods == null) {
            return Result.error("商品不存在");
        }
        return Result.success(convertToDetailDTO(goods));
    }

    /**
     * 修改商品售价
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PutMapping("/{goodsId}/salePrice")
    public Result<Void> updateSalePrice(
            @PathVariable @NotBlank String goodsId,
            @Valid @RequestBody GoodsPriceDTO priceDTO) {
        goodsService.changeSalePrice(goodsId, priceDTO.getSalePrice());
        return Result.success("修改售价成功", null);
    }

    /**
     * 修改商品进价（售价不会自动更新）
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PutMapping("/{goodsId}/inPrice")
    public Result<Void> updateInPrice(
            @PathVariable @NotBlank String goodsId,
            @RequestParam @DecimalMin(value = "0.01", message = "进价必须大于0") BigDecimal inPrice) {
        goodsService.changeInPrice(goodsId, inPrice);
        return Result.success("修改进价成功", null);
    }

    /**
     * 修改商品进价并自动同步售价（售价 = 进价 * 1.1）
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PutMapping("/{goodsId}/inPriceAndSalePrice")
    public Result<Void> updateInPriceAndSalePrice(
            @PathVariable @NotBlank String goodsId,
            @RequestParam @DecimalMin(value = "0.01", message = "进价必须大于0") BigDecimal inPrice) {
        goodsService.changeInPriceAndSalePrice(goodsId, inPrice);
        return Result.success("修改进价并同步售价成功", null);
    }

    /**
     * 修改商品预警数量
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PutMapping("/{goodsId}/warnNum")
    public Result<Void> updateWarnNum(
            @PathVariable @NotBlank String goodsId,
            @RequestParam @jakarta.validation.constraints.Min(value = 0, message = "预警数量不能小于0") Integer warnNum) {
        goodsService.changeWarnNum(goodsId, warnNum);
        return Result.success("修改预警数量成功", null);
    }

    /**
     * 查询低库存商品（库存低于预警值）
     * 权限：所有登录用户
     */
    @GetMapping("/lowStock")
    public Result<List<GoodsListDTO>> getLowStockGoods() {
        List<Goods> goodsList = goodsService.getLowStockGoods();
        List<GoodsListDTO> dtoList = goodsList.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    /**
     * 新增商品
     * 权限：管理员(admin)、经理(manager)
     */
    @RoleRequired({Constants.ROLE_ADMIN, Constants.ROLE_MANAGER})
    @PostMapping("/add")
    public Result<Void> addGoods(@Valid @RequestBody GoodsCreateRequestDTO request) {
        Goods goods = new Goods();
        goods.setGoodsId(request.getGoodsId());
        goods.setGoodsName(request.getGoodsName());
        goods.setTypeId(request.getTypeId());
        goods.setSupplyId(request.getSupplyId());
        goods.setInPrice(request.getInPrice());
        goods.setSalePrice(request.getSalePrice());
        goods.setStockNum(request.getStockNum() != null ? request.getStockNum() : 0);
        goods.setWarnNum(request.getWarnNum() != null ? request.getWarnNum() : 10);

        goodsService.insertGoods(goods);
        return Result.success("添加成功", null);
    }

    /**
     * 删除商品
     * 权限：仅管理员(admin)
     */
    @RoleRequired(Constants.ROLE_ADMIN)
    @DeleteMapping("/{goodsId}")
    public Result<Void> deleteGoods(@PathVariable @NotBlank String goodsId) {
        goodsService.deleteGoods(goodsId);
        return Result.success("删除成功", null);
    }

    // ========== 转换方法 ==========

    private GoodsListDTO convertToListDTO(Goods goods) {
        GoodsListDTO dto = new GoodsListDTO();
        dto.setGoodsId(goods.getGoodsId());
        dto.setGoodsName(goods.getGoodsName());
        dto.setSalePrice(goods.getSalePrice());
        dto.setStockNum(goods.getStockNum());
        dto.setWarnNum(goods.getWarnNum());
        return dto;
    }

    private GoodsDetailDTO convertToDetailDTO(Goods goods) {
        GoodsDetailDTO dto = new GoodsDetailDTO();
        dto.setGoodsId(goods.getGoodsId());
        dto.setGoodsName(goods.getGoodsName());
        dto.setTypeId(goods.getTypeId());
        dto.setSupplyId(goods.getSupplyId());
        dto.setSalePrice(goods.getSalePrice());
        dto.setStockNum(goods.getStockNum());
        dto.setWarnNum(goods.getWarnNum());
        return dto;
    }
}