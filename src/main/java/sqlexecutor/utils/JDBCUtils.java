package sqlexecutor.utils;

import sqlexecutor.pojo.*;
import sqlexecutor.runner.JDBCSingleRunner;
import sqlexecutor.singlecore.ExecutorFactory;
import sqlexecutor.singlecore.SqlExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JDBCUtils {
	public static String getJdbcUrl(int type, String host, int port, String dbaseName) {
		String jdbcUrl = null;
		DatabaseTypeEnum dbTypeEnum = DatabaseTypeEnum.getById(type);
		switch (dbTypeEnum) {
			case ORACLE:
				if (dbaseName.startsWith("/")) {
					jdbcUrl = "jdbc:oracle:thin:@//" + host + ":" + port + dbaseName;
				} else {
					jdbcUrl = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbaseName;
				}
				break;
			case MYSQL:
				jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbaseName;
				break;
			case SQLSERVER:
				jdbcUrl = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + dbaseName;
				break;
			case POSTGRESQL:
				jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbaseName;
				break;
			case HIVE:
				jdbcUrl = "jdbc:hive2://" + host + ":" + port + "/" + dbaseName;
				break;
			case HBASE:
				if (dbaseName == null || dbaseName.isEmpty()) {
					jdbcUrl = "jdbc:phoenix:" + host + ":" + port + ":/hbase";
				} else {
					jdbcUrl = "jdbc:phoenix:" + host + ":" + port + ":" + dbaseName;
				}
				break;
			case DB2:
				jdbcUrl = "jdbc:db2://" + host + ":" + port + "/" + dbaseName;
				break;
			default:
				break;
		}
		return jdbcUrl;
	}

	/**
	 * 获取模式
	 *
	 * @param database 数据源信息
	 * @return
	 */
	public static List<String> getSchemas(Database database) {
		List<String> resultList = new ArrayList<String>();
		DatabaseTypeEnum dbType = DatabaseTypeEnum.getById(database.getDbase_type());
		String sql = "";
		switch (dbType) {
			case ORACLE:
				sql = "SELECT distinct owner from all_tables  order by owner";
				break;
			case MYSQL:
				sql = "select schema_name from information_schema.schemata order by schema_name";
				break;
			case POSTGRESQL:
				sql = "select spcname from pg_tablespace order by spcname";
				break;
			case DB2:
				sql = "";
				break;
			case HIVE:
				sql = "show databases";
				break;
			case HBASE:
				sql = "select distinct TABLE_SCHEM from SYSTEM.\"CATALOG\"";
				break;
			default:
				break;

		}
		if (sql != null) {
			SqlExecutor SqlExector = ExecutorFactory.createExector(database);
			ExecuteResult eresult = SqlExector.execute(sql);
			Table table = eresult.getTable();
			if (table == null) {
			} else {
				if (table.getRows() != null) {
					table.getRows().forEach(R -> resultList.add(R.getColumns().get(0)));
				}
			}
			try {
				SqlExector.destroy();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * 获取表或视图名称列表
	 *
	 * @param database   数据源信息
	 * @param schemaName 模式名
	 * @param type       table||view
	 * @param query      表名或视图名称模糊搜索关键字
	 * @return 表或视图名称列表
	 */
	public static List<String> getTableOrViewList(Database database, String schemaName, String type, String query) {
		List<String> resultList = new ArrayList<String>();
		DatabaseTypeEnum dbType = DatabaseTypeEnum.getById(database.getDbase_type());
		String sql = "";
		switch (dbType) {
			case ORACLE:
				if (type.equals("view")) {
					sql = "select view_name from all_views where owner='" + schemaName + "' ";
					if (query != null && query.trim().isEmpty() == false) {
						sql = sql + "and view_name like '%" + query + "%' ";
					}
				} else {
					sql = "select table_name from all_tables where owner='" + schemaName + "' ";
					if (query != null && query.trim().isEmpty() == false) {
						sql = sql + " and table_name like '%" + query + "%' ";
					}
				}
				break;
			case MYSQL:
				if (type.equals("view")) {
					sql = "select table_name from information_schema.`VIEWS` where table_schema='" + schemaName + "'";
					if (query != null && query.isEmpty() == false) {
						sql = sql + " and table_name like '%" + query + "%' ";
					}
				} else {
					sql = "select table_name from information_schema.`TABLES` where table_schema='" + schemaName + "' ";
					if (query != null && query.isEmpty() == false) {
						sql = sql + " and table_name like '%" + query + "%' ";
					}
				}
				break;
			case POSTGRESQL:
				sql = "select spcname from pg_tablespace order by spcname";
				break;
			case DB2:
				sql = "";
				break;
			case HIVE:
				HiveBo hiveBo = (HiveBo) database;
				hiveBo.setDbase_name(schemaName);
				if (type.equals("view")) {
					sql = "";
				} else {
					sql = "show tables";
					if (query != null && query.isEmpty() == false) {
						sql = "show tables like '*" + query + "*'";
					}
				}
				break;
			case HBASE:
				if (type.equals("view")) {
					sql = "select distinct TABLE_NAME from SYSTEM.\"CATALOG\" where TABLE_TYPE = 'v' and TABLE_SCHEM ='" + schemaName + "'";
				} else {
					sql = "select distinct TABLE_NAME from SYSTEM.\"CATALOG\" where TABLE_TYPE = 'u' and TABLE_SCHEM ='" + schemaName + "'";
				}
				if (query != null && query.isEmpty() == false) {
					sql = sql + " and TABLE_NAME like '%" + query + "%' ";
				}
			default:
				break;

		}
		if (sql != null) {
			SqlExecutor SqlExector = ExecutorFactory.createExector(database);
			ExecuteResult eresult = SqlExector.execute(sql);
			Table table = eresult.getTable();
			if (table == null) {
			} else {
				if (table.getRows() != null) {
					table.getRows().forEach(R -> resultList.add(R.getColumns().get(0)));
				}
			}
			try {
				SqlExector.destroy();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * 获取table下的列
	 *
	 * @param database
	 * @param schemaName
	 * @param tableName
	 * @return
	 */
	public static Map<String, String> getAllColumns(Database database, String schemaName, String tableName) {
		SqlExecutor exector = ExecutorFactory.createExector(database);
		String sql = "select * from " + schemaName + "." + tableName + " where 1=2";
		JDBCSingleRunner runner = JDBCSingleRunner.getRunner();
		Map<String, String> resulMap = null;
		try {
			resulMap = runner.query(exector.getConn(), true, sql, new ColumnListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exector.destroy();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (null != resulMap) {
			return resulMap;
		} else {
			return null;
		}
	}
}
