package com.example.model.request.trans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-22  18:23
 * @Description: 用户查询请求体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransOrderRequest {
    private String requestType;
    private String requestUserId;
    private int pageNo;
    private int pageNumber;

}
