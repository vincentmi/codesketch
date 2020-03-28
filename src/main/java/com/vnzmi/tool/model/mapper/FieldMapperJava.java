package com.vnzmi.tool.model.mapper;

//import com.sun.tools.javac.util.ArrayUtils;
import com.vnzmi.tool.ArrayUtil;
import com.vnzmi.tool.model.FieldInfo;

import java.util.ArrayList;

public class FieldMapperJava implements FieldMapper{

    private FieldInfo info;

    private String type = null ;
    private String getter = null;

    public FieldMapperJava(FieldInfo info)
    {
        this.info = info;
    }

    public  String getType() {
        if(type == null)
        {
            type = doGetType();
        }
        return type;
    }

    private String doGetType() {

        String dataType;
        String orgDataType = info.getDataType();
        if (orgDataType.equals("enum")) {
            dataType = "String";
        } else if (orgDataType.equals("year")) {
            dataType = "Date";
        } else if (orgDataType.equals("time")) {
            dataType = "LocalTime";
        } else if (orgDataType.equals("timestamp")) {
            dataType = "DateTime";
        } else if (orgDataType.equals("date")) {
            dataType = "Date";
        } else if (orgDataType.equals("datetime")) {
            dataType = "Date";
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"tinyint", "mediumint", "smallint", "int", "bigint"})) {
            dataType = "long";
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"tinytext", "text", "mediumtext", "longtext"})) {
            dataType = "String";
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"decimal", "real", "double", "float"})) {
            dataType = "Double";
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"tinyblob", "blob", "mediumblob", "longblob"})) {
            dataType = "String";
        } else {
            //char varchar
            dataType = "String";
        }

        if (orgDataType.equals("tinyint") && info.getDataTypeStr().equals("tinyint(1)")) {
            dataType = "boolean";
        }
        return dataType;
    }

    public String getGetter()
    {
        if(getType().equals("boolean"))
        {
            return "is"+info.getNameCamelUpper();
        }else{
            return "get"+info.getNameCamelUpper();
        }
    }

    public String getSetter()
    {
        return "set"+info.getNameCamelUpper();
    }

    public String[] getValidators()
    {
        ArrayList<String> validators = new ArrayList<>();
        FieldMapper defaultMapper = info.getMapper();
        if(info.isRequired())
        {
            validators.add("@NotNull");
            if(ArrayUtil.inArray(defaultMapper.getType(),new String[]{FieldMapper.TYPE_TEXT ,FieldMapper.TYPE_STRING }))
            {
                validators.add("@NotBlank");
            }

            if(defaultMapper.getType().equals(FieldMapper.TYPE_STRING))
            {
                validators.add("@LENGTH(1,"+info.getMax()+")");
            }
        }
        return validators.toArray(new String[validators.size()]);
    }
}
