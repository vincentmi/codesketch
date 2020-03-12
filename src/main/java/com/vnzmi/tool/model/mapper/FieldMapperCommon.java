package com.vnzmi.tool.model.mapper;

import com.vnzmi.tool.ArrayUtil;
import com.vnzmi.tool.model.FieldInfo;

public class FieldMapperCommon implements FieldMapper {
    private FieldInfo info;

    public FieldMapperCommon(FieldInfo info) {
        this.info = info;
    }

    @Override
    public String getType() {
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

    @Override
    public String getGetter() {
        return null;
    }
}
