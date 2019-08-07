package com.tsrain.sqlexecutor.core;

import com.tsrain.sqlexecutor.pojo.ExecuteResult;
import com.tsrain.sqlexecutor.pojo.ExecuteResultHandler;
import com.tsrain.sqlexecutor.pojo.ExecuteResultType;
import com.tsrain.sqlexecutor.pool.DBManager;
import com.tsrain.sqlexecutor.runner.JDBCRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hbase执行器
 *
 * @author cuitianyu
 */
public class HbaseSqlExecutor extends AbstractExecutor implements SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(HbaseSqlExecutor.class);

    public HbaseSqlExecutor(DBManager dbManager) {
        runner = JDBCRunner.getRunner(db);
        this.db = dbManager;
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
                case SELECT:
                    er = runner.query(sql, new ExecuteResultHandler());
                    return er;
                case CREATE:
                case ALTER:
                case DROP:
                case UPSERT:
                case DELETE:
                case UNKNOW:
                default:
                    update = runner.update(sql);
                    er.setResultCode(update);
                    er.setIsSucceed(true);
                    return er;
            }
        } catch (Exception e) {
            er.setIsSucceed(false);
            er.setExceptionInfo(e.getMessage());
            return er;
        }
    }

    @Override
    public void setAutoCommit(Boolean flag) {
        log.info("Hbase Transactions are not supported untile now");

    }

    @Override
    public void commit() {
        log.info("Hbase Transactions are not supported untile now");

    }

}
