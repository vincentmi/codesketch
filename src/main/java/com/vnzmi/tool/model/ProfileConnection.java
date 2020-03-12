package com.vnzmi.tool.model;

import com.vnzmi.tool.ArrayUtil;
import com.vnzmi.tool.CodeSketch;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileConnection {

    private Profile profile ;
    private Connection connection = null;
    private String[] databases = null;

    private static HashMap<String,ProfileConnection> connections = new HashMap<String, ProfileConnection>() ;

    public ProfileConnection(Profile profile)
    {
        this.profile = profile;
    }


    public static ProfileConnection create(Profile profile)
    {
        ProfileConnection conn = connections.get(profile.getName());
        if(conn == null)
        {
            conn = new ProfileConnection(profile);
            connections.put(profile.getName(),conn);
        }
        return conn;
    }

    private Connection createConnection() throws SQLException
    {
        return DriverManager.getConnection(
                "jdbc:mysql://"
                        +this.profile.getHost()
                        +":"+Integer.toString(this.profile.getPort())+"/?"
                        +"user="+this.profile.getUser()
                        +"&password="+this.profile.getPassword()
        );
    }

    public Connection getConnection() throws SQLException
    {
        if(connection ==null) {
            connection = createConnection();
        }else {
            com.mysql.jdbc.Connection c = (com.mysql.jdbc.Connection)connection;
            try{
                c.ping();
            }catch (SQLException e)
            {
                connection = createConnection();
            }
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



    public  String[] doGetDatabases() throws SQLException
    {
        ArrayList<String> data = new ArrayList<String>() ;
        Statement query = getConnection().createStatement();
        ResultSet rs = query.executeQuery("SHOW DATABASES");

        while(rs.next())
        {
            data.add(rs.getString("Database"));
        }
        rs.close();
        query.close();
        String[] d = new String[data.size()];
        for (int i = 0 , max = data.size();i<max;i++)
        {
            d[i] = data.get(i);
        }
        return d;
    }

    public HashMap<String,TableInfo> getTableInfos() throws  SQLException {
        Statement query = getConnection().createStatement();
        String sql = "SELECT * " +
                "FROM  information_schema.columns " +
                "WHERE  TABLE_SCHEMA='" + profile.getSchema() + "' " +
                "ORDER BY `TABLE_NAME` ASC ," +
                "`ORDINAL_POSITION` ASC";

        CodeSketch.info(sql);

        ResultSet rs = query.executeQuery(sql);

        HashMap<String, TableInfo> tables = new HashMap<String, TableInfo>();

        String tableName;
        TableInfo tableInfo;

        while (rs.next()) {
            tableName = rs.getString("TABLE_NAME");
            tableInfo = tables.get(tableName);
            if (tableInfo == null) {
                tableInfo = new TableInfo();
                tableInfo.setName(tableName);
                tableInfo.setCatalog(rs.getString("TABLE_CATALOG"));
                tableInfo.setSchema(rs.getString("TABLE_SCHEMA"));
                tableInfo.setFields(new ArrayList<FieldInfo>());
                tables.put(tableName, tableInfo);
            }
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setName(rs.getString("COLUMN_NAME"));
            fieldInfo.setDataType(rs.getString("DATA_TYPE"));
            fieldInfo.setDataTypeStr(rs.getString("COLUMN_TYPE"));
            fieldInfo.setComment(rs.getString("COLUMN_COMMENT"));
            fieldInfo.setNullable(rs.getString("IS_NULLABLE").equals("YES"));
            fieldInfo.setDefaultValue(rs.getString("COLUMN_DEFAULT"));
            fieldInfo.setExtra(rs.getString("EXTRA"));
            fieldInfo.setKey(rs.getString("COLUMN_KEY"));
            if(ArrayUtil.inArray(
                    fieldInfo.getDataType(),
                    new String[]{
                            "tinytext", "text", "mediumtext", "longtext",
                            "tinyblob", "blob", "mediumblob", "longblob",
                            "char","varchar"
                    }))
            {
                fieldInfo.setMax(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
            }else if(ArrayUtil.inArray(
                    fieldInfo.getDataType(),
                    new String[]{
                            "tinyint", "mediumint", "smallint", "int", "bigint",
                            "decimal", "real", "double", "float",
                    }))
            {
                fieldInfo.setNumericPrecision(rs.getInt("NUMERIC_PRECISION"));
                fieldInfo.setNumericScale(rs.getInt("NUMERIC_SCALE"));
            }
            tableInfo.getFields().add(fieldInfo);

        }
        rs.close();
        query.close();
        return tables;
    }
}
