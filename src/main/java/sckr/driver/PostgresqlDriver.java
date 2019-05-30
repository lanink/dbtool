package sckr.driver;

import sckr.core.DBDriver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PostgresqlDriver
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 16:19
 */

public class PostgresqlDriver extends DBDriver {

    private String driver = "org.postgresql.Driver";

    private String host;

    private String port;

    private String user;

    private String password;

    private String database;

    private static MysqlDriver instance;

    public static MysqlDriver instance()
    {
        if (null == instance){
            instance = new MysqlDriver();
        }
        return instance;
    }
    @Override
    public void init(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    @Override
    public List<Map<String, Object>> runSQl(String sql) throws SQLException, ClassNotFoundException {
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


    private Connection getConnection() throws ClassNotFoundException,SQLException
    {
        Connection connection;
        Class.forName(driver);
        connection = DriverManager.getConnection(getUrl(),user,password);
        if(connection.isClosed()){
            throw new SQLException("connect to postgresql fail!");
        }

        return connection;
    }


    private String getUrl()
    {
        return "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }
}
