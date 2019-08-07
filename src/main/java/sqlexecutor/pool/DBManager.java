package sqlexecutor.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import sqlexecutor.pojo.DatabaseTypeEnum;


/**
 * 单个连接池管理
 *
 * @author cuitianyu
 */
public interface DBManager {
    /**
     * 获取数据源连接
     *
     * @return
     */
    DruidPooledConnection getConn();

    /**
     * 获取数据源类型
     *
     * @return
     */
    DatabaseTypeEnum getDBType();

    /**
     * 获取数据源
     *
     * @return
     */
    DruidDataSource getDataSource();

    /**
     * 关闭数据源
     */
    void closeDataSource();

    /**
     * 获得数据源hash值
     *
     * @return
     */
    int gethashcode();
}
