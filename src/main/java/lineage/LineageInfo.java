package lineage;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import sqlexecutor.pojo.DatabaseTypeEnum;
import sqlexecutor.pojo.RdbmsBo;

import java.util.*;
import java.util.Map.Entry;

/**
 * 血缘详情信息
 *
 * @author maolh
 * @date 2019/06/26
 */
public class LineageInfo {

    /**
     * 所有的table, 其中key是table的id 可能也包含临时表
     */
    private Map<String, Table> tables = new HashMap<String, Table>();

    /**
     * 真正的数据库中的表之间的关系 可能也包含临时表
     */
    private List<TableRelation> relations = new ArrayList<TableRelation>();

    private String sql;

    /**
     * 设置分析的数据源
     */
    private RdbmsBo db;

    private boolean hasSyntaxError = false;

    public LineageInfo() {

    }

    public LineageInfo(String sql) {
        this.sql = sql;
    }

    public LineageInfo(String sql, RdbmsBo db) {
        this.sql = sql;
        this.db = db;
    }

    public List<TableRelation> getRelations() {
        return relations;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public RdbmsBo getDatabase() {
        return db;
    }

    public void setDatabase(RdbmsBo db) {
        this.db = db;
    }

    public boolean isHasSyntaxError() {
        return hasSyntaxError;
    }

    public void setHasSyntaxError(boolean hasSyntaxError) {
        this.hasSyntaxError = this.hasSyntaxError || hasSyntaxError;
    }

    /**
     * 增加另一个LineageInfo的信息
     *
     * @param info
     */
    public void add(LineageInfo info) {
        if (info != null) {
            if (sql == null) {
                sql = info.getSql();
            } else {
                sql = sql + ";" + info.getSql();
            }
            if (info.getRelations() != null) {
                relations.addAll(info.getRelations());
//				List<TableRelation> rels = info.getRelations();
//				for (TableRelation rel : rels) {
//					this.AddRelation(rel);
//				}
            }
            if (info.getTables() != null) {
                Map<String, Table> tablesMap = info.getTables();
                for (String key : tablesMap.keySet()) {
                    tables.put(key, tablesMap.get(key));
                }
            }
            if (null != info.getDatabase()) {
                this.db = info.getDatabase();
            }
        }
    }

    /**
     * @return
     */
    public Map<String, Table> getTables() {
        return tables;
    }

    /**
     * 获得第一个table
     *
     * @return
     */
    public Table getFirstTable() {
        Table table = null;
        for (Entry<String, Table> entry : tables.entrySet()) {
            table = entry.getValue();
            if (table != null && table.getType().contentEquals(LineageTableEnum.DSTTABLE.getMsg())) {
                break;
            }
        }
        return table;
    }

    /**
     * 获取所有的输出表
     *
     * @return
     */
    public List<Table> getOutputTables() {
        // 遍历所有的tableRelations,如果有一个dst表，不是其它表的src表，则将其返回
        List<Table> outputTables = new ArrayList<Table>();
        if (tables != null) {
            Set<String> srcSet = new TreeSet<String>();
            for (TableRelation tableRelation : relations) {
                //去掉dst src相同的情况，避免找不到输出表
                if (!tableRelation.getDstTableName().contentEquals(tableRelation.getSrcTableName())) {
                    srcSet.add(tableRelation.getSrcTable().getId());
                }
            }
            for (Table table : tables.values()) {
                if (!srcSet.contains(table.getId())) {
                    outputTables.add(table);
                }
            }
            return outputTables;
        }
        return null;
    }

    /**
     * 获取所有的输入表
     * <p>血缘关系输出表中不包含的表就是输入表</p>
     *
     * @return
     */
    public List<Table> getInputTables() {

        List<Table> inputTables = new ArrayList<Table>();
        if (tables != null) {
            Set<String> dstSet = new TreeSet<String>();
            for (TableRelation tableRelation : relations) {
                //去掉dst src相同的情况，避免找不到输出表
//				if (!tableRelation.getDstTableName().contentEquals(tableRelation.getSrcTableName())) {
                dstSet.add(tableRelation.getDstTable().getId());
//				}
            }
            for (Table table : tables.values()) {
                if (!dstSet.contains(table.getId())) {
                    inputTables.add(table);
                }
            }
            return inputTables;
        }
        return null;
    }

    /**
     * 模式
     */
    public String getMode() {
        if (db.getDbase_type() == DatabaseTypeEnum.ORACLE.getCode()) {
            return db.getDbase_user();
        }
        return null == db ? "" : db.getDbase_name();
    }

    /**
     * @param relation
     */
    public void AddRelation(TableRelation relation) {

        if (relation != null && null != relation.getColumnMapping()) {
            Table srcTable = relation.getSrcTable();
            Table dstTable = relation.getDstTable();
            //如果源表和目标表已经存在，合并表
            if (tables.containsKey(srcTable.getId())) {
                Table tmpTable = tables.get(srcTable.getId());
                tmpTable.addMeta(srcTable.getMeta());
            } else {
                tables.put(srcTable.getId(), srcTable);
            }
            if (tables.containsKey(dstTable.getId())) {
                Table tmpTable = tables.get(dstTable.getId());
                tmpTable.addMeta(dstTable.getMeta());
            } else {
                tables.put(dstTable.getId(), dstTable);
            }

            for (TableRelation tableRelation : relations) {
                if (tableRelation.getSrcTable().getId().contentEquals(relation.getSrcTable().getId()) && tableRelation.getDstTable().getId().contentEquals(relation.getDstTable().getId())) {
                    for (com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair<String, String> colmap : relation.getColumnMapping()) {
                        if (!tableRelation.existsColumnMapping(colmap.getKey(), colmap.getValue())) {
                            tableRelation.addColumnMapping(colmap.getKey(), colmap.getValue());
                        }
                    }
                    return;
                }
            }
            relations.add(relation);
        }

    }

    /**
     * 移除某张中间表的血缘关系
     *
     * @param cursor          中间表
     * @param relationOfCuror 与中间表相关的血缘
     */
    public void removeRelOfCursor(Table cursor, List<TableRelation> relationOfCuror) {

        relations.removeAll(relationOfCuror);
        tables.remove(cursor.getId());
    }

    /**
     * 判断是否为临时表
     */
    public boolean isCursor(Table temp) {

        boolean isCursor = false;
        String type = temp.getType();
        if (!"table".equalsIgnoreCase(type)
                && !"view".equalsIgnoreCase(type)) {
            if (!getInputTables().contains(temp)
                    && !getOutputTables().contains(temp)) {
                isCursor = true;
            }
        }
        return isCursor;
    }

    /**
     * @param dstTable
     */
    public void addTable(Table table) {
        if (table != null) {
            //如果已经存在，进行列的合并
            if (tables.containsKey(table.getId())) {
                Table tmpTable = tables.get(table.getId());
                tmpTable.addMeta(table.getMeta());
            } else {
                tables.put(table.getId(), table);
            }
        }
    }

    @Override
    public String toString() {
        return "LineageInfo [tables=" + tables + ", relations=" + relations + "]";
    }

    /**
     * sql出错则将血缘置为空
     */
    public void clearRelation() {
        tables = null;
        relations = null;
    }

    public String toLineageString() {
        StringBuffer sb = new StringBuffer();
        sb.append("---------------lineage--------------------\n");
        for (TableRelation tableRelation : relations) {
            sb.append("relation:" + tableRelation.getSrcTableName() + "(" + tableRelation.getSrcTable().getId() + ")" + "==>" + tableRelation.getDstTableName() + "(" + tableRelation.getDstTable().getId() + ")" + "\n");
            List<Pair<String, String>> columnMapping = tableRelation.getColumnMapping();
            for (Pair<String, String> pair : columnMapping) {
                sb.append("         " + pair.getLeft() + "=>" + pair.getRight() + "\n");
            }
        }
        return sb.toString();
    }

    /**
     * 查找左表和右表之间的血缘关系
     *
     * @param lefTable
     * @param table
     * @return
     */
    public TableRelation searchRelation(Table srcTable, Table dstTable) {
        if (srcTable == null || dstTable == null) {
            return null;
        }
        for (TableRelation relation : relations) {
            if (relation.getDstTable().getId().equals(dstTable.getId()) &&
                    relation.getSrcTable().getId().equals(srcTable.getId())) {
                return relation;
            }
        }
        return null;
    }

}
