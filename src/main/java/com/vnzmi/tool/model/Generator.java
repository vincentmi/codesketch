package com.vnzmi.tool.model;

import com.vnzmi.tool.CodeSketch;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.codec.digest.DigestUtils;
import sun.security.krb5.Config;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;

/**
 * 代码生成器
 */
public class Generator {
    private Configuration cfg ;

    private Configuration cfgString;
    private TemplateInfo templateInfo;
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


    }

    public String  perform(String template , HashMap<String,Object> data)
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

    public String performString(String templateString , HashMap<String,Object> data)
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
}
