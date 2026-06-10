package com.dto.response;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO<T> {
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;  // 改为 Integer，与 PageInfo.getPages() 一致
    private List<T> list;

    public static <T> PageResponseDTO<T> of(PageInfo<T> pageInfo) {
        PageResponseDTO<T> response = new PageResponseDTO<>();
        response.setTotal(pageInfo.getTotal());
        response.setPageNum(pageInfo.getPageNum());
        response.setPageSize(pageInfo.getPageSize());
        response.setPages(pageInfo.getPages());  // 直接赋值，无需转换
        response.setList(pageInfo.getList());
        return response;
    }
}