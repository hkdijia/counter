package com.gotkx.counter.bean.res;

import lombok.*;

/**
 * 成交类
 */

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfo {

    private long uid;

    private String date;

    private String time;

    private String bank;

    private int type;

    private int moneyType;

    private long money;


}
