package com.vnzmi.tool.model;

public class CodePack {
    private String name ;
    private String saveTo;
    private String content;

    public CodePack(String name , String saveTo , String content)
    {
        this.name = name ;
        this.saveTo = saveTo;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSaveTo() {
        return saveTo;
    }

    public void setSaveTo(String saveTo) {
        this.saveTo = saveTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
