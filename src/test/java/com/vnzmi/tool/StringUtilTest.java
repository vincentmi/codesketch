package com.vnzmi.tool;


import static org.junit.Assert.*;
import org.junit.Test;

public class StringUtilTest {


    @Test
    public void toCamel() {
        assertEquals(StringUtil.toCamel("cost_price"),"costPrice");
        assertEquals(StringUtil.toCamel("costprice"),"costprice");
        assertEquals(StringUtil.toCamel("costPrice_Num"),"costpriceNum");
        assertEquals(StringUtil.toCamel("costPrice-Num"),"costpriceNum");
    }

    @Test
    public void toCamelUpper() {
        assertEquals(StringUtil.toCamelUpper("cost_price"),"CostPrice");
        assertEquals(StringUtil.toCamelUpper("costprice"),"Costprice");
        assertEquals(StringUtil.toCamelUpper("costPrice_Num"),"CostpriceNum");
        assertEquals(StringUtil.toCamelUpper("costPrice-Num"),"CostpriceNum");
    }


    @Test
    public void testToLine() {
        assertEquals(StringUtil.toLine("newAge"),"new_age");
        assertEquals(StringUtil.toLine("NewAge"),"new_age");
        assertEquals(StringUtil.toLine("NewAge",'-'),"new-age");
    }

    @Test
    public void ltrim() {
        assertEquals(StringUtil.ltrim("/newAge/","/"),"newAge/");
        assertEquals(StringUtil.ltrim("///newAge/","/"),"newAge/");
    }

    @Test
    public void rtrim() {
        assertEquals(StringUtil.rtrim("/newAge/","/"),"/newAge");
        assertEquals(StringUtil.rtrim("/newAge/","/"),"/newAge");
    }

    @Test
    public void trim() {
        assertEquals(StringUtil.trim("/newAge/","/"),"newAge");
    }

    @Test
    public void ext() {
        assertEquals(StringUtil.ext("video.mp4"),"mp4");
        assertEquals(StringUtil.ext("video.MP4"),"mp4");
        assertEquals(StringUtil.ext(".MP4"),"mp4");
        assertEquals(StringUtil.ext("MP4"),"");
    }
}