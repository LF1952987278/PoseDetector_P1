package com.bjtu.pose.pose.entity;

import java.util.Arrays;
import java.util.List;

/**
 * * Author: Liu Feng
 * * Date:2023/4/22 16:14
 * * Description
 */
public class IndoorSenUnitV2 extends SenUnitV2{

    public IndoorSenUnitV2(String line) {
        super(line);
        List<String> items = Arrays.asList(line.trim().split(","));
        if (items.get(0).length() == 10) {
            this.setTs(Long.valueOf(items.get(0)));
        } else if (items.get(0).length() == 13) {
            this.setTs(Long.valueOf(items.get(0)) / 1000);
        }
    }

}
