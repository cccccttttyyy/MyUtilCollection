package sqlexecutor.singlecore;


import sqlexecutor.pojo.Database;
import sqlexecutor.pojo.HbaseBo;
import sqlexecutor.pojo.HiveBo;
import sqlexecutor.pojo.RdbmsBo;

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
        if (database instanceof RdbmsBo) {
            return new RdbmsSqlExecutor((RdbmsBo) database);
        } else if (database instanceof HiveBo) {
            return new HiveSqlExecutor((HiveBo) database);
        } else if (database instanceof HbaseBo) {
            return new HbaseSqlExecutor((HbaseBo) database);
        } else {
            return null;
        }
    }
}
