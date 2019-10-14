package lineage;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * OracleVisitor
 *
 * @author cuitianyu
 */
public class OracleLineageVisitor extends OracleASTVisitorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OracleLineageVisitor.class);
    private static int i = 1;
    private LineageInfo lineage;

    /**
     * 构造函数
     *
     * @param info
     */
    public OracleLineageVisitor(LineageInfo info) {
        this.lineage = info;
    }

    /**
     * select转换 completed
     *
     * @param x
     * @param dstTable
     * @param addColumns 是否需要加x中的列名添加到dstTable中
     * @return
     */
    private boolean transferVisitSelect(SQLSelectQuery query, Table dstTable) {
        if (query instanceof SQLSelectQueryBlock) {
            visit((SQLSelectQueryBlock) query, dstTable);
        } else if (query instanceof SQLUnionQuery) {
            visit((SQLUnionQuery) query, dstTable);
        }
        return true;
    }

    /**
     * 遍历完之后，所有的Select中的字段添加到目标表中，并添加映射关系
     *
     * @param x
     * @param dstTable
     * @param addColumns 是否将select中的字段添加到parent表中，false时只添加映射关系
     * @return
     */
    private boolean visit(SQLSelectQueryBlock x, Table dstTable) {
        List<SQLSelectItem> selectItems = x.getSelectList();
        SQLTableSource source = x.getFrom();
        if (source != null) {
            transferVisitTableSource(source, selectItems, dstTable);
        }
        return true;
    }

    /**
     * 肯定不添加字段到dstTable中，只添加映射关系
     *
     * @param x
     * @param dstTable
     * @return
     */

    private boolean visit(SQLUnionQuery x, Table dstTable) {
        Table unionTable = VisitorUtils.createTable(LineageConstants.RESULT_OF_SELECT_QUERY,
                LineageConstants.RESULT_OF_SELECT_QUERY, LineageTableEnum.TEMP);
        SQLSelectQuery left = x.getLeft();
        SQLSelectQuery right = x.getRight();
        transferVisitSelect(left, unionTable);
        //union 最终列明以左边第一个表为主 所以右边再经过一步unionRelation 进行列名转换
        Table rightUnionTable = VisitorUtils.createTable(LineageConstants.RESULT_OF_SELECT_QUERY,
                LineageConstants.RESULT_OF_SELECT_QUERY, LineageTableEnum.TEMP);
        transferVisitSelect(right, rightUnionTable);
        List<String> unionColumns = unionTable.getMeta().getColumns();
        List<String> rightUnionColumns = rightUnionTable.getMeta().getColumns();
        if (unionColumns.size() != rightUnionColumns.size()) {
            lineage.setHasSyntaxError(true);
            return false;
        }
        TableRelation unionRelation = new TableRelation(rightUnionTable, unionTable, 0);
        for (int i = 0; i < unionColumns.size(); i++) {
            unionRelation.addColumnMapping(rightUnionColumns.get(i), unionColumns.get(i));
        }
        lineage.AddRelation(unionRelation);

        TableRelation relation = new TableRelation(unionTable, dstTable, 0);
        List<String> columns = unionTable.getMeta().getColumns();
        for (String column : columns) {
            relation.getDstTable().addColumn(column);
            relation.addColumnMapping(column, column);
        }
        lineage.AddRelation(relation);
        return true;
    }

    /**
     * 因为不能有visit(SQLTableSource)接口，只能工具类进行转发 若后两个参数为null 则只在血缘中添加表不添加关系
     * 针对select中的from与目标表建立联系
     *
     * @param source
     */
    private void transferVisitTableSource(SQLTableSource source, List<SQLSelectItem> items, Table dstTable) {
        // 已经覆盖全部子类
        if (source instanceof SQLExprTableSource) {
            visit((SQLExprTableSource) source, items, dstTable);
        } else if (source instanceof SQLJoinTableSource) {
            visit((SQLJoinTableSource) source, items, dstTable);
        } else if (source instanceof SQLSubqueryTableSource) {
            visit((SQLSubqueryTableSource) source, items, dstTable);
        } else if (source instanceof SQLUnionQueryTableSource) {
            visit((SQLUnionQueryTableSource) source, items, dstTable);
        }
    }

    /**
     * @param x
     * @param items
     * @param dstTable
     * @param addColumns
     * @return
     */
    private boolean visit(SQLExprTableSource x, List<SQLSelectItem> items, Table dstTable) {
        Table table = VisitorUtils.createTable(x.getAlias(), x.getExpr().toString(), LineageTableEnum.TABLE);
        lineage.setHasSyntaxError(!VisitorUtils.checkTableExistInDb(lineage.getDatabase(), table.getLabel()));
        if (null == items && null == dstTable) {
            lineage.addTable(table);
        } else {
            TableRelation relation = new TableRelation(table, dstTable, 0);
            for (SQLSelectItem item : items) {
                transferVisitItem(item.getExpr(), relation, item.getAlias());
            }
            if (null != relation.getColumnMapping()) {
                lineage.AddRelation(relation);
            }
        }

        return true;
    }

    private boolean visit(SQLJoinTableSource x, List<SQLSelectItem> items, Table dstTable) {

        SQLTableSource left = x.getLeft();
        SQLTableSource right = x.getRight();
        // 判断item属于哪个里面
        List<SQLSelectItem> leftItems = new LinkedList<SQLSelectItem>();
        List<SQLSelectItem> rightItems = new LinkedList<SQLSelectItem>();
        for (SQLSelectItem sqlSelectItem : items) {
            SQLExpr expr = sqlSelectItem.getExpr();
            if (expr instanceof SQLQueryExpr) {
                // 查询类型的item 既不属于左又不属于右 所以单独处理
                TableRelation relation = new TableRelation(new Table(), dstTable);
                visit((SQLQueryExpr) sqlSelectItem.getExpr(), relation, sqlSelectItem.getAlias());
            } else if (VisitorUtils.isItemBelongtoValue(expr)) {
            } else {
                boolean belongLeft = VisitorUtils.isColumnBelongTableSource(lineage.getDatabase(), left, sqlSelectItem);
                boolean belongRight = VisitorUtils.isColumnBelongTableSource(lineage.getDatabase(), right, sqlSelectItem);
                if (belongLeft) {
                    leftItems.add(sqlSelectItem);
                    transferVisitTableSource(left, leftItems, dstTable);
                    leftItems.clear();
                }
                if (belongRight) {
                    rightItems.add(sqlSelectItem);
                    transferVisitTableSource(right, rightItems, dstTable);
                    rightItems.clear();
                }
                if (!(belongLeft || belongRight)) {
                    lineage.setHasSyntaxError(true);
                    return false;
                }
            }

        }
//		transferVisitTableSource(left, leftItems, dstTable); 为什么改这里呢 是为了保证 dstTable表中顺序不变 
//		transferVisitTableSource(right, rightItems, dstTable);
        return true;
    }

    private boolean visit(SQLSubqueryTableSource x, List<SQLSelectItem> items, Table dstTable) {
        Table table = VisitorUtils.createTable(x.getAlias(), LineageConstants.RESULT_OF_SELECT_QUERY,
                LineageTableEnum.TEMP);
        transferVisitSelect(x.getSelect().getQuery(), table);

        // 在这里更新relation.......
        if (null == items && null == dstTable) {
            lineage.addTable(table);
        } else {
            TableRelation relation = new TableRelation(table, dstTable, 0);
            for (SQLSelectItem item : items) {
                transferVisitItem(item.getExpr(), relation, item.getAlias());
            }
            if (null != relation.getColumnMapping()) {
                lineage.AddRelation(relation);
            }
        }
        return true;
    }

    /**
     * union类型的select的最终列，是第一个select的列的名字 union相关的表，需要是由相同的列的个数
     *
     * @param x
     * @param items
     * @param dstTable
     * @return
     */
    private boolean visit(SQLUnionQueryTableSource x, List<SQLSelectItem> items, Table dstTable) {
        SQLUnionQuery unionQuery = x.getUnion();
        Table unionTable = VisitorUtils.createTable(LineageConstants.RESULT_OF_SELECT_QUERY,
                LineageConstants.RESULT_OF_SELECT_QUERY, LineageTableEnum.TEMP);
        SQLSelectQuery left = unionQuery.getLeft();
        SQLSelectQuery right = unionQuery.getRight();
        transferVisitSelect(left, unionTable);
        transferVisitSelect(right, unionTable);
        TableRelation relation = new TableRelation(unionTable, dstTable, 0);
        for (SQLSelectItem sqlSelectItem : items) {
            transferVisitItem(sqlSelectItem.getExpr(), relation, sqlSelectItem.getAlias());
        }
        lineage.AddRelation(relation);
        return true;
    }

    /**
     * 访问SQLSelectItem并补充关系中的信息
     * 针对其中的item
     *
     * @param x
     * @param srcTable
     * @param dstTable
     * @return
     */
    private boolean transferVisitItem(SQLExpr expr, TableRelation relation, String alias) {
        if (expr instanceof SQLBinaryOpExpr) {
            return visit((SQLBinaryOpExpr) expr, relation, alias);
        } else if (expr instanceof SQLAggregateExpr) {
            return visit((SQLAggregateExpr) expr, relation, alias);
        } else if (expr instanceof SQLMethodInvokeExpr) {
            return visit((SQLMethodInvokeExpr) expr, relation, alias);
        } else if (expr instanceof SQLPropertyExpr) {
            return visit((SQLPropertyExpr) expr, relation, alias);
        } else if (expr instanceof SQLAllColumnExpr) {
            return visit((SQLAllColumnExpr) expr, relation);
        } else if (expr instanceof SQLIdentifierExpr) {
            return visit((SQLIdentifierExpr) expr, relation, alias);
        } else if (expr instanceof SQLQueryExpr) {
            return visit((SQLQueryExpr) expr, relation, alias);
        } else if (expr instanceof SQLCaseExpr) {
            return visit((SQLCaseExpr) expr, relation, alias);
        } else if (VisitorUtils.isItemBelongtoValue(expr)) {
            return true;
        }
        lineage.setHasSyntaxError(true);
        return false;
    }

    private boolean visit(SQLCaseExpr expr, TableRelation relation, String alias) {
        Table srcTable = relation.getSrcTable();
        Table dstTable = relation.getDstTable();
        SQLCaseExpr caseExpr = expr;
        if (null == caseExpr.getValueExpr()) {
            return false;
        }
        String column = caseExpr.getValueExpr().toString();

        if (alias == null) {
            alias = column;
        }
        srcTable.addColumn(column);
        dstTable.addColumn(alias);
        relation.addColumnMapping(column, alias);
        return true;
    }

    /**
     * id
     *
     * @param x
     * @param relation
     * @param alias
     * @return
     */
    private boolean visit(SQLIdentifierExpr x, TableRelation relation, String alias) {
        Table srcTable = relation.getSrcTable();
        Table dstTable = relation.getDstTable();
        String column = x.getName();
        if (alias == null) {
            alias = column;
        }
        srcTable.addColumn(column);
        dstTable.addColumn(alias);
        relation.addColumnMapping(column, alias);
        return true;
    }

    /**
     * a.id
     *
     * @param x
     * @param relation
     * @param alias
     * @return
     */
    private boolean visit(SQLPropertyExpr x, TableRelation relation, String alias) {
        Table srcTable = relation.getSrcTable();
        Table dstTable = relation.getDstTable();
        String fullColumn = x.toString();
        String owner = x.getOwner().toString();
        String column = x.getName();
        if (owner.contentEquals(srcTable.getName())) {
            if (column.equalsIgnoreCase("*")) {
                return visit(new SQLAllColumnExpr(), relation);
            } else {
                srcTable.addColumn(fullColumn);
                if (alias == null) {
                    alias = fullColumn;
                }
                dstTable.addColumn(alias);
                relation.addColumnMapping(fullColumn, alias);
            }
        }
        return true;
    }

    /**
     * select *
     *
     * @param x
     * @param relation
     * @param alias
     * @return
     */
    private boolean visit(SQLAllColumnExpr x, TableRelation relation) {
        Table srcTable = relation.getSrcTable();
        Table dstTable = relation.getDstTable();
        List<String> srcColumns = srcTable.getMeta().getColumns();
        /**
         * 若不是中间表则需要查列
         */
        if (srcTable.getType() != LineageTableEnum.TEMP.getMsg()) {
            srcColumns.add("-");
            if (lineage.getDatabase() != null) {
                try {
                    srcColumns = VisitorUtils.getTableColumns(lineage.getDatabase(), srcTable);
                } catch (Exception e) {
                    LOGGER.error("连接数据库查询表的字段出错", e.getMessage());
                    lineage.setHasSyntaxError(true);
                    return false;
                }
            } else {
                srcColumns.add("*");
            }
        }
        for (String column : srcColumns) {
            dstTable.addColumn(column);
            relation.addColumnMapping(column, column);
        }
        return true;
    }

    /**
     * AVG(price)
     *
     * @param x
     * @param relation
     * @param alias
     * @return
     */
    private boolean visit(SQLAggregateExpr x, TableRelation relation, String alias) {
        if (alias == null) {
            alias = x.getMethodName() + (i++);
        }
        List<SQLExpr> exprs = x.getArguments();
        if (exprs.size() <= 0) {
            String methodName = x.getMethodName();
            if (methodName.equals("COUNT") | methodName.equals("SUM")) {
                relation.getSrcTable().addColumn("*");
                relation.getDstTable().addColumn(alias);
                relation.addColumnMapping("*", alias);
            }
            return true;
        }

        for (SQLExpr expr : exprs) {
            if (expr instanceof SQLBinaryOpExpr) {
                visit((SQLBinaryOpExpr) expr, relation, alias);
            } else if (expr instanceof SQLIdentifierExpr) {
                visit((SQLIdentifierExpr) expr, relation, alias);
            } else if (expr instanceof SQLAggregateExpr) {
                visit((SQLAggregateExpr) expr, relation, alias);
            } else if (expr instanceof SQLPropertyExpr) {
                visit((SQLPropertyExpr) expr, relation, alias);
            } else if (expr instanceof SQLIntegerExpr || expr instanceof SQLAllColumnExpr) {
                String methodName = x.getMethodName();
                if (methodName.equals("COUNT") | methodName.equals("SUM")) {
                    relation.getSrcTable().addColumn("*");
                    relation.getDstTable().addColumn(alias);
                    relation.addColumnMapping("*", alias);
                }
            }
        }
        return true;
    }

    private boolean visit(SQLMethodInvokeExpr x, TableRelation relation, String alias) {
        if (alias == null) {
            alias = x.getMethodName() + (i++);
        }
        List<SQLExpr> exprs = x.getParameters();
        for (SQLExpr expr : exprs) {
            if (expr instanceof SQLBinaryOpExpr) {
                visit((SQLBinaryOpExpr) expr, relation, alias);
            } else if (expr instanceof SQLIdentifierExpr) {
                visit((SQLIdentifierExpr) expr, relation, alias);
            } else if (expr instanceof SQLAggregateExpr) {
                visit((SQLAggregateExpr) expr, relation, alias);
            } else if (expr instanceof SQLMethodInvokeExpr) {
                visit((SQLMethodInvokeExpr) expr, relation, alias);
            } else if (expr instanceof SQLPropertyExpr) {
                visit((SQLPropertyExpr) expr, relation, alias);
            } else if (expr instanceof SQLIntegerExpr) {
                if (exprs.size() == 1) {
                    relation.getSrcTable().addColumn("*");
                    relation.getDstTable().addColumn(alias);
                    relation.addColumnMapping("*", alias);
                }
            }
        }
        return true;
    }

    /**
     * ID = 3
     *
     * @param x
     * @param relation
     * @param alias    该表达式所对应的列名，可能为空
     * @return
     */
    private boolean visit(SQLBinaryOpExpr x, TableRelation relation, String alias) {
        SQLExpr left = x.getLeft();
        SQLExpr right = x.getRight();
        transferVisitItem(left, relation, alias);
        transferVisitItem(right, relation, alias);
        return true;
    }

    /*
     * a=(select ...)
     */
    private boolean visit(SQLQueryExpr x, TableRelation relation, String alias) {
        Table curTable = VisitorUtils.createTable(LineageConstants.RESULT_OF_SELECT_QUERY,
                LineageConstants.RESULT_OF_SELECT_QUERY, LineageTableEnum.TEMP);
        transferVisitSelect(x.getSubQuery().getQuery(), curTable);
        List<String> srcColumns = curTable.getMeta().getColumns();
        TableRelation relation2 = new TableRelation(curTable, relation.getDstTable(), 0);
        relation2.getSrcTable().addColumn(srcColumns.get(1));
        relation2.getDstTable().addColumn(alias);
        relation2.addColumnMapping(srcColumns.get(1), alias);
        lineage.AddRelation(relation2);
        return true;
    }

    /**
     * 这里只负责 对lineage中添加表 具体关系在item中建立
     *
     * @param tableSource
     * @param items       update的item项
     * @param dstTable    若为null 说明为update最顶层表
     */
    private void transferVisitUpdateTableSource(SQLTableSource tableSource) {
        if (tableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource jointableSource = (SQLJoinTableSource) tableSource;
            SQLTableSource left = jointableSource.getLeft();
            SQLTableSource right = jointableSource.getRight();
            transferVisitUpdateTableSource(left);
            transferVisitUpdateTableSource(right);
        } else if (tableSource instanceof SQLSubqueryTableSource) {
            SQLSubqueryTableSource dstTable = (SQLSubqueryTableSource) tableSource;
            Table curTable = VisitorUtils.createTable(tableSource.getAlias(), LineageConstants.RESULT_OF_SELECT_QUERY,
                    LineageTableEnum.TEMP);
            transferVisitSelect(dstTable.getSelect().getQuery(), curTable);
            lineage.addTable(curTable);
        } else if (tableSource instanceof SQLExprTableSource) {
            SQLExprTableSource sqlExprTableSource = (SQLExprTableSource) tableSource;
            Table curTable = VisitorUtils.createTable(sqlExprTableSource.getAlias(),
                    sqlExprTableSource.getExpr().toString(), LineageTableEnum.DSTTABLE);
            lineage.setHasSyntaxError(!VisitorUtils.checkTableExistInDb(lineage.getDatabase(), curTable.getLabel()));
            lineage.addTable(curTable);
        }
    }

    /**
     * 对item右部进行解析
     *
     * @param right
     * @param dstTable
     * @param dstColumn
     * @return
     */
    private boolean transferVisitUpdateItem(SQLExpr right, Table dstTable, List<String> dstColumns) {
        Table srcTable = null;
        if (right instanceof SQLAggregateExpr) {
            SQLAggregateExpr rightAgg = (SQLAggregateExpr) right;
            List<SQLExpr> arguments = rightAgg.getArguments();
            for (SQLExpr sqlExpr : arguments) {
                transferVisitUpdateItem(sqlExpr, dstTable, dstColumns);
            }
            return true;
        } else if (right instanceof SQLPropertyExpr) {
            SQLPropertyExpr x = (SQLPropertyExpr) right;
            String owner = x.getOwner().toString();
            String fullColumn = x.toString();
            String column = x.getName();
            srcTable = lineage.getTables().get(owner);
            if (null == srcTable) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            TableRelation relation = new TableRelation(srcTable, dstTable, 0);
            if (column.equalsIgnoreCase("*")) {
                return visit(new SQLAllColumnExpr(), relation);
            } else {
                srcTable.addColumn(fullColumn);
                String dstColumn = dstColumns.get(0);
                if (dstColumn == null) {
                    dstColumn = fullColumn;
                }
                dstTable.addColumn(dstColumn);
                relation.addColumnMapping(fullColumn, dstColumn);
            }
            lineage.AddRelation(relation);
            return true;
        } else if (right instanceof SQLIdentifierExpr) {
            srcTable = lineage.getFirstTable();
            if (null == srcTable) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            TableRelation relation = new TableRelation(srcTable, dstTable, 0);
            String column = ((SQLIdentifierExpr) right).getName();
            String dstColumn = dstColumns.get(0);
            if (dstColumn == null) {
                dstColumn = column;
            }
            srcTable.addColumn(column);
            dstTable.addColumn(dstColumn);
            relation.addColumnMapping(column, dstColumn);
            lineage.AddRelation(relation);
            return true;
        } else if (right instanceof SQLQueryExpr) {
            SQLQueryExpr queryExpr = (SQLQueryExpr) right;
            Table tempTable = VisitorUtils.createTable(LineageConstants.RESULT_OF_SELECT_QUERY,
                    LineageConstants.RESULT_OF_SELECT_QUERY, LineageTableEnum.TEMP);
            transferVisitSelect(queryExpr.getSubQuery().getQuery(), tempTable);
            TableRelation relation = new TableRelation(tempTable, dstTable, 0);
            List<String> tempcolumns = tempTable.getMeta().getColumns();
            for (int i = 0; i < dstColumns.size(); i++) {
                dstTable.addColumn(dstColumns.get(i));
                relation.addColumnMapping(tempcolumns.get(i + 1), dstColumns.get(i));
            }
            lineage.AddRelation(relation);
            return true;
        } else if (right instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr bin = (SQLBinaryOpExpr) right;
            SQLExpr leftBin = bin.getLeft();
            SQLExpr rightBin = bin.getRight();
            transferVisitUpdateItem(leftBin, dstTable, dstColumns);
            transferVisitUpdateItem(rightBin, dstTable, dstColumns);
            return true;
        } else if (VisitorUtils.isItemBelongtoValue(right)) {
            return true;
        }
        lineage.setHasSyntaxError(true);
        return false;
    }

    /**
     * 访问SQLUpdateSetItem并补充关系中的信息
     *
     * @param x
     * @param relation
     * @return
     */
    @Override
    public boolean visit(SQLUpdateSetItem x) {
        SQLExpr left = x.getColumn();
        SQLExpr right = x.getValue();
        Table dstTable = null;
        List<String> dstColumns = new LinkedList<String>();
        if (left instanceof SQLIdentifierExpr) {
            dstTable = lineage.getFirstTable();
            String dstColumn = ((SQLIdentifierExpr) left).getName();
            dstColumns.add(dstColumn);
        } else if (left instanceof SQLPropertyExpr) {
            String owner = ((SQLPropertyExpr) left).getOwner().toString();
            String columnName = ((SQLPropertyExpr) left).getName();
            dstTable = lineage.getTables().get(owner);
            dstColumns.add(columnName);
        } else if (left instanceof SQLListExpr) {
            SQLListExpr leftList = (SQLListExpr) left;
            List<SQLExpr> items = leftList.getItems();
            dstTable = lineage.getFirstTable();
            for (SQLExpr sqlExpr : items) {
                if (sqlExpr instanceof SQLIdentifierExpr) {
                    String dstColumn = ((SQLIdentifierExpr) sqlExpr).getName();
                    dstColumns.add(dstColumn);
                }
            }
        }
        if (null == dstTable || dstColumns.size() <= 0) {
            lineage.setHasSyntaxError(true);
            return false;
        } else {
            return transferVisitUpdateItem(right, dstTable, dstColumns);
        }
    }

    @Override
    public boolean visit(OracleInsertStatement x) {
        //获取需要插入数据的目标表
        SQLExprTableSource tableSource = x.getTableSource();
        //创建目标表
        Table dstTable = VisitorUtils.createTable(tableSource.getAlias(), tableSource.getExpr().toString(), LineageTableEnum.DSTTABLE);
        lineage.setHasSyntaxError(!VisitorUtils.checkTableExistInDb(lineage.getDatabase(), dstTable.getLabel()));

        //获取需要插入的列名
        List<SQLExpr> columns = x.getColumns();
        // 获取查询语句
        SQLSelect select = x.getQuery();
        //如果查询语句为null，并且插入的数据集是空，判断为插入的数据是一个select union语句
        List<String> dstColumns = new ArrayList<String>();
        if (null != select) {
            if (columns.size() > 0) {
                dstColumns = columns.stream().map(column -> column.toString()).collect(Collectors.toList());
            } else {
                try {
                    dstColumns = VisitorUtils.getTableColumns(lineage.getDatabase(), dstTable);
                } catch (SQLException e) {
                    lineage.setHasSyntaxError(true);
                    e.printStackTrace();
                    return false;
                }
            }
        } else if (columns.size() > 0 && columns.get(0) instanceof SQLQueryExpr) {
            select = ((SQLQueryExpr) columns.get(0)).getSubQuery();
            try {
                dstColumns = VisitorUtils.getTableColumns(lineage.getDatabase(), dstTable);
            } catch (SQLException e) {
                lineage.setHasSyntaxError(true);
                e.printStackTrace();
                return false;
            }
        } else {
            //无血缘关系
            return true;
        }
        return conSelectAndDst(select.getQuery(), dstTable, dstColumns);
    }

    @Override
    public boolean visit(SQLMergeStatement x) {
        Table dstTable = VisitorUtils.createTable(x.getAlias(), x.getInto().toString(), LineageTableEnum.DSTTABLE);
        lineage.setHasSyntaxError(!VisitorUtils.checkTableExistInDb(lineage.getDatabase(), dstTable.getLabel()));
        transferVisitUpdateTableSource(x.getUsing());
        lineage.addTable(dstTable);
        SQLMergeStatement.MergeInsertClause insertClause = x.getInsertClause();
        SQLMergeStatement.MergeUpdateClause updateClause = x.getUpdateClause();
        if (null != insertClause) {
            List<SQLExpr> columns = insertClause.getColumns();
            List<String> dstColumns = null;
            boolean dstempty = columns.size() <= 0;
            if (dstempty) {
                try {
                    dstColumns = VisitorUtils.getTableColumns(lineage.getDatabase(), dstTable);
                } catch (SQLException e) {
                    e.printStackTrace();
                    lineage.setHasSyntaxError(true);
                    return false;
                }
            } else {
                dstColumns = columns.stream().map(column -> column.toString()).collect(Collectors.toList());
            }
            List<SQLExpr> values = insertClause.getValues();
            if (dstColumns.size() != values.size()) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            for (int i = 0; i < values.size(); i++) {
                Table srcTable = null;
                if (values.get(i) instanceof SQLPropertyExpr) {
                    SQLPropertyExpr propertyExpr = (SQLPropertyExpr) values.get(i);
                    srcTable = lineage.getTables().get(propertyExpr.getOwner().toString());
                } else if (values.get(i) instanceof SQLIdentifierExpr) {
                    srcTable = lineage.getFirstTable();
                } else if (VisitorUtils.isItemBelongtoValue(values.get(i))) {
                    continue;
                } else {
                    lineage.setHasSyntaxError(true);
                    return false;
                }

                TableRelation relation = new TableRelation(srcTable, dstTable, 0);
                transferVisitItem(values.get(i), relation, dstColumns.get(i));
                lineage.AddRelation(relation);
            }

        }
        if (null != updateClause) {
            List<SQLUpdateSetItem> items = updateClause.getItems();
            for (SQLUpdateSetItem sqlUpdateSetItem : items) {
                visit(sqlUpdateSetItem);
            }
        }
        return true;
    }

    @Override
    public boolean visit(OracleCreateTableStatement x) {
        SQLExprTableSource tableSource = x.getTableSource();
        Table dstTable = VisitorUtils.createTable(tableSource.getAlias(), tableSource.getExpr().toString(),
                LineageTableEnum.DSTTABLE);
        SQLSelect select = x.getSelect();
        if (select != null) {
            List<SQLTableElement> elementList = x.getTableElementList();
            if (elementList.size() > 0) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            SQLSelectQuery query = select.getQuery();
            if (VisitorUtils.isSelectContainsNoNameItem(query)) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            return conSelectAndDst(query, dstTable, new ArrayList<String>());
        } else {
            return true;
        }
    }

    @Override
    public boolean visit(OracleUpdateStatement x) {
        transferVisitUpdateTableSource(x.getTableSource());
        return true;
    }

    @Override
    public boolean visit(SQLCreateViewStatement x) {
        Table dstTable = VisitorUtils.createTable(x.getName().getSimpleName(), x.getName().getSimpleName(),
                LineageTableEnum.DSTTABLE);
        SQLSelect select = x.getSubQuery();
        if (select != null) {
            SQLSelectQuery query = select.getQuery();
            List<String> dstColumns = x.getColumns().stream().map(column -> column.toString()).collect(Collectors.toList());
            if (dstColumns.size() == 0 && VisitorUtils.isSelectContainsNoNameItem(query)) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            return conSelectAndDst(query, dstTable, dstColumns);
        } else {
            //没有血缘关系
            return true;
        }
    }

    /**
     * 直接visit select finish
     * 为方便测试暂时算作DSTTABLE 之后需改为TEMP
     */
    @Override
    public boolean visit(SQLSelectStatement astNode) {
        Table dstTable = VisitorUtils.createTable(LineageConstants.RESULT_OF_SELECT_QUERY,
                LineageConstants.RESULT_OF_SELECT_QUERY, LineageTableEnum.TEMP);
        transferVisitSelect(astNode.getSelect().getQuery(), dstTable);
        return true;
    }

    /**
     * 建立query与目标表的关系
     *
     * @param query      需要与目标表建立血缘关系的查询语句
     * @param dstTable   目标表
     * @param dstColumns 传过来的是不带 - 的目标列
     * @return
     */
    private boolean conSelectAndDst(SQLSelectQuery query, Table dstTable, List<String> dstColumns) {
        //对query解析血缘关系
        Table queryTable = VisitorUtils.createTable(LineageConstants.RESULT_OF_SELECT_QUERY,
                LineageConstants.RESULT_OF_SELECT_QUERY, LineageTableEnum.TEMP);
        transferVisitSelect(query, queryTable);
        TableRelation relation = new TableRelation(queryTable, dstTable, 0);
        List<String> queryColumns = new ArrayList<String>(queryTable.getMeta().getColumns());

        //将dstColumn拼装成带-可用的目标列
        if (dstColumns.size() == 0) {
            dstColumns.addAll(queryColumns);
        } else {
            if (!dstColumns.contains("-")) {
                dstColumns.add(0, "-");
            }
        }
        //拿到select Item
        List<SQLSelectItem> selectItems = new ArrayList<SQLSelectItem>();
        if (query instanceof SQLSelectQueryBlock) {
            selectItems = ((SQLSelectQueryBlock) query).getSelectList();
        } else if (query instanceof SQLUnionQuery) {
            SQLSelectQuery left = ((SQLUnionQuery) query).getLeft();
            if (left instanceof SQLSelectQueryBlock) {
                selectItems = ((SQLSelectQueryBlock) left).getSelectList();
            } else {
                //目前判断left只可能是SQLSelectQueryBlock
                return false;
            }
        } else {
            lineage.setHasSyntaxError(true);
            return false;
        }

        boolean goodSelectItem = true;
        if (selectItems.size() > 0) {
            if (selectItems.get(0).getExpr() instanceof SQLAllColumnExpr) {
                goodSelectItem = false;
            } else if (selectItems.get(0).getExpr() instanceof SQLPropertyExpr) {
                String item = selectItems.get(0).getExpr().toString().split("\\.")[1];
                if (item.contentEquals("*")) {
                    goodSelectItem = false;
                }
            }
        } else {
            goodSelectItem = false;
        }

        //建立最终关系
        if (goodSelectItem) {
            if (selectItems.size() + 1 != dstColumns.size()) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            transferVisitItem(new SQLIdentifierExpr("-"), relation, "-");
            for (int i = 0; i < selectItems.size(); i++) {
                String itemName = selectItems.get(i).getAlias();
                if (null == itemName) {
                    itemName = selectItems.get(i).getExpr().toString();
                }
                if (queryTable.existColumn(itemName)) {
                    transferVisitItem(new SQLIdentifierExpr(itemName), relation, dstColumns.get(i + 1));
                }
            }
        } else {
            if (dstColumns.size() != queryColumns.size()) {
                lineage.setHasSyntaxError(true);
                return false;
            }
            for (int i = 0; i < dstColumns.size(); i++) {
                transferVisitItem(new SQLIdentifierExpr(queryColumns.get(i)), relation, dstColumns.get(i));
            }
        }
        lineage.AddRelation(relation);
        return true;
    }
}
