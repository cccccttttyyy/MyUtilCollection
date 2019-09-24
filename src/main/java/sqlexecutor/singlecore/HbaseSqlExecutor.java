package sqlexecutor.singlecore;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqlexecutor.pojo.*;
import sqlexecutor.runner.JDBCSingleRunner;
import sqlexecutor.utils.JDBCUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Hbase执行器
 * @author cuitianyu
 *
 */
public class HbaseSqlExecutor extends AbstractExecutor implements SqlExecutor {

	private static final Logger log = LoggerFactory.getLogger(HbaseSqlExecutor.class);
	private HbaseBo bo;

	public HbaseSqlExecutor(HbaseBo db) {
		runner = JDBCSingleRunner.getRunner();
		this.bo = db;
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
					er = runner.query(getConn(), true, sql, new ExecuteResultHandler());
					return er;
				case CREATE:
				case ALTER:
				case DROP:
				case UPSERT:
				case DELETE:
				case UNKNOW:
				default:
					update = runner.update(getConn(), true, sql);
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

	@Override
	public Connection getConn() throws SQLException {
		String jdbcUrl = JDBCUtils.getJdbcUrl(bo.getDbase_type(), bo.getZk_quorum(), bo.getZk_port(), bo.getHbase_rootdir());
		if (bo.isKerberos_enable()) {
//			System.setProperty("java.security.krb5.conf", "conf/krb5.conf");//// linux 环境会默认读取/etc/krb.cnf文件
			Configuration conf = HBaseConfiguration.create();
			conf.set("hadoop.security.authentication", "Kerberos");
			UserGroupInformation.setConfiguration(conf);
			try {
				UserGroupInformation.loginUserFromKeytab(bo.getKerberos_principal(), bo.getKerberos_keytab_file_path());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jdbcUrl += ":" + bo.getKerberos_principal() + ":" + bo.getKerberos_keytab_file_path();
		}
        String driverName = DataBaseDriverType.getById(bo.getDbase_type()).getDriverClassName();
		try {
			Class.forName(driverName);
			return DriverManager.getConnection(jdbcUrl);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean destroy() throws SQLException {
		close();
		runner = null;
		bo = null;
		return true;
	}

	public void rollback() {
		// TODO Auto-generated method stub

	}

}
