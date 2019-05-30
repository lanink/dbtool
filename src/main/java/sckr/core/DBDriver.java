package sckr.core;

import sckr.config.Dbtype;
import sckr.driver.MysqlDriver;
import sckr.driver.OracleDriver;
import sckr.driver.PostgresqlDriver;
import sckr.driver.SqlServerDriver;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * DBDriver
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 11:47
 */

public abstract class DBDriver {


    public static DBDriver getDriver(String type) throws ClassNotFoundException
    {
        type = type.toLowerCase();

        if (type.equals(Dbtype.MYSQL.getType())){
            return MysqlDriver.instance();
        }else if (type.equals(Dbtype.ORACLE.getType())){
            return OracleDriver.getinstance();
        }else if (type.equals(Dbtype.SQLSERVER.getType())){
            return SqlServerDriver.getinstance();
        }else if (type.equals(Dbtype.POSTGRESQL.getType())){
            return PostgresqlDriver.instance();
        }
        else {
            throw new ClassNotFoundException(type + " is not supported！");
        }

    }

    public abstract void init(String host, String port, String database, String user, String password );

    public abstract List<Map<String,Object>> runSQl(String sql) throws SQLException,ClassNotFoundException;
}
