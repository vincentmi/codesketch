package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.TemplateInfo;
import com.vnzmi.tool.model.TemplateVariable;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TemplateView {

    private int selectedIndex;
    private TemplateInfo templateInfo;
    private Map<String,String> templateValue;
    private ArrayList<JTextField> textFields;

    private JFrame frame;


    public TemplateView(int index) {
        selectedIndex = index;
        templateInfo = Loader.getInstance().getTemplateInfos().get(selectedIndex);
        templateValue = Loader.getInstance().getTemplateValues().get(templateInfo.getName());
        if(templateValue ==null)
        {
            templateValue = new HashMap<String,String>();
            Loader.getInstance().getTemplateValues().put(templateInfo.getName(),templateValue);
        }
    }

    public void show() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(new Dimension(650, 600));
        frame.setResizable(false);

        frame.getContentPane().add(getHeaderPanel(), BorderLayout.NORTH);
        frame.getContentPane().add(getVariablePanel(), BorderLayout.CENTER);

        frame.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        CodeSketch.center(frame);
        frame.setVisible(true);

    }

    private String getVariableValue(TemplateVariable templateVariable)
    {
        if(templateValue == null )
        {
            return templateVariable.getDefaultValue();
        }else{
            return templateValue.getOrDefault(templateVariable.getName() , templateVariable.getDefaultValue());
        }
    }

    private JScrollPane getVariablePanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel panel = new JPanel();
        TemplateVariable[] valueDefines = templateInfo.getVariables();

        scrollPane.getViewport().add(panel);

        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        panel.setBackground(Color.WHITE);

        JLabel label,tips;
        JTextField textField;
        TemplateVariable templateVariable;
        int rowHeight = 30,northStart = 10,rowIndex = 0;
        SpringLayout.Constraints textConstraints,labelConstraints,tipsConstraints;
        String value;
        textFields = new ArrayList<>();
        for(int i = 0 ;i<valueDefines.length;i++)
        {
            templateVariable = valueDefines[i];
            label = new JLabel(templateVariable.getName());
            textField = new JTextField(getVariableValue(templateVariable));
            textField.setColumns(40);

            tips = new JLabel(templateVariable.getDescription());
            tips.setForeground(Color.lightGray);

            //label
            panel.add(label);
            labelConstraints = layout.getConstraints(label);
            labelConstraints.setConstraint(SpringLayout.WEST , Spring.constant(10));
            labelConstraints.setConstraint(SpringLayout.NORTH , Spring.constant(northStart + 5));

            //textfield
            panel.add(textField);
            textConstraints = layout.getConstraints(textField);
            textConstraints.setConstraint(SpringLayout.WEST , Spring.constant(120));
            textConstraints.setConstraint(SpringLayout.NORTH , Spring.constant(northStart));

            //tips
            panel.add(tips);
            tipsConstraints = layout.getConstraints(tips);
            tipsConstraints.setConstraint(SpringLayout.WEST , Spring.constant(10));
            tipsConstraints.setConstraint(SpringLayout.NORTH , Spring.constant(northStart + rowHeight));

            //next
            rowIndex++;
            northStart = northStart + 2*rowHeight;
            textFields.add(i,textField);

        }

        panel.setPreferredSize(new Dimension(650,northStart));



        return scrollPane;
    }

    private JPanel getButtonPanel() {
        JPanel pButton = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> frame.setVisible(false));

        btnSave.addActionListener(e -> {
            TemplateVariable[] variables = templateInfo.getVariables();
            for(int i = 0 ;i<textFields.size();i++)
            {
                TemplateVariable info = variables[i];
                JTextField field = textFields.get(i);
                templateValue.put(info.getName(),field.getText());
            }
            Loader.getInstance().saveTemplateValues();
            frame.setVisible(false);
        });

        pButton.add(btnSave);
        pButton.add(btnCancel);
        return pButton;
    }

    private JPanel getHeaderPanel() {
        //Title
        BorderLayout layout = new BorderLayout();
        layout.setHgap(2);
        layout.setVgap(2);
        JPanel pTitle = new JPanel();
        JTextPane text = new JTextPane();

        text.setBackground(null);
        text.setForeground(Color.darkGray);

        SimpleAttributeSet headStyle = new SimpleAttributeSet();
        StyleConstants.setBold(headStyle, true);
        SimpleAttributeSet normalSet = new SimpleAttributeSet();



        Document doc = text.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "Name:", headStyle);
            doc.insertString(doc.getLength(), templateInfo.getName(), normalSet);
            doc.insertString(doc.getLength(), "\nUrl:", headStyle);
            doc.insertString(doc.getLength(), templateInfo.getUrl(), normalSet);
            doc.insertString(doc.getLength(), "\nAuthor:", headStyle);
            doc.insertString(doc.getLength(), templateInfo.getAuthor(), normalSet);
            doc.insertString(doc.getLength(), "\nDescription:", headStyle);
            doc.insertString(doc.getLength(), templateInfo.getDescription(), normalSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        text.setVisible(true);
        pTitle.add(text, BorderLayout.CENTER);

        JButton btnOpen = new JButton("Open File");

        btnOpen.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            File dirToOpen = null;
            try {
                dirToOpen = new File(templateInfo.getPath());
                desktop.open(dirToOpen);
            } catch (IOException e1) {
                CodeSketch.error(e1.getMessage());
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.add(btnOpen);
        pTitle.add(rightPanel, BorderLayout.EAST);
        return pTitle;
    }
}
