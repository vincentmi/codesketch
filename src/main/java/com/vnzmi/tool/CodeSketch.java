package com.vnzmi.tool;

import com.vnzmi.tool.ui.MainFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;


public class CodeSketch {
    private static Logger logger = null;
    private static MainFrame mainFrame = null;

    public static void main(String[] args){
        info("Application starting");
        mainFrame = new MainFrame();

    }

    public static MainFrame getMainFrame()
    {
        return mainFrame;
    }

    public static Logger getLogger()
    {
        if(logger == null)
        {
            logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        }
        return logger;
    }

    public static void  center(JFrame frame)
    {
        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width/2-frame.getWidth()/2,screenSize.height/2-frame.getHeight()/2);
    }

    public static  void info(String msg)
    {
        getLogger().info(msg);
        if(mainFrame != null){
            getMainFrame().appendConsole(msg);
        }

    }

    public static  void error(String msg)
    {
        getLogger().error(msg);
        if(mainFrame != null){
            getMainFrame().appendConsole(msg);
        }
    }
}
