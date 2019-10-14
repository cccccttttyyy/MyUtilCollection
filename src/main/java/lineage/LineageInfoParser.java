package lineage;

import sqlexecutor.pojo.Database;

/**
 * 血缘关系类
 *
 * @author maolh
 * @date 2019/06/26
 */
public abstract class LineageInfoParser {

    protected LineageInfo lineageInfo;


    /**
     * 解析sql，并从中获取表的血缘关系
     *
     * @param sql
     * @param db
     * @throws ParseException
     */
    public abstract void parseTable(String sql, Database db);

    /**
     * 获取血缘关系
     *
     * @return
     */
    public LineageInfo getLineageInfo() {
        return this.lineageInfo;
    }

}
