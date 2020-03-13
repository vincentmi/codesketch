package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;
import com.vnzmi.tool.model.Profile;
import com.vnzmi.tool.model.Setting;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ProfileListView {
    private JPanel pLeft;
    private JPanel pLeftSelector;
    private JPanel pCenter;
    private JPanel pBottom;
    private JDialog dialog;

    JTextField nameField;
    JTextField driverField;
    JTextField hostField;
    JTextField portField;
    JTextField schemaField;
    JTextField userField;
    JTextField passwordField;
    JTextField encodingField;

    JList<String> profileSelector;

    public ProfileListView(Window parent) {
        dialog = new JDialog(parent);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        dialog.setTitle("Profile setting");
        dialog.setSize(600, 400);

        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);
        CodeSketch.center(dialog);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        JButton btnRemove = new JButton("-");
        JButton btnAdd = new JButton("+");

        btnAdd.addActionListener(e -> {
            Setting setting = Loader.getInstance().getSetting();
            Profile p = new Profile();
            String profileName = "profile-" + Double.toHexString(Math.random());
            p.setName(profileName);
            p.setHost("127.0.0.1");
            p.setSchema("test");
            p.setUser("root");
            p.setPassword("root");
            p.setEncoding("UTF-8");
            p.setDriver("MySQL");
            p.setPort(3306);

            setting.appendProfile(p);

            refreshProfileSelector(profileName);
            CodeSketch.getMainFrame().reloadProfileSelection();
        });

        btnRemove.addActionListener(e->{
                String selectedValue = profileSelector.getSelectedValue();
                int selectedIndex = profileSelector.getSelectedIndex();
                Loader.getInstance().getSetting().getProfiles().remove(selectedIndex);
                refreshProfileSelector(selectedValue);
                CodeSketch.getMainFrame().reloadProfileSelection();
        });

        btnPanel.add(btnAdd);
        btnPanel.add(btnRemove);

        pLeft = new JPanel(new BorderLayout());
        pLeftSelector = new JPanel(new GridLayout(1, 1));
        pLeft.add(btnPanel, BorderLayout.SOUTH);
        pLeft.add(pLeftSelector, BorderLayout.CENTER);

        pCenter = new JPanel(new BorderLayout());
        pBottom = new JPanel(new GridLayout(1, 2));
        dialog.getContentPane().add(pLeft, BorderLayout.WEST);
        dialog.getContentPane().add(pCenter, BorderLayout.CENTER);
        dialog.getContentPane().add(pBottom, BorderLayout.SOUTH);
        refreshProfileSelector("");

        //pCenter.add(new JLabel("Please select a profile"), BorderLayout.CENTER);

        refreshButtonPanel();
    }

    public void show()
    {
        dialog.setVisible(true);
    }

    public void refreshCenter(Profile profile) {
        pCenter.removeAll();
        SpringLayout layout = new SpringLayout();
        pCenter.setLayout(layout);

        nameField = new JTextField(profile.getName());
        nameField.setColumns(30);
        driverField = new JTextField(profile.getDriver());
        driverField.setColumns(30);
        hostField = new JTextField(profile.getHost());
        hostField.setColumns(30);
        portField = new JTextField(Integer.toString(profile.getPort()));
        portField.setColumns(10);
        schemaField = new JTextField(profile.getSchema());
        schemaField.setColumns(30);
        userField = new JTextField(profile.getUser());
        userField.setColumns(30);
        passwordField = new JPasswordField(profile.getPassword());
        passwordField.setColumns(30);
        encodingField = new JTextField(profile.getEncoding());
        encodingField.setColumns(30);

        appendRow(0, "Name:", nameField);
        appendRow(1, "Driver:", driverField);
        appendRow(2, "Host:", hostField);
        appendRow(3, "Port:", portField);
        appendRow(4, "Schema:", schemaField);
        appendRow(5, "Encoding:", encodingField);
        appendRow(6, "User:", userField);
        appendRow(7, "Password:", passwordField);

        pCenter.validate();
        pCenter.repaint();
    }

    private void appendRow(int index, String labelText, Component component) {
        JLabel label = new JLabel(labelText);
        pCenter.add(label);
        pCenter.add(component);
        int rowHeight = 30;
        SpringLayout layout = (SpringLayout) pCenter.getLayout();
        SpringLayout.Constraints constraints = layout.getConstraints(label);
        constraints.setConstraint(SpringLayout.WEST, Spring.constant(10));
        constraints.setConstraint(SpringLayout.NORTH, Spring.constant(5 + index * rowHeight));
        constraints = layout.getConstraints(component);
        constraints.setConstraint(SpringLayout.WEST, Spring.constant(80));
        constraints.setConstraint(SpringLayout.NORTH, Spring.constant(0 + index * rowHeight));
    }

    public void refreshButtonPanel() {
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            String name = nameField.getText();
            ArrayList<Profile> profiles = Loader.getInstance().getSetting().getProfiles();
            Profile p = profiles.get(profileSelector.getSelectedIndex());
            p.setName(name);
            p.setDriver(driverField.getText());
            p.setHost(hostField.getText());
            p.setPort(Integer.valueOf(portField.getText()));
            p.setSchema(schemaField.getText());
            p.setEncoding(encodingField.getText());
            p.setUser(userField.getText());
            p.setPassword(passwordField.getText());
            Loader.getInstance().saveSetting();
            Loader.getInstance().loadSetting();
            refreshProfileSelector(name);
            CodeSketch.getMainFrame().reloadProfileSelection();
        });

        btnCancel.addActionListener(e -> {
            dialog.setVisible(false);
        });

        pBottom.add(btnSave);
        pBottom.add(btnCancel);
    }

    private void selectProfile(String name) {
        ArrayList<Profile> profiles = Loader.getInstance().getSetting().getProfiles();
        int selectedIndex = 0;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getName().equals(name)) {
                selectedIndex = i;
                break;
            }
        }
        profileSelector.setSelectedIndex(selectedIndex);
    }

    public void refreshProfileSelector(String selected) {
        profileSelector = new JList<>();
        profileSelector.setFixedCellWidth(120);
        profileSelector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Setting setting = Loader.getInstance().getSetting();
        ArrayList<Profile> profiles = setting.getProfiles();
        String[] profileSelectorData = new String[profiles.size()];
        for (int i = 0; i < profiles.size(); i++) {
            profileSelectorData[i] = profiles.get(i).getName();
        }
        profileSelector.setListData(profileSelectorData);
        profileSelector.setBorder(new EmptyBorder(8, 8, 8, 8));


        profileSelector.addListSelectionListener(e -> {
            JList<String> list = (JList<String>) e.getSource();
            ArrayList<Profile> profiles1 = Loader.getInstance().getSetting().getProfiles();
            refreshCenter(profiles1.get(list.getSelectedIndex()));
        });

        selectProfile(selected);

        pLeftSelector.removeAll();
        pLeftSelector.add(new JScrollPane(profileSelector));
        pLeftSelector.validate();
        pLeftSelector.repaint();
    }
}
