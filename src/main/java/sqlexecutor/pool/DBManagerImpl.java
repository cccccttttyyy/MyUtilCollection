package sqlexecutor.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.datasource.DataSourceException;
import sqlexecutor.config.PropertiesBuilder;
import sqlexecutor.pojo.*;
import sqlexecutor.utils.JDBCUtils;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;


/**
 * DBmanager实现类 初始化内部数据源
 *
 * @author cuitianyu
 */
public class DBManagerImpl implements DBManager {

    protected static final Log log = LogFactory.getLog(DBManagerImpl.class);
    protected DruidDataSource dataSource;
    protected DatabaseTypeEnum type;
    protected int hashcode;

    public DBManagerImpl(Database bo) {
        try {
            hashcode = bo.hashCode();
            dataSource = new DruidDataSource();
            dataSource = dsCommonProperties(dataSource);
            DatabaseTypeEnum type = DatabaseTypeEnum.getById(bo.getDbase_type());
            this.type = type;
            if (type.getCode().equals(-1)) {
                throw new DataSourceException(this.getClass() + ":init dataSource error! dataSource type not found");
            }
            if (DatabaseTypeEnum.ORACLE.getCode().equals(bo.getDbase_type())) {
                dataSource.setPoolPreparedStatements(true);
                dataSource.setValidationQuery("select 1 from dual");
            } else if (DatabaseTypeEnum.POSTGRESQL.getCode().equals(bo.getDbase_type())) {
                dataSource.setValidationQuery("select version()");
            } else {
                dataSource.setValidationQuery("select 1");//hive phoenix均支持
            }
            dataSource.setDbType(type.getName());
            dataSource.setDriverClassName(DataBaseDriverType.getById(bo.getDbase_type()).getDriverClassName());
            if (bo instanceof RdbmsBo) {
                setRdbmsdataSource((RdbmsBo) bo);
            } else if (bo instanceof HiveBo) {
                setHivedataSource((HiveBo) bo);
            } else if (bo instanceof HbaseBo) {
                setHBasedataSource((HbaseBo) bo);
            } else {

            }
            testDs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setHivedataSource(HiveBo bo) {
        try {
            String jdbcUrl = JDBCUtils.getJdbcUrl(bo.getDbase_type(), bo.getDbase_ip(), bo.getDbase_port(),
                    bo.getDbase_name());
            if (null != bo.getConnect_params()) {
                jdbcUrl = jdbcUrl + "?" + bo.getConnect_params();
            }
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(bo.getDbase_user());
            dataSource.setPassword(bo.getDbase_passwd());
            dataSource.setValidationQuery("SELECT 1");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据库初始化连接池失败：" + e);
        }
    }

    private void setHBasedataSource(HbaseBo bo) {
        try {
            String jdbcUrl = JDBCUtils.getJdbcUrl(bo.getDbase_type(), bo.getZk_quorum(), bo.getZk_port(), "");
            dataSource.setUrl(jdbcUrl);
            dataSource.setValidationQuery("SELECT 1");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据库初始化连接池失败：" + e);
        }
    }

    private void setRdbmsdataSource(RdbmsBo bo) {
        try {

            String jdbcUrl = JDBCUtils.getJdbcUrl(bo.getDbase_type(), bo.getDbase_ip(), bo.getDbase_port(),
                    bo.getDbase_name());
            if (null != bo.getConnect_params() && bo.getConnect_params().trim().isEmpty() == false) {
                jdbcUrl = jdbcUrl + "?" + bo.getConnect_params();
            }
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(bo.getDbase_user());
            dataSource.setPassword(bo.getDbase_passwd());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据库初始化连接池失败：" + e);
        }
    }

    public DruidDataSource getDataSource() {
        return dataSource;
    }

    public DatabaseTypeEnum getDBType() {
        return type;
    }

    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    /**
     * 获取链接，用完后记得关闭
     *
     * @return
     */
    public final DruidPooledConnection getConn() {
        DruidPooledConnection conn = null;
        try {
            synchronized (dataSource) {
                conn = dataSource.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("获取数据库Connection失败：" + e);
        }
        return conn;
    }

    /**
     * 数据源基本连接测试
     *
     * @throws SQLException
     */
    private void testDs() throws SQLException {
        DruidPooledConnection conn = getConn();
        DatabaseMetaData mdm = conn.getMetaData();
        log.info("Connected to " + mdm.getDatabaseProductName() + " " + mdm.getDatabaseProductVersion());
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * 公共配置读取
     *
     * @param dataSource
     * @return
     */
    private DruidDataSource dsCommonProperties(DruidDataSource dataSource) {
        Properties config = PropertiesBuilder.getConfig("conf/druid_pool_common.properties");
        DruidDataSourceFactory.config(dataSource, config);
        return dataSource;
    }

    @Override
    public int gethashcode() {
        return this.hashcode;
    }
}
