package com.vnzmi.tool.ui;


import com.apple.eawt.Application;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class MainFrame extends JFrame {

    private JPanel toolbarLeftPanel = null;
    private JComboBox comboboxProfile = null;
    private JComboBox comboboxSchema = null;
    private JPanel centerPanel = null;
    private JTextArea console = null;
    private JComboBox comboboxTemp;

    public ImageIcon icon;

    private HashMap<String, TableInfo> tablesInfos;

    private ArrayList<TablePanel> _tablePanels;

    private JFrame main;

    public MainFrame() {
        init();
    }


    public void init() {
        setTitle("Code Sketch");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        icon = new ImageIcon(ClassLoader.getSystemResource("codesketch.png"));

        setIconImage(icon.getImage());

        //System.out.println(SystemUtil.getOSType());

        if (System.getProperty("os.name").indexOf("Mac OS") != -1) {
            Application application = Application.getApplication();
            application.setDockIconImage(icon.getImage());
            application.setAboutHandler(e -> {
                showAboutFrame();
            });
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

        JPanel flag = new JPanel(new GridLayout(1, 1, 5, 5));
        flag.setBackground(null);
        JLabel flagText = new JLabel("Please select schema then press refresh");
        Font font = new Font("Dialog", 1, 18);
        flagText.setFont(font);
        flagText.setForeground(Color.lightGray);
        flag.add(flagText);
        centerPanel = new JPanel(new FlowLayout());

        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(flag, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);

        addWindowListener(new MainFrameListener());

        CodeSketch.center(this);
        main = this;


    }

    public ArrayList<TablePanel> getTablePanels() {
        return _tablePanels;
    }

    public TemplateInfo getSelectedTemplateInfo() {
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
        //menuFile.add(new JMenuItem("Preferences"));
        JMenuItem menuDatabaseProfile = new JMenuItem("Database Profile");
        final ProfileListView view = new ProfileListView(main);
        menuDatabaseProfile.addActionListener(e -> {
           view.show();
        });
        menuFile.add(menuDatabaseProfile);

        /*JMenuItem menuTemplate = new JMenuItem("Templates");
        menuTemplate.addActionListener(e->{
            new TemplateListView(main);
        });
        menuFile.add(menuTemplate);*/
        return menuFile;
    }

    public void showAboutFrame() {
        new AboutUs(this);
        //showMessage("CodeSketch by Vincent Mi http://vnzmi.com ");
    }

    public JMenu createHelpMenu() {
        JMenu menu = new JMenu("Help");

        JMenuItem docItem = new JMenuItem("Document");
        docItem.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("https://github.com/vincentmi/codesketch"));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
        menu.add(docItem);


        JMenuItem homeItem = new JMenuItem("Homepage");
        homeItem.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("http://vnzmi.com"));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
        menu.add(homeItem);

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
        tempButton.addActionListener(e -> new TemplateView(comboboxTemp.getSelectedIndex()).show(main));
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
                String selected = (String) comboboxTemp.getItemAt(comboboxTemp.getSelectedIndex());
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

        final JButton reloadProfileButton = new JButton("Save Setting");
        reloadProfileButton.addActionListener(e -> saveSetting());

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
            new GeneratorInfoPanel(this).show();

        });
        rightPanel.add(generateButton);

        //JToolBar toolbar = new JToolBar();
        JPanel toolbar = new JPanel();
        //toolbar.setFloatable(false);
        toolbar.setLayout(new GridLayout(4, 1, 3, 3));
        toolbar.add(toolbarLeftPanel);
        toolbar.add(tempPanel);
        toolbar.add(projectPanel);
        toolbar.add(rightPanel);

        return toolbar;
    }

    public void saveSetting()
    {
        Loader.getInstance().saveSetting();
        Loader.getInstance().saveTemplateValues();
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
                //showMessage(e.getMessage());
            }
        }
    }

    public JPanel getTableToolbar() {
        JPanel toolbar = new JPanel();
        //tablePanel.setBackground(Color.white);
        //toolbar.setPreferredSize(new Dimension(CodeSketch.getMainFrame().getWidth() - 10,30));
        toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JCheckBox chkAllTable = new JCheckBox("All tables");
        chkAllTable.addItemListener(e -> {
            if (_tablePanels != null) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    for (int i = 0, max = _tablePanels.size(); i < max; i++) {
                        TablePanel panel = _tablePanels.get(i);
                        panel.getTableCheckbox().setSelected(true);
                    }
                } else {
                    for (int i = 0, max = _tablePanels.size(); i < max; i++) {
                        TablePanel panel = _tablePanels.get(i);
                        panel.getTableCheckbox().setSelected(false);
                    }
                }

            }
        });
        toolbar.add(chkAllTable);

        JCheckBox inverseAllTable = new JCheckBox("Inverse Tables");
        toolbar.add(inverseAllTable);
        inverseAllTable.addItemListener(e -> {
            if (_tablePanels != null) {
                for (int i = 0, max = _tablePanels.size(); i < max; i++) {
                    TablePanel panel = _tablePanels.get(i);
                    boolean newCheck = panel.getTableCheckbox().isSelected();
                    panel.getTableCheckbox().setSelected(!newCheck);
                }

            }
        });

        TemplateInfo templateInfo = CodeSketch.getMainFrame().getSelectedTemplateInfo();
        TemplateFile[] files = templateInfo.getFiles();
        for (int i = 0; i < files.length; i++) {
            JCheckBox allChk = new JCheckBox(files[i].getFile());
            JCheckBox invChk = new JCheckBox("Inverse " + files[i].getFile());
            toolbar.add(allChk);
            toolbar.add(invChk);
            allChk.addItemListener(e -> {
                if (_tablePanels != null) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        for (int j = 0, max = _tablePanels.size(); j < max; j++) {
                            TablePanel panel = _tablePanels.get(j);
                            panel.getOptions().get(allChk.getText()).setSelected(true);
                        }
                    } else {
                        for (int j = 0, max = _tablePanels.size(); j < max; j++) {
                            TablePanel panel = _tablePanels.get(j);
                            panel.getOptions().get(allChk.getText()).setSelected(false);
                        }
                    }
                }
            });

            invChk.addItemListener(e -> {
                if (_tablePanels != null) {
                    for (int j = 0, max = _tablePanels.size(); j < max; j++) {
                        TablePanel panel = _tablePanels.get(j);
                        JCheckBox box = panel.getOptions().get(allChk.getText());
                        box.setSelected(!box.isSelected());
                    }
                }
            });
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
        centerPanel.add(getTableToolbar(), BorderLayout.SOUTH);

        _tablePanels = new ArrayList<TablePanel>();

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
                TablePanel tp = new TablePanel(tableInfo);
                _tablePanels.add(tp);

                JPanel tpPanel = tp.getPanel();


                panel.add(tpPanel);
                constraints = panelLayout.getConstraints(tpPanel);
                constraints.setConstraint(SpringLayout.WEST, Spring.constant(10));
                constraints.setConstraint(SpringLayout.NORTH, Spring.constant(30 * i));
                i++;
            }
            panel.setPreferredSize(new Dimension(main.getWidth(), i * 30));
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
