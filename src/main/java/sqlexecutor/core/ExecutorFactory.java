package sqlexecutor.core;


import sqlexecutor.pojo.Database;
import sqlexecutor.pojo.HbaseBo;
import sqlexecutor.pojo.HiveBo;
import sqlexecutor.pojo.RdbmsBo;
import sqlexecutor.pool.DBManager;
import sqlexecutor.pool.DBManagerFactory;

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
