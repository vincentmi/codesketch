package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class MainFrame extends JFrame {

    private JMenuBar mainMenubar = null;

    private JPanel toolbarLeftPanel = null;
    private JPanel toolbarRightPanel = null;
    private JComboBox comboboxProfile = null;
    private JComboBox comboboxSchema = null;
    private JScrollPane centerPanel = null;
    private JTextArea console = null;

    private JFrame main;

    public MainFrame() {
        init();
    }

    public void init() {
        setTitle("Code Sketch");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));

        BorderLayout layout = new BorderLayout();
        getContentPane().setLayout(layout);

        setJMenuBar(getMainMenubar());

        add(getMainToolbar(), BorderLayout.NORTH);

        console = new JTextArea(5, 100);

        console.setBackground(Color.black);
        console.setForeground(Color.WHITE);
        console.setEditable(false);
        console.setAutoscrolls(true);

        JScrollPane consolePanel = new JScrollPane();
        consolePanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        consolePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        consolePanel.getViewport().add(console);
        add(consolePanel, BorderLayout.SOUTH);

        centerPanel = new JScrollPane();
        centerPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        centerPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel flag = new JPanel(new FlowLayout());
        flag.setBackground(Color.white);
        JLabel flagText = new JLabel("Please select schema then press refresh");
        flagText.setFont(new java.awt.Font("Dialog", 1, 18));
        flag.add(flagText);
        centerPanel.getViewport().add(flag);
        centerPanel.setBackground(Color.WHITE);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);

        addWindowListener(new MainFrameListener());

        CodeSketch.center(this);
        main = this;


    }


    public void appendConsole(String msg) {
        this.console.append(new SimpleDateFormat("MM-dd HH:mm:ss - ").format(new Date()) + msg + "\n");
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



    private JPanel getMainToolbar() {

        final Setting setting = Loader.getInstance().getSetting();

        toolbarLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboboxProfile = new JComboBox();
        comboboxSchema = new JComboBox();
        toolbarLeftPanel.add(new JLabel("Profile:"));
        toolbarLeftPanel.add(comboboxProfile);
        toolbarLeftPanel.add(comboboxSchema);


        JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tempPanel.add(new JLabel("Template:"));
        final JComboBox comboboxTemp = new JComboBox();
        ArrayList<TemplateInfo> templateInfos = Loader.getInstance().getTemplateInfos();
        for (int i = 0; i < templateInfos.size(); i++) {
            comboboxTemp.addItem(templateInfos.get(i).getName());
        }
        tempPanel.add(comboboxTemp);
        JButton tempButton = new JButton("Variables");
        tempButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TemplateView(comboboxTemp.getSelectedIndex()).show();
            }
        });
        tempPanel.add(tempButton);

        JPanel projectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JTextField textProject = new JTextField(setting.getProject());
        textProject.setColumns(40);
        projectPanel.add(new JLabel("Project:"));
        projectPanel.add(textProject);

        textProject.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                setting.setProject(textProject.getText());
                Loader.getInstance().saveSetting();
            }
        });

        comboboxProfile.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Setting setting = Loader.getInstance().getSetting();
                    Profile selected = setting.getProfiles().get(comboboxProfile.getSelectedIndex());
                    setting.setProfile(selected.getName());
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
                    Setting setting = Loader.getInstance().getSetting();
                    Profile selected = setting.getProfiles().get(comboboxProfile.getSelectedIndex());
                    selected.setSchema(selectedItem);
                    Loader.getInstance().saveSetting();
                }
            }
        });

        JPanel rightPanel = new JPanel();

        JButton btnOpenProject = new JButton("Open Project");

        btnOpenProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                File dirToOpen = null;
                try {
                    dirToOpen = new File(textProject.getText());
                    desktop.open(dirToOpen);
                } catch (IOException e1) {
                    CodeSketch.error(e1.getMessage());
                }
            }
        });

        rightPanel.add(btnOpenProject);

        final JButton openSetting = new JButton("Open Setting");
        openSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                File dirToOpen = null;
                try {
                    dirToOpen = new File(Loader.getInstance().getRootPath());
                    desktop.open(dirToOpen);
                } catch (IOException e1) {
                    CodeSketch.error(e1.getMessage());
                }
            }
        });
        rightPanel.add(openSetting);

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
                TemplateInfo info = Loader.getInstance().getTemplateInfos().get(comboboxTemp.getSelectedIndex());
                Generator gen = new Generator(info);
                HashMap<String,Object> data = new HashMap<String, Object>() {{
                    put("name", "miwenshu");
                }};
                CodeSketch.info(gen.perform("info.json", data));
                CodeSketch.info(gen.performString("xxx${name}n , ${name},12312", data));

            }
        });
        rightPanel.add(generateButton);

        //JToolBar toolbar = new JToolBar();
        JPanel toolbar = new JPanel();
        //toolbar.setFloatable(false);
        toolbar.setLayout(new GridLayout(4,1,3,3));
        toolbar.add(toolbarLeftPanel);
        toolbar.add(tempPanel);
        toolbar.add(projectPanel);
        toolbar.add(rightPanel);

        return toolbar;
    }

    public void reloadProfileSelection() {
        Setting setting = Loader.getInstance().getSetting();
        ArrayList<Profile> profiles = setting.getProfiles();

        DefaultComboBoxModel cbm = (DefaultComboBoxModel) comboboxProfile.getModel();
        cbm.removeAllElements();
        int selected = 0;
        Profile profile;
        for (int i = 0; i < profiles.size(); i++) {
            profile = profiles.get(i);
            cbm.addElement(profile.getName());
            if (profile.getName().equals(setting.getProfile())) {
                selected = i;
            }
        }
        comboboxProfile.setSelectedIndex(selected);
    }

    synchronized public void reloadSchemaSelection() {
        comboboxSchema.removeAllItems();
        Profile profile = Loader.getInstance().getSetting().getProfiles().get(comboboxProfile.getSelectedIndex());
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
        Profile profile = Loader.getInstance().getSetting().getProfiles().get(comboboxProfile.getSelectedIndex());
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

            panel.setBackground(Color.white);

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
