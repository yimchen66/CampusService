package com.example.model.dto;

import com.example.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chenyim
 * @CreateTime: 2023-06-21  09:04
 * @Description: TODO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private User user;
    private String token;
}
