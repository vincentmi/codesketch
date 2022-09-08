package com.vnzmi.tool.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.StringUtil;
import com.vnzmi.tool.model.FieldInfo;
import com.vnzmi.tool.model.TableInfo;
import com.vnzmi.tool.model.mapper.FieldMapper;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

public class JsonGeneratorView{

    JDialog dialog;

    public JsonGeneratorView(Window parent) {
        dialog = new JDialog(parent);
        //dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        dialog.setTitle("JSON generator");
        dialog.setSize(CodeSketch.getFrameSize(0.7));

        dialog.setLayout(new BorderLayout());
        dialog.setResizable(true);
        CodeSketch.center(dialog);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton btnGen = new JButton("Preview");
        JPanel btnPanel = new JPanel(new BorderLayout(3,3));
        btnPanel.add(new Label("Please paste you data json below .We will try to guess a  data structure."),BorderLayout.WEST);
        btnPanel.add(btnGen,BorderLayout.EAST);

        mainPanel.add(btnPanel, BorderLayout.NORTH);

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAutoIndentEnabled(true);
        textArea.setLineWrap(true);
        textArea.setText("");

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/idea.xml"));
            theme.apply(textArea);
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnGen.addActionListener(e -> {
            String jsonData = textArea.getText();
            CodeSketch.info(jsonData);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode node = objectMapper.readTree(jsonData);
                if(!node.isObject()){
                    JOptionPane.showMessageDialog(dialog,"Please input a JSON object string");
                }else{

                    List<FieldInfo> fieldInfos = new ArrayList<>();

                    Iterator<Map.Entry<String, JsonNode>> children  = node.fields();
                    Map.Entry<String, JsonNode> temp;
                    while(children.hasNext()){
                        temp = children.next();
                        JsonNode value = temp.getValue();
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.setName(StringUtil.toLine(temp.getKey()));
                        fieldInfo.setComment("");

                        String dataType ;
                        if(value.isBoolean()){
                            dataType = FieldMapper.TYPE_BOOLEAN;
                        }else if(value.isNumber() || value.isLong() || value.isBigDecimal()){
                            dataType = FieldMapper.TYPE_INT;
                        }else if(value.isDouble() || value.isBigDecimal()){
                            dataType = FieldMapper.TYPE_FLOAT;
                        }else if(value.isTextual()){
                            String textValue = value.textValue();
                            if(textValue.matches("[0-9\\-: ]*")){
                                dataType = FieldMapper.TYPE_DATETIME;
                            }else{
                                dataType = FieldMapper.TYPE_STRING;
                            }

                        }else{
                            dataType = FieldMapper.TYPE_STRING;
                        }



                        fieldInfo.setDataType(dataType);
                        fieldInfo.setDefaultValue(value.textValue());
                        fieldInfos.add(fieldInfo);
                    }

                    TableInfo jsonTable = new TableInfo();
                    jsonTable.setComment("");
                    jsonTable.setCatalog("");
                    jsonTable.setSchema("");
                    jsonTable.setName("json_object");
                    jsonTable.setFields(fieldInfos);
                    jsonTable.setPk(fieldInfos.get(0));
                    new PreviewPanel(jsonTable);


                }
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
                JOptionPane.showMessageDialog(dialog,"Bad Json Data : "+jsonProcessingException.getMessage());
            }
        });

        RTextScrollPane sp = new RTextScrollPane(textArea);
        mainPanel.add(sp, BorderLayout.CENTER);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

}
