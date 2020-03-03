package com.vnzmi.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnzmi.tool.model.Profile;
import com.vnzmi.tool.model.Setting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingLoader {
    private static SettingLoader self ;


    private Setting setting = null;

    private SettingLoader()
    {
    }

    public static SettingLoader getInstance()
    {
        if(self == null)
        {
            self = new SettingLoader();
        }
        return self;
    }


    public Setting getSetting()
    {
        if(setting == null) {
            load();
        }

        return setting;
    }

    /**
     * GET  current selected profile
     * @return
     */
    public Profile getSelectedProfile()
    {
        Profile[]  profiles = setting.getProfiles();
        Profile selected = null ;
        CodeSketch.getLogger().info("starting find selected profile");
        for(int i = 0 ; i< profiles.length;i++)
        {
            CodeSketch.getLogger().info("compare "+ profiles[i].getName() + " - "+ setting.getProfile());
            if(profiles[i].getName().equals(setting.getProfile()))
            {
                CodeSketch.getLogger().info(profiles[i]);
                selected =profiles[i];
                break;
            }
        }
        return selected;
    }

    /**
     * load setting from setting.json
     * @return
     */
    public SettingLoader load()
    {
        try {

            String filepath = System.getProperty("user.home") + File.separator + ".codesketch" + File.separator + "setting.json";
            CodeSketch.getLogger().info("loading "+filepath);
            File f = new File(filepath);
            ObjectMapper mapper = new ObjectMapper();
            this.setting = (Setting) mapper.readValue(f, Setting.class);

            CodeSketch.getLogger().info("loaded setting " + this.setting);

        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getLogger().error(e.getMessage());
            CodeSketch.getMainFrame().showMessage(e.getMessage());
        }
        return this;
    }

    /**
     * write current setting to  setting.json
     */
    public void store() {
        String filepath = System.getProperty("user.home") + File.separator + ".codesketch" + File.separator + "setting.json";
        File f = new File(filepath);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(setting.toJson().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            CodeSketch.getLogger().error(e.getMessage());
        }

    }
}
