package com.vnzmi.tool.ui;

import com.sun.tools.javac.jvm.Code;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.TemplateInfo;
import com.vnzmi.tool.model.TemplateValue;
import com.vnzmi.tool.model.TemplateVariable;
import freemarker.cache.TemplateLoader;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class TemplateVariablesView {

    private  int selectedIndex;
    private TemplateInfo info;

    private JFrame frame;


    public TemplateVariablesView(int index)
    {
        selectedIndex = index;
        info = Loader.getInstance().getTemplateInfos().get(selectedIndex);
    }

    public void show()
    {

        HashMap<String, TemplateValue> values = Loader.getInstance().getTemplateValues();

        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(new Dimension(600,600));

        //Title
        JPanel pTitle = new JPanel(new BorderLayout());
        JTextPane text = new JTextPane();
        text.setBackground(null);

        SimpleAttributeSet headStyle = new SimpleAttributeSet();
        StyleConstants.setBold(headStyle, true);
        SimpleAttributeSet normalSet = new SimpleAttributeSet();


        Document doc = text.getStyledDocument();
        try {
            doc.insertString(doc.getLength(),"Name:",headStyle);
            doc.insertString(doc.getLength(),info.getName(),normalSet);
            doc.insertString(doc.getLength(),"\nUrl:",headStyle);
            doc.insertString(doc.getLength(),info.getUrl(),normalSet);
            doc.insertString(doc.getLength(),"\nAuthor:",headStyle);
            doc.insertString(doc.getLength(),info.getAuthor(),normalSet);
            doc.insertString(doc.getLength(),"\nDescription:",headStyle);
            doc.insertString(doc.getLength(),info.getDescription(),normalSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        text.setVisible(true);
        pTitle.add(text,BorderLayout.CENTER);

        JButton btnOpen= new JButton("Open File");

        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                File dirToOpen = null;
                try {
                    dirToOpen = new File(info.getPath());
                    desktop.open(dirToOpen);
                } catch (IOException e1) {
                    CodeSketch.error(e1.getMessage());
                }
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.add(btnOpen);

        pTitle.add(rightPanel,BorderLayout.EAST);


        frame.getContentPane().add(pTitle,BorderLayout.NORTH);
        frame.setVisible(true);

        JPanel pButton = new JPanel(new GridLayout(1,2,5,5));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        pButton.add(btnSave);
        pButton.add(btnCancel);
        frame.getContentPane().add(pButton,BorderLayout.SOUTH);

    }
}
