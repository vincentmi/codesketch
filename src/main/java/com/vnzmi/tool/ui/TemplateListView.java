package com.vnzmi.tool.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnzmi.tool.*;
import com.vnzmi.tool.model.TemplateInfo;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class TemplateListView {

    private String url = "https://vnzmi.com/codesketch/templates.json";

    private JDialog dialog ;

    private  TemplateInfo[] remoteInfos;

    public TemplateListView(JFrame parent) {
        dialog = new JDialog(parent, false);
        dialog.setTitle("Template List");
        dialog.setSize(600, 600);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        CodeSketch.center(dialog);
        dialog.setVisible(true);
        drawTemplateList();
    }

    public TemplateInfo[] readMetaData() {
        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
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
        return templateInfos;
    }


    public void drawTemplateList()
    {
        SpringLayout layout = new SpringLayout();
        Container p = dialog.getContentPane();
        p.setPreferredSize(new Dimension(600,600));
        dialog.setLayout(layout);
        remoteInfos = readMetaData();
        for(int i = 0;i<remoteInfos.length;i++)
        {
            TemplateInfo info =remoteInfos[i];
            JPanel p1 =new JPanel();
            p1.setLayout(new BorderLayout());
            p1.setPreferredSize(new Dimension(590,30));
            p1.add(new JLabel(info.getName() + "(by "+info.getAuthor()+")"),BorderLayout.WEST);
            p1.add(new JLabel(info.getDescription()),BorderLayout.WEST);
            p1.add(new JButton("Download"),BorderLayout.EAST);
            p.add(p1);
            SpringLayout.Constraints   constraints = layout.getConstraints(p1);
            constraints.setConstraint(SpringLayout.WEST,Spring.constant(10));
            constraints.setConstraint(SpringLayout.NORTH,Spring.constant(30 * i));

        }
    }
}
