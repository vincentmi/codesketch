package com.vnzmi.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnzmi.tool.model.Setting;
import com.vnzmi.tool.model.TemplateInfo;
import com.vnzmi.tool.model.TemplateValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Loader {
    public static Loader instance = null ;
    private  String rootPath = null;
    private  Setting setting = null;
    private HashMap<String,TemplateValue> templateValues = null;

    private ArrayList<TemplateInfo> templateInfos;
    public static Loader getInstance()
    {
        if(instance == null)
        {
            instance = new Loader();
            File root = new File(System.getProperty("user.home") + File.separator + ".codesketch");
            if(!root.exists())
            {
                root.mkdirs();
            }
            instance.rootPath = root.getAbsolutePath();
        }
        return instance;
    }

    public String getRootPath()
    {
        return rootPath;
    }

    public Setting getSetting()
    {
        if(setting == null)
        {
            loadSetting();
        }
        return setting;
    }

    public ArrayList<TemplateInfo> getTemplateInfos() {
        if(templateInfos == null)
        {
            loadTemplateInfos();
        }
        return templateInfos;
    }

    public void saveSetting()
    {
        String filepath = rootPath+ File.separator + "setting.json";
        File f = new File(filepath);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(setting.toJson().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.error(e.getMessage());
        }
    }

    private void loadSetting()
    {
        Setting loadedSetting = new Setting();
        try {
            String filepath = rootPath + File.separator + "setting.json";
            CodeSketch.info("loading "+filepath);
            File f = new File(filepath);
            ObjectMapper mapper = new ObjectMapper();
            loadedSetting = (Setting) mapper.readValue(f, Setting.class);
            CodeSketch.info("loaded setting " + this.setting);
            setting = loadedSetting;
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.error(e.getMessage());
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }
    }

    private void loadTemplateInfos()
    {
        File templateRoot = new File(rootPath + File.separator + "templates");
        if (!templateRoot.isDirectory())
        {
            templateRoot.mkdir();
        }

        String[] templates  = templateRoot.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return dir.isDirectory();
            }
        });

        ArrayList<TemplateInfo> infos = new ArrayList<TemplateInfo>();

        File infoFile ;
        String infoFilePath,templatePath ;
        for(int i = 0 ;i<templates.length ;i++)
        {
            templatePath = templateRoot.getAbsolutePath() + File.separator + templates[i];
            infoFilePath = templatePath+ File.separator + "info.json";
            CodeSketch.info("load template info : "+infoFilePath);
            infoFile = new File(infoFilePath);
            TemplateInfo info;
            if(infoFile.exists())
            {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    info = mapper.readValue(infoFile, TemplateInfo.class);
                    info.setPath(templatePath);
                    infos.add(info);
                    CodeSketch.info(info.toJson());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        templateInfos = infos;
    }


    public HashMap<String,TemplateValue> getTemplateValues()
    {
        if(templateValues == null)
        {
            loadTemplateValues();
        }
        return templateValues;
    }


    private void loadTemplateValues()
    {
        String valuesPath = rootPath + File.separator  + "values.json";

        CodeSketch.info("load template values : "+valuesPath);
        File file = new File(valuesPath);
        if(file.exists())
        {
            ObjectMapper mapper = new ObjectMapper();
            try {
                HashMap<String,TemplateValue> tem = new HashMap<String, TemplateValue>();
                templateValues = mapper.readValue(file,tem.getClass());
                CodeSketch.info(" values size=" + templateValues.size());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveTemplateValues()
    {
        String filepath = rootPath+ File.separator + "values.json";
        File f = new File(filepath);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String text = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getTemplateValues());
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(text.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.error(e.getMessage());
        }
    }



}
