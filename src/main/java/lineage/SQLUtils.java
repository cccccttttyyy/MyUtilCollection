package lineage;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import sqlexecutor.pojo.DatabaseTypeEnum;
import sqlexecutor.pojo.RdbmsBo;

import java.util.*;

/**
 * @author maolh
 * @date 2019/06/26
 */
public class SQLUtils {

    /**
     * 获取表血缘关系
     *
     * @param dbType
     * @param sql
     * @return
     * @throws ParseException
     */
    public static LineageInfo parseTable(String sql, RdbmsBo db) {
        LineageInfoParser parser = LineageInfoParserUtils.createLineageInfoParser(db.getDbase_type());
        parser.parseTable(sql, db);
        return parser.getLineageInfo();
    }

    /**
     * 获取表血缘关系
     *
     * @param dbType
     * @param sql
     * @return
     * @throws ParseException
     */
    public static LineageInfo parseTable(String sql, DatabaseTypeEnum dbtype) {
        LineageInfoParser parser = LineageInfoParserUtils.createLineageInfoParser(dbtype.getCode());
        parser.parseTable(sql, null);
        return parser.getLineageInfo();
    }

    /**
     * 去除SQL中的注释
     *
     * @param originSql
     * @param dbType
     */
    public static String removeComments(String originSql, String dbType) {

        String sql = "";
        List<SQLStatement> stmtList = com.alibaba.druid.sql.SQLUtils.parseStatements(originSql, dbType);
        for (SQLStatement stmt : stmtList) {
            sql += stmt.toString() + ";\n";
        }
        return sql;
    }


    /**
     * SQL 格式化
     *
     * @param originSql
     * @param dbType
     * @return
     */
    public static String beautifySql(String originSql, String dbType) {

        return com.alibaba.druid.sql.SQLUtils.format(originSql, dbType);
    }

    /**
     * @param sql
     * @param map
     * @param dialect
     * @return
     */
    public static String generatePageSql(String sql, int start, int limit, DatabaseTypeEnum dialect) {
        if (dialect != null) {
            StringBuffer pageSql = new StringBuffer();
            if (DatabaseTypeEnum.MYSQL == dialect) {
                String sqlLowerCase = sql.toLowerCase();
                if (sqlLowerCase.contains("limit")) {
                    sqlLowerCase = sqlLowerCase.substring(sqlLowerCase.lastIndexOf("limit"));
                    if (sqlLowerCase.matches("limit\\s*\\d+\\s*,\\s*\\d+\\s*")) {
                        pageSql.append(sql);
                    } else {
                        pageSql.append("select * from (");
                        pageSql.append(sql);
                        pageSql.append(" ) t limit " + start + "," + limit);
                    }
                } else {
                    pageSql.append(sql);
                    pageSql.append(" limit " + start + "," + limit);
                }
            } else if (DatabaseTypeEnum.ORACLE == dialect) {
                sql = sql.replaceAll("\\s+", " ");
                pageSql.append("SELECT * FROM (SELECT TMP_TB.*,ROWNUM ROW_ID FROM (");
                pageSql.append(sql);

                if (sql.toUpperCase().indexOf("ORDER BY") == -1) {
                    pageSql.append(" ORDER BY ROWNUM ASC");
                }
                pageSql.append(") TMP_TB WHERE ROWNUM<=");
                pageSql.append(start + limit);
                pageSql.append(") WHERE ROW_ID>=");
                pageSql.append(start + 1);
            } else if (DatabaseTypeEnum.DB2 == dialect) {
                pageSql.append("SELECT * FROM ( SELECT B.*, ROWNUMBER() OVER() AS ROW_ID FROM    (    ");
                pageSql.append(sql);
                pageSql.append(" ) AS B   ) AS A  ");
                pageSql.append(" WHERE A.ROW_ID BETWEEN " + (start + 1) + " AND " + (start + limit) + " ");
            } else if (DatabaseTypeEnum.SQLSERVER == dialect) {
                sql = sql.toUpperCase().replaceAll("\\s+", " ");
                String tableName = "";
                String pk = "";
                int fIdx = sql.lastIndexOf("FROM") + 4;
                int wIdx = sql.lastIndexOf("WHERE");
                if (wIdx != -1)
                    tableName = sql.substring(fIdx, wIdx).trim();
                else {
                    tableName = sql.substring(fIdx).trim();
                }

                if (sql.indexOf("ORDER BY") == -1) {
                    pk = "(select name from sysobjects where parent_obj=object_id('" + tableName + "') and xtype='PK')";
                } else {
                    int tmp = sql.lastIndexOf("ORDER BY") + 8;
                    pk = sql.substring(tmp).trim();
                }

                pageSql.append("SELECT * FROM ( SELECT B.*, ROW_NUMBER() OVER(ORDER BY ");
                pageSql.append(pk);
                pageSql.append(") AS ROW_ID FROM    (    ");
                pageSql.append(sql);
                pageSql.append(" ) AS B   ) AS A  ");
                pageSql.append(" WHERE A.ROW_ID BETWEEN " + (start + 1) + " AND " + (start + limit) + " ");
            } else if (DatabaseTypeEnum.POSTGRESQL == dialect) {
                pageSql.append("select * from (");
                pageSql.append(sql);
                pageSql.append(" ) t limit " + limit + " offset " + start);
            }
            return pageSql.toString().isEmpty() ? sql : pageSql.toString();
        }
        return sql;
    }

