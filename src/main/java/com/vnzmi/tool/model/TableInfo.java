package com.vnzmi.tool.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class TableInfo {
    private String catalog ;
    private String schema ;
    private String name;
    private ArrayList<FieldInfo> fields;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(ArrayList<FieldInfo> fields) {
        this.fields = fields;
    }

    @Override
    public String toString()
    {
        return toJson();
    }

    public String toJson()
    {
        ObjectMapper mapper = new ObjectMapper();
        String text = "{}";
        try {
            text = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return text;
    }
}
