package jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.telnet.TelnetClient;
import String.StringUtils;


/**
 * jdbc工具类
 * 
 * @author liutx
 */
public class JdbcUtils {
    public static final String TABLE_TYPE = "TABLqE";

    public static final String VIEW_TYPE = "VIEW";

    private static Log log = LogFactory.getLog(JdbcUtils.class);// 日志

    /**
     * 测试是否能连通指定的ip和端口
     * 
     * @param ip
     * @param port
     * @return
     */
    public static boolean testAddress(String ip, int port) {
        try {
            // 方法一
            TelnetClient telnet = new TelnetClient();
            telnet.connect(ip, port);
            return telnet.isConnected();
            // 方法二
            // Socket connect = new Socket();
            // connect.connect(new InetSocketAddress(ip, port),3000);
            // return connect.isConnected();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 批量执行 <br>
     * <p>
     * Description: <br>
     * <p>
     * Date: 2015年3月21日 下午2:02:50<br/>
     * <p>
     * 
     * @param conn
     *            数据连接
     * @param sql
     *            执行sql
     * @param objs
     *            数据集合
     * @throws SQLException
     *
     */
    public static void insertBatch(Connection conn, String sql, List<Map<Integer, String>> objs) throws SQLException {
    	log.info("insertBatch插入Sql："+sql);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (Map<Integer, String> obj : objs) {
                for (int j = 0; j < obj.size(); j++) {
                    ps.setObject(j + 1, obj.get(j));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.error("关闭ps出现异常", e);
                }
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * 根据数据源信息获取数据库链接conn
     * 
     * @return
     *//*
    public static Connection getConnByDataSource(Map<String, Object> dataSource) {
        return ConnectionManager.getPooledConnection(dataSource);
    }*/
    /**
     * 
     * @title getJDBCConnection<br/>
     * <p>Description: 
     * <br>通过原生的JDBC获取连接，不放入连接池
     * @author heshenghao<br>
     * <p>Date: 2018年10月17日 下午3:20:21<br/>
     * <p>
     * @param driverName
     * @param jdbcurl
     * @param uname
     * @param pword
     * @return   
     * @see Connection
     */
    public static Connection getJDBCConnection(String driverName, String jdbcurl, String uname, String pword) {
       
        String jdbcUrl = jdbcurl;
        String userName = uname;
        String password = pword;
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch (SQLException e) {
            log.error("getJDBCConnection Error", e);
        } catch (ClassNotFoundException e) {
            log.error("getJDBCConnection Error", e);
        }
        return conn;
    }
   
    /**
     * 根据数据源信息获取数据库链接conn,元数据同步专用（需要同步备注信息非常慢，不适用取数据及其他一般获取连接方式）
     * 
     * @return
     */
   /* public static Connection getConnByDataSourceOnlyMetaSyn(Map<String, Object> dataSource) {
        return ConnectionManager.getConnByDataSourceOnlyMetaSyn(dataSource);
    }*/
   
    /**
     * 获取数据库所有的schem
     * 
     * @param Connection
     * @return List<String>
     */
    public static List<String> getDataBaseList(Connection conn) {
        List<String> list = new ArrayList<String>();
        try {
            if (conn == null)
                return list;
            ResultSet rs = conn.getMetaData().getCatalogs();
            while (rs.next()) {
                list.add(rs.getObject("TABLE_CAT").toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("getDataBaseList出现异常:", ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("关闭conn出现异常", e);
                }
            }
        }
        return list;
    }

    /**
     * 获取元数据API
     * 
     * @param conn
     * @return
     */
    public static DatabaseMetaData getDatabaseMetaData(Connection conn) {
        DatabaseMetaData dbmd = null;
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getDatabaseMetaData()出错！", ex);
        } finally {
            /*
             * if(conn!=null){ try { conn.close(); } catch (SQLException e) {
             * log.error("关闭conn出现异常", e); } }
             */
        }
        return dbmd;
    }

    /**
     * 根据数据库类型获取对应的schame
     * 
     * @param dbType
     * @param dbName
     * @param user
     */
    public static String getSchemaName(Object dbType, Object dbName, Object user) {
        String schema = "";
        if ("mysql".equals(dbType)) {// mysql的schema是数据库名
            schema = String.valueOf(dbName);
        } else if ("oracle".equals(dbType)) {
        	//oracle 的schema大小写区分是需要配置的，大写通用
            schema = String.valueOf(user).toUpperCase();// oracle是用户名
            
        } else if ("sqlserver".equals(dbType)) {
            schema = "dbo";// sqlserver是dbo
        } else if("db2".equals(dbType)) {
        	schema = String.valueOf(user); // db2的schema是用户名
        }else if("postgresql".equals(dbType)) {
        	schema = "public"; // postgres的schema暂时都为public
        }
        return schema;
    }

    /**
     * 获取数据库所有的schem
     * 
     * @param Connection
     * @return
     */
    public static List<Map<String, Object>> getSchemasList(Connection conn) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbmd = getDatabaseMetaData(conn);
            rs = dbmd.getSchemas();
            while (rs.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("TABLE_SCHEM", rs.getObject("TABLE_SCHEM"));
                result.add(resultMap);
            }
        } catch (SQLException e) {
            log.error("getSchemasList出现异常:", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                /*
                 * if (conn != null){ conn.close(); } conn = null;
                 */
            } catch (SQLException ex) {
                log.error("关闭连接出现异常", ex);
            }
        }
        return result;
    }

    /**
     * 判断表或者视图是否存在
     * 
     * @param Connection
     * @param schema
     *            模式名称：oracle(用户名);mysql(数据库名);sqlServer(dbo)
     * @param Type
     *            (TABLE和VIEW两种)
     * @return
     */
    public static boolean isExistTable(Connection conn, String schema, String tableName, String type) {
        ResultSet rs = null;
        try {
            DatabaseMetaData dbmd = getDatabaseMetaData(conn);
            rs = dbmd.getTables(null, schema, tableName, new String[] {type });
            if (rs != null)
                return true;
        } catch (SQLException e) {
            log.error("getTablesList出现异常:", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                /*
                 * if (conn != null){ conn.close(); } conn = null;
                 */
            } catch (SQLException ex) {
                log.error("关闭连接出现异常", ex);
            }
        }
        return false;
    }

    /**
     * 获取对给定表的主键列的描述
     * 
     * @param Connection
     * @param schema
     *            模式名称：oracle(用户名);mysql(数据库名);sqlServer(dbo)
     * @param tableName
     *            表名称
     */
    public static List<Map<String, Object>> getPrimaryKeys(Connection conn, String schema, String tableName) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbmd = getDatabaseMetaData(conn);
            rs = dbmd.getPrimaryKeys(null, schema, tableName);
            while (rs.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("TABLE_SCHEM", rs.getObject("TABLE_SCHEM"));// 表模式（可为
                                                                          // null）
                resultMap.put("TABLE_NAME", rs.getString("TABLE_NAME"));// 表名称
                resultMap.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));// 列名称
                resultMap.put("KEY_SEQ", rs.getString("KEY_SEQ"));// 主键中的序列号（值 1
                                                                  // 表示主键中的第一列，值
                                                                  // 2
                                                                  // 表示主键中的第二列）。
                resultMap.put("PK_NAME", rs.getString("PK_NAME"));// 主键的名称（可为
                                                                  // null）
                result.add(resultMap);
            }
        } catch (SQLException e) {
            log.error("getPrimaryKeys出现异常:", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
            } catch (SQLException ex) {
                log.error("关闭连接出现异常", ex);
            }
        }
        return result;
    }

    /**
     * 获取引用给定表的主键列（表导入的外键）的外键列的描述
     * 
     * @param Connection
     * @param schema
     *            模式名称：oracle(用户名);mysql(数据库名);sqlServer(dbo)
     * @param tableName
     *            表名称
     * @return
     */
    public static List<Map<String, Object>> getExportedKeysList(Connection conn, String schema, String tableName) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbmd = getDatabaseMetaData(conn);
            rs = dbmd.getExportedKeys(null, schema, tableName);
            while (rs.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("PKTABLE_SCHEM", rs.getObject("PKTABLE_SCHEM"));// 主键表模式（可为
                                                                              // null）
                resultMap.put("PKTABLE_NAME", rs.getObject("PKTABLE_NAME"));// 主键表名称
                resultMap.put("PKCOLUMN_NAME", rs.getString("PKCOLUMN_NAME"));// 主键列名称
                resultMap.put("FKTABLE_SCHEM", rs.getObject("FKTABLE_SCHEM"));// 被导入的外键表模式（可能为
                                                                              // null），该字符串可能为
                                                                              // null
                resultMap.put("FKTABLE_NAME", rs.getString("FKTABLE_NAME"));// 被导入的外键表名称
                resultMap.put("FKCOLUMN_NAME", rs.getString("FKCOLUMN_NAME"));// 被导入的外键列名称
                resultMap.put("PK_NAME", rs.getString("PK_NAME"));// 外键的名称
                resultMap.put("FK_NAME", rs.getString("FK_NAME"));// 主键的名称
                result.add(resultMap);
            }
        } catch (SQLException e) {
            log.error("getExportedKeysList出现异常:", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
            } catch (SQLException ex) {
                log.error("关闭连接出现异常", ex);
            }
        }
        return result;
    }

    /**
     * 获取给定表的索引和统计信息的描述
     * 
     * @param Connection
     *            conn
     * @param schema
     *            模式名称：oracle(用户名);mysql(数据库名);sqlServer(dbo)
     * @param tableName
     *            表名称
     * @return
     */
    public static List<Map<String, Object>> getIndexInfoList(Connection conn, String schema, String tableName) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbmd = getDatabaseMetaData(conn);
            /**
             * unique - 该参数为 true 时，仅返回唯一值的索引；该参数为 false 时，返回所有索引，不管它们是否唯一
             * approximate - 该参数为 true 时，允许结果是接近的数据值或这些数据值以外的值；该参数为 false
             * 时，要求结果是精确结果
             */
            boolean unique = false;
            boolean approximate = true;
            rs = dbmd.getIndexInfo(null, schema, tableName, unique, approximate);
            while (rs.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                // 表模式（可为null）
                resultMap.put("TABLE_SCHEM", rs.getObject("TABLE_SCHEM"));
                // 表名称
                resultMap.put("TABLE_NAME", rs.getObject("TABLE_NAME"));
                // 索引值是否可以不唯一
                resultMap.put("NON_UNIQUE", rs.getString("NON_UNIQUE"));
                // 索引类别(可为 null);TYPE 为 0 时索引类别为 null
                resultMap.put("INDEX_QUALIFIER", rs.getString("INDEX_QUALIFIER"));
                // 索引名称 ;TYPE为 0时索引类别为null
                resultMap.put("INDEX_NAME", rs.getString("INDEX_NAME"));
                // 列名称;TYPE为0时索引类别为null
                resultMap.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
                /**
                 * 0: tableIndexStatistic - 此标识与表的索引描述一起返回的表统计信息 1:
                 * tableIndexClustered - 此为集群索引 2: tableIndexHashed - 此为散列索引 3:
                 * tableIndexOther - 此为某种其他样式的索引
                 */
                // 索引类型
                resultMap.put("TYPE", rs.getString("TYPE"));
                result.add(resultMap);
            }
        } catch (SQLException e) {
            log.error("getIndexInfoList出现异常:", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                /*
                 * if (conn != null){ conn.close(); } conn = null;
                 */
            } catch (SQLException ex) {
                log.error("关闭连接出现异常", ex);
            }
        }
        return result;
    }

    

    /**
     * 查询list
     * 
     * @param 数据库连接conn
     *            ，执行的sql语句
     * @return
     */
    public static List<Map<String, String>> queryForList(Connection conn, String sql) {
    	log.info("queryForList查询SQL："+sql);
        PreparedStatement psmt = null;
        ResultSet rs = null;
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        try {
            if (conn == null)
                return result;
            psmt = conn.prepareStatement(sql);
            psmt.setQueryTimeout(5*60);
            rs = psmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
            int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<String, String>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnLabel(i), rs.getObject(i).toString());
                }
                result.add(rowData);
            }
        } catch (Exception e) {
            log.error("获取结果集出现异常", e);
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("关闭rs出现异常", e);
                }
                rs = null;
            }
            if (psmt != null) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    log.error("关闭psmt出现异常", e);
                }
                psmt = null;
            }
            // if(conn != null){
            // try {
            // conn.close();
            // } catch (SQLException e) {
            // log.error("关闭conn出现异常", e);
            // }
            // }
        }
        return result;
    }



    /**
     * 查询单条记录（改了toString）
     * 
     * @param 数据库连接conn
     *            ，执行的sql语句
     * @return
     */
    public static Map<String, String> queryForMap(Connection conn, String sql) {
        PreparedStatement psmt = null;
        ResultSet rs = null;
        Map<String, String> result = null;//改了toString
        log.info("queryForMap查询Sql："+sql);
        try {
            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
            int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
            while (rs.next()) {
                result = new HashMap<String, String>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                	String name = md.getColumnLabel(i);
                	Object value = rs.getObject(i);
                	if(null == value) {
                		value = "null";
                	}
                    result.put(name, value.toString());//改了toString
                }
            }
        } catch (SQLException e) {
            log.error("获取单条结果出现异常", e);
        } catch(Exception e){
        	log.error("获取单条结果出现异常", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("关闭rs出现异常", e);
                }
                rs = null;
            }
            if (psmt != null) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    log.error("关闭psmt出现异常", e);
                }
                psmt = null;
            }
            /*
             * if(conn != null){ try { conn.close(); } catch (SQLException e) {
             * log.error("关闭conn出现异常", e); } }
             */
        }
        return result;
    }

    /**
     * 查询总记录数
     * 
     * @param   数据库连接conn
     *            ，执行的sql语句
     * @return
     */
    public static int queryForCount(Connection conn, String sql) {
    	log.info("queryForCount查询Sql："+sql);
        PreparedStatement psmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取总记录数出现异常", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("关闭rs出现异常", e);
                }
                rs = null;
            }
            if (psmt != null) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    log.error("关闭psmt出现异常", e);
                }
                psmt = null;
            }
            /*
             * if(conn != null){ try { conn.close(); } catch (SQLException e) {
             * log.error("关闭conn出现异常", e); } }
             */
        }
        return count;
    }

    /**
     * 执行SQL语句
     * 
     * @param 数据库连接conn
     *            ，执行的sql语句
     * @return
     * @throws Exception
     */
    public static boolean executeSql(Connection conn, String sql) throws Exception {
    	log.info("executeSql执行Sql："+sql);
        PreparedStatement psmt = null;
        boolean flag = false;
        try {
            psmt = conn.prepareStatement(sql);
            flag = psmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (psmt != null) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    log.error("关闭psmt出现异常", e);
                }
                psmt = null;
            }
            /*
             * if(conn != null){ try { conn.close(); } catch (SQLException e) {
             * log.error("关闭conn出现异常", e); } }
             */
        }
        return flag;
    }
    public static boolean executeCreateTableSql(Connection conn, String sql) throws Exception {
    	log.info("executeSql执行Sql："+sql);
        PreparedStatement psmt = null;
        boolean flag = false;
        try {
            psmt = conn.prepareStatement(sql);
            flag = psmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (psmt != null) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    log.error("关闭psmt出现异常", e);
                }
                psmt = null;
            }
            /*
             * if(conn != null){ try { conn.close(); } catch (SQLException e) {
             * log.error("关闭conn出现异常", e); } }
             */
        }
        return flag;
    }

    /**
     * 将resultMap转化成普通map
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> getTableResultMap(ResultSet rs) throws SQLException {
        Map<String, Object> hm = new HashMap<String, Object>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String key = rsmd.getColumnLabel(i);
            String value = rs.getString(i);
            if (key.equals("DATA_TYPE")) {
                if (value.equals("VARCHAR2") || value.equals("CHAR")) {
                    value = "VARCHAR";
                } else if (value.equals("BLOB")) {
                    value = "VARCHAR";
                } else if (value.equals("FLOAT")) {
                    value = "VARCHAR";
                } else if (value.equals("NUMBER")) {
                    value = "VARCHAR";
                } else if (value.equals("DATE")) {
                    value = "VARCHAR";
                }
                value = value.toLowerCase();
            }
            hm.put(key, value);
            if ("NULLABLE".equals(key)) {
                if ("N".equals(value)) {
                    hm.put("NOT_NULL", 1);
                } else if ("Y".equals(value)) {
                    hm.put("NOT_NULL", 0);
                }
            }
        }
        return hm;
    }

    /**
     * 将resultMap转化成普通map
     * 
     * @return
     */
    public static Map<String, Object> getResultMapByResultSet(ResultSet rs) throws SQLException {
        Map<String, Object> hm = new HashMap<String, Object>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String key = rsmd.getColumnLabel(i);
            String value = rs.getString(i);
            hm.put(key, value);
        }
        return hm;
    }

    /**
     * 支持的驱动
     */
    public static final String driver_mysql = "com.mysql.jdbc.Driver";

    public static final String driver_hbase = "hbase.zookeeper.quorum";

    public static final String driver_oracle = "oracle.jdbc.driver.OracleDriver";

    public static final String driver_sqlserver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String driver_hive = "org.apache.hadoop.hive.jdbc.HiveDriver";// Apache版本hive的jdbc驱动

    public static final String driver_hive2 = "org.apache.hive.jdbc.HiveDriver";// CDH版本hive的jdbc驱动

    public static final String driver_postgresql = "org.postgresql.Driver";

    public static final String driver_oscar = "com.oscar.Driver";// 神通数据库

    /**
     * 根据数据库类型获取驱动
     * 
     * @param dbType
     * @return
     */
    public static String getDriverClass(Object dbType) {
        String driverclass = null;
        if (StringUtils.isNotEmptyObject(dbType)) {
            String databaseType = dbType.toString();
            if ("mysql".equals(databaseType)) {
                driverclass = driver_mysql;
            } else if ("oracle".equals(databaseType)) {
                driverclass = driver_oracle;
            } else if ("hbase".equals(databaseType)) {
                driverclass = driver_hbase;
            } else if ("sqlserver".equals(databaseType)) {
                driverclass = driver_sqlserver;
            } else if ("hive".equals(databaseType)) {
                driverclass = driver_hive;
            } else if ("postgresql".equals(databaseType)) {
                driverclass = driver_postgresql;
            } else if ("oscar".equals(databaseType)) {
                driverclass = driver_oscar;
            }
        }
        return driverclass;
    }

    /**
     * 获取jdbcurl
     * 
     * @param map
     * @return
     */
    public static String getJdbcUrlByIpAndPort(Map<String, Object> map) {
        Object dbName = map.get("database_name");
        String jdbc = map.get("datasource_host") + ":" + map.get("datasource_port");
        String databaseType = map.get("database_type").toString();
        if ("oracle".equals(databaseType)) {
            jdbc = "jdbc:oracle:thin:@" + jdbc;
            if (StringUtils.isNotEmptyObject(dbName))
                jdbc += ":" + dbName;
        } else if ("sqlserver".equals(databaseType)) {
            jdbc = "jdbc:sqlserver://" + jdbc;
            if (StringUtils.isNotEmptyObject(dbName))
                jdbc += ";DatabaseName=" + dbName;
        } else if ("mysql".equals(databaseType)) {
            jdbc = "jdbc:mysql://" + jdbc;
            if (!StringUtils.isNotEmptyObject(dbName)) {
                dbName = "mysql";
            }
            jdbc += "/" + dbName + "?useUnicode=true&characterEncoding=";
            if (StringUtils.isNotEmptyObject(map.get("defaultCharacterSetName"))) {
                jdbc += map.get("defaultCharacterSetName");
            } else {
                jdbc += "UTF8";
            }
        } else if ("db2".equals(databaseType)) {
            jdbc = "jdbc:db2://" + jdbc;
            if (StringUtils.isNotEmptyObject(dbName))
                jdbc += "/" + dbName;
        } else if ("hive".equals(databaseType)) {
            jdbc = "jdbc:hive://" + jdbc;
            if (StringUtils.isNotEmptyObject(dbName))
                jdbc += "/" + dbName;
        } else if ("hive".equals(databaseType)) {
            jdbc = "jdbc:scar://" + jdbc;
            if (StringUtils.isNotEmptyObject(dbName))
                jdbc += "/" + dbName;
        }
        else if ("postgresql".equals(databaseType)) {
            jdbc = "jdbc:postgresql://" + jdbc;
            if (StringUtils.isNotEmptyObject(dbName))
                jdbc += "/" + dbName;
        }
        return jdbc;
    }

    /**
     * 获取普通map结果集(改了toString)
     * 
     * @param conn
     * @param sql
     * @return
     */
    public static List<Map<String, String>> queryForResultMapList(Connection conn, String sql) {
    	log.info("queryForResultMapList查询Sql："+sql);
        PreparedStatement psmt = null;
        ResultSet rs = null;
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();//改了toString
        try {
            if (conn == null)
                return result;
            psmt = conn.prepareStatement(sql);
            psmt.setQueryTimeout(5*60);
            rs = psmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
            int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<String, String>(columnCount);//改了toString
                for (int i = 1; i <= columnCount; i++) {
                	String name = md.getColumnLabel(i);
                	Object value = rs.getObject(i);
                	if(null == value) {
                		value = "null";
                	}
                    //result.put(name, value.toString());//改了toString
                    //rowData.put(md.getColumnLabel(i), rs.getString(i).toString());//改成了toString
                	rowData.put(name, value.toString());
                }
                result.add(rowData);
            }
        } catch (Exception e) {
            log.error("获取结果集出现异常", e);
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("关闭rs出现异常", e);
                }
                rs = null;
            }
            if (psmt != null) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    log.error("关闭psmt出现异常", e);
                }
                psmt = null;
            }
            /*
             * if (conn != null) { try { conn.close(); } catch (SQLException e)
             * { // TODO Auto-generated catch block log.error("关闭连接出现异常", e); }
             * } conn = null;
             */
        }
        return result;
    }

    //将jdbctype 数字转换为字符串
    public static String getJDBCTypeString(int type) {
        switch (type) {
            case -7:
                return "BIT";
            case -6:
                return "TINYINT";
            case 5:
                return "SMALLINT";
            case 4:
                return "INTEGER";
            case -5:
                return "BIGINT";
            case 6:
                return "FLOAT";
            case 7:
                return "REAL";
            case 8:
                return "DOUBLE";
            case 2:
                return "NUMERIC";
            case 3:
                return "DECIMAL";
            case 1:
                return "CHAR";
            case 12:
                return "VARCHAR";
            case -1:
                return "TEXT";
            case 91:
                return "DATE";
            case 92:
                return "TIME";
            case 93:
                return "TIMESTAMP";
            case -2:
                return "BINARY";
            case 16:
                return "BOOLEAN";
            case 2013:
                return "TIME_WITH_TIMEZONE";
            case 2014:
                return "TIMESTAMP_WITH_TIMEZONE";
        }
        return null;
    }
    
    public static boolean isTableExists(Connection conn, String tableName) {
        PreparedStatement pstmt = null;
        try {
            String sql = "select count(*) from " + tableName;
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
           if(null != pstmt){
        	   try {
				pstmt.close();
			} catch (SQLException e) {
				log.error("关闭psmt出现异常", e);
			}
           }
           if(null != conn){
        	   try {
        		   conn.close();
			} catch (SQLException e) {
				log.error("关闭conn出现异常", e);
			}
           }
        }
    }

}
