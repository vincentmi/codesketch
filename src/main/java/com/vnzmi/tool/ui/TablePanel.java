package com.vnzmi.tool.ui;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.TableInfo;
import com.vnzmi.tool.model.TemplateFile;
import com.vnzmi.tool.model.TemplateInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TablePanel {
    private TableInfo tableInfo;
    private JPanel tablePanel;
    private JCheckBox tableCheckBox ;
    public TablePanel(TableInfo tableinfo)
    {
        this.tableInfo = tableinfo;
        tablePanel =  new JPanel();
        tablePanel.setBackground(Color.white);

        tablePanel.setPreferredSize(new Dimension(CodeSketch.getMainFrame().getWidth() - 10,30));

        tablePanel.setLayout(new GridLayout(1,3,0,0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));

        tableCheckBox = new JCheckBox(tableInfo.getName());

        titlePanel.add(tableCheckBox);
        titlePanel.setBackground(null);
        tablePanel.add(titlePanel);

        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        checkboxPanel.setBackground(null);

        TemplateInfo templateInfo = CodeSketch.getMainFrame().getSelectedTemplateInfo();
        TemplateFile[] files = templateInfo.getFiles();
        for(int i = 0 ;i<files.length ;i++)
        {
            checkboxPanel.add(new JCheckBox(files[i].getFile()));
        }
        tablePanel.add(checkboxPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        buttonPanel.setBackground(null);
        JButton btnPreview = new JButton("Preview");
        btnPreview.addActionListener(e -> new PreviewPanel(tableInfo));
        buttonPanel.add(btnPreview);
        buttonPanel.add(new JButton("Generate"));
        tablePanel.add(buttonPanel);
        tablePanel.addMouseListener(new TablePanelMouseListener(tableCheckBox));
    }

    public JPanel getPanel()
    {
        return tablePanel;

    }
}
