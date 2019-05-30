package sckr.driver;

import sckr.core.DBDriver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OracleDriver
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 16:02
 */

public class OracleDriver extends DBDriver {

    private String driver = "oracle.jdbc.OracleDriver";

    private String host;

    private String port;

    private String user;

    private String password;

    private String database;

    private static OracleDriver instance;

    public static DBDriver getinstance()
    {
        if (null == instance){
            instance = new OracleDriver();
        }
        return instance;
    }

    @Override
    public void init(String host, String port, String database, String user, String password) {

    }

    @Override
    public List<Map<String, Object>> runSQl(String sql) throws SQLException , ClassNotFoundException{
        Connection connection = getConnection();

        List<Map<String,Object>> result = new ArrayList<>();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (;resultSet.next();){
            Map<String,Object> map = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                map.put(metaData.getColumnName(i),resultSet.getObject(i));
            }
            result.add(map);
        }

        resultSet.close();
        connection.close();

        return result;
    }


    private Connection getConnection() throws SQLException,ClassNotFoundException
    {
        Connection connection;

        Class.forName(driver);
        connection = DriverManager.getConnection(getUrl(),user,password);
        if(connection.isClosed()){
            throw new SQLException("connect to oracle fail!");
        }

        return connection;

    }


    private String getUrl()
    {
        return "jdbc:oracle:thin:@" + host + ":" + port + ":"+ database;

    }
}
