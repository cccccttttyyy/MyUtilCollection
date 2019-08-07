package com.tsrain.sqlexecutor.pojo;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * sql执行器执行结果封装器
 *
 * @author cuitianyu
 */
public class ExecuteResultHandler implements ResultSetHandler<ExecuteResult> {
    private int cols;

    /**
     * 结果处理器 将结果封装在 ExecuteResult 中
     */
    @Override
    public ExecuteResult handle(ResultSet rs) throws SQLException {
        ExecuteResult executeResult = new ExecuteResult();
        executeResult.setType(ExecuteResultType.SELECT);
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        this.cols = cols;
        Table table = new Table();
        for (int i = 1; i <= cols; i++) {
            table.addColumn(meta.getColumnName(i));
        }
        // 填入row
        while (rs.next()) {
            Table.Row handleRow = this.handleRow(rs);
            table.addRows(handleRow);
        }
        executeResult.setTable(table);
        executeResult.setIsSucceed(true);
        return executeResult;
    }

    /**
     * 行处理器：将当前行转换为Row对象
     */
    protected Table.Row handleRow(ResultSet rs) throws SQLException {
        List<String> columns = new LinkedList<String>();
        for (int i = 0; i < cols; i++) {
            columns.add(String.valueOf(rs.getObject(i + 1)));
        }
        return new Table.Row(columns);
    }
}
