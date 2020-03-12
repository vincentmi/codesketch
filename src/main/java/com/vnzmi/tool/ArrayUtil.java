package com.vnzmi.tool;

public class ArrayUtil {
    public static boolean inArray(String item,String[] compare)
    {
        for(int i = 0;i<compare.length;i++)
        {
            if(item.equals(compare[i]))
            {
                return true;
            }
        }
        return false;
    }
}
