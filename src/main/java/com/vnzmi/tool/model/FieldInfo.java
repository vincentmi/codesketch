package com.vnzmi.tool.model;

import com.vnzmi.tool.StringUtil;
import com.vnzmi.tool.model.mapper.*;

import java.util.HashMap;


public class FieldInfo {
    private String name;
    private String defaultValue;
    private boolean nullable;
    private String dataType;
    private long max = -1;
    private long min = -1;
    private int numericPrecision = -1;
    private int numericScale = -1;
    private String dataTypeStr;
    private String key = "";
    private String extra = "";
    private String comment = "";

    private FieldInfo that;

    private HashMap<String, Object> cache = new HashMap<String, Object>();


    public FieldInfo() {
        that = this;
    }

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

    public long getMax() {
        return max;
    }

    public long getMin() {
        return min;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public void setMin(long min) {
        this.min = min;
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

    public String getNameCamel() {
        return StringUtil.toCamel(getName());
    }

    public String getNameCamelUpper() {
        return StringUtil.toCamelUpper(getName());
    }

    /**
     * 是否自增
     *
     * @return
     */
    public boolean isAutoIncrement() {
        return getExtra().indexOf("auto_increment") != -1;
    }

    /**
     * 是否必填
     *
     * @return
     */
    public boolean isRequired() {
        return !isNullable();
    }

    public String getGuessedTitle() {
        return (String) getOrCreate("guess_title", () -> ExtraResolver.guessTitle(this));
    }

    public boolean isCreated() {
        return (boolean) getOrCreate("guess_title", () -> ExtraResolver.guessIsCreated(this));
    }

    public boolean isUpdated() {
        return (boolean) getOrCreate("guess_title", () -> ExtraResolver.guessIsUpdated(this));
    }

    private Object getOrCreate(String key, Closures closures) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            Object result = closures.execute();
            cache.put(key, result);
            return result;
        }
    }

    /**
     * 获取数据类型映射
     *
     * @return
     */
    public FieldMapper getMapper() {
        return (FieldMapper) getOrCreate("mapper_normal", () -> new FieldMapperCommon(this));
    }

    public FieldMapper getJava() {
        return (FieldMapper) getOrCreate("mapper_java", () -> new FieldMapperJava(this));
    }

    public FieldMapper getPhp() {
        return (FieldMapper) getOrCreate("mapper_php", () -> new FieldMapperPhp(this));
    }


}
