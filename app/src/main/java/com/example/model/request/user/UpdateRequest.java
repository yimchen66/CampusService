package com.example.model.request.user;

import com.example.model.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-20  19:24
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private User user;

    private String password;
}
