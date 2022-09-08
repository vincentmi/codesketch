package com.vnzmi.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    public static List<DateTimeFormatter> formatterDatetimeList = new ArrayList<DateTimeFormatter>(){{
        add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
        add(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        add(DateTimeFormatter.ISO_DATE_TIME);
        add(DateTimeFormatter.RFC_1123_DATE_TIME);
        add(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }};

    public static List<DateTimeFormatter> formatterDateList = new ArrayList<DateTimeFormatter>(){{
        add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        add(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        add(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        add(DateTimeFormatter.ISO_DATE);
        add(DateTimeFormatter.ISO_LOCAL_DATE);
        add(DateTimeFormatter.BASIC_ISO_DATE);
    }};

    public static LocalDateTime tryParseDateTime(String dateStr){
        for(int i=0;i<formatterDatetimeList.size();i++){
            try{
                LocalDateTime result = LocalDateTime.parse(dateStr,formatterDatetimeList.get(i));
                return result;
            }catch (Exception e){

            }
        }
        return null;
    }

    public static LocalDate tryParseDate(String dateStr){
        for(int i=0;i<formatterDateList.size();i++){
            try{
                LocalDate result = LocalDate.parse(dateStr,formatterDateList.get(i));
                return result;
            }catch (Exception e){

            }
        }
        return null;
    }
}
