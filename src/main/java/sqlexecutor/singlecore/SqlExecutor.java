package sqlexecutor.singlecore;

import sqlexecutor.pojo.ExecuteResult;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * sql执行器通用接口
 *
 * @author cuitianyu
 */
public interface SqlExecutor {

    /**
     * @param flag false: 开启事务 true 关闭事务
     * @throws SQLException
     */
    void setAutoCommit(Boolean flag) throws SQLException;

    /**
     * 执行单条sql的方法
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    ExecuteResult execute(String sql); // 创建表的手动回滚

    /**
     * 获取Connection连接
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    Connection getConn() throws SQLException;

    /**
     * 若开启了事务，提交时调用
     *
     * @throws SQLException
     */
    void commit() throws SQLException;

    /**
     * 关闭执行器
     *
     * @throws SQLException
     */
    void close() throws SQLException;

    /**
     * 取消正在执行的sql
     *
     * @return
     */
    boolean cancel() throws SQLException;

    /**
     * 销毁所有内容
     *
     * @return
     * @throws SQLException
     */
    boolean destroy() throws SQLException;

}
