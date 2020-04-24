package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.model.CodePack;
import com.vnzmi.tool.model.Generator;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GeneratorInfoPanel {
    JDialog dialog;
    JProgressBar jpb;
    Generator generator;
    JButton processBtn;
    JButton exitBtn;

    public GeneratorInfoPanel(JFrame frame) {
        generator = new Generator(CodeSketch.getMainFrame().getSelectedTemplateInfo());
        dialog = new JDialog(frame, true);
        dialog.setTitle("Generating...");

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);

        dialog.setSize(400, 100);
        dialog.setLayout(new BorderLayout());

        JLabel genLabel = new JLabel("File generating");
        genLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        dialog.add(genLabel, BorderLayout.NORTH);

        dialog.setJMenuBar(null);
        jpb = new JProgressBar();
        jpb.setMinimum(0);
        jpb.setMaximum(100);

        dialog.add(jpb,BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2));


        processBtn = new JButton("Process");
        processBtn.addActionListener(e -> {
            processBtn.setEnabled(false);
            exitBtn.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    generate();
                }
            }).start();


        });

        exitBtn = new JButton("Cancel");
        exitBtn.addActionListener(e -> {
            dialog.setVisible(false);
        });

        btnPanel.add(processBtn);
        btnPanel.add(exitBtn);

        dialog.add(btnPanel, BorderLayout.SOUTH);

        CodeSketch.center(dialog);
    }


    private void generate() {
        ArrayList<TablePanel> tables = CodeSketch.getMainFrame().getTablePanels();
        int  max = tables.size();

        jpb.setMaximum(max);

        CodeSketch.info("start generate "+tables.size()+" tables");
        jpb.setValue(0);

        for (int i = 0; i < max; i++) {

            TablePanel p = tables.get(i);
            if (p.getTableCheckbox().isSelected()) {
                CodePack[] codes = generator.build(p.getTableInfo(),getSelectedOptions(p.getOptions()));
                for (int j = 0; j < codes.length; j++) {
                    codes[j].saveFile();
                }
            }
            jpb.setValue(i);
        }
        jpb.setValue(jpb.getMaximum());
        processBtn.setEnabled(true);
        exitBtn.setEnabled(true);
    }

    private HashSet<String> getSelectedOptions(HashMap<String, JCheckBox> options) {
        Collection<JCheckBox> values = options.values();
        HashSet<String> selected = new HashSet<>();
        Iterator<JCheckBox> it = options.values().iterator();
        while (it.hasNext()) {
            JCheckBox box = it.next();
            if (box.isSelected()) {
                selected.add(box.getText());
            }
        }
        return selected;
    }

    public void show() {
        dialog.setVisible(true);
    }
}
