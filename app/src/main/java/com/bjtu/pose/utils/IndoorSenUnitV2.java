package com.bjtu.pose.utils;

public class IndoorSenUnitV2 extends SenUnitV2{
    public IndoorSenUnitV2(String line){
        super(line);
        String[] data = line.split(",");
        if(data[0].length() == 10){
            this.ts = Long.valueOf(data[0]);
        }else if((data[0].length() == 13)){
            this.ts = Long.valueOf(data[0])/1000;
        }
    }
}
