package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.SettingLoader;
import com.vnzmi.tool.model.Profile;
import com.vnzmi.tool.model.Setting;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.xml.bind.JAXBPermission;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainFrame extends JFrame {

    private JMenuBar mainMenubar = null ;

    private JPanel toolbarLeftPanel = null;
    private JPanel toolbarRightPanel = null;
    private JComboBox comboboxProfile = null;
    private JComboBox comboboxSchema = null;

    private JFrame main ;
    public MainFrame()
    {
        init();
    }

    public void init()
    {
        setTitle("CodeSketch");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        setSize(800,600);
        JLabel label = new JLabel("Code Sketch");

        BorderLayout layout = new BorderLayout();
        getContentPane().setLayout(layout);

        setJMenuBar(getMainMenubar());


        add(getMainToolbar(),BorderLayout.NORTH);

        JTable table=new JTable();
        table.setShowGrid(false);
        JTableHeader header = new JTableHeader();
        header.add(new Label("table"));
        header.add(new Label("generate"));

        header.add(new Label("preview"));
        //table.setTableHeader(header);
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.addRow(new Object[]{"product", "1", "1"});
        //tableModel.addRow(new Object[]{"product", new Checkbox(" "), new JButton("预览")});

        JScrollPane pane=new JScrollPane(table);
        add(pane,BorderLayout.CENTER);

        setVisible(true);


        main = this;

    }

    public void loadProfile()
    {

    }


    public void showMessage(String message)
    {
        JOptionPane.showInternalMessageDialog(main.getContentPane(),message);
    }



    public JMenuBar getMainMenubar()
    {
        JMenuBar main = new JMenuBar();
        main.add(createFileMenu());
        main.add(createHelpMenu());
        main.setVisible(true);
        return main;
    }

    public JMenu createFileMenu()
    {
        JMenu menuFile = new JMenu("Setting");
        menuFile.add(new JMenuItem("Preferences"));
        menuFile.add(new JMenuItem("Database Profile"));
        menuFile.add(new JMenuItem("Templates"));
        return menuFile;
    }

    public JMenu createHelpMenu()
    {
        JMenu menu = new JMenu("Help");
        menu.add(new JMenuItem("Document"));
        menu.add(new JMenuItem("Homepage"));

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showInternalMessageDialog(main.getContentPane(),"CodeSketch by Vincent Mi ");
            }
        });
        menu.add(aboutItem);
        return menu;
    }


    private JToolBar getMainToolbar()
    {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setLayout(new BorderLayout());
        toolbarLeftPanel= new JPanel(new GridLayout(1,2,5,5));
        comboboxProfile = new JComboBox();
        comboboxSchema = new JComboBox();
        toolbarLeftPanel.add(comboboxProfile);
        toolbarLeftPanel.add(comboboxSchema);
        toolbar.add(toolbarLeftPanel,BorderLayout.CENTER);

        comboboxProfile.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {

                String selectedItem = (String)comboboxProfile.getSelectedItem();
                if(selectedItem != null) {
                    SettingLoader.getInstance().getSetting().setProfile(selectedItem);
                    SettingLoader.getInstance().store();
                }

            }
        });

        JPanel rightPanel = new JPanel();
        toolbar.add( rightPanel,BorderLayout.EAST);

        final JButton reloadProfileButton = new JButton("Reload Profile");
        reloadProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadProfileSelection();
            }
        });

        rightPanel.add(reloadProfileButton);
        rightPanel.add(new JButton("Refresh"));
        rightPanel.add(new JButton("Generate"));

        reloadProfileSelection();

        return toolbar;
    }

    private void reloadProfileSelection()
    {
        Setting setting = SettingLoader.getInstance().load().getSetting();
        Profile[] profiles = setting.getProfiles();

        DefaultComboBoxModel cbm = (DefaultComboBoxModel)comboboxProfile.getModel();
        cbm.removeAllElements();
        int selected = 0;
        String selectedName = "";
        for(int i = 0;i<profiles.length;i++)
        {
            cbm.addElement(profiles[i].getName());
            if(profiles[i].getName().equals(setting.getProfile()))
            {
                selected = i;
                selectedName = profiles[i].getName();
            }
        }
        setting.setProfile(selectedName);
        comboboxProfile.setSelectedIndex(selected);
    }

    private void reloadSchemaSelection()
    {
        JComboBox cbx2 = new JComboBox();
        cbx2.addItem("Select a database");
        cbx2.addItem("usr");
        cbx2.addItem("dev");
        toolbarLeftPanel.add(cbx2);
    }


}
