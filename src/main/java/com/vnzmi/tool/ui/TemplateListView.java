package com.vnzmi.tool.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnzmi.tool.*;
import com.vnzmi.tool.model.TemplateInfo;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.intellij.lang.annotations.JdkConstants;
import sun.font.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

public class TemplateListView {

    private String url = "http://127.0.0.1:4000/templates.json";

    private JDialog dialog ;

    private  TemplateInfo[] remoteInfos;

    private Dimension size ;

    public TemplateListView(JFrame parent) {
        size = CodeSketch.getFrameSize(0.5);
        dialog = new JDialog(parent, false);
        dialog.setTitle("Template Marketing");
        dialog.setSize(size);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        CodeSketch.center(dialog);
        dialog.setVisible(true);
        drawTemplateList();
    }

    public TemplateInfo[] readMetaData() {
        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .connectionSpecs(Arrays.asList(
                            ConnectionSpec.MODERN_TLS,
                            ConnectionSpec.COMPATIBLE_TLS,
                            ConnectionSpec.CLEARTEXT
                    ))
                    .sslSocketFactory(TrustAll.socketFactory(), new TrustAll.trustManager())
                    .hostnameVerifier(new TrustAll.hostnameVerifier())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url(url).build();

        TemplateInfo[]  templateInfos= new TemplateInfo[0];

        //CodeSketch.info(request.toString());

        try (Response response = client.newCall(request).execute()) {
            InputStream is = response.body().byteStream();
            ObjectMapper om = new ObjectMapper();
            templateInfos = om.readValue(is, TemplateInfo[].class);
            /*CodeSketch.info(templateInfos.length);
            for (int i = 0; i < templateInfos.length; i++) {
                System.out.println(templateInfos[i]);
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.getLogger(this.getClass().toString()).info(templateInfos.toString());

        return templateInfos;
    }


    public void drawTemplateList()
    {
        int width = (int) size.getWidth();
        int height = (int) size.getHeight();
        int p0Height = 30;
        SpringLayout layout = new SpringLayout();
        JPanel p = new JPanel();
        p.setPreferredSize(size);
        p.setLayout(layout);
        p.setBackground(Color.WHITE);


        JScrollPane jScrollPane = new JScrollPane(p);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jScrollPane.setPreferredSize(new Dimension(width,height - p0Height));

        //dialog.setLayout(layout);

        JPanel main = new JPanel(new BorderLayout());

        JPanel p0 =new JPanel(new FlowLayout());
        JLabel title = new JLabel("Template List : " + url);
        p0.add(title);
        JButton refreshBtn = new JButton("Reload");
        refreshBtn.addActionListener(e->{drawTemplateList();});
        p0.add(refreshBtn);
        Font font = p0.getFont();
        p0.setFont(new Font(font.getFontName() , Font.BOLD, 18));
        p0.setPreferredSize(new Dimension(width,p0Height));

        main.add(p0,BorderLayout.NORTH);
        main.add(jScrollPane,BorderLayout.CENTER);

        dialog.getContentPane().add(main);

        remoteInfos = readMetaData();
        for(int i = 0;i<remoteInfos.length;i++)
        {
            TemplateInfo info =remoteInfos[i];
            JPanel p1 =new JPanel();
            p1.setLayout(new BorderLayout());
            p1.setPreferredSize(new Dimension(width - 25,40));
            p1.add(new JLabel(info.getName() + "(by "+info.getAuthor()+")"),BorderLayout.WEST);
            p1.add(new JLabel(info.getDescription()),BorderLayout.CENTER);
            p1.add(new JButton("Download"),BorderLayout.EAST);
            p.add(p1);
            p1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    p1.setBackground(Color.getHSBColor(0,0,0.75F));
                    super.mouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    p1.setBackground(Color.WHITE);
                    super.mouseExited(e);
                }
            });
            SpringLayout.Constraints   constraints = layout.getConstraints(p1);
            constraints.setConstraint(SpringLayout.WEST,Spring.constant(5));
            constraints.setConstraint(SpringLayout.NORTH,Spring.constant(40 * i));

        }
    }
}
