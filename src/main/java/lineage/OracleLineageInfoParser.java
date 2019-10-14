package lineage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcUtils;
import sqlexecutor.pojo.Database;
import sqlexecutor.pojo.RdbmsBo;

import java.util.List;

public class OracleLineageInfoParser extends LineageInfoParser {


    @Override
    public void parseTable(String sql, Database db) {
        RdbmsBo dBo = (RdbmsBo) db;
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcUtils.ORACLE);
        super.lineageInfo = new LineageInfo();
        for (SQLStatement stmt : stmtList) {
            LineageInfo info = new LineageInfo(stmt.toString(), dBo);
            OracleLineageVisitor visitor = new OracleLineageVisitor(info);
            stmt.accept(visitor);
            if (info.isHasSyntaxError()) {
                info.clearRelation();
            }
            lineageInfo.add(info);
        }
    }
}
