package com.vnzmi.tool.ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PreviewPanel {
    public  PreviewPanel()
    {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(new Dimension(600,600));

        JPanel cp = new JPanel(new BorderLayout());

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/idea.xml"));
            theme.apply(textArea);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RTextScrollPane sp = new RTextScrollPane(textArea);
        cp.add(sp);

        frame.add(cp,BorderLayout.CENTER);
        frame.setVisible(true);

    }
}
