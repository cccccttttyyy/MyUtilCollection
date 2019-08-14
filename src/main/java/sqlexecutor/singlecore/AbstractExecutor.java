package sqlexecutor.singlecore;

import sqlexecutor.runner.JDBCRunner;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * sql执行器的公共抽象类
 *
 * @author cuitianyu
 */
public abstract class AbstractExecutor {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 获取Connection连接
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    abstract public Connection getConn() throws SQLException;
}
