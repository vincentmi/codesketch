package com.vnzmi.tool.ui;

import com.vnzmi.tool.CodeSketch;

import javax.swing.*;

public class TemplateMarketView {
    public TemplateMarketView(){
        JFrame frame =new JFrame();
        frame.setSize(CodeSketch.getFrameSize(0.7));
        CodeSketch.center(frame);
        frame.setVisible(true);
    }
}
