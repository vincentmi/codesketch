package com.vnzmi.tool.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrameListener implements WindowListener {
    public void windowOpened(WindowEvent e) {
        MainFrame mainFrame = (MainFrame) e.getSource() ;
        mainFrame.reloadProfileSelection();
    }

    public void windowClosing(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
}
