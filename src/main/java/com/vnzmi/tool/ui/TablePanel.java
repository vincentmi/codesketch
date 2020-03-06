package com.vnzmi.tool.ui;
import com.vnzmi.tool.model.TableInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TablePanel {
    private TableInfo tableInfo;
    private JPanel tablePanel;
    public TablePanel(TableInfo tableinfo)
    {
        this.tableInfo = tableinfo;
        tablePanel =  new JPanel();
        tablePanel.setBackground(Color.white);

        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.setBackground(Color.lightGray);
        titlePanel.add(new JCheckBox(tableInfo.getName()));
        tablePanel.add(titlePanel, BorderLayout.NORTH);

        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        checkboxPanel.add(new JCheckBox("view"));
        checkboxPanel.add(new JCheckBox("model"));
        checkboxPanel.add(new JCheckBox("controller"));
        checkboxPanel.add(new JCheckBox("view"));
        //tablePanel.add(checkboxPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        buttonPanel.setBackground(Color.white);
        JButton btnPreview = new JButton("Preview");
        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PreviewPanel();
            }
        });
        buttonPanel.add(btnPreview);
        buttonPanel.add(new JButton("Generate"));
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);
        tablePanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {
                JPanel jp = (JPanel) e.getSource();
                jp.setBorder(BorderFactory.createLineBorder(Color.red));
            }

            public void mouseExited(MouseEvent e) {
                JPanel jp = (JPanel) e.getSource();
                jp.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            }
        });
    }

    public JPanel getPanel()
    {
        return tablePanel;

    }
}
