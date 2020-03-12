package com.vnzmi.tool.model;

import com.vnzmi.tool.StringUtil;
import com.vnzmi.tool.model.mapper.FieldMapper;
import com.vnzmi.tool.model.mapper.FieldMapperCommon;
import com.vnzmi.tool.model.mapper.FieldMapperJava;
import com.vnzmi.tool.model.mapper.TitleResolver;

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

    private HashMap<String,FieldMapper> mappers = new  HashMap<String,FieldMapper>();


    public FieldInfo()
    {

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
        return TitleResolver.getGuessedTitle(this);
    }

    /**
     * 获取数据类型映射
     *
     * @return
     */
    public FieldMapper getMapper() {
        if (!mappers.containsKey("normal")) {
           FieldMapper  mapper = new FieldMapperCommon(this);
           mappers.put("normal",mapper);
        }
        return mappers.get("normal");
    }

    public FieldMapper getJavaMapper() {
        if (!mappers.containsKey("java")) {
            FieldMapper  mapper = new FieldMapperJava(this);
            mappers.put("java",mapper);
        }
        return mappers.get("java");
    }


}
