package sqlexecutor.runner;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import sqlexecutor.pojo.ResultSetHandler;
import sqlexecutor.pool.DBManager;
import sqlexecutor.utils.DataSourceUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;

/**
 * jdbc 带连接池的基本执行器
 *
 * @author cuitianyu
 */
public class JDBCRunner {

    private final DruidDataSource ds;
    private PreparedStatement currentstmt = null;

    public JDBCRunner() {
        ds = null;
    }

    public JDBCRunner(DruidDataSource ds) {
        this.ds = ds;
    }

    /**
     * 获取jdbc基本执行器
     */
    public static JDBCRunner getRunner(DBManager db) {
        DruidDataSource ds = db.getDataSource();
        if (ds != null) {
            return new JDBCRunner(ds);
        } else {
            return null;
        }
    }

    public DruidDataSource getDataSource() {
        return this.ds;
    }


    /**
     * Factory method that creates and initializes a
     * <code>PreparedStatement</code> object for the given SQL.
     * <code>QueryRunner</code> methods always call this method to prepare
     * statements for them. Subclasses can override this method to provide
     * special PreparedStatement configuration if needed. This implementation
     * simply calls <code>conn.prepareStatement(sql)</code>.
     *
     * @param conn The <code>Connection</code> used to create the
     *             <code>PreparedStatement</code>
     * @param sql  The SQL statement to prepare.
     * @return An initialized <code>PreparedStatement</code>.
     */
    protected PreparedStatement prepareStatement(DruidPooledConnection conn, String sql) {

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    /**
     * Factory method that creates and initializes a
     * <code>PreparedStatement</code> object for the given SQL.
     * <code>QueryRunner</code> methods always call this method to prepare
     * statements for them. Subclasses can override this method to provide
     * special PreparedStatement configuration if needed. This implementation
     * simply calls <code>conn.prepareStatement(sql, returnedKeys)</code>
     * which will result in the ability to retrieve the automatically-generated
     * keys from an auto_increment column.
     *
     * @param conn         The <code>Connection</code> used to create the
     *                     <code>PreparedStatement</code>
     * @param sql          The SQL statement to prepare.
     * @param returnedKeys Flag indicating whether to return generated keys or not.
     * @return An initialized <code>PreparedStatement</code>.
     * @since 1.6
     */
    protected PreparedStatement prepareStatement(DruidPooledConnection conn, String sql, int returnedKeys) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql, returnedKeys);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }


    /**
     * Factory method that creates and initializes a
     * <code>CallableStatement</code> object for the given SQL.
     * <code>QueryRunner</code> methods always call this method to prepare
     * callable statements for them. Subclasses can override this method to
     * provide special CallableStatement configuration if needed. This
     * implementation simply calls <code>conn.prepareCall(sql)</code>.
     *
     * @param conn The <code>Connection</code> used to create the
     *             <code>CallableStatement</code>
     * @param sql  The SQL statement to prepare.
     * @return An initialized <code>CallableStatement</code>.
     */
    protected CallableStatement prepareCall(DruidPooledConnection conn, String sql) {

        try {
            return conn.prepareCall(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Factory method that creates and initializes a <code>Connection</code>
     * object. <code>QueryRunner</code> methods always call this method to
     * retrieve connections from its DruidDataSource. Subclasses can override this
     * method to provide special <code>Connection</code> configuration if
     * needed. This implementation simply calls <code>ds.getConnection()</code>.
     *
     * @return An initialized <code>Connection</code>.
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected DruidPooledConnection prepareConnection() throws SQLException {
        if (this.getDataSource() == null) {
            throw new SQLException(
                    "QueryRunner requires a DruidDataSource to be "
                            + "invoked in this way, or a Connection should be passed in");
        }
        return this.getDataSource().getConnection();
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given objects.
     *
     * @param stmt   PreparedStatement to fill
     * @param params Query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLException if a database access error occurs
     */
    public void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {

        // nothing to do here
        if (params == null) {
            return;
        }

        CallableStatement call = null;
        if (stmt instanceof CallableStatement) {
            call = (CallableStatement) stmt;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            } else {
                int sqlType = Types.VARCHAR;
                stmt.setNull(i + 1, sqlType);
            }
        }
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given object's bean property values.
     *
     * @param stmt       PreparedStatement to fill
     * @param bean       a JavaBean object
     * @param properties an ordered array of properties; this gives the order to insert
     *                   values in the statement
     * @throws SQLException if a database access error occurs
     */
    public void fillStatementWithBean(PreparedStatement stmt, Object bean,
                                      PropertyDescriptor[] properties) throws SQLException {
        Object[] params = new Object[properties.length];
        for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor property = properties[i];
            Object value = null;
            Method method = property.getReadMethod();
            if (method == null) {
                throw new RuntimeException("No read method for bean property "
                        + bean.getClass() + " " + property.getName());
            }
            try {
                value = method.invoke(bean);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Couldn't invoke method: " + method,
                        e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(
                        "Couldn't invoke method with 0 arguments: " + method, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't invoke method: " + method,
                        e);
            }
            params[i] = value;
        }
        fillStatement(stmt, params);
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given object's bean property values.
     *
     * @param stmt          PreparedStatement to fill
     * @param bean          A JavaBean object
     * @param propertyNames An ordered array of property names (these should match the
     *                      getters/setters); this gives the order to insert values in the
     *                      statement
     * @throws SQLException If a database access error occurs
     */
    public void fillStatementWithBean(PreparedStatement stmt, Object bean,
                                      String... propertyNames) throws SQLException {
        PropertyDescriptor[] descriptors;
        try {
            descriptors = Introspector.getBeanInfo(bean.getClass())
                    .getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException("Couldn't introspect bean "
                    + bean.getClass().toString(), e);
        }
        PropertyDescriptor[] sorted = new PropertyDescriptor[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            if (propertyName == null) {
                throw new NullPointerException("propertyName can't be null: "
                        + i);
            }
            boolean found = false;
            for (int j = 0; j < descriptors.length; j++) {
                PropertyDescriptor descriptor = descriptors[j];
                if (propertyName.equals(descriptor.getName())) {
                    sorted[i] = descriptor;
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Couldn't find bean property: "
                        + bean.getClass() + " " + propertyName);
            }
        }
        fillStatementWithBean(stmt, bean, sorted);
    }

    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause  The original exception that will be chained to the new
     *               exception when it's rethrown.
     * @param sql    The query that was executing when the exception happened.
     * @param params The query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLException if a database access error occurs
     */
    protected void rethrow(SQLException cause, String sql, Object... params)
            throws SQLException {

        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuffer msg = new StringBuffer(causeMessage);

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.deepToString(params));
        }

        SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
                cause.getErrorCode());
        e.setNextException(cause);

        throw e;
    }

    /**
     * Wrap the <code>ResultSet</code> in a decorator before processing it. This
     * implementation returns the <code>ResultSet</code> it is given without any
     * decoration.
     *
     * <p>
     * Often, the implementation of this method can be done in an anonymous
     * inner class like this:
     * </p>
     *
     * <pre>
     * QueryRunner run = new QueryRunner() {
     *     protected ResultSet wrap(ResultSet rs) {
     *         return StringTrimmedResultSet.wrap(rs);
     *     }
     * };
     * </pre>
     *
     * @param rs The <code>ResultSet</code> to decorate; never
     *           <code>null</code>.
     * @return The <code>ResultSet</code> wrapped in some decorator.
     */
    protected ResultSet wrap(ResultSet rs) {
        return rs;
    }

    /**
     * Close a <code>Connection</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param conn Connection to close
     * @since DbUtils 1.1
     */
    protected void close(DruidPooledConnection conn) {
        if (conn != null) {
            DataSourceUtils.closeConn(conn);
        }
    }

    protected void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    protected void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    public <T> T query(DruidPooledConnection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return this.query(conn, false, sql, rsh, params);
    }

    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for closing the connection.
     *
     * @param <T>  The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql  The query to execute.
     * @param rsh  The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(DruidPooledConnection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return this.query(conn, false, sql, rsh, (Object[]) null);
    }

    /**
     * Executes the given SELECT SQL query and returns a result object.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param <T>    The type of object that the handler returns
     * @param sql    The SQL statement to execute.
     * @param rsh    The handler used to create the result object from
     *               the <code>ResultSet</code>.
     * @param params Initialize the PreparedStatement's IN parameters with
     *               this array.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        DruidPooledConnection conn = this.prepareConnection();

        return this.query(conn, true, sql, rsh, params);
    }

    /**
     * Executes the given SELECT SQL without any replacement parameters.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     *            the <code>ResultSet</code>.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        DruidPooledConnection conn = this.prepareConnection();

        return this.query(conn, true, sql, rsh, (Object[]) null);
    }

    /**
     * Calls query after checking the parameters to ensure nothing is null.
     *
     * @param conn      The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param params    An array of query replacement parameters.  Each row in
     *                  this array is one set of batch replacement values.
     * @return The results of the query.
     * @throws SQLException If there are database or parameter errors.
     */
    private <T> T query(DruidPooledConnection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                DataSourceUtils.closeConn(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                DataSourceUtils.closeConn(conn);
            }
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            currentstmt = stmt;
            this.fillStatement(stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rsh.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            try {
                close(rs);
            } finally {
                close(stmt);
                if (closeConn) {
                    DataSourceUtils.closeConn(conn);
                }
            }
        }

        return result;
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param conn The connection to use to run the query.
     * @param sql  The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(DruidPooledConnection conn, String sql) throws SQLException {
        return this.update(conn, false, sql, (Object[]) null);
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query with a single replacement
     * parameter.
     *
     * @param conn  The connection to use to run the query.
     * @param sql   The SQL to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(DruidPooledConnection conn, String sql, Object param) throws SQLException {
        return this.update(conn, false, sql, param);
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn   The connection to use to run the query.
     * @param sql    The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(DruidPooledConnection conn, String sql, Object... params) throws SQLException {
        return update(conn, false, sql, params);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement without
     * any replacement parameters. The <code>Connection</code> is retrieved
     * from the <code>DataSource</code> set in the constructor.  This
     * <code>Connection</code> must be in auto-commit mode or the update will
     * not be saved.
     *
     * @param sql The SQL statement to execute.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(String sql) throws SQLException {
        DruidPooledConnection conn = this.prepareConnection();

        return this.update(conn, true, sql, (Object[]) null);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement with
     * a single replacement parameter.  The <code>Connection</code> is
     * retrieved from the <code>DataSource</code> set in the constructor.
     * This <code>Connection</code> must be in auto-commit mode or the
     * update will not be saved.
     *
     * @param sql   The SQL statement to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(String sql, Object param) throws SQLException {
        DruidPooledConnection conn = this.prepareConnection();

        return this.update(conn, true, sql, param);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.  The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the update will not be saved.
     *
     * @param sql    The SQL statement to execute.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     *               parameters.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(String sql, Object... params) throws SQLException {
        DruidPooledConnection conn = this.prepareConnection();

        return this.update(conn, true, sql, params);
    }

    /**
     * Calls update after checking the parameters to ensure nothing is null.
     *
     * @param conn      The connection to use for the update call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param params    An array of update replacement parameters.  Each row in
     *                  this array is one set of update replacement values.
     * @return The number of rows updated.
     * @throws SQLException If there are database or parameter errors.
     */
    private int update(DruidPooledConnection conn, boolean closeConn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn && conn != null) {
                DataSourceUtils.closeConn(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            currentstmt = stmt;
            this.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            if (closeConn) {
                DataSourceUtils.closeConn(conn);
            }
        }

        return rows;
    }


    public void cancelStmt() throws SQLException {
        if (currentstmt != null) {
            currentstmt.cancel();
        }
    }
}

