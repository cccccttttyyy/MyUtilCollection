package com.tsrain.sqlexecutor.pool;

import com.tsrain.sqlexecutor.pojo.Database;

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
        DBManager dbManager = DBManagerCache.getDb(Integer.toString(db.getId()));
        if (null == dbManager || dbManager.gethashcode() != db.hashCode()) {
            dbManager = new DBManagerImpl(db);
            DBManagerCache.putDb(Integer.toString(db.getId()), dbManager);
        }
        return dbManager;
    }
}
