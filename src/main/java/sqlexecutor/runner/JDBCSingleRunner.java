package sqlexecutor.runner;

import sqlexecutor.pojo.ResultSetHandler;

import java.sql.*;


/**
 * jdbc无连接池基本执行器
 *
 * @author cuitianyu
 */
public class JDBCSingleRunner {

    private PreparedStatement currentstmt = null;

    /**
     * 获取jdbc基本执行器
     */
    public static JDBCRunner getRunner() {
        return new JDBCRunner();

    }

    /**
     * 获取PreparedStatement
     *
     * @param conn
     * @param sql
     * @return An initialized <code>PreparedStatement</code>.
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps;
    }

    /**
     * query类型的sql执行函数
     *
     * @param conn      The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @return The results of the query.
     * @throws SQLException If there are database or parameter errors.
     */
    public <T> T query(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                closeResource(conn, null, null);
            }
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                closeResource(conn, null, null);
            }
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            currentstmt = stmt;
            rs = stmt.executeQuery();
            result = rsh.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql);

        } finally {
            if (closeConn) {
                closeResource(conn, stmt, rs);
            } else {
                closeResource(null, stmt, rs);
            }
        }

        return result;
    }

    /**
     * update 类型的sql执行函数
     *
     * @param conn      The connection to use for the update call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @return The number of rows updated.
     * @throws SQLException If there are database or parameter errors.
     */
    public int update(Connection conn, boolean closeConn, String sql) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn && conn != null) {
                closeResource(conn, null, null);
            }
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            currentstmt = stmt;
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql);

        } finally {
            if (closeConn) {
                closeResource(conn, stmt, null);
            } else {
                closeResource(null, stmt, null);
            }
        }

        return rows;
    }

    public void cancelStmt() throws SQLException {
        if (currentstmt != null) {
            currentstmt.cancel();
        }
    }

    /**
     * 对异常信息进行扩充
     *
     * @param cause  The original exception that will be chained to the new
     *               exception when it's rethrown.
     * @param sql    The query that was executing when the exception happened.
     * @param params The query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLException if a database access error occurs
     */
    private void rethrow(SQLException cause, String sql) throws SQLException {

        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuffer msg = new StringBuffer(causeMessage);

        msg.append(" Query: ");
        msg.append(sql);

        SQLException e = new SQLException(msg.toString(), cause.getSQLState(), cause.getErrorCode());
        e.setNextException(cause);

        throw e;
    }

    private void closeResource(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
        try {
            if (rs != null) {
                rs.close();
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
    }
}
