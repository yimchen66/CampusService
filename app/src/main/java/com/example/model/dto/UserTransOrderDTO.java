package com.example.model.dto;

import com.example.model.domain.Orders;
import com.example.model.domain.TransOrder;
import com.example.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-22  18:20
 * @Description: 与用户相关的订单返回体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransOrderDTO {
    private Orders orders;
    private TransOrder transOrder;
    private User opposeUser;
}
