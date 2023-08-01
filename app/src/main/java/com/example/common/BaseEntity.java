package com.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenyim
 * @Description 解析实体基类
 * @date 2023/6/24 16:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity<T> {
    private static int SUCCESS_CODE=0;//成功的code
    private int code;
    private String message;
    private T data;


    public boolean isSuccess(){
//        return getCode()==SUCCESS_CODE;
        return true;
    }
}
