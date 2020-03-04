package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.TemplateInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TemplateVariablesView {

    private  int selectedIndex;


    public TemplateVariablesView(int index)
    {
        selectedIndex = index;
    }

    public void show()
    {

        TemplateInfo info = Loader.getInstance().getTemplateInfos().get(selectedIndex);

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(600,600));
        frame.getContentPane().add(new JLabel("Template Veriables"));
        frame.setVisible(true);


    }
}
