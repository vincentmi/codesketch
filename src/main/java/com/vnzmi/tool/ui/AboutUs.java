package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.Loader;

import javax.swing.*;
import java.awt.*;

public class AboutUs {
    private  Frame parent;
    public AboutUs(Frame parent)
    {
        this.parent = parent;
        init();
    }

    public void  init()
    {


        JPanel topPanel = new JPanel();
        ImageIcon logo = new ImageIcon(Loader.getResource("codesketch.png"));
        logo.setImage(logo.getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH));

        JLabel logoLabel = new JLabel(logo);

        logoLabel.setSize(80,80);
        topPanel.add(logoLabel);


        JDialog dialog = new JDialog(parent);
        SpringLayout sl = new SpringLayout();
        dialog.setLayout(sl);


        dialog.add(topPanel);
        SpringLayout.Constraints constraints = sl.getConstraints(topPanel);
        constraints.setConstraint(SpringLayout.NORTH , Spring.constant(50));
        constraints.setConstraint(SpringLayout.WEST , Spring.constant(50));

        int westPoint = 180;

        JLabel name = new JLabel("CodeSketch");
        name.setFont(new java.awt.Font("Dialog", 1, 16));
        dialog.add(name);
        constraints = sl.getConstraints(name);
        constraints.setConstraint(SpringLayout.NORTH , Spring.constant(60));
        constraints.setConstraint(SpringLayout.WEST , Spring.constant(westPoint));

        JLabel ver = new JLabel("1.0");
        dialog.add(ver);
        constraints = sl.getConstraints(ver);
        constraints.setConstraint(SpringLayout.NORTH , Spring.constant(80));
        constraints.setConstraint(SpringLayout.WEST , Spring.constant(westPoint));

        JLabel author = new JLabel("Code by Vincent Mi");
        dialog.add(author);
        constraints = sl.getConstraints(author);
        constraints.setConstraint(SpringLayout.NORTH , Spring.constant(110));
        constraints.setConstraint(SpringLayout.WEST , Spring.constant(westPoint));

        JLabel mail = new JLabel("miwenshu@gmail.com");
        dialog.add(mail);
        constraints = sl.getConstraints(mail);
        constraints.setConstraint(SpringLayout.NORTH , Spring.constant(130));
        constraints.setConstraint(SpringLayout.WEST , Spring.constant(westPoint));

        JLabel web = new JLabel("https://www.vnzmi.com");
        dialog.add(web);
        constraints = sl.getConstraints(web);
        constraints.setConstraint(SpringLayout.NORTH , Spring.constant(150));
        constraints.setConstraint(SpringLayout.WEST , Spring.constant(westPoint));

        JLabel github = new JLabel("https://github.com/vincentmi/codesketch");
        dialog.add(github);
        constraints = sl.getConstraints(github);
        constraints.setConstraint(SpringLayout.NORTH , Spring.constant(190));
        constraints.setConstraint(SpringLayout.WEST , Spring.constant(westPoint));

        dialog.setSize(550,280);
        dialog.setResizable(false);
        CodeSketch.center(dialog);
        dialog.setVisible(true);
    }
}
