package com.vnzmi.tool.model;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.StringUtil;
import com.vnzmi.tool.exception.UnsupportedTableException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Code generator
 */
public class Generator {
    private Configuration cfg;

    private Configuration cfgString;
    private TemplateInfo templateInfo;

    private HashMap<String, Object> baseData;

    public Generator(TemplateInfo templateInfo) {
        init(templateInfo);
    }

    private void init(TemplateInfo templateInfo)
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
            cfg.setWhitespaceStripping(true);

            cfgString.setDefaultEncoding("UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }

        //模板数据
        baseData = new HashMap<String, Object>();
        Setting setting = Loader.getInstance().getSetting();

        baseData.put("projectPath", StringUtil.rtrim(setting.getProject().trim(), "/\\"));

        baseData.putAll(templateInfo.getDefaultValues());


        Map<String, String> values = Loader.getInstance()
                .getTemplateValues()
                .getOrDefault(templateInfo.getName(), new HashMap<String, String>()
                );

        baseData.putAll(values);
    }

    /**
     * perform a template
     *
     * @param template
     * @param data
     * @return
     */
    private String process(String template, HashMap<String, Object> data) {
        try {
            Template temp = cfg.getTemplate(template);
            StringWriter sw = new StringWriter();
            temp.process(data, sw);
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
            //CodeSketch.getMainFrame().showMessage(e.getMessage());
        } catch (TemplateException e) {
            e.printStackTrace();
            //CodeSketch.getMainFrame().showMessage(e.getMessage());
        }
        return "";

    }

    /**
     * replace variable from string
     *
     * @param templateString
     * @param data
     * @return
     */
    private String processString(String templateString, HashMap<String, Object> data) {
        String templateName = DigestUtils.md2Hex(templateString);
        try {
            Template temp = new Template(templateName, null, new StringReader(templateString), cfgString);
            StringWriter sw = new StringWriter();
            temp.process(data, sw);
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

    public CodePack[] build(TableInfo tableInfo) throws Exception {
        if(tableInfo.getPrimaryKeyNum() == 0)
        {
            throw new UnsupportedTableException("TABLE(" + tableInfo.getName()+") have no primary key");
        }else {
            return build(tableInfo, null);
        }
    }

    public CodePack[] build(TableInfo tableInfo, HashSet<String> need) {
        TemplateFile[] files = templateInfo.getFiles();
        HashMap<String, Object> tData = new HashMap<String, Object>();
        tData.putAll(baseData);

        tData.put("TAG_LEFT", "<");
        tData.put("TAG_RIGHT", ">");
        tData.put("TAG_DOLLAR", "$");
        tData.put("TAG_WELL", "#");
        tData.put("TIME" , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S z").format(new Date()));
        tData.put("table", tableInfo.getName());
        tData.put("tableLowcase", tableInfo.getName().toLowerCase());
        tData.put("tableInfo" , tableInfo);
        tData.put("modelCamel", StringUtil.toCamel(tableInfo.getName()));
        tData.put("model", StringUtil.toCamelUpper(tableInfo.getName()));
        tData.put("modelLine", StringUtil.toLine((String) tData.get("modelCamel")));
        tData.put("modelMidLine", StringUtil.toLine((String) tData.get("modelCamel"),'-'));
        tData.put("fields", tableInfo.getFields());
        tData.put("schema", tableInfo.getSchema());
        tData.put("primaryKey",tableInfo.getFirstPK());

        CodePack[] codePacks;
        if (need == null) {
            codePacks = new CodePack[files.length];
        } else {
            codePacks = new CodePack[need.size()];
        }

        int arrayIndex = 0;
        for (int i = 0; i < files.length; i++) {
            TemplateFile fileInfo = files[i];
            String name = processString(fileInfo.getName(), tData);

            if (need == null || need.contains(fileInfo.getFile())) {
                String saveTo = tData.get("projectPath")
                        + File.separator + processString(fileInfo.getSaveTo(), tData)
                        + File.separator + name;
                String content = process(fileInfo.getFile(), tData);
                codePacks[arrayIndex] = new CodePack(name, saveTo, content);
                arrayIndex++;
                //System.out.println(name);
            }
        }
        return codePacks;
    }
}
