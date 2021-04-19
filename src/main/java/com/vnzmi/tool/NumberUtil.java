package com.vnzmi.tool;

public class NumberUtil {

    public static int intValue(double num){
        String num1 = Double.toString(num);
        int index = num1.indexOf('.');
        if(index == -1){
            return Integer.valueOf(num1);
        }else{
            return Integer.valueOf(num1.substring(0,index));
        }
    }
}
