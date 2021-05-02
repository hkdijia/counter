package com.gotkx.counter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通用返回
 */

@Data
@AllArgsConstructor
public class CounterRes {

    private int code;

    private String message;

    private Object data;

    public CounterRes(Object data){
        this(0,"",data);
    }

}
