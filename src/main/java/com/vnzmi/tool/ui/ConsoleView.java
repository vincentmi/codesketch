package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.TemplateInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConsoleView {


    private JFrame frame;
    private JTextArea text ;


    public ConsoleView() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(new Dimension(650, 600));
        frame.setResizable(false);



        text = new JTextArea();
        text.setBackground(Color.black);
        text.setForeground(Color.WHITE);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setAutoscrolls(true);

        JScrollPane jScrollPane = new JScrollPane(text);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jScrollPane.setPreferredSize(new Dimension(650,600));
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(jScrollPane,BorderLayout.CENTER);
        textPanel.setBackground(Color.GREEN);


        frame.getContentPane().add(getHeaderPanel(), BorderLayout.NORTH);
        frame.getContentPane().add(textPanel,BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        CodeSketch.center(frame);
        frame.setVisible(false);
    }

    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    public void append(String str){
        text.append(str);
    }

    public JPanel getHeaderPanel(){
        JPanel panel = new JPanel();
        panel.add(new Label("控制台"));
        return panel;
    }
}