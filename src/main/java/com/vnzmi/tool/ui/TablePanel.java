package com.vnzmi.tool.ui;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.TableInfo;
import com.vnzmi.tool.model.TemplateFile;
import com.vnzmi.tool.model.TemplateInfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TablePanel {
    private TableInfo tableInfo;
    private JPanel tablePanel;
    private JCheckBox table ;
    private HashMap<String,JCheckBox>  options;
    public TablePanel(TableInfo tableinfo)
    {
        this.tableInfo = tableinfo;
        tablePanel =  new JPanel();
        tablePanel.setBackground(Color.white);

        tablePanel.setPreferredSize(new Dimension(CodeSketch.getMainFrame().getWidth() - 10,30));

        tablePanel.setLayout(new GridLayout(1,3,0,0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));

        table = new JCheckBox(tableInfo.getName());
        options = new HashMap<>();

        titlePanel.add(table);
        titlePanel.setBackground(null);
        tablePanel.add(titlePanel);

        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        checkboxPanel.setBackground(null);

        TemplateInfo templateInfo = CodeSketch.getMainFrame().getSelectedTemplateInfo();
        TemplateFile[] files = templateInfo.getFiles();
        for(int i = 0 ;i<files.length ;i++)
        {
            JCheckBox option = new JCheckBox(files[i].getFile());
            options.put(files[i].getFile() , option);
            checkboxPanel.add(option);
        }
        tablePanel.add(checkboxPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        buttonPanel.setBackground(null);
        JButton btnPreview = new JButton("Preview");
        btnPreview.addActionListener(e -> new PreviewPanel(tableInfo));
        buttonPanel.add(btnPreview);
        buttonPanel.add(new JButton("Generate"));
        tablePanel.add(buttonPanel);
        tablePanel.addMouseListener(new TablePanelMouseListener(table));
    }

    public JCheckBox getTableCheckbox()
    {
        return table;
    }

    public TableInfo getTableInfo(){
        return tableInfo;
    }

    public HashMap<String,JCheckBox> getOptions()
    {
        return options;
    }

    public JPanel getPanel()
    {
        return tablePanel;

    }
}
