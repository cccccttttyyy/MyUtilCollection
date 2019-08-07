package com.tsrain.sqlexecutor.utils;


import com.tsrain.sqlexecutor.pojo.DatabaseTypeEnum;

public class JDBCUtils {
    public static String getJdbcUrl(int type, String host, int port, String dbaseName) {
        String jdbcUrl = null;
        DatabaseTypeEnum dbTypeEnum = DatabaseTypeEnum.getById(type);
        switch (dbTypeEnum) {
            case ORACLE:
                if (dbaseName.startsWith("/")) {
                    jdbcUrl = "jdbc:oracle:thin:@//" + host + ":" + port + dbaseName;
                } else {
                    jdbcUrl = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbaseName;
                }
                break;
            case MYSQL:
                jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbaseName;
                break;
            case SQLSERVER:
                jdbcUrl = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + dbaseName;
                break;
            case POSTGRESQL:
                jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbaseName;
                break;
            case HIVE:
                jdbcUrl = "jdbc:hive2://" + host + ":" + port + "/" + dbaseName;
                break;
            case HBASE:
                if (dbaseName == null || dbaseName.isEmpty()) {
                    jdbcUrl = "jdbc:phoenix:" + host + ":" + port + ":/hbase";
                } else {
                    jdbcUrl = "jdbc:phoenix:" + host + ":" + port + ":" + dbaseName;
                }
                break;
            case DB2:
                jdbcUrl = "jdbc:db2://" + host + ":" + port + "/" + dbaseName;
                break;
            default:
                break;
        }
        return jdbcUrl;
    }
}
