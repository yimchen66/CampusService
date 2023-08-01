package com.example.model.sqlliteentity;


import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends DataSupport implements Serializable {
    /**
     * 唯一标识
     */
    private long id;


    private String user;


}