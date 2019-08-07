package sqlexecutor.core;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqlexecutor.pojo.Database;
import sqlexecutor.pojo.ExecuteResult;
import sqlexecutor.pojo.ExecuteResultHandler;
import sqlexecutor.pojo.ExecuteResultType;
import sqlexecutor.pool.DBManager;
import sqlexecutor.runner.JDBCRunner;
import sqlexecutor.utils.DataSourceUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Rdbms执行器
 *
 * @author cuitianyu
 */
public class RdbmsSqlExecutor extends AbstractExecutor implements SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(RdbmsSqlExecutor.class);
    private Boolean isAutoCommit = true;
    private DruidPooledConnection conn;
    private Stack<String> newTableList = new Stack<String>();

    public RdbmsSqlExecutor(Database db) {
    }

    public RdbmsSqlExecutor(DBManager db) {
        runner = JDBCRunner.getRunner(db);
        this.db = db;
    }

    /**
     * 用于分隔多条sql语句
     *
     * @param sqlFile
     * @return
     * @throws Exception
     */
    private List<String> loadSql(String sqlFile) throws Exception {
        List<String> sqlList = new ArrayList<String>();
        try {
            // Windows 下换行是 /r/n, Linux 下是 /n
            String[] sqlArr = sqlFile.split("(;\\s*\\r\\n)|(;\\s*\\n)");
            for (int i = 0; i < sqlArr.length; i++) {
                String sql = sqlArr[i].trim();// .replaceAll("--.*", "")
                if (!sql.equals("")) {
                    sqlList.add(sql);
                }
            }
            return sqlList;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 执行多个sql语句
     *
     * @param sqlFile
     * @param isAutoCommit 是否开启事务 开启事务 false
     * @return
     * @throws Exception
     */
    public String executeChunk(String sqlFile, boolean isAutoCommit) throws Exception {
        StringBuffer sb = new StringBuffer();
        List<String> sqlList = loadSql(sqlFile);
        if (isAutoCommit) {
            for (String sql : sqlList) {
                sb.append(execute(sql));
            }
        } else {
            DruidPooledConnection conn = db.getConn();
            conn.setAutoCommit(false);
            for (String sql : sqlList) {
                sb.append(execute(sql));
            }
            DataSourceUtils.commitAndCloseConn(conn);
        }
        return sb.toString();
    }

    /**
     * 单条sql的执行
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    @Override
    public ExecuteResult execute(String sql) {
        ExecuteResult er = new ExecuteResult();
        er.setSql(sql);
        String trimsql = sql.trim();
        String firstLetter = trimsql.split(" ")[0].toUpperCase();
        er.setType(ExecuteResultType.getByValue(firstLetter));
        try {
            int update;
            switch (ExecuteResultType.getByValue(firstLetter)) {
                case SELECT:
                    if (isAutoCommit) {
                        er = runner.query(sql, new ExecuteResultHandler());
                    } else {
                        er = runner.query(conn, sql, new ExecuteResultHandler());
                    }
                    return er;
                case CREATE:
                    if (isAutoCommit) {
                        update = runner.update(sql);
                    } else {
                        update = runner.update(conn, sql);
                        newTableList.push(trimsql.split(" ")[1] + " " + trimsql.split(" ")[2]);
                    }
                    er.setResultCode(update);
                    er.setIsSucceed(true);
                    return er;
                case ALTER:
                case DROP:
                case INSERT:
                case UPDATE:
                case MERGE:
                case DELETE:
                case UNKNOW:
                default:
                    if (isAutoCommit) {
                        update = runner.update(sql);
                    } else {
                        update = runner.update(conn, sql);
                    }
                    er.setResultCode(update);
                    er.setIsSucceed(true);
                    return er;
            }
        } catch (Exception e) {
            if (!isAutoCommit) {
                try {
                    if (null != conn) {
                        conn.rollback();
                        DataSourceUtils.closeConn(conn);
                        conn = null;
                    }
                    rollbackCreate();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            er.setIsSucceed(false);
            er.setExceptionInfo(e.getMessage());
            return er;
        }
    }

    /**
     * 回滚create类型的sql语句
     */
    private void rollbackCreate() {
        while (!newTableList.empty()) {
            String content = newTableList.pop();
            try {
                runner.update("drop " + content);
            } catch (Exception e) {
                log.info(this.getClass() + ":rdbms rollback operation [drop " + content + "] fail");
            }
        }
    }

    @Override
    public void setAutoCommit(Boolean flag) throws SQLException {
        this.isAutoCommit = flag;
        if (!flag) {
            if (null != conn && !conn.isClosed()) {
                DataSourceUtils.closeConn(conn);
                conn = null;
            }
            conn = db.getConn();
            conn.setAutoCommit(false);
        } else {
            commit();
        }
    }

    @Override
    public void commit() throws SQLException {
        if (null != conn && !conn.isClosed()) {
            DataSourceUtils.commitAndCloseConn(conn);
            conn = null;
        }
    }

}
