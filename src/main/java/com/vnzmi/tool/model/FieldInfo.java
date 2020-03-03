package com.vnzmi.tool.model;

public class FieldInfo {
    private String name;
    private String defaultValue;
    private boolean nullable;
    private String dataType;
    private int maxLength = -1 ;
    private int numericPrecision = -1;
    private int numericScale = -1;
    private String dataTypeStr ;
    private String key  = "";
    private String extra = "";
    private String comment = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(int numericPrecision) {
        this.numericPrecision = numericPrecision;
    }

    public int getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(int numericScale) {
        this.numericScale = numericScale;
    }

    public String getDataTypeStr() {
        return dataTypeStr;
    }

    public void setDataTypeStr(String dataTypeStr) {
        this.dataTypeStr = dataTypeStr;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
