package sqlexecutor.singlecore;

import com.di.common.utils.JDBCUtils;
import com.di.dmas.common.datasource.pojo.DataBaseDriverType;
import com.di.dmas.common.datasource.pojo.RdbmsBo;
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
 * Rdbms执行器
 *
 * @author cuitianyu
 */
public class RdbmsSqlExecutor extends AbstractExecutor implements SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(RdbmsSqlExecutor.class);
    private Boolean isAutoCommit = true;
    private Connection conn = null;
    //	private Stack<String> createObjectList = new Stack<String>();
    private RdbmsBo bo;

    public RdbmsSqlExecutor(RdbmsBo db) {
        this.bo = db;
        runner = JDBCRunner.getRunner();
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
    public ExecuteResult execute(String sql) {
        ExecuteResult er = new ExecuteResult();
        er.setSql(sql);
        String trimsql = sql.trim();
        String firstLetter = trimsql.split(" ")[0].toUpperCase();
        er.setType(ExecuteResultType.getByValue(firstLetter));
        try {
            int update;
            switch (ExecuteResultType.getByValue(firstLetter)) {
                case SHOW:
                case DESCRIBE:
                case SELECT:
                    if (isAutoCommit) {
                        er = runner.query(getConn(), true, sql, new ExecuteResultHandler());
                    } else {
                        er = runner.query(conn, false, sql, new ExecuteResultHandler());
                    }
                    return er;
                case CREATE:
                    if (isAutoCommit) {
                        update = runner.update(getConn(), true, sql);
                    } else {
                        update = runner.update(conn, false, sql);
//					createObjectList.push(trimsql.split(" ")[1] + " " + trimsql.split(" ")[2]);
                    }
                    er.setResultCode(update);
                    er.setIsSucceed(true);
                    return er;
                case ALTER:
                case DROP:
                case INSERT:
                case UPDATE:
                case MERGE:
                case DELETE:
                case UNKNOW:
                default:
                    if (isAutoCommit) {
                        update = runner.update(getConn(), true, sql);
                    } else {
                        update = runner.update(conn, false, sql);
                    }
                    er.setResultCode(update);
                    er.setIsSucceed(true);
                    return er;
            }
        } catch (Exception e) {
            StringBuffer errorInfo = new StringBuffer();
            errorInfo.append(e.getMessage());
            try {
                rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                errorInfo.append("\r\n" + e1.getMessage());
            }
            try {
                close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            er.setIsSucceed(false);
            er.setExceptionInfo(errorInfo.toString());
            return er;
        }
    }

    /**
     * 回滚create类型的sql语句
     * 暂弃用
     *
     * @throws SQLException
     */
//	private void rollbackCreate() throws SQLException {
//		if(null!=conn) {
//			while (!createObjectList.empty()) {
//				String content = createObjectList.pop();
//				try {
//					runner.update(conn,false,"drop " + content);
//				} catch (Exception e) {
//					throw new SQLException(this.getClass() + ":rdbms rollback operation [drop " + content + "] fail");
//				}
//			}
//		}
//	}
    @Override
    public void setAutoCommit(Boolean flag) throws SQLException {
        this.isAutoCommit = flag;
        if (!flag) {
            close();
            conn = getConn();
            conn.setAutoCommit(isAutoCommit);
        } else {
            close();
        }
    }

    @Override
    public void commit() throws SQLException {
        if (null != conn && !conn.isClosed()) {
            conn.commit();
//			createObjectList.clear();
        }
    }

    public void rollback() throws SQLException {
        if (!isAutoCommit) {
            if (null != conn && !conn.isClosed()) {
                conn.rollback();
//				rollbackCreate();
            }
        }
    }


    @Override
    public void close() throws SQLException {
        if (null != conn) {
            conn.close();
            conn = null;
        }
    }

    @Override
    public boolean destroy() throws SQLException {
        close();
        runner = null;
//		createObjectList = null;
        bo = null;
        return true;
    }

}