    /**
     * 过滤掉临时表
     * <p>
     * 用于兼容原版mysql血缘
     * </p>
     */
    @Deprecated
    public static LineageInfo getLineageWithoutCursor(LineageInfo originLineageInfo) {

        LineageInfo finalLineageInfo = new LineageInfo();
        finalLineageInfo = originLineageInfo;

        while (hasCursor(finalLineageInfo)) {
            LineageInfo tempLineageInfo = new LineageInfo();
            Collection<Table> tables = finalLineageInfo.getTables().values();
            Iterator it = tables.iterator();
            while (it.hasNext()) {
                Table tempTable = (Table) it.next();
                if (finalLineageInfo.isCursor(tempTable)) {
                    List<TableRelation> srcRels = getRelationsBySrc(tempTable, finalLineageInfo.getRelations());
                    List<TableRelation> dstRels = getRelationsByDst(tempTable, finalLineageInfo.getRelations());
                    if (null != srcRels && null != dstRels && srcRels.size() > 0 && dstRels.size() > 0) {
                        for (TableRelation dstRel : dstRels) {
                            for (TableRelation srcRel : srcRels) {
                                TableRelation curRel = new TableRelation(dstRel.getSrcTable(), srcRel.getDstTable());
                                curRel.addColumnMappingList(dstRel.getSrcColumns(),
                                        getRealDstColumns(dstRel.getDstColumns(), srcRel.getColumnMapping()));
                                finalLineageInfo.AddRelation(curRel);
                            }
                        }
                    }
                    it.remove();
                    finalLineageInfo.removeRelOfCursor(tempTable, srcRels);
                    finalLineageInfo.removeRelOfCursor(tempTable, dstRels);
                }
            }
        }

        return finalLineageInfo;
    }

    /**
     * 目前针对oracle 的血缘临时表过滤
     */
    public static LineageInfo getSimpleLineage(LineageInfo originLineageInfo) {
        LineageInfo finalLineageInfo = new LineageInfo();
        finalLineageInfo = originLineageInfo;
        Collection<Table> tables = finalLineageInfo.getTables().values();
        Iterator<Table> it = tables.iterator();
        while (it.hasNext()) {
            Table tempTable = it.next();
            if (tempTable.getType() == LineageTableEnum.TEMP.getMsg()) {
                List<TableRelation> srcRels = getRelationsBySrc(tempTable, finalLineageInfo.getRelations());
                List<TableRelation> dstRels = getRelationsByDst(tempTable, finalLineageInfo.getRelations());
                if (null != srcRels && null != dstRels && srcRels.size() > 0 && dstRels.size() > 0) {
                    for (TableRelation dstRel : dstRels) {
                        for (TableRelation srcRel : srcRels) {
                            TableRelation curRel = new TableRelation(dstRel.getSrcTable(), srcRel.getDstTable());
                            List<Pair<String, String>> columnMapping = dstRel.getColumnMapping();
                            for (Pair<String, String> dstmap : columnMapping) {
                                for (Pair<String, String> mapping : srcRel.getColumnMapping()) {
                                    String rightKey = mapping.getKey();
                                    String leftValue = dstmap.getValue();
                                    if (leftValue.contains(".") && !rightKey.contains(".")) {
                                        leftValue = leftValue.split("\\.")[1].replaceAll("\"", "");
                                    } else if (rightKey.contains(".") && !leftValue.contains(".")) {
                                        rightKey = rightKey.split("\\.")[1].replaceAll("\"", "");
                                    }
                                    if (rightKey.equals(leftValue)) {
                                        curRel.addColumnMapping(dstmap.getKey(), mapping.getValue());
                                        break;
                                    }
                                }
                            }
                            finalLineageInfo.AddRelation(curRel);
                        }
                    }
                }
                it.remove();
                finalLineageInfo.removeRelOfCursor(tempTable, srcRels);
                finalLineageInfo.removeRelOfCursor(tempTable, dstRels);
            }
        }
        return finalLineageInfo;
    }

    /**
     * 是否有临时表 判断是否存在输入和输出都不包含的表
     *
     * @param originLineageInfo
     * @return
     */
    private static boolean hasCursor(LineageInfo originLineageInfo) {

        Map<String, Table> originTables = originLineageInfo.getTables();
        for (Table table : originTables.values()) {
            if (originLineageInfo.isCursor(table)) {
                return true;
            }
        }
        return false;
    }

    private static List<String> getRealDstColumns(List<String> srcColumns, List<Pair<String, String>> srcMapping) {

        List<String> dstColumns = new ArrayList<String>();
        for (String srcColumn : srcColumns) {
            for (Pair<String, String> mapping : srcMapping) {
                if (mapping.getKey().equals(srcColumn)) {
                    dstColumns.add(mapping.getValue());
                }
            }
        }
        return dstColumns;
    }

    /**
     * 取出该表为源表的血缘关系
     *
     * @param srcTable
     * @param originRelations
     * @return
     */
    private static List<TableRelation> getRelationsBySrc(Table srcTable, List<TableRelation> originRelations) {

        List<TableRelation> curRelations = new ArrayList<TableRelation>();
        for (TableRelation tempRel : originRelations) {
            if (tempRel.getSrcTable().getId() == srcTable.getId()) {
                curRelations.add(tempRel);
            }
        }
        return curRelations;
    }

    /**
     * 取出该表为目标表的血缘关系
     *
     * @param dstTable
     * @param originRelations
     * @return
     */
    private static List<TableRelation> getRelationsByDst(Table dstTable, List<TableRelation> originRelations) {

        List<TableRelation> curRelations = new ArrayList<TableRelation>();
        for (TableRelation tempRel : originRelations) {
            if (tempRel.getDstTable().getId() == dstTable.getId()) {
                curRelations.add(tempRel);
            }
        }
        return curRelations;
    }
}
