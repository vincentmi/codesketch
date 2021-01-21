package com.vnzmi.tool.model.mapper;

//import com.sun.tools.javac.util.ArrayUtils;

import com.vnzmi.tool.ArrayUtil;
import com.vnzmi.tool.model.FieldInfo;

import java.util.ArrayList;

public class FieldMapperJava extends FieldMapperCommon {

    private String typeObject = null;

    public FieldMapperJava(FieldInfo info) {
        super(info);
    }

    @Override
    public String getDefaultText() {
        String defaultValue = info.getDefaultValue();

        if (defaultValue == null) {
            return null;
        }
        String type = getType();

        if ("CURRENT_TIMESTAMP".equals(defaultValue)) {
            return null;
        }
        if (ArrayUtil.inArray(type, new String[]{"String", "Date"})) {
            return "\"" + defaultValue + "\"";
        } else if ("double".equals(type)) {
            return defaultValue + "D";
        } else if ("long".equals(type)) {
            return defaultValue + "L";
        } else if ("int".equals(type)) {
            return defaultValue;
        } else if ("boolean".equals(type)) {
            return Integer.parseInt(defaultValue) > 0 ? "true" : "false";
        }
        return null;
    }
    @Override
    public String getTypeObject() {
        if (typeObject == null) {
            type = getType();
            if (type.equals("long")) {
                typeObject = "Long";
            } else if (type.equals("boolean")) {
                typeObject = "Boolean";
            } else if (type.equals("double")) {
                typeObject = "Double";
            } else if (type.equals("int")) {
                typeObject = "Integer";
            } else {
                typeObject = type;
            }
        }

        return typeObject;
    }

    @Override
    protected String doGetType() {

        String dataType;
        String orgDataType = info.getDataType();
        if (orgDataType.equals("enum")) {
            dataType = "String";
        } else if (orgDataType.equals("year")) {
            dataType = "Date";
        } else if (orgDataType.equals("time")) {
            dataType = "Date";
        } else if (orgDataType.equals("timestamp")) {
            dataType = "Date";
        } else if (orgDataType.equals("date")) {
            dataType = "Date";
        } else if (orgDataType.equals("datetime")) {
            dataType = "Date";
        }  else if (ArrayUtil.inArray(orgDataType, new String[]{"tinyint", "mediumint", "smallint"})) {
            dataType = "int";
        } else if (ArrayUtil.inArray(orgDataType, new String[]{ "int", "bigint"})) {
            dataType = "long";
        }  else if (ArrayUtil.inArray(orgDataType, new String[]{"tinytext", "text", "mediumtext", "longtext"})) {
            dataType = "String";
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"decimal", "real", "double", "float"})) {
            dataType = "double";
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

    @Override
    public String[] getValidators() {
        ArrayList<String> validators = new ArrayList<>();
        FieldMapper defaultMapper = info.getMapper();
        if (info.isRequired()) {
            validators.add("@NotNull");
            if (ArrayUtil.inArray(defaultMapper.getType(), new String[]{FieldMapper.TYPE_TEXT, FieldMapper.TYPE_STRING})) {
                validators.add("@NotBlank");
            }

            if (defaultMapper.getType().equals(FieldMapper.TYPE_STRING)) {
                StringBuffer sb = new StringBuffer("@Size(");
                if (!info.isNullable()) {
                    sb.append("min=1,");
                }
                sb.append("max=");
                sb.append(info.getMax());
                sb.append(")");
                validators.add(sb.toString());
            }
        }
        return validators.toArray(new String[validators.size()]);
    }
}
