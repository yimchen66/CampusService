package com.example.model.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-21  15:49
 * @Description: 分页订单请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageOrderRequest {
    private int pages;

    private int pageNumber;

    private String column;

    private String sortedType;

    public PageOrderRequest(int pages, int pageNumber) {
        this.pages = pages;
        this.pageNumber = pageNumber;
    }

}
