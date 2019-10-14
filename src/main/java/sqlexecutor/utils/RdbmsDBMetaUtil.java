package sqlexecutor.utils;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.ImmutableTriple;
import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqlexecutor.pojo.DataBaseDriverType;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;


/**
 * @author linguixu
 */
public class RdbmsDBMetaUtil {
    public static final String OB10_SPLIT_STRING = "||_dsc_ob10_dsc_||";
    public static final String OB10_SPLIT_STRING_PATTERN = "\\|\\|_dsc_ob10_dsc_\\|\\|";
    public static final String TMP_IDTT_TRANS_SECU_FLAG = "TMP_IDTT_TRANS_SECU_FLAG";
    private static final Logger LOG = LoggerFactory.getLogger(RdbmsDBMetaUtil.class);

    /**
     * Get direct JDBC connection
     * <p/>
     * if connecting failed, try to connect for MAX_TRY_TIMES times
     * <p/>
     * NOTE: In DataX, we don't need connection pool in fact
     */
    public static Connection getConnection(final DataBaseDriverType dataBaseType,
                                           final String jdbcUrl, final String username, final String password) {

        return getConnection(dataBaseType, jdbcUrl, username, password, String.valueOf(RdbmsConstant.SOCKET_TIMEOUT_INSECOND * 1000));
    }

    /**
     * @param dataBaseType
     * @param jdbcUrl
     * @param username
     * @param password
     * @param socketTimeout 设置socketTimeout，单位ms，String类型
     * @return
     */
    public static Connection getConnection(final DataBaseDriverType dataBaseType,
                                           final String jdbcUrl, final String username, final String password, final String socketTimeout) {

        try {
            return RetryUtil.executeWithRetry(new Callable<Connection>() {
                @Override
                public Connection call() {
                    return RdbmsDBMetaUtil.connect(dataBaseType, jdbcUrl, username,
                            password, socketTimeout);
                }
            }, 9, 1000L, true);
        } catch (Exception e) {
            String message = String.format("数据库连接失败. 因为根据您配置的连接信息:%s获取数据库连接失败. 请检查您的配置并作出修改.", jdbcUrl);
        }
        return null;
    }

    /**
     * Get direct JDBC connection
     * <p/>
     * if connecting failed, try to connect for MAX_TRY_TIMES times
     * <p/>
     * NOTE: In DataX, we don't need connection pool in fact
     */
    public static Connection getConnectionWithoutRetry(final DataBaseDriverType dataBaseType,
                                                       final String jdbcUrl, final String username, final String password) {
        return getConnectionWithoutRetry(dataBaseType, jdbcUrl, username,
                password, String.valueOf(RdbmsConstant.SOCKET_TIMEOUT_INSECOND * 1000));
    }

    public static Connection getConnectionWithoutRetry(final DataBaseDriverType dataBaseType,
                                                       final String jdbcUrl, final String username, final String password, String socketTimeout) {
        return RdbmsDBMetaUtil.connect(dataBaseType, jdbcUrl, username,
                password, socketTimeout);
    }

    private static synchronized Connection connect(DataBaseDriverType dataBaseType,
                                                   String url, String user, String pass) {
        return connect(dataBaseType, url, user, pass, String.valueOf(RdbmsConstant.SOCKET_TIMEOUT_INSECOND * 1000));
    }

    private static synchronized Connection connect(DataBaseDriverType dataBaseType,
                                                   String url, String user, String pass, String socketTimeout) {

        //ob10的处理
        if (url.startsWith(OB10_SPLIT_STRING) && dataBaseType == DataBaseDriverType.MySql) {
            String[] ss = url.split(OB10_SPLIT_STRING_PATTERN);
            if (ss.length != 3) {
                LOG.error("JDBC OB10格式错误，请联系管理员");
            }
            LOG.info("this is ob1_0 jdbc url.");
            user = ss[1].trim() + ":" + user;
            url = ss[2];
            LOG.info("this is ob1_0 jdbc url. user=" + user + " :url=" + url);
        }

        Properties prop = new Properties();
        prop.put("user", user);
        prop.put("password", pass);

        if (dataBaseType == DataBaseDriverType.Oracle) {
            //oracle.net.READ_TIMEOUT for jdbc versions < 10.1.0.5 oracle.jdbc.ReadTimeout for jdbc versions >=10.1.0.5
            // unit ms
            prop.put("oracle.jdbc.ReadTimeout", socketTimeout);
        }

        return connect(dataBaseType, url, prop);
    }

