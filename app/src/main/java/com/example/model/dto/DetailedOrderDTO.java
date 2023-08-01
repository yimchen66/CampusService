package com.example.model.dto;

import com.example.model.domain.Orders;
import com.example.model.domain.TransOrder;
import com.example.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-21  16:46
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedOrderDTO {
    private Orders orders;
    private User hostUser;
    private User acceptedUser;
    private int orderState = 0;
    private TransOrder transOrder;
}
