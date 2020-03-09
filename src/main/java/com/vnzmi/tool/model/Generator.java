package com.vnzmi.tool.model;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.StringUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 代码生成器
 */
public class Generator {
    private Configuration cfg ;

    private Configuration cfgString;
    private TemplateInfo templateInfo;

    private HashMap<String,Object> baseData;
    public Generator(TemplateInfo templateInfo)
    {
        this.templateInfo = templateInfo;
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        File templateHome = new File(templateInfo.getPath());
        cfgString = new Configuration(Configuration.VERSION_2_3_28);
        StringTemplateLoader stl = new StringTemplateLoader();
        cfgString.setTemplateLoader(stl);

        try {
            cfg.setDirectoryForTemplateLoading(templateHome);
            cfg.setDefaultEncoding("UTF-8");

            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);

            cfgString.setDefaultEncoding("UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }

        //模板数据
        baseData = new HashMap<String,Object>();
        Setting setting = Loader.getInstance().getSetting();
        Map<String,String> values = Loader.getInstance().getTemplateValues().getOrDefault(templateInfo.getName() , new HashMap<String,String>());
        baseData.put("projectPath",StringUtil.rtrim(setting.getProject().trim(),"/\\"));
        baseData.putAll(values);
    }

    /**
     * perform a template
     * @param template
     * @param data
     * @return
     */
    private String  process(String template , HashMap<String,Object> data)
    {
        try {
            Template temp = cfg.getTemplate(template);
            StringWriter sw = new StringWriter();
            temp.process(data,sw);
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        } catch (TemplateException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }
        return "";

    }

    /**
     * replace variable from string
     * @param templateString
     * @param data
     * @return
     */
    private String processString(String templateString , HashMap<String,Object> data)
    {
        String templateName = DigestUtils.md2Hex(templateString);
        try {
            Template temp  = new Template(templateName,null, new StringReader(templateString),cfgString);
            StringWriter sw = new StringWriter();
            temp.process(data,sw);
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        } catch (TemplateException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }
        return "";
    }

    public CodePack[]  build(TableInfo tableInfo){
        return build(tableInfo,null);
    }

    public CodePack[]  build(TableInfo tableInfo , HashSet<String> need)
    {
        TemplateFile[] files = templateInfo.getFiles();
        HashMap<String,Object> tData = new HashMap<String,Object>();
        tData.putAll(baseData);

        tData.put("table",tableInfo.getName());
        tData.put("modelCamel", StringUtil.toCamel(tableInfo.getName()));
        tData.put("model", StringUtil.toCamelUpper(tableInfo.getName()));
        tData.put("modelLine", StringUtil.toLine((String)tData.get("modelCamel")));
        tData.put("fields",tableInfo.getFields());
        tData.put("schema",tableInfo.getSchema());

        CodePack[] codePacks = new CodePack[files.length];

        for(int i = 0 ;i<files.length;i++)
        {
            TemplateFile fileInfo = files[i];
            String name = processString(fileInfo.getName() , tData);

            if(need == null || need.contains(fileInfo.getFile()))
            {
                String saveTo = (String)tData.get("projectPath")
                        +File.separator+ processString(fileInfo.getSaveTo() , tData)
                        +File.separator+name;
                String content = process(fileInfo.getFile() , tData);
                codePacks[i] = new CodePack(name,saveTo,content);
                //System.out.println(name);
            }
        }

        return codePacks;
    }
}
