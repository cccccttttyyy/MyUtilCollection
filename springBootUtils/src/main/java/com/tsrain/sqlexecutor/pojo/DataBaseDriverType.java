package com.tsrain.sqlexecutor.pojo;


/**
 * refer:http://blog.csdn.net/ring0hx/article/details/6152528
 * <p/>
 */
public enum DataBaseDriverType {
    MySql("mysql", "com.mysql.jdbc.Driver"),
    Tddl("mysql", "com.mysql.jdbc.Driver"),
    DRDS("drds", "com.mysql.jdbc.Driver"),
    Oracle("oracle", "oracle.jdbc.OracleDriver"),
    SQLServer("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    PostgreSQL("postgresql", "org.postgresql.Driver"),
    RDBMS("rdbms", "com.alibaba.datax.plugin.rdbms.util.DataBaseType"),
    DB2("db2", "com.ibm.db2.jcc.DB2Driver"),
    ADS("ads", "com.mysql.jdbc.Driver"),
    HDFS("hdfs", "org.apache.hive.jdbc.HiveDriver"),
    HIVE("hive", "org.apache.hive.jdbc.HiveDriver"),
    HBASE("hbase", "org.apache.phoenix.jdbc.PhoenixDriver");

    private String typeName;
    private String driverClassName;

    DataBaseDriverType(String typeName, String driverClassName) {
        this.typeName = typeName;
        this.driverClassName = driverClassName;
    }

    public static DataBaseDriverType getByName(String name) {
        if (MySql.typeName.equals(name)) {
            return MySql;
        } else if (Oracle.typeName.equals(name)) {
            return Oracle;
        } else if (SQLServer.typeName.equals(name)) {
            return SQLServer;
        } else if (PostgreSQL.typeName.equals(name)) {
            return PostgreSQL;
        } else if (HDFS.typeName.equals(name)) {
            return HDFS;
        } else if (HIVE.typeName.equals(name)) {
            return HIVE;
        } else if (HBASE.typeName.equals(name)) {
            return HBASE;
        }
        return MySql;
    }

    //
    public static DataBaseDriverType getById(int id) {
        DataBaseDriverType dbtype = null;
        DatabaseTypeEnum dbTypeEnum = DatabaseTypeEnum.getById(id);
        switch (dbTypeEnum) {
            case ORACLE:
                dbtype = DataBaseDriverType.Oracle;
                break;
            case MYSQL:
                dbtype = DataBaseDriverType.MySql;
                break;
            case SQLSERVER:
                dbtype = DataBaseDriverType.SQLServer;
                break;
            case POSTGRESQL:
                dbtype = DataBaseDriverType.PostgreSQL;
                break;
            case HIVE:
                dbtype = DataBaseDriverType.HIVE;
                break;
            case HBASE:
                dbtype = DataBaseDriverType.HBASE;
                break;
            case DB2:
                dbtype = DataBaseDriverType.DB2;
            default:
                break;
        }
        return dbtype;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }
}
