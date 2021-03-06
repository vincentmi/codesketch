package com.vnzmi.tool;

import com.vnzmi.tool.ui.MainFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;


public class CodeSketch {
    private static Logger logger = null;
    private static MainFrame mainFrame = null;
    public static boolean inJar = false;
    public static String jarFile = "";
    public static float javaVersion = 0;

    public static void main(String[] args){
        info("Application starting");

        javaVersion = javaVersion(System.getProperty("java.version"));
        info("JVM="+javaVersion);
        setDockIcon();

        URL resource = CodeSketch.class.getClassLoader().getResource("setting.json");
        String path = resource.getPath();

        int pos = path.indexOf('!');

        if(pos != -1)
        {
            inJar = true;
            jarFile = path.substring(5,pos);
            System.out.println(jarFile);
        }else{
            inJar = false;
        }

        mainFrame = new MainFrame();
    }

    private static void setDockIcon(){
        try {
            Class util = Class.forName("com.apple.eawt.Application");
            Method getApplication = util.getMethod("getApplication", new Class[0]);
            Object application = getApplication.invoke(util);
            Class params[] = new Class[1];
            params[0] = Image.class;
            Method setDockIconImage = util.getMethod("setDockIconImage", params);
            URL url = CodeSketch.class.getClassLoader().getResource("codesketch.png");
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            setDockIconImage.invoke(application, image);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static MainFrame getMainFrame()
    {
        if(mainFrame == null){
            mainFrame = new MainFrame();
        }
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

    public static float javaVersion(String version)
    {
        if(version == null){
            throw new RuntimeException("No JVM");
        }
        version  = version.replace('_','.');
        int dot1 = version.indexOf('.');

        if(dot1 == -1){
            return Float.parseFloat(version);
        }else {
            int dot2 = version.indexOf('.',dot1+1);
            if(dot2 == -1){
                return Float.parseFloat(version);
            }else{
                return Float.parseFloat(version.substring(0,dot2));
            }
        }
    }

    public static void  center(Window frame)
    {
        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width/2-frame.getWidth()/2,screenSize.height/2-frame.getHeight()/2);
    }

    public static Dimension getFrameSize()
    {
        return getFrameSize(0.8);
    }

    public static Dimension getFrameSize(double percent)
    {
        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width *percent);
        int height = (int) (screenSize.height * percent);
        return new Dimension(width,height);
    }

    public static void info(int msg)
    {
        info(Integer.toString(msg));
    }

    public static  void info(String msg)
    {
        getLogger().info(msg);
        if(mainFrame != null){
            getMainFrame().appendConsole(msg);
        }

    }

    public static void fatal(String msg)
    {
        JOptionPane.showMessageDialog(getMainFrame(),msg,"错误",JOptionPane.ERROR_MESSAGE);
        error(msg);
    }

    public static  void error(String msg)
    {
        getLogger().error(msg);
        if(mainFrame != null){
            getMainFrame().appendConsole(msg);
        }
    }
}
