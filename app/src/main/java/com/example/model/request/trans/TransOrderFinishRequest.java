package com.example.model.request.trans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-22  15:52
 * @Description: 完成订单请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransOrderFinishRequest {
    private String hostId;
    private String orderID;
}
