package com.vnzmi.tool.ui;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.*;
import sun.plugin2.util.SystemUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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
    private JPanel centerPanel = null;
    private JTextArea console = null;
    private JComboBox comboboxTemp;


    private HashMap<String,TableInfo> tablesInfos ;

    private JFrame main;

    public MainFrame() {
        init();
    }



    public void init() {
        setTitle("Code Sketch");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        ImageIcon icon = new ImageIcon(Loader.getResource("codesketch.png"));

        setIconImage(icon.getImage());

        if(SystemUtil.getOSType() == SystemUtil.MACOSX)
        {
            Application application = Application.getApplication();
            application.setDockIconImage(icon.getImage());
            application.setAboutHandler(e -> {showAboutFrame();});
        }

        setSize(CodeSketch.getFrameSize());
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

        JPanel flag = new JPanel(new GridLayout(1,1,5,5));
        flag.setBackground(null);
        JLabel flagText = new JLabel("Please select schema then press refresh");
        flagText.setFont(new java.awt.Font("Dialog", 1, 18));
        flag.add(flagText);
        centerPanel = new JPanel(new FlowLayout());

        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(flag,BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);

        addWindowListener(new MainFrameListener());

        CodeSketch.center(this);
        main = this;


    }

    public TemplateInfo getSelectedTemplateInfo()
    {
        return Loader.getInstance().getTemplateInfos().get(comboboxTemp.getSelectedIndex());
    }


    public void appendConsole(String msg) {
        this.console.append(new SimpleDateFormat("MM-dd HH:mm:ss - ").format(new Date()) + msg + "\n");
    }


    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
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

    public void showAboutFrame()
    {
        showMessage("CodeSketch by Vincent Mi ");
    }

    public JMenu createHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.add(new JMenuItem("Document"));
        menu.add(new JMenuItem("Homepage"));

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAboutFrame();
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
        comboboxTemp = new JComboBox();
        ArrayList<TemplateInfo> templateInfos = Loader.getInstance().getTemplateInfos();
        for (int i = 0; i < templateInfos.size(); i++) {
            comboboxTemp.addItem(templateInfos.get(i).getName());
        }
        tempPanel.add(comboboxTemp);
        JButton tempButton = new JButton("Variables");
        tempButton.addActionListener(e -> new TemplateView(comboboxTemp.getSelectedIndex()).show());
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

        comboboxProfile.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Setting setting1 = Loader.getInstance().getSetting();
                Profile selected = setting1.getProfiles().get(comboboxProfile.getSelectedIndex());
                setting1.setProfile(selected.getName());
            }
            new Thread(new Runnable() {
                public void run() {
                    reloadSchemaSelection();
                }
            }).start();
        });


        comboboxTemp.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Setting setting12 = Loader.getInstance().getSetting();
                String selected = (String)comboboxTemp.getItemAt(comboboxTemp.getSelectedIndex());
                setting12.setTemplate(selected);
            }
        });

        comboboxSchema.addItemListener(e -> {
            String selectedItem = (String) comboboxSchema.getSelectedItem();
            if (selectedItem != null) {
                Setting setting13 = Loader.getInstance().getSetting();
                Profile selected = setting13.getProfiles().get(comboboxProfile.getSelectedIndex());
                selected.setSchema(selectedItem);
                //Loader.getInstance().saveSetting();
            }
        });

        JPanel rightPanel = new JPanel();

        JButton btnOpenProject = new JButton("Open Project");

        btnOpenProject.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            File dirToOpen = null;
            try {
                dirToOpen = new File(textProject.getText());
                desktop.open(dirToOpen);
            } catch (IOException e1) {
                CodeSketch.error(e1.getMessage());
            }
        });

        rightPanel.add(btnOpenProject);

        final JButton openSetting = new JButton("Open Setting");
        openSetting.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            File dirToOpen = null;
            try {
                dirToOpen = new File(Loader.getInstance().getRootPath());
                desktop.open(dirToOpen);
            } catch (IOException e1) {
                CodeSketch.error(e1.getMessage());
            }
        });
        rightPanel.add(openSetting);

        final JButton reloadProfileButton = new JButton("Reload Profile");
        reloadProfileButton.addActionListener(e -> reloadProfileSelection());

        rightPanel.add(reloadProfileButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTables();
            }
        });
        rightPanel.add(refreshButton);

        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            TemplateInfo info = Loader.getInstance().getTemplateInfos().get(comboboxTemp.getSelectedIndex());
            Generator gen = new Generator(info);

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
                //CodeSketch.info(databases.toString());
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

    public JPanel getTableToolbar()
    {
        JPanel toolbar = new JPanel();
        //tablePanel.setBackground(Color.white);
        //toolbar.setPreferredSize(new Dimension(CodeSketch.getMainFrame().getWidth() - 10,30));

        toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        toolbar.add(new JCheckBox("All tables"));
        toolbar.add(new JCheckBox("Inverse Tables"));

        TemplateInfo templateInfo = CodeSketch.getMainFrame().getSelectedTemplateInfo();
        TemplateFile[] files = templateInfo.getFiles();
        for(int i = 0 ;i<files.length ;i++)
        {
            toolbar.add(new JCheckBox(files[i].getFile()));
            toolbar.add(new JCheckBox("Inverse "+ files[i].getFile()));
        }

        return toolbar;
    }

    public void refreshTables() {
        Profile profile = Loader.getInstance().getSetting().getProfiles().get(comboboxProfile.getSelectedIndex());
        if (profile == null) {
            showMessage("profile not selected");
        }
        Loader.getInstance().saveSetting();

        CodeSketch.info("current db = " + profile);

        ProfileConnection pc = ProfileConnection.create(profile);
        centerPanel.removeAll();
        centerPanel.setLayout(new BorderLayout());

        centerPanel.add(getTableToolbar(),BorderLayout.SOUTH);

        JScrollPane tableList = new JScrollPane();
        tableList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //tableList.setBorder(null);
        centerPanel.add(tableList, BorderLayout.CENTER);

        try {
            tablesInfos = pc.getTableInfos();

            JPanel panel = new JPanel();
            panel.setBackground(Color.white);

            Iterator<TableInfo> it = tablesInfos.values().iterator();
            TableInfo tableInfo;
            SpringLayout panelLayout = new SpringLayout();
            panel.setLayout(panelLayout);
            SpringLayout.Constraints constraints;

            int i = 0;
            while (it.hasNext()) {
                tableInfo = it.next();
                JPanel tablePanel = new TablePanel(tableInfo).getPanel();
                panel.add(tablePanel);
                constraints = panelLayout.getConstraints(tablePanel);
                constraints.setConstraint(SpringLayout.WEST,Spring.constant(10));
                constraints.setConstraint(SpringLayout.NORTH,Spring.constant(30*i));
                i++;
            }
            panel.setPreferredSize(new Dimension(main.getWidth() , i*30));
            CodeSketch.info(tablesInfos.size() + " tables loaded");
            panel.setVisible(true);
            panel.setAutoscrolls(false);

            JViewport jw = new JViewport();
            jw.add(panel);
            jw.validate();
            jw.repaint();

            tableList.setViewport(jw);
            //centerPanel.setViewport(jw);
            //centerPanel.setVisible(true);
            centerPanel.updateUI();
            centerPanel.revalidate();
            centerPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
}
