package sqlexecutor.pojo;

/**
 * 存储select结果 执行日志
 *
 * @author cuitianyu
 */
public class ExecuteResult {
    /**
     * 执行的sql语句
     */
    private String sql;
    /**
     * 语句类型type: 分select update create三种
     */
    private ExecuteResultType type;
    /**
     * 执行是否成功
     */
    private Boolean isSucceed;
    /**
     * update与create类型执行返回值
     */
    private int resultCode;
    /**
     * select类型 返回的数据
     */
    private Table table;

    private String exceptionInfo;

    public ExecuteResultType getType() {
        return type;
    }

    public void setType(ExecuteResultType type) {
        this.type = type;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Boolean getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(Boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    @Override
    public String toString() {
        return "ExecuteResult [sql=" + sql + ", type=" + type + ", isSucceed=" + isSucceed + "]";
    }

}
