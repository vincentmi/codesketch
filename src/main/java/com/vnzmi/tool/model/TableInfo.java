package com.vnzmi.tool.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

import java.util.ArrayList;

public class TableInfo {
    private String catalog ;
    private String schema ;
    private String name;
    private String comment;
    private String group = "";
    private List<FieldInfo> fields;

    private List<FieldInfo> pk = null;

    public String getCatalog() {
        return catalog;
    }

    public String getGroup(){
        return group;
    }

    public void setGroup(String group){
        this.group = group;
    }

    public String getPrefix(){
        int index = name.indexOf("_");
        if(index != -1)
        {
            return name.substring(0,index);
        }else{
            return "";
        }
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

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }

    public void setPk(FieldInfo pk){
        List<FieldInfo> pkList = new ArrayList<>();
        pkList.add(pk);
        setPkList(pkList);
    }

    public void setPkList(List<FieldInfo> pk){

        this.pk = pk;
    }


    @Override
    public String toString()
    {
        return toJson();
    }

    private List<FieldInfo> getPrimaryKeys()
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
