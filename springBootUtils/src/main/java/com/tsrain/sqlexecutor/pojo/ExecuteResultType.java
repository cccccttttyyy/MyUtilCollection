package com.tsrain.sqlexecutor.pojo;

/**
 * sql语句类型
 *
 * @author cuitianyu
 */
public enum ExecuteResultType {
    SELECT(1, "SELECT"), UPDATE(2, "UPDATE"), DELETE(3, "DELETE"), INSERT(4, "INSERT"), UPSERT(5, "UPSERT"),
    CREATE(6, "CREATE"), ALTER(7, "ALTER"), DROP(8, "DROP"), MERGE(9, "MERGE"), SHOW(10, "SHOW"), DESCRIBE(11, "DESCRIBE"), UNKNOW(-1, "UNKNOW");
    private int code;
    private String value;

    ExecuteResultType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ExecuteResultType getByValue(String value) {
        if (SELECT.value.equalsIgnoreCase(value)) {
            return SELECT;
        } else if (SHOW.value.equalsIgnoreCase(value)) {
            return SHOW;
        } else if (UPDATE.value.equalsIgnoreCase(value)) {
            return UPDATE;
        } else if (DELETE.value.equalsIgnoreCase(value)) {
            return DELETE;
        } else if (INSERT.value.equalsIgnoreCase(value)) {
            return INSERT;
        } else if (UPSERT.value.equalsIgnoreCase(value)) {
            return UPSERT;
        } else if (CREATE.value.equalsIgnoreCase(value)) {
            return CREATE;
        } else if (ALTER.value.equalsIgnoreCase(value)) {
            return ALTER;
        } else if (DROP.value.equalsIgnoreCase(value)) {
            return DROP;
        } else if (MERGE.value.equalsIgnoreCase(value)) {
            return MERGE;
        } else if (DESCRIBE.value.equalsIgnoreCase(value)) {
            return DESCRIBE;
        } else {
            return UNKNOW;
        }
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
