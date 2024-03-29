package com.vnzmi.tool.ui;


import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.*;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel toolbarLeftPanel = null;
    private JComboBox comboboxProfile = null;
    private JComboBox comboboxSchema = null;
    private JPanel centerPanel = null;
    private ConsoleView console = null;
    private JComboBox comboboxTemp;

    public ImageIcon icon;

    private HashMap<String, TableInfo> tablesInfos;

    private ArrayList<TablePanel> _tablePanels;

    private JFrame main;

    public MainFrame() {
        console = new ConsoleView();
        setGlobalFont(new Font("Dialog",Font.PLAIN,12));
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
        /*
        if(CodeSketch.javaVersion > 1.9F) {

        }else{
            if (System.getProperty("os.name").indexOf("Mac OS") != -1) {
                Application application = Application.getApplication();
                application.setDockIconImage(icon.getImage());
                application.setAboutHandler(e -> {
                    showAboutFrame();
                });
            }
        }*/





        setSize(CodeSketch.getFrameSize());
        setMinimumSize(new Dimension(400, 300));

        BorderLayout layout = new BorderLayout();
        getContentPane().setLayout(layout);

        setJMenuBar(getMainMenubar());

        add(getMainToolbar(), BorderLayout.NORTH);

        /*console = new JTextArea(5, 100);

        console.setBackground(Color.black);
        console.setForeground(Color.WHITE);
        console.setEditable(false);
        console.setAutoscrolls(true);
        console.setVisible(false);*/




        JScrollPane consolePanel = new JScrollPane();
        consolePanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        consolePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //consolePanel.getViewport().add(console);
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

    public void setGlobalFont(Font font){
        FontUIResource fontRes = new FontUIResource(font);
        for(Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }

    public ArrayList<TablePanel> getTablePanels() {
        return _tablePanels;
    }

    public TemplateInfo getSelectedTemplateInfo() {
        if(comboboxTemp.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(this,"please select a template.");
            return null;
        }else{
            return Loader.getInstance().getTemplateInfos().get(comboboxTemp.getSelectedIndex());
        }
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
        main.add(createViewMenu());
        main.add(createHelpMenu());
        main.setVisible(true);
        return main;
    }

    public JMenu createViewMenu(){
        JMenu menuFile = new JMenu("View");
        JMenuItem hideConsole = new JMenuItem("Hide Console");
        JMenuItem showConsole = new JMenuItem("Show Console");
        hideConsole.addActionListener(e -> {
            console.setVisible(false);
        });
        menuFile.add(hideConsole);

        showConsole.addActionListener(e -> {
            console.setVisible(true);
        });
        menuFile.add(showConsole);
        return menuFile;
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

    public synchronized void reloadTemplates(){
        int selectedIndex = comboboxTemp.getSelectedIndex();
        comboboxTemp.removeAllItems();
        ArrayList<TemplateInfo> templateInfos = Loader.getInstance().getTemplateInfos(true);
        for (int i = 0; i < templateInfos.size(); i++) {
            comboboxTemp.addItem(templateInfos.get(i).getName());
        }
        comboboxTemp.setSelectedIndex(selectedIndex);
    }

    private JComboBox getTemplateCombo()
    {
        return comboboxTemp;
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
        reloadTemplates();
        tempPanel.add(comboboxTemp);

        JButton tempReloadButton = new JButton("Reload Templates");
        tempReloadButton.addActionListener(e -> reloadTemplates());
        tempPanel.add(tempReloadButton);

        JButton tempButton = new JButton("Variables");
        tempButton.addActionListener(e -> new TemplateView(getTemplateCombo().getSelectedIndex()).show(main));
        tempPanel.add(tempButton);

        JButton tempMktButton = new JButton("More Templates");
        tempMktButton.addActionListener(e -> new TemplateListView(this));
        tempPanel.add(tempMktButton);


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
                Loader.getInstance().saveSetting();
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

        JButton generateFromJsonButton = new JButton("Generate From JSON");
        generateFromJsonButton.addActionListener(e -> {
            new JsonGeneratorView(this);

        });
        rightPanel.add(generateFromJsonButton);

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
        TemplateInfo templateInfo = CodeSketch.getMainFrame().getSelectedTemplateInfo();
        TemplateFile[] files = templateInfo.getFiles();

        //tablePanel.setBackground(Color.white);
        //toolbar.setPreferredSize(new Dimension(CodeSketch.getMainFrame().getWidth() - 10,30));
        LayoutManager layoutManager;

        layoutManager = new GridLayout(2,14);

        toolbar.setLayout(layoutManager);
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


        for (int i = 0; i < files.length; i++) {
            JCheckBox allChk = new JCheckBox(files[i].getFile());
            JCheckBox invChk = new JCheckBox("Inv " + files[i].getFile());

            toolbar.add(allChk);
            //toolbar.add(invChk);
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

            Set<String> keys = tablesInfos.keySet();
            List<String> keysList =  new ArrayList<>();
            keysList.addAll(tablesInfos.keySet());

            Collections.sort(keysList);

            TableInfo tableInfo = null;
            SpringLayout panelLayout = new SpringLayout();
            panel.setLayout(panelLayout);
            SpringLayout.Constraints constraints;
            int i=0;
            for(int j = 0,m=keysList.size();j<m;j++){
                tableInfo = tablesInfos.get(keysList.get(i));
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

            int scrollUnit = Math.round(i/20);

            scrollUnit = scrollUnit > 1 ? scrollUnit : 1;

            tableList.getVerticalScrollBar().setUnitIncrement(scrollUnit);

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
