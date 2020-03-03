package com.vnzmi.tool.ui;

import com.sun.tools.javac.jvm.Code;
import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.SettingLoader;
import com.vnzmi.tool.model.Profile;
import com.vnzmi.tool.model.ProfileConnection;
import com.vnzmi.tool.model.Setting;
import com.vnzmi.tool.model.TableInfo;
import org.intellij.lang.annotations.Flow;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

public class MainFrame extends JFrame {

    private JMenuBar mainMenubar = null;

    private JPanel toolbarLeftPanel = null;
    private JPanel toolbarRightPanel = null;
    private JComboBox comboboxProfile = null;
    private JComboBox comboboxSchema = null;
    private JScrollPane centerPanel = null;

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
        centerPanel = new JScrollPane();
        centerPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        centerPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

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
                reloadSchemaSelection();

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
        rightPanel.add(new JButton("Generate"));


        return toolbar;
    }

    public void reloadProfileSelection() {
        Setting setting = SettingLoader.getInstance().load().getSetting();
        Profile[] profiles = setting.getProfiles();

        DefaultComboBoxModel cbm = (DefaultComboBoxModel) comboboxProfile.getModel();
        cbm.removeAllElements();
        CodeSketch.getLogger().info(setting.getProfile());
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
                CodeSketch.getLogger().info(databases);
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
                CodeSketch.getLogger().error(e.getMessage());
                showMessage(e.getMessage());
            }
        }
    }

    public void refreshTables() {
        Profile profile = SettingLoader.getInstance().getSelectedProfile();
        if (profile == null) {
            showMessage("profile not selected");
        }

        CodeSketch.getLogger().info("current db = " + profile);

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
                CodeSketch.getLogger().info(tableInfo);
                JPanel tablePanel = new JPanel();
                tablePanel.setLayout(new BorderLayout());
                tablePanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));

                JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
                titlePanel.setBackground(Color.lightGray);
                titlePanel.add(new JCheckBox(""));
                titlePanel.add(new JLabel(tableInfo.getName()));
                tablePanel.add(titlePanel, BorderLayout.NORTH);

                JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
                checkboxPanel.add(new JCheckBox("view"));
                checkboxPanel.add(new JCheckBox("model"));
                checkboxPanel.add(new JCheckBox("controller"));
                checkboxPanel.add(new JCheckBox("view"));
                tablePanel.add(checkboxPanel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
                buttonPanel.add(new JButton("Preview"));
                buttonPanel.add(new JButton("Generate"));
                tablePanel.add(buttonPanel, BorderLayout.SOUTH);
                panel.add(tablePanel);
                tablePanel.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {

                    }

                    public void mousePressed(MouseEvent e) {

                    }

                    public void mouseReleased(MouseEvent e) {

                    }

                    public void mouseEntered(MouseEvent e) {
                        JPanel jp = (JPanel) e.getSource();
                        jp.setBorder(BorderFactory.createLineBorder(Color.red));
                    }

                    public void mouseExited(MouseEvent e) {
                        JPanel jp = (JPanel) e.getSource();
                        jp.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                    }
                });
            }

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
