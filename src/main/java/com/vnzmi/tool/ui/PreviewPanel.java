package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.StringUtil;
import com.vnzmi.tool.model.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PreviewPanel {
    private TableInfo tableInfo;
    private TemplateInfo templateInfo;
    private CodePack[] codePacks ;

    public  PreviewPanel(TableInfo tableInfo)
    {
        this.tableInfo = tableInfo;

        templateInfo = CodeSketch.getMainFrame().getSelectedTemplateInfo();

        JTabbedPane tab = new JTabbedPane();
        tab.setForeground(Color.BLACK);


        TemplateFile[] files = templateInfo.getFiles();

        Generator gen = new Generator(templateInfo);
        codePacks = null;
        try {
            codePacks = gen.build(tableInfo);
        }catch (Exception e){
            e.printStackTrace();
            CodeSketch.fatal(e.getMessage());
            return ;
        }

        for(int i = 0;i<codePacks.length;i++)
        {
            CodePack codePack = codePacks[i];
            tab.addTab(codePack.getName(),buildEditorBox(codePack));
        }

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(CodeSketch.getFrameSize(0.7));
        CodeSketch.center(frame);
        frame.add(tab,BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private String getSyntaxDefine(String name)
    {
        String ext = StringUtil.ext(name);
        String syntx = SyntaxConstants.SYNTAX_STYLE_NONE ;
        if(ext.equals("json")){
            syntx = SyntaxConstants.SYNTAX_STYLE_JSON;
        }else if(ext.equals("php")){
            syntx = SyntaxConstants.SYNTAX_STYLE_PHP;
        }else if(ext.equals("java")){
            syntx = SyntaxConstants.SYNTAX_STYLE_JAVA;
        }else if(ext.equals("js")){
            syntx = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
        }else if(ext.equals("html")){
            syntx = SyntaxConstants.SYNTAX_STYLE_HTML;
        }else if(ext.equals("xml")){
            syntx = SyntaxConstants.SYNTAX_STYLE_XML;
        }else if(ext.equals("yaml")){
            syntx = SyntaxConstants.SYNTAX_STYLE_YAML;
        }else if(ext.equals("css")){
            syntx = SyntaxConstants.SYNTAX_STYLE_CSS;
        }else if(ext.equals("sql")){
            syntx = SyntaxConstants.SYNTAX_STYLE_SQL;
        }else if(ext.equals("jsp")){
            syntx = SyntaxConstants.SYNTAX_STYLE_JSP;
        }else if(ext.equals("dockerfile")){
            syntx = SyntaxConstants.SYNTAX_STYLE_DOCKERFILE;
        }else if(ext.equals("ts")){
            syntx = SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT;
        }else if(ext.equals("vue")){
            syntx = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
        }else if(ext.equals("dart")){
            syntx = SyntaxConstants.SYNTAX_STYLE_DART;
        }else if(ext.equals("py")){
            syntx = SyntaxConstants.SYNTAX_STYLE_PYTHON;
        }
        return syntx;
    }

    private JPanel  buildEditorBox(CodePack codePack){

        JPanel cp = new JPanel(new BorderLayout());
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(getSyntaxDefine(codePack.getName()));
        textArea.setCodeFoldingEnabled(true);
        textArea.setAutoIndentEnabled(true);
        textArea.setLineWrap(true);
        textArea.setText(codePack.getContent());

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/idea.xml"));
            theme.apply(textArea);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RTextScrollPane sp = new RTextScrollPane(textArea);
        cp.add(sp,BorderLayout.CENTER);
        JPanel statusPanel = new JPanel(new BorderLayout());
        JLabel saveToLabel = new JLabel(codePack.getSaveTo());
        saveToLabel.setToolTipText(codePack.getSaveTo());
        statusPanel.add(saveToLabel , BorderLayout.CENTER);
        JButton btn = new JButton("Save");
        btn.addActionListener(e -> {
            codePack.saveFile();
        });
        JButton btnSaveAll = new JButton("Save All");
        btnSaveAll.addActionListener(e -> {
            saveAll();
        });

        JPanel btnPanel = new JPanel();

        btnPanel.add(btnSaveAll);
        btnPanel.add(btn);
        statusPanel.add(btnPanel,BorderLayout.EAST);
        cp.add(statusPanel,BorderLayout.SOUTH);

        return cp;
    }

    private void saveAll()
    {
        for(CodePack cp : codePacks){
            cp.saveFile();
        }
    }
}
