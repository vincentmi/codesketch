package com.vnzmi.tool;

import com.vnzmi.tool.model.Setting;
import com.vnzmi.tool.ui.MainFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeSketch {
    private static Logger logger = null;
    private static MainFrame mainFrame = null;
    private static Setting setting = null;

    public static void main(String[] args){
        getLogger().info("Application starting");
        mainFrame = new MainFrame();
        SettingLoader.getInstance().load();

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
}
