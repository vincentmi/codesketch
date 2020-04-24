package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class JsonGeneratorView{

    JDialog dialog;

    public JsonGeneratorView(Window parent) {
        dialog = new JDialog(parent);
        //dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        dialog.setTitle("JSON generator");
        dialog.setSize(CodeSketch.getFrameSize(0.7));

        dialog.setLayout(new BorderLayout());
        dialog.setResizable(true);
        CodeSketch.center(dialog);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton btnGen = new JButton("Preview");
        JPanel btnPanel = new JPanel(new BorderLayout(3,3));
        btnPanel.add(new Label("Please paste you data json below .We will try to guess a  data structure."),BorderLayout.WEST);
        btnPanel.add(btnGen,BorderLayout.EAST);

        mainPanel.add(btnPanel, BorderLayout.NORTH);

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAutoIndentEnabled(true);
        textArea.setLineWrap(true);
        textArea.setText("");

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/idea.xml"));
            theme.apply(textArea);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RTextScrollPane sp = new RTextScrollPane(textArea);
        mainPanel.add(sp, BorderLayout.CENTER);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

}
