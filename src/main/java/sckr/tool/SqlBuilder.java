package sckr.tool;

import sckr.config.Dbtype;

/**
 * SqlBuilder
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 14:49
 */

public class SqlBuilder {

    private static String mysql = "SELECT {cloums} FROM {table} WHERE {condition}";

    private static String sqlserver = "SELECT {cloums} FROM {table} WHERE {condition}";

    private static String oracle = "SELECT {cloums} FROM {table} WHERE {condition}";

    private static String postgresql = "SELECT {cloums} FROM {table} WHERE {condition}";

    public static String build(String type, String table, String cloums, String wheres) throws ClassNotFoundException
    {

        String sql = getSql(type);

        String cloum = getCloum(cloums.split(","));
        String condition = getCondition(wheres.split(","));

        sql = sql.replace("{table}" , table);
        sql = sql.replace("{cloums}" , cloum);
        sql = sql.replace("{condition}" , condition);

        return sql;

    }

    private static String getSql(String type) throws ClassNotFoundException
    {
        String t = type.toLowerCase();
        if (t.equals(Dbtype.MYSQL.getType())){
            return mysql;
        }else if (t.equals(Dbtype.SQLSERVER.getType())){
            return sqlserver;
        }else if (t.equals(Dbtype.ORACLE.getType())){
            return oracle;
        }else if (t.equals(Dbtype.POSTGRESQL.getType())){
            return postgresql;
        }
        else {
            throw new ClassNotFoundException(type + " is not supported！");
        }
    }

    private static String getCloum(String ... cloums)
    {
        if (null == cloums) return "*";

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < cloums.length; i++) {
            buffer.append(cloums[i] + ",");
        }
        buffer.deleteCharAt(buffer.length()-1);
        return buffer.toString();
    }

    private static String getCondition(String ... wheres)
    {
        if (null == wheres) return "";

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < wheres.length; i++) {
            buffer.append(wheres[i] + " AND ");
        }
        buffer.delete(buffer.length() - 4, buffer.length() - 1);
        return buffer.toString();
    }


}
