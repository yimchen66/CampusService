package com.example.model.request.order;

import com.example.model.domain.Orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-22  11:01
 * @Description: 用户更新订单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderRequest {
    private String id;
    private String orderTitle;
    private String orderContent;
    private String orderAddress;
    private String orderLabel;
    private String orderDeadline;
    private String orderPictures;
    private Orders orders;
}
