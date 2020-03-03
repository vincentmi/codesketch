package com.vnzmi.tool.ui;

import com.sun.javafx.font.FontFactory;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.SettingLoader;
import com.vnzmi.tool.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.SimpleFormatter;

public class MainFrame extends JFrame {

    private JMenuBar mainMenubar = null;

    private JPanel toolbarLeftPanel = null;
    private JPanel toolbarRightPanel = null;
    private JComboBox comboboxProfile = null;
    private JComboBox comboboxSchema = null;
    private JScrollPane centerPanel = null;
    private TextArea console = null;

    private JFrame main;

    public MainFrame() {
        init();
    }

    public void init() {
        setTitle("CodeSketch");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));
        JLabel label = new JLabel("Code Sketch");

        BorderLayout layout = new BorderLayout();
        getContentPane().setLayout(layout);

        setJMenuBar(getMainMenubar());

        add(getMainToolbar(), BorderLayout.NORTH);


        console =  new TextArea(5,100);


        console.setBackground(Color.black);
        console.setForeground(Color.WHITE);
        console.setEditable(false);

        add(console,BorderLayout.SOUTH);

        centerPanel = new JScrollPane();
        centerPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        centerPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel flag = new JPanel(new FlowLayout());
        JLabel flagText = new JLabel("Please select schema then press refresh");

        flag.add(flagText);
        centerPanel.getViewport().add(flag);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);


        addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
                reloadProfileSelection();
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
        });


        main = this;

    }

    public void appendConsole(String msg)
    {
        this.console.append(new SimpleDateFormat("MM-dd HH:mm:ss ").format(new Date()) + msg + "\n");
    }


    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }


    public JMenuBar getMainMenubar() {
        JMenuBar main = new JMenuBar();
        main.add(createFileMenu());
        main.add(createHelpMenu());
        main.setVisible(true);
        return main;
    }

    public JMenu createFileMenu() {
        JMenu menuFile = new JMenu("Setting");
        menuFile.add(new JMenuItem("Preferences"));
        menuFile.add(new JMenuItem("Database Profile"));
        menuFile.add(new JMenuItem("Templates"));
        return menuFile;
    }

    public JMenu createHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.add(new JMenuItem("Document"));
        menu.add(new JMenuItem("Homepage"));

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMessage("CodeSketch by Vincent Mi ");
            }
        });
        menu.add(aboutItem);
        return menu;
    }


    private JToolBar getMainToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setLayout(new BorderLayout());
        toolbarLeftPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        comboboxProfile = new JComboBox();
        comboboxSchema = new JComboBox();
        toolbarLeftPanel.add(comboboxProfile);
        toolbarLeftPanel.add(comboboxSchema);
        toolbar.add(toolbarLeftPanel, BorderLayout.CENTER);

        comboboxProfile.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {

                String selectedItem = (String) comboboxProfile.getSelectedItem();
                if (selectedItem != null) {
                    SettingLoader.getInstance().getSetting().setProfile(selectedItem);
                    //SettingLoader.getInstance().store();
                }

                    new Thread(new Runnable() {
                        public void run() {
                            reloadSchemaSelection();
                        }
                    }).start();


            }
        });

        comboboxSchema.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedItem = (String) comboboxSchema.getSelectedItem();
                if (selectedItem != null) {
                    Profile profile = SettingLoader.getInstance().getSelectedProfile();
                    profile.setSchema(selectedItem);
                    SettingLoader.getInstance().store();
                }
            }
        });

        JPanel rightPanel = new JPanel();
        toolbar.add(rightPanel, BorderLayout.EAST);

        final JButton reloadProfileButton = new JButton("Reload Profile");
        reloadProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadProfileSelection();
            }
        });

        rightPanel.add(reloadProfileButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTables();
            }
        });
        rightPanel.add(refreshButton);

        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String templatePath = System.getProperty("user.home")+ File.separator + ".codesketch"+File.separator + "templates"+File.separator + "default";
                CodeSketch.info(templatePath);
                Generator gen = new Generator(templatePath);
                gen.perform("info.json" , new HashMap<String, Object>(){{put("name","miwenshu");}});
            }
        });
        rightPanel.add(generateButton);


        return toolbar;
    }

     public void reloadProfileSelection() {
        Setting setting = SettingLoader.getInstance().load().getSetting();
        Profile[] profiles = setting.getProfiles();

        DefaultComboBoxModel cbm = (DefaultComboBoxModel) comboboxProfile.getModel();
        cbm.removeAllElements();
        CodeSketch.info(setting.getProfile());
        int selected = 0;
        for (int i = 0; i < profiles.length; i++) {
            cbm.addElement(profiles[i].getName());
            if (profiles[i].getName().equals(setting.getProfile())) {
                selected = i;
            }
        }

        comboboxProfile.setSelectedIndex(selected);
    }

    public void reloadSchemaSelection() {
        comboboxSchema.removeAllItems();
        Profile profile = SettingLoader.getInstance().getSelectedProfile();
        if (profile == null) {
            showMessage("profile not selected");
        } else {
            ProfileConnection connection = ProfileConnection.create(profile);
            try {
                String[] databases = connection.getDatabases();
                CodeSketch.info(databases.toString());
                int selected = -1;
                for (int i = 0; i < databases.length; i++) {
                    comboboxSchema.addItem(databases[i]);
                    if (databases[i].equals(profile.getSchema())) {
                        selected = i;
                    }
                }
                if (selected > -1) {
                    comboboxSchema.setSelectedIndex(selected);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                CodeSketch.error(e.getMessage());
                showMessage(e.getMessage());
            }
        }
    }

    public void refreshTables() {
        Profile profile = SettingLoader.getInstance().getSelectedProfile();
        if (profile == null) {
            showMessage("profile not selected");
        }

        CodeSketch.info("current db = " + profile);

        ProfileConnection pc = ProfileConnection.create(profile);
        HashMap<String, TableInfo> tables;
        try {
            tables = pc.getTableInfos();
            centerPanel.removeAll();

            centerPanel.setVerticalScrollBar(new JScrollBar());

            JPanel panel = new JPanel();

            Iterator<TableInfo> it = tables.values().iterator();
            TableInfo tableInfo;
            GridLayout panelLayout = new GridLayout(0, 2, 3, 3);
            panel.setLayout(panelLayout);
            while (it.hasNext()) {
                tableInfo = it.next();
                panel.add(new TablePanel(tableInfo).getPanel());
            }
            CodeSketch.info(tables.size() + " tables loaded");
            panel.setVisible(true);
            int rows = panelLayout.getRows();
            panel.setAutoscrolls(false);
            JViewport jw = new JViewport();
            jw.add(panel);
            jw.validate();
            jw.repaint();
            centerPanel.setViewport(jw);
            centerPanel.setVisible(true);
            centerPanel.updateUI();
            centerPanel.revalidate();
            centerPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
}
