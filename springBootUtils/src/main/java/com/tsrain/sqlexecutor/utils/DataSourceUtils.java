package com.tsrain.sqlexecutor.utils;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;

public class DataSourceUtils extends org.springframework.jdbc.datasource.DataSourceUtils {

    protected static final Log log = LogFactory.getLog(DataSourceUtils.class);

    /**
     * 关闭连接
     *
     * @param conn 需要关闭的连接
     * @throws SQLException
     */
    public static void closeConn(DruidPooledConnection conn) throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("关闭数据库连接失败：" + e);
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }

    /**
     * 提交并关闭连接
     *
     * @param conn 需要关闭的连接
     * @throws SQLException
     */
    public static void commitAndCloseConn(DruidPooledConnection conn) throws SQLException {
        try {
            conn.commit();
        } catch (SQLException e) {
            log.error("事务提交失败连接失败：" + e);
        } finally {
            closeConn(conn);
        }
    }

    /**
     * 事务回滚且释放资源
     *
     * @throws SQLException
     */
    public static void rollbackAndCloseConn(DruidPooledConnection conn) throws SQLException {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConn(conn);
        }
    }
}