    private static synchronized Connection connect(DataBaseDriverType dataBaseType,
                                                   String url, Properties prop) {
        try {
            Class.forName(dataBaseType.getDriverClassName());
            DriverManager.setLoginTimeout(RdbmsConstant.TIMEOUT_SECONDS);
            return DriverManager.getConnection(url, prop);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * @return Left:ColumnName Middle:ColumnType Right:ColumnTypeName
     */
    public static Triple<List<String>, List<Integer>, List<String>> getColumnMetaData(
            Connection conn, String tableName, String column) {
        Statement statement = null;
        ResultSet rs = null;

        Triple<List<String>, List<Integer>, List<String>> columnMetaData = new ImmutableTriple<List<String>, List<Integer>, List<String>>(
                new ArrayList<String>(), new ArrayList<Integer>(),
                new ArrayList<String>());
        try {
            statement = conn.createStatement();
            String queryColumnSql = "select " + column + " from " + tableName
                    + " where 1=2";

            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {

                columnMetaData.getLeft().add(rsMetaData.getColumnName(i + 1));
                columnMetaData.getMiddle().add(rsMetaData.getColumnType(i + 1));
                columnMetaData.getRight().add(
                        rsMetaData.getColumnTypeName(i + 1));
            }
            return columnMetaData;

        } catch (SQLException e) {
            String message = String.format("获取表:%s 的字段的元信息时失败. 请联系 DBA 核查该库、表信息.", tableName);

        } finally {
            RdbmsDBMetaUtil.closeDBResources(rs, statement, null);
        }
        return null;
    }

    public static void closeDBResources(ResultSet rs, Statement stmt,
                                        Connection conn) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException unused) {
            }
        }

        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException unused) {
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException unused) {
            }
        }
    }

    public static List<Map<String, Object>> getTableColumns(DataBaseDriverType dataBaseType,
                                                            String jdbcUrl, String user, String pass, String tableName) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        Connection conn = getConnection(dataBaseType, jdbcUrl, user, pass);
        try {
            if (conn != null) {
                list = getTableColumnsByConn(dataBaseType, conn, tableName, "jdbcUrl:" + jdbcUrl);
            } else {
                SQLException ex = new SQLException("数据库连接错误", "数据库连接错误", 4);
                throw ex;
            }

        } catch (SQLException e) {
            throw e;
        }
        return list;
    }

    public static boolean checkFlagColumnExist(DataBaseDriverType dataBaseType, String jdbcUrl, String user, String pass, String tableName) {
        Connection conn = getConnection(dataBaseType, jdbcUrl, user, pass);
        try {
            return checkColumnExistByConn(conn, tableName);
        } catch (Throwable e) {
            LOG.error(e.getMessage());
        } finally {
            closeDBResources(null, null, conn);
        }
        return false;
    }

    private static boolean checkColumnExistByConn(Connection conn, String tableName) throws SQLException {
        Statement statement = null;
        ResultSet rs = null;
        String queryColumnSql = null;
        try {
            statement = conn.createStatement();
            queryColumnSql = String.format("select * from %s where 1=2", tableName);
            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
                if (TMP_IDTT_TRANS_SECU_FLAG.equals(rsMetaData.getColumnName(i + 1))) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw e;
        } finally {
            RdbmsDBMetaUtil.closeDBResources(rs, statement, null);
        }
        return false;
    }

    public static void createFlagColumnOnTable(final DataBaseDriverType dataBaseType, final String jdbcUrl, final String user, final String pass, final String tableName) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = getConnection(dataBaseType, jdbcUrl, user, pass);
                try {
                    createFlagColumn(conn, tableName);
                } catch (Throwable e) {
                    LOG.error(e.getMessage());
                } finally {
                    closeDBResources(null, null, conn);
                }
            }
        });
        thread.start();
    }

    private static void createFlagColumn(Connection conn, String tableName) {
        Statement statement = null;
        ResultSet rs = null;
        String queryColumnSql = null;
        try {
            statement = conn.createStatement();
            statement.setQueryTimeout(0);
            queryColumnSql = String.format("alter table %s add %s varchar(50)", tableName, TMP_IDTT_TRANS_SECU_FLAG);
            statement.execute(queryColumnSql);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            RdbmsDBMetaUtil.closeDBResources(rs, statement, null);
        }
    }

    private static List<Map<String, Object>> getTableColumnsByConn(DataBaseDriverType dataBaseType, Connection conn, String tableName, String basicMsg) throws SQLException {
        List<Map<String, Object>> columns = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        String queryColumnSql = null;
        try {
            statement = conn.createStatement();
            queryColumnSql = String.format("select * from %s where 1=2",
                    tableName);
            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
                Map<String, Object> item = new HashMap<>();
                String columnName = rsMetaData.getColumnName(i + 1);
                if (TMP_IDTT_TRANS_SECU_FLAG.equals(columnName)) {
                    continue;
                }
                item.put("name", columnName);
                item.put("type", rsMetaData.getColumnTypeName(i + 1));
                item.put("checked", true);
                columns.add(item);
            }

        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw e;
        } finally {
            RdbmsDBMetaUtil.closeDBResources(rs, statement, conn);
        }
        return columns;
    }

    public static List<String> getTableNameByCon(Connection con) {
        List<String> set = new ArrayList<>();
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables(null, null, null,
                    new String[]{"TABLE"});
            while (rs.next()) {
                set.add(rs.getString(3));
            }
            con.close();
        } catch (Exception e) {
            try {
                con.close();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
            }
            LOG.error(e.getMessage());
        }
        return set;
    }

    public static String getJdbcUrl(Integer type, String host, int port, String dbaseName, String username, String passwd) {
        String jdbcUrl = null;
        switch (type) {
            case 0:
                if (dbaseName.startsWith("/")) {
                    jdbcUrl = "jdbc:oracle:thin:@//" + host + ":" + port + dbaseName;
                } else {
                    jdbcUrl = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbaseName;
                }
                break;
            case 1:
                jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbaseName;
                break;
            case 2:
                jdbcUrl = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + dbaseName;
                break;
            case 3:
                jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbaseName;
                break;
            case 9:
                jdbcUrl = "jdbc:db2://" + host + ":" + port + "/" + dbaseName;
                break;
            default:
                break;
        }
        return jdbcUrl;
    }

}
