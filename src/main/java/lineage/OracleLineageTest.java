package lineage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.util.JdbcUtils;
import junit.framework.TestCase;
import sqlexecutor.pojo.DatabaseTypeEnum;
import sqlexecutor.pojo.RdbmsBo;

import java.util.List;

public class OracleLineageTest extends TestCase {

    public static void test_parseSql() {
        String sql = "SELECT  \r\n" +
                "CASE WHEN salary <= 500 THEN '1'  \r\n" +
                "WHEN salary > 500 AND salary <= 600  THEN '2'  \r\n" +
                "WHEN salary > 600 AND salary <= 800  THEN '3'  \r\n" +
                "WHEN salary > 800 AND salary <= 1000 THEN '4'  \r\n" +
                "ELSE NULL END salary_class, -- 别名命名\r\n" +
                "COUNT(*)  \r\n" +
                "FROM    Table_A  \r\n" +
                "GROUP BY  \r\n" +
                "CASE WHEN salary <= 500 THEN '1'  \r\n" +
                "WHEN salary > 500 AND salary <= 600  THEN '2'  \r\n" +
                "WHEN salary > 600 AND salary <= 800  THEN '3'  \r\n" +
                "WHEN salary > 800 AND salary <= 1000 THEN '4'  \r\n" +
                "ELSE NULL END;";
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcUtils.ORACLE);
        for (SQLStatement stmt : stmtList) {
            StringBuffer bf = new StringBuffer();
            SQLASTOutputVisitor statVisitor = SQLUtils.createFormatOutputVisitor(bf, stmtList, JdbcUtils.ORACLE);

            stmt.accept(statVisitor);
            System.out.println(bf);
        }
        return;
    }

    public static void test_parseSqlWithLineage() {
        String sql = "update A_CTY_GOODSTUDENT\r\n" +
                "set NAME= (select NAME from  A_CTY_CHSTUDENT where A_CTY_GOODSTUDENT.ID=  A_CTY_CHSTUDENT.ID ) , \r\n" +
                "    (ID,RANK) =(ID+1,RANK+1)\r\n" +
                "    where  A_CTY_GOODSTUDENT.ID  in (select ID from A_CTY_CHSTUDENT);\r\n" +
                "";
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcUtils.ORACLE);
        for (SQLStatement stmt : stmtList) {
            LineageInfo info = new LineageInfo(stmt.toString(), newOracleInstance());
            OracleLineageVisitor visitor = new OracleLineageVisitor(info);
            stmt.accept(visitor);
            System.out.println(info.toLineageString());
            LineageInfo lineageWithoutCursor = lineage.SQLUtils.getSimpleLineage(info);
            System.out.println(lineageWithoutCursor.toLineageString());
            System.out.println("是否解析出错" + lineageWithoutCursor.isHasSyntaxError());
        }
        return;
    }

    public static void test_hello() {
//		boolean checkTableExistInDb = VisitorUtils.checkTableExistInDb(newOracleInstance(), "CREDITOR.JUXINLIMIDATA");
    }

    public static RdbmsBo newOracleInstance() {
        RdbmsBo oracledb = new RdbmsBo();
        oracledb.setId("237");
        oracledb.setDbase_type(DatabaseTypeEnum.ORACLE.getCode());
        oracledb.setConnect_params(
                "");
        oracledb.setDbase_ip("172.22.5.61");
        oracledb.setDbase_port(1526);
        oracledb.setDbase_name("/dcappdb");
        oracledb.setDbase_user("crawlm");
        oracledb.setDbase_passwd("crawlm_Sd");
        return oracledb;
    }
}
