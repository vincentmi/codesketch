package com.vnzmi.tool.model.mapper;

public interface FieldMapper {
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

    String getType();
    String getGetter();
    String getSetter();
    String[] getValidators();
}