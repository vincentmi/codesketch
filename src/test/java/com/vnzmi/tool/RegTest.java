package com.vnzmi.tool;

import com.sun.xml.internal.ws.policy.AssertionSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class RegTest {

    @Test
    public void dateRegex(){
        Assert.assertTrue( DateUtil.tryParseDateTime("2022-09-01 15:24:26")!=null);

        String floatRegex = "^[0-9]+\\.+[0-9]+$";

        Assert.assertTrue("1.22".matches(floatRegex));
        Assert.assertTrue("104.0847460000".matches(floatRegex));
        Assert.assertFalse("12".matches(floatRegex));
    }
}
