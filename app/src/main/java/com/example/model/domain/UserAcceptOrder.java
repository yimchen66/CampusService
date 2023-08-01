package com.example.model.domain;


import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAcceptOrder extends DataSupport implements Serializable {
    private long id;
    private String orders;
    private String hostUser;
    private String acceptTime;
}
