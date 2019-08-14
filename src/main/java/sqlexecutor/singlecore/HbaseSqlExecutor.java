package sqlexecutor.singlecore;

import com.di.common.utils.JDBCUtils;
import com.di.dmas.common.datasource.pojo.DataBaseDriverType;
import com.di.dmas.common.datasource.pojo.HbaseBo;
import com.di.dmas.common.sqlexecutor.dao.JDBCRunner;
import com.di.dmas.common.sqlexecutor.pojo.ExecuteResult;
import com.di.dmas.common.sqlexecutor.pojo.ExecuteResultHandler;
import com.di.dmas.common.sqlexecutor.pojo.ExecuteResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Hbase执行器
 *
 * @author cuitianyu
 */
public class HbaseSqlExecutor extends AbstractExecutor implements SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(HbaseSqlExecutor.class);
    private HbaseBo bo;

    public HbaseSqlExecutor(HbaseBo db) {
        runner = JDBCRunner.getRunner();
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
                    er = runner.query(getConn(), true, sql, new ExecuteResultHandler());
                    return er;
                case CREATE:
                case ALTER:
                case DROP:
                case UPSERT:
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
        log.info("Hbase Transactions are not supported untile now");

    }

    @Override
    public void commit() {
        log.info("Hbase Transactions are not supported untile now");

    }

    @Override
    public Connection getConn() throws SQLException {
        String jdbcUrl = JDBCUtils.getJdbcUrl(bo.getDbase_type(), bo.getZk_quorum(), bo.getZk_port(), bo.getHbase_rootdir());
        String driverName = DataBaseDriverType.getById(bo.getDbase_type()).getDriverClassName();
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(jdbcUrl);
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
