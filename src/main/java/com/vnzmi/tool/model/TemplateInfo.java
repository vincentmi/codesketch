package com.vnzmi.tool.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TemplateInfo {
    private String name;
    private String url;
    private String author;
    private String description;

    private TemplateVariable[]  variables;
    private TemplateFile[] files;
    private String path;


    public String toJson()
    {
        ObjectMapper mapper = new ObjectMapper();
        String text = "{}";
        try {
            text = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return text;
    }

    public String toString()
    {
        return toJson();
    }

    public Map<String,String> getDefaultValues(){
        HashMap<String,String> vars  = new HashMap<>();
        for ( TemplateVariable variable : variables){
            vars.put(variable.getName(),variable.getDefaultValue());
        }
        return vars;
    }

}
