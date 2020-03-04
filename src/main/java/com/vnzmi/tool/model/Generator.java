package com.vnzmi.tool.model;

import com.vnzmi.tool.CodeSketch;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * 代码生成器
 */
public class Generator {
    private Configuration cfg ;
    private TemplateInfo templateInfo;
    public Generator(TemplateInfo templateInfo)
    {
        this.templateInfo = templateInfo;
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        File templateHome = new File(templateInfo.getPath());
        try {
            cfg.setDirectoryForTemplateLoading(templateHome);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }
    }

    public void  perform(String template , HashMap<String,Object> data)
    {
        try {
            Template temp = cfg.getTemplate(template);
            StringWriter sw = new StringWriter();
            temp.process(data,sw);
            CodeSketch.info(sw.toString());
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        } catch (TemplateException e) {
            e.printStackTrace();
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }

    }
}
