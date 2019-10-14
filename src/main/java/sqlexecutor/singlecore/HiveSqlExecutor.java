package sqlexecutor.singlecore;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqlexecutor.pojo.*;
import sqlexecutor.runner.JDBCSingleRunner;
import sqlexecutor.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * https://cwiki.apache.org/confluence/display/Hive/Hive+Transactions
 * Hive执行器
 *
 * @author cuitianyu
 */
public class HiveSqlExecutor extends AbstractExecutor implements SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(RdbmsSqlExecutor.class);
    private HiveBo bo;

    public HiveSqlExecutor(HiveBo db) {
        runner = JDBCSingleRunner.getRunner();
        this.bo = db;

    }

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
                case SHOW:
                case DESCRIBE:
                    er = runner.query(getConn(), true, sql, new ExecuteResultHandler());
                    return er;
                case CREATE:
                case ALTER:
                case DROP:
                case INSERT:
                case UPDATE:
                case DELETE:
                case UNKNOW:
                default:
                    update = runner.update(getConn(), true, sql);
                    er.setResultCode(update);
                    er.setIsSucceed(true);
                    return er;
            }
        } catch (Exception e) {
            er.setIsSucceed(false);
            er.setExceptionInfo(e.getMessage());
            return er;
        }
    }

    @Override
    public void setAutoCommit(Boolean flag) {
        log.info("HIVE Transactions are not supported untile now");
    }

    @Override
    public void commit() {
        log.info("HIVE Transactions are not supported untile now");
    }

    @Override
    public Connection getConn() throws SQLException {
        String jdbcUrl = JDBCUtils.getJdbcUrl(bo.getDbase_type(), bo.getDbase_ip(), bo.getDbase_port(),
                bo.getDbase_name());
        if (null != bo.getConnect_params() && bo.getConnect_params().trim().isEmpty() == false) {
            jdbcUrl = jdbcUrl + "?" + bo.getConnect_params();
        }
        String user = bo.getDbase_user();
        String password = bo.getDbase_passwd();
        String driverName = DataBaseDriverType.getById(bo.getDbase_type()).getDriverClassName();
        try {
            Class.forName(driverName);
            return (user == null || user.trim().isEmpty()) ? DriverManager.getConnection(jdbcUrl) : DriverManager.getConnection(jdbcUrl, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean destroy() {
        close();
        runner = null;
        bo = null;
        return true;
    }


}
