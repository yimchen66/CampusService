package com.example.model.request.order;

import com.example.model.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-22  11:01
 * @Description: 用户创造一个订单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String title;
    private String content;
    private double price;
    private String address;
    private String label;
    private String deadLine;
    private String hostId;
    private String pictureUrls;
    private User user;
}
