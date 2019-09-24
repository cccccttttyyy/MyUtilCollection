package sqlexecutor.pojo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColumnListHandler implements ResultSetHandler<Map<String, String>> {

    @Override
    public Map<String, String> handle(ResultSet rs) throws SQLException {
        Map<String, String> resultMap = new HashMap<String, String>();
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            String columnName = meta.getColumnName(i);
            String columnType = meta.getColumnTypeName(i);
            resultMap.put(columnName, columnType.split(" ")[0]);
        }
        return resultMap;
    }

}
