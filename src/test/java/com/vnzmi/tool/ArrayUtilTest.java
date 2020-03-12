package com.vnzmi.tool;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;

import static org.junit.Assert.*;

public class ArrayUtilTest {

    @Test
    public void inArray() {
        Assert.assertTrue(ArrayUtil.inArray("vincent",new String[]{"freya","vincent","nico"}));
        Assert.assertFalse(ArrayUtil.inArray("vincent",new String[]{"freya","vincent1","nico"}));
        Assert.assertFalse(ArrayUtil.inArray("vincent",new String[]{"freya","1vincent","nico"}));
    }
}