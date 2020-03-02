package com.vnzmi.tool.model;

import java.sql.*;
import java.util.ArrayList;

public class ProfileConnection {

    private Profile profile ;
    private Connection connection = null;
    private String[] databases = null;

    public ProfileConnection(Profile profile)
    {
        this.profile = profile;
    }

    public Connection getConnection() throws SQLException
    {
        if(connection ==null) {
            connection = DriverManager.getConnection("jdbc:mysql://"+this.profile.getHost()+":"+Integer.toString(this.profile.getPort())+"/?" +
                    "user="+this.profile.getUser()+"&password="+this.profile.getPassword());

        }



        return connection;
    }

    public String[] getDatabases() throws SQLException
    {
        if(databases == null)
        {
            databases = doGetDatabases();
        }
        return databases;
    }

    private  String[] doGetDatabases() throws SQLException
    {
        ArrayList<String> data = new ArrayList<String>() ;
        Statement query = getConnection().createStatement();
        ResultSet rs = query.executeQuery("SHOW DATABASES");
        while(rs.next())
        {
            data.add(rs.getString(0));
        }
        rs.close();
        query.close();
        return (String[]) data.toArray();
    }
}
