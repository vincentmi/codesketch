package com.vnzmi.tool.model.mapper;

import com.vnzmi.tool.ArrayUtil;
import com.vnzmi.tool.model.FieldInfo;

public class FieldMapperCommon implements FieldMapper {
    protected FieldInfo info;
    protected String type;

    public FieldMapperCommon(FieldInfo info) {
        this.info = info;
    }


    public String getTypeObject() {
        return getType();
    }

    public String getType() {
        if (type == null) {
            type = doGetType();
        }
        return type;
    }


    public String getDefaultText() {
        return info.getDefaultValue();
    }


    public String getDefaultExpression() {
        String defaultValueText = getDefaultText();
        return defaultValueText == null ? "" : " = " + defaultValueText;
    }

    protected String doGetType() {
        String mappedType;
        String orgDataType = info.getDataType();
        if (orgDataType.equals("enum")) {
            mappedType = TYPE_ENUM;
        } else if (orgDataType.equals("year")) {
            mappedType = TYPE_YEAR;
        } else if (orgDataType.equals("time")) {
            mappedType = TYPE_TIME;
        } else if (orgDataType.equals("timestamp")) {
            mappedType = TYPE_TIMESTAMP;
        } else if (orgDataType.equals("date")) {
            mappedType = TYPE_DATE;
        } else if (orgDataType.equals("datetime")) {
            mappedType = TYPE_DATETIME;
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"tinyint", "mediumint", "smallint", "int", "bigint"})) {
            mappedType = TYPE_INT;
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"tinytext", "text", "mediumtext", "longtext"})) {
            mappedType = TYPE_TEXT;
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"decimal", "real", "double", "float"})) {
            mappedType = TYPE_FLOAT;
        } else if (ArrayUtil.inArray(orgDataType, new String[]{"tinyblob", "blob", "mediumblob", "longblob"})) {
            mappedType = TYPE_BLOB;
        } else {
            mappedType = TYPE_STRING;
        }

        if (orgDataType.equals("tinyint") && info.getNumericPrecision() == 1) {
            mappedType = TYPE_BOOLEAN;
        }
        return mappedType;
    }

    public String getGetter() {
        return "get" + info.getNameCamelUpper();
    }

    public String getSetter() {
        return "set" + info.getNameCamelUpper();
    }

    public String[] getValidators() {
        return new String[]{};
    }
}
