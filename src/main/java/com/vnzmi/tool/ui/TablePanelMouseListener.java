package com.vnzmi.tool.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TablePanelMouseListener implements MouseListener {
    private JCheckBox tableCheckBox;

    public TablePanelMouseListener(JCheckBox checkBox) {
        tableCheckBox = checkBox;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

        tableCheckBox.setSelected(!tableCheckBox.isSelected());
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        JPanel jp = (JPanel) e.getSource();
        jp.setBackground(new Color(245, 245, 245));
    }

    public void mouseExited(MouseEvent e) {
        JPanel jp = (JPanel) e.getSource();
        jp.setBackground(Color.white);
    }
}
