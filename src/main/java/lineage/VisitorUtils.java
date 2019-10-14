package lineage;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import sqlexecutor.pojo.DataBaseDriverType;
import sqlexecutor.pojo.DatabaseTypeEnum;
import sqlexecutor.pojo.RdbmsBo;
import sqlexecutor.utils.RdbmsDBMetaUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author maolh
 * @date 2019/06/28
 */
public class VisitorUtils {

    public VisitorUtils() {
    }

    /**
     * 获取某张表的所有列 针对 select * 等
     *
     * @param database
     * @param srcTable
     * @return
     * @throws SQLException
     */
    public static List<String> getTableColumns(RdbmsBo database, Table srcTable) throws SQLException {

        DatabaseTypeEnum dbTypeEnum = DatabaseTypeEnum.getById(database.getDbase_type());
        DataBaseDriverType dataBaseType = DataBaseDriverType.getByName(dbTypeEnum.getName());
        String jdbcUrl = RdbmsDBMetaUtil.getJdbcUrl(database.getDbase_type(),
                database.getDbase_ip(), database.getDbase_port(),
                database.getDbase_name(), database.getDbase_user(), database.getDbase_passwd());
        List<Map<String, Object>> columnsDetailList = RdbmsDBMetaUtil.getTableColumns(dataBaseType, jdbcUrl, database.getDbase_user(),
                database.getDbase_passwd(), srcTable.getLabel());

        List<String> columns = new ArrayList<String>();
        for (Map<String, Object> map : columnsDetailList) {
            columns.add(map.get("name").toString());
        }

        return columns;
    }

    /**
     * 获取某张表的所有列 针对 select * 等
     *
     * @param database
     * @param srcTable
     * @return
     * @throws SQLException
     */
    public static List<String> getTableColumnsByName(RdbmsBo database, Table srcTable) throws SQLException {

        DatabaseTypeEnum dbTypeEnum = DatabaseTypeEnum.getById(database.getDbase_type());
        DataBaseDriverType dataBaseType = DataBaseDriverType.getByName(dbTypeEnum.getName());
        String jdbcUrl = RdbmsDBMetaUtil.getJdbcUrl(database.getDbase_type(),
                database.getDbase_ip(), database.getDbase_port(),
                database.getDbase_name(), database.getDbase_user(), database.getDbase_passwd());
        List<Map<String, Object>> columnsDetailList = RdbmsDBMetaUtil.getTableColumns(dataBaseType, jdbcUrl, database.getDbase_user(),
                database.getDbase_passwd(), srcTable.getName());

        List<String> columns = new ArrayList<String>();
        for (Map<String, Object> map : columnsDetailList) {
            columns.add(map.get("name").toString());
        }

        return columns;
    }


