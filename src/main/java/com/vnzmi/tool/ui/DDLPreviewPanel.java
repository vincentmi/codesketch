package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.StringUtil;
import com.vnzmi.tool.model.CodePack;
import com.vnzmi.tool.model.FieldInfo;
import com.vnzmi.tool.model.TableInfo;
import com.vnzmi.tool.model.mapper.FieldMapper;
import org.apache.logging.log4j.core.appender.FileManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import sun.misc.FileURLMapper;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DDLPreviewPanel {
    private  TableInfo tableInfo;
    public DDLPreviewPanel(TableInfo tableInfo){

        this.tableInfo = tableInfo;

        List<CodePack> codePacks = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE `"+tableInfo.getName()+"` (\n");
        List<FieldInfo> fieldInfoList = tableInfo.getFields();
        for(int i = 0 ;i< fieldInfoList.size();i++){
            FieldInfo info = fieldInfoList.get(i);
            stringBuilder.append(" `");
            stringBuilder.append(info.getName());
            stringBuilder.append("` ");
            stringBuilder.append(getMySQLDataType(info));
            stringBuilder.append(" DEFAULT NULL COMMENT '");
            stringBuilder.append(info.getComment());
            stringBuilder.append("',\n");
        }
        stringBuilder.append("PRIMARY KEY(`"+tableInfo.getFirstPK().getName()+"`) \n");
        stringBuilder.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='code_sketch_gen_table';");


        CodePack ddlMysql = new CodePack("json.sql","./json.sql",stringBuilder.toString());

        codePacks.add(ddlMysql);

        JTabbedPane tab = new JTabbedPane();
        tab.setForeground(Color.BLACK);

        for(int i = 0;i<codePacks.size();i++)
        {
            CodePack codePack = codePacks.get(i);
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

        //btnPanel.add(btnSaveAll);
       // btnPanel.add(btn);
        statusPanel.add(btnPanel,BorderLayout.EAST);
        cp.add(statusPanel,BorderLayout.SOUTH);

        return cp;
    }

    private void saveAll()
    {

    }

    private String getMySQLDataType(FieldInfo fieldInfo){
        String commonType = fieldInfo.getDataType();
        String defaultValue  = fieldInfo.getDefaultValue();
        if(Objects.isNull(defaultValue)) defaultValue = "";
        String mysqlType = "varchar(200)";
        if(commonType.equals(FieldMapper.TYPE_INT)){
            if(defaultValue.length() > 11){
                mysqlType = "bigint(20)";
            }else{
                mysqlType = "int(11)";
            }

        }else if(commonType.equals(FieldMapper.TYPE_DATETIME)){
            mysqlType = "datetime";
        }else if(commonType.equals(FieldMapper.TYPE_BLOB)){
            mysqlType = "blob";
        }else if(commonType.equals(FieldMapper.TYPE_FLOAT)){
            mysqlType = "decimal(11,7)";
        }else if(commonType.equals(FieldMapper.TYPE_STRING)){
           if(defaultValue.length() > 200){
                mysqlType = "text";
            }else if(defaultValue.length() > 100){
                mysqlType = "varchar(200)";
            }else if(defaultValue.length() > 50){
                mysqlType = "varchar(80)";
            }else if(defaultValue.length() > 30){
                mysqlType = "varchar(40)";
            }else{
                mysqlType = "varchar(20)";
            }
        }else if(commonType.equals(FieldMapper.TYPE_BOOLEAN)){
            mysqlType = "tinyint(1)";
        }else if(commonType.equals(FieldMapper.TYPE_TIMESTAMP)){
            mysqlType = "datetime";
        }else if(commonType.equals(FieldMapper.TYPE_ENUM)){
            mysqlType = "varchar(20)";
        }else if(commonType.equals(FieldMapper.TYPE_YEAR)){
            mysqlType = "int(11)";
        }else if(commonType.equals(FieldMapper.TYPE_TIME)){
            mysqlType = "time";
        }else if(commonType.equals(FieldMapper.TYPE_DATE)){
            mysqlType = "date";
        }else if(commonType.equals(FieldMapper.TYPE_TEXT)){
            mysqlType = "text";
        }
        return mysqlType;
    }
}
