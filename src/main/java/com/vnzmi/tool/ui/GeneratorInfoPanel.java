package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import com.vnzmi.tool.model.CodePack;
import com.vnzmi.tool.model.Generator;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class GeneratorInfoPanel {
    JDialog dialog ;
    public GeneratorInfoPanel(JFrame frame) {
         dialog = new JDialog(frame,true);
         dialog.setTitle("Generating...");
    }

    private void generate() {
        Generator generator = new Generator(CodeSketch.getMainFrame().getSelectedTemplateInfo());

        ArrayList<TablePanel> tables = CodeSketch.getMainFrame().getTablePanels();

        for (int i = 0, max = tables.size(); i < max; i++) {
            TablePanel p = tables.get(i);
            if (p.getTableCheckbox().isSelected()) {
                //selected table
                CodePack[] codes = generator.build(p.getTableInfo(), getSelectedOptions(p.getOptions()));
            }
        }

    }

    private HashSet<String> getSelectedOptions(HashMap<String, JCheckBox> options) {
        Collection<JCheckBox> values = options.values();
        HashSet<String> selected = new HashSet<>();

        while (values.iterator().hasNext()) {
            JCheckBox box = values.iterator().next();
            if (box.isSelected()) {
                selected.add(box.getText());
            }
        }
        return selected;
    }

    public void show() {
        dialog.setSize(300,300);
        CodeSketch.center(dialog);
        dialog.setVisible(true);
    }
}