    /**
     * 判断某列是否从属与某张表
     *
     * @param database
     * @param srcTable
     * @param column
     * @return
     * @throws SQLException
     */
    public final static boolean getColumnFrom(RdbmsBo database, String srcTable, String column) throws SQLException {
        DatabaseTypeEnum dbTypeEnum = DatabaseTypeEnum.getById(database.getDbase_type());
        DataBaseDriverType dataBaseType = DataBaseDriverType.getByName(dbTypeEnum.getName());
        String jdbcUrl = RdbmsDBMetaUtil.getJdbcUrl(database.getDbase_type(),
                database.getDbase_ip(), database.getDbase_port(),
                database.getDbase_name(), database.getDbase_user(), database.getDbase_passwd());
        List<Map<String, Object>> columnsDetailList = RdbmsDBMetaUtil.getTableColumns(dataBaseType, jdbcUrl, database.getDbase_user(),
                database.getDbase_passwd(), srcTable);
        for (Map<String, Object> map : columnsDetailList) {
            String columnName = map.get("name").toString();
            if (columnName.equals(column)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 生成Table
     *
     * @param alias tableName 表别名
     * @param label tableLabel 表真名
     * @param type  LineageTableEnum枚举
     * @return
     */
    public final static Table createTable(String alias, String label, LineageTableEnum type) {
        Table table = new Table();
        if (null == alias) {
            table.setName(label);
            table.setId(label);
        } else {
            table.setName(alias);
            table.setId(alias);
        }
        if (type == LineageTableEnum.TEMP && table.getName().equals(LineageConstants.RESULT_OF_SELECT_QUERY)) {
            table.setId(VisitorUtils.generateRandomId());
        }
        table.setLabel(label);
        table.setType(type.getMsg());
        return table;
    }

    /**
     * 检查数据库中是否存在表
     *
     * @param database
     * @param table
     * @return
     */
    public final static boolean checkTableExistInDb(RdbmsBo database, String table) {

        DatabaseTypeEnum dbTypeEnum = DatabaseTypeEnum.getById(database.getDbase_type());
        DataBaseDriverType dataBaseType = DataBaseDriverType.getByName(dbTypeEnum.getName());
        String jdbcUrl = RdbmsDBMetaUtil.getJdbcUrl(database.getDbase_type(), database.getDbase_ip(),
                database.getDbase_port(), database.getDbase_name(), database.getDbase_user(),
                database.getDbase_passwd());
        Connection connection = RdbmsDBMetaUtil.getConnection(dataBaseType, jdbcUrl, database.getDbase_user(),
                database.getDbase_passwd());
        List<String> set = new ArrayList<>();
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = null;
            if (table.contains(".")) {
                String[] split = table.split("\\.");

                rs = meta.getTables(null, split[0].replaceAll("\"", ""), split[1].replaceAll("\"", ""), new String[]{"TABLE", "VIEW"});

                table = split[1];
            } else {
                rs = meta.getTables(null, null, table.replaceAll("\"", ""), new String[]{"TABLE", "VIEW"});
            }
            while (rs.next()) {
                set.add(rs.getString(3));
            }
            RdbmsDBMetaUtil.closeDBResources(rs, null, connection);
            return set.size() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成6位随机数,用于中间表的tableId
     *
     * @return
     */
    private final static String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     * 判断column是否属于这个query
     *
     * @param sqlExpr
     * @param column
     * @return
     */
    public static boolean isColumnBelongQuery(SQLSelectQuery sqlExpr, String column) {
        if (sqlExpr instanceof SQLSelectQueryBlock) {
            List<SQLSelectItem> selectList = ((SQLSelectQueryBlock) sqlExpr).getSelectList();
            for (SQLSelectItem sqlSelectItem : selectList) {
                String alias = sqlSelectItem.getAlias();
                if (null != alias) {
                    if (column.equals(alias)) {
                        return true;
                    }
                } else {
                    SQLExpr expr = sqlSelectItem.getExpr();
                    if (expr instanceof SQLPropertyExpr) {
                        if (column.equals(((SQLPropertyExpr) expr).getSimpleName())) {
                            return true;
                        }
                    } else if (expr instanceof SQLIdentifierExpr) {
                        if (column.equals(((SQLIdentifierExpr) expr).getSimpleName())) {
                            return true;
                        }
                    } else if (expr instanceof SQLQueryExpr) {
                        SQLQueryExpr sqlQuery = (SQLQueryExpr) expr;
                        return isColumnBelongQuery(sqlQuery.getSubQuery().getQuery(), column);
                    } else {
                        return false;
                    }
                }
            }
            return false;

        } else if (sqlExpr instanceof SQLUnionQuery) {
            SQLSelectQuery left = ((SQLUnionQuery) sqlExpr).getLeft();
            SQLSelectQuery right = ((SQLUnionQuery) sqlExpr).getRight();
            return isColumnBelongQuery(left, column) || isColumnBelongQuery(right, column);
        } else {
            return false;
        }
    }

    /**
     * 判断列书否属于TableSource
     *
     * @param database
     * @param source
     * @param item
     * @return
     */
    public static boolean isColumnBelongTableSource(RdbmsBo database, SQLTableSource source, SQLSelectItem item) {
        if (item.getExpr() instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) item.getExpr();
            SQLSelectItem itemleft = new SQLSelectItem(binaryOpExpr.getLeft());
            SQLSelectItem itemright = new SQLSelectItem(binaryOpExpr.getRight());
            boolean flag1 = isColumnBelongTableSource(database, source, itemleft);
            boolean flag2 = isColumnBelongTableSource(database, source, itemright);
            return flag1 || flag2;
        }
        if (source instanceof SQLJoinTableSource) {
            SQLJoinTableSource joinTableSource = (SQLJoinTableSource) source;
            SQLTableSource left = joinTableSource.getLeft();
            SQLTableSource right = joinTableSource.getRight();
            boolean flag1 = isColumnBelongTableSource(database, left, item);
            boolean flag2 = isColumnBelongTableSource(database, right, item);
            return flag1 || flag2;
        } else if (source instanceof SQLUnionQueryTableSource) {
            SQLUnionQueryTableSource queryTableSource = (SQLUnionQueryTableSource) source;
            boolean flag1 = VisitorUtils.isColumnBelongQuery(queryTableSource.getUnion().getLeft(), item.getExpr().toString());
            boolean flag2 = VisitorUtils.isColumnBelongQuery(queryTableSource.getUnion().getLeft(), item.getExpr().toString());
            return flag1 || flag2;
        }

        if (item.getExpr() instanceof SQLPropertyExpr) {
            //这里就可以根据别名或真实表名直接判断
            SQLPropertyExpr sqlPro = (SQLPropertyExpr) item.getExpr();
            String owner = sqlPro.getOwner().toString();
            if (source.getAlias() != null) {
                return owner.equals(source.getAlias());
            } else if (source instanceof SQLExprTableSource) {
                SQLExprTableSource leftTable = (SQLExprTableSource) source;
                return leftTable.getExpr().toString().equals(owner);

            }
        } else if (item.getExpr() instanceof SQLIdentifierExpr) {
            if (source instanceof SQLExprTableSource) {
                SQLExprTableSource lefttable = (SQLExprTableSource) source;
                try {
                    return VisitorUtils.getColumnFrom(database, lefttable.getExpr().toString(), item.getExpr().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (source instanceof SQLSubqueryTableSource) {
                SQLSubqueryTableSource leftQuery = (SQLSubqueryTableSource) source;
                return VisitorUtils.isColumnBelongQuery(leftQuery.getSelect().getQuery(), item.getExpr().toString());
            } else {
                return false;
            }

        } else if (item.getExpr() instanceof SQLAggregateExpr) {
            SQLAggregateExpr aggregateExpr = (SQLAggregateExpr) item.getExpr();
            List<SQLExpr> arguments = aggregateExpr.getArguments();
            boolean flag = true;
            for (SQLExpr sqlExpr : arguments) {
                flag = flag && isColumnBelongTableSource(database, source, new SQLSelectItem(sqlExpr));
            }
            return flag;
        } else if (item.getExpr() instanceof SQLMethodInvokeExpr) {
            SQLMethodInvokeExpr aggregateExpr = (SQLMethodInvokeExpr) item.getExpr();
            List<SQLExpr> arguments = aggregateExpr.getParameters();
            boolean flag = false;
            for (SQLExpr sqlExpr : arguments) {
                flag = flag || isColumnBelongTableSource(database, source, new SQLSelectItem(sqlExpr));
            }
            return flag;
        } else return item.getExpr() instanceof SQLAllColumnExpr;
        return false;

    }

    /**
     * 判断Select中是否存在item 为非真实列类型且没有别名如 max(age)
     * <p>用于create table view等 在未指定目标列情况下对select item是否合法的判断</p>
     *
     * @param query
     * @return
     */
    public static boolean isSelectContainsNoNameItem(SQLSelectQuery query) {
        if (query instanceof SQLSelectQueryBlock) {
            List<SQLSelectItem> selectItems = ((SQLSelectQueryBlock) query).getSelectList();
            for (SQLSelectItem sqlSelectItem : selectItems) {
                SQLExpr expr = sqlSelectItem.getExpr();
                if (expr instanceof SQLPropertyExpr || expr instanceof SQLAllColumnExpr || expr instanceof SQLIdentifierExpr) {
                } else {
                    if (null == sqlSelectItem.getAlias()) {
                        return true;
                    }
                }
            }
        } else if (query instanceof SQLUnionQuery) {
            SQLSelectQuery left = ((SQLUnionQuery) query).getLeft();
            return isSelectContainsNoNameItem(left);
        }
        return false;

    }

    /**
     * 判断item项是否是无血缘关系的类型
     *
     * @param expr
     * @return
     */
    public static boolean isItemBelongtoValue(SQLExpr expr) {
        return expr instanceof SQLValuableExpr || expr instanceof SQLNullExpr || expr instanceof SQLTimestampExpr || expr instanceof SQLHexExpr || expr instanceof SQLBooleanExpr || expr instanceof SQLBinaryExpr || expr instanceof SQLListExpr || expr instanceof SQLNumericLiteralExpr || expr instanceof SQLTextLiteralExpr;
    }
}
