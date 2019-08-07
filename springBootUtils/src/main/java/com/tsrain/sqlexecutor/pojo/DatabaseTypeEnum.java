package com.tsrain.sqlexecutor.pojo;


/**
 * 0 oracle; 1 mysql; 2 sqlserver; 3 postgresql; 4 hdfs; 5 hbase; 6 mongodb; 7
 * ftp; 8 txt; 9 db2; 10 hive; 11 es; 12 kafka;
 *
 * @author maolh
 * @date 2019-03-19
 */
public enum DatabaseTypeEnum {

    ORACLE(0, "oracle"), MYSQL(1, "mysql"), SQLSERVER(2, "sqlserver"), POSTGRESQL(3, "postgresql"), HDFS(4,
            "hdfs"), HBASE(5, "hbase"), MONGODB(6, "mongodb"), FTP(7, "ftp"), TXT(8, "txt"), DB2(9, "db2"), HIVE(10, "hive"),
    ES(11, "es"), KAFKA(12, "kafka");

    private Integer code;
    private String name;

    DatabaseTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;

    }

    public static DatabaseTypeEnum getByName(String code) {
        DatabaseTypeEnum[] enums = DatabaseTypeEnum.values();
        for (DatabaseTypeEnum enumt : enums) {
            if (enumt.getName().equalsIgnoreCase(code)) {
                return enumt;
            }
        }
        return null;
    }

    public static DatabaseTypeEnum getById(int id) {
        DatabaseTypeEnum[] enums = DatabaseTypeEnum.values();
        for (DatabaseTypeEnum enumt : enums) {
            if (enumt.getCode().intValue() == id) {
                return enumt;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;

    }
}
