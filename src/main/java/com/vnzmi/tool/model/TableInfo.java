package com.vnzmi.tool.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class TableInfo {
    private String catalog ;
    private String schema ;
    private String name;
    private String comment;
    private String group = "";
    private ArrayList<FieldInfo> fields;

    private ArrayList<FieldInfo> pk = null;

    public String getCatalog() {
        return catalog;
    }

    public String getGroup(){
        return group;
    }

    public void setGroup(String group){
        this.group = group;
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

    public void setComment(String comment) {this.comment = comment;}

    public String getComment(){return this.comment;}

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

    private ArrayList<FieldInfo> getPrimaryKeys()
    {
        if(pk == null)
        {
            pk = new ArrayList<>();
            for(int i = 0,max = fields.size();i<max;i++)
            {
                FieldInfo f = fields.get(i);
                if( f.isPrimaryKey())
                {
                    pk.add(f);
                }
            }
        }
        return pk;
    }

    public int getPrimaryKeyNum()
    {
        return getPrimaryKeys().size();
    }

    public FieldInfo getFirstPK()
    {
        return getPrimaryKeys().get(0);
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
