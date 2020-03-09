package com.vnzmi.tool.ui;

import apple.laf.JRSUIUtils;
import com.vnzmi.tool.CodeSketch;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class ProfileListView {
    public ProfileListView()
    {
        JDialog main = new JDialog();
        main.setTitle("Profile setting");
        main.setSize(600,400);
        main.setResizable(false);
        CodeSketch.center(main);

        JTabbedPane tab = new JTabbedPane();
        

        main.setVisible(true);


    }
}
