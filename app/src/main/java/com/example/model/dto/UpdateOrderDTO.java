package com.example.model.dto;

import com.example.model.domain.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-23  11:16
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO implements Serializable {
    private Orders orders_ori;
    private Orders orders_update;
    private String orderId;
}
