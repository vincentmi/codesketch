package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.model.TableInfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

public class TableView extends JPanel {
    private HashMap<String, TableInfo> info;
    public TableView(HashMap<String, TableInfo> info)
    {
        this.info = info;
        init();
    }

    public void init()
    {

        setLayout(new FlowLayout());
        Iterator<TableInfo> it = info.values().iterator();
        TableInfo tableInfo;
        while (it.hasNext())
        {
            tableInfo = it.next();
            CodeSketch.getLogger().info(tableInfo);
            add(new JLabel(tableInfo.getName()));
        }
        setVisible(true);
        validate();
        repaint();

    }
}
