package com.tsrain.sqlexecutor.core;


import com.tsrain.sqlexecutor.pojo.Database;
import com.tsrain.sqlexecutor.pojo.HbaseBo;
import com.tsrain.sqlexecutor.pojo.HiveBo;
import com.tsrain.sqlexecutor.pojo.RdbmsBo;
import com.tsrain.sqlexecutor.pool.DBManager;
import com.tsrain.sqlexecutor.pool.DBManagerFactory;

/**
 * 执行器工厂
 *
 * @author cuitianyu
 */
public class ExecutorFactory {
    /**
     * 执行器优先从cache中获取连接池
     *
     * @param database
     * @return
     */
    public static SqlExecutor createExector(Database database) {
        DBManager dbManager = DBManagerFactory.getDbManager(database);
        if (database instanceof RdbmsBo) {
            return new RdbmsSqlExecutor(dbManager);
        } else if (database instanceof HiveBo) {
            return new HiveSqlExecutor(dbManager);
        } else if (database instanceof HbaseBo) {
            return new HbaseSqlExecutor(dbManager);
        } else {
            return null;
        }
    }
}
