package com.vnzmi.tool.model;

import com.vnzmi.tool.CodeSketch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public boolean saveFile()
    {
        try {
            File file = new File(getSaveTo());
            File dir = new File(file.getParent());
            if(!dir.exists()){
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getContent().getBytes("UTF-8"));
            CodeSketch.info("Saved -  " + getSaveTo());
            return true;
        }catch (IOException e1){
            CodeSketch.error(e1.getMessage());
            return false;
        }
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
