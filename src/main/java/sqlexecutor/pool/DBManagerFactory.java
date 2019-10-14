package sqlexecutor.pool;

import sqlexecutor.pojo.Database;

/**
 * Database 数据源信息转为 DBManager 数据源
 *
 * @author cuitianyu
 */
public class DBManagerFactory {
    /**
     * Database 数据源信息转为 DBManager 数据源
     *
     * @param db
     * @return
     */
    public static DBManager getDbManager(Database db) {
        DBManager dbManager = DBManagerCache.getDb(db.getId());
        if (null == dbManager || dbManager.gethashcode() != db.hashCode()) {
            dbManager = new DBManagerImpl(db);
            DBManagerCache.putDb(db.getId(), dbManager);
        }
        return dbManager;
    }
}
