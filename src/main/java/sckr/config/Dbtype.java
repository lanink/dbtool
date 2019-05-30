package sckr.config;

/**
 * Export
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 09:52
 */

public enum Dbtype {

    MYSQL("mysql"),
    SQLSERVER("sqlserver"),
    ORACLE("oracle"),
    POSTGRESQL("postgresql")
    ;

    private String type;

    Dbtype(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
