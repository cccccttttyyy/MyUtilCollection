package sqlexecutor.druidpoolcore;

import sqlexecutor.pool.DBManager;
import sqlexecutor.runner.JDBCRunner;

import java.sql.SQLException;

/**
 * sql执行器的公共抽象类
 *
 * @author cuitianyu
 */
public abstract class AbstractExecutor {

    protected DBManager db = null;
    protected JDBCRunner runner = null;

    public AbstractExecutor() {
    }

    /**
     * 中断sql执行器当前执行
     *
     * @return
     */
    public boolean cancel() {
        try {
            runner.cancelStmt();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
