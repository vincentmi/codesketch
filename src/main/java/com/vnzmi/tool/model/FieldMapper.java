package com.vnzmi.tool.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class FieldMapper {

    public final static String TYPE_ENUM = "enum";
    public final static String TYPE_INT = "int";
    public final static String TYPE_FLOAT = "float";
    public final static String TYPE_STRING = "string";
    public final static String TYPE_TEXT = "text";
    public final static String TYPE_BLOB = "blob";
    public final static String TYPE_YEAR = "year";
    public final static String TYPE_TIME = "time";
    public final static String TYPE_TIMESTAMP = "timestamp";
    public final static String TYPE_DATE = "date";
    public final static String TYPE_DATETIME = "datetime";
    public final static String TYPE_BOOLEAN = "boolean";

    public final static HashMap<String,String[]> guessTitles = new HashMap<String,String[]>(){{
        put("ID",new String[] {"id"});
        put("电子邮件",new String[] {"email","email_address","mail"});
        put("地址",new String[] {"addr","address"});
        put("手机号码",new String[] {"mobile","cellphone","mobile_phone"});
        put("电话号码",new String[] {"phone","tel"});
        put("创建时间",new String[] {"created_at","inserted_at","created"});
        put("创建人",new String[] {"created_by","inserted_by","created_user"});
        put("更新时间",new String[] {"updated_at","updated","update_time","updated_time"});
        put("更新人",new String[] {"updated_by","updated_user"});
    }};

    private String mappedType = null ;


    private FieldInfo info;

    public FieldMapper(FieldInfo fieldInfo)
    {
        info = fieldInfo;
    }

    /**
     * 获取可能的栏位名称
     * @return
     */
    public String getGuessedTitle()
    {
        String title = null;
        String name = info.getName();
        Iterator<String> keys = guessTitles.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            String[] compare = guessTitles.get(key);
            if (inArray(name, compare)) {
                title = key;
                break;
            }
        }

        if(title == null && info.getComment() != "" && info.getComment().length() < 6) {
            return info.getComment();
        }else{
            return info.getName();
        }
    }

    public String getMappedType()
    {
        if(mappedType == null)
        {
            parseFieldInfo();
        }
        return mappedType;
    }

    private boolean inArray(String item,String[] compare)
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

    private void parseFieldInfo() {
        String orgDataType = info.getDataType();
        if(orgDataType.equals("enum"))
        {
            mappedType = TYPE_ENUM;
        }else if(orgDataType.equals("year"))
        {
            mappedType = TYPE_YEAR;
        }else if(orgDataType.equals("time"))
        {
            mappedType = TYPE_TIME;
        }else if(orgDataType.equals("timestamp"))
        {
            mappedType = TYPE_TIMESTAMP;
        }else if(orgDataType.equals("date"))
        {
            mappedType = TYPE_DATE;
        }else if(orgDataType.equals("datetime"))
        {
            mappedType = TYPE_DATETIME;
        }else if(inArray(orgDataType , new String[]{"tinyint","mediumint","smallint","int","bigint"}))
        {
            mappedType = TYPE_INT;
        }else if(inArray(orgDataType , new String[]{"tinytext","text","mediumtext","longtext"}))
        {
            mappedType = TYPE_TEXT;
        }else if(inArray(orgDataType , new String[]{"decimal","real","double","float"}))
        {
            mappedType = TYPE_FLOAT;
        }else if(inArray(orgDataType , new String[]{"tinyblob","blob","mediumblob","longblob"}))
        {
            mappedType = TYPE_BLOB;
        }else {
            mappedType = TYPE_STRING;
        }

        if(orgDataType.equals("tinyint") && info.getNumericPrecision() == 1)
        {
            mappedType = TYPE_BOOLEAN;
        }

    }
}
