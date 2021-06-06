package com.gotkx.counter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通用返回
 */

@Data
@AllArgsConstructor
public class CounterRes {

    public static final int SUCCESS = 0;
    public static final int RELOGIN = 1;
    public static final int FAIL = 2;

    private int code;

    private String message;

    private Object data;

    public CounterRes(Object data){
        this(0,"",data);
    }



}
