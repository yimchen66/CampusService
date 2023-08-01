package com.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-28  15:48
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransHistoryDTO {
    private double order_money;
    private String time;
    private String picUrl;
    private String nickName;
}
