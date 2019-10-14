package lineage;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.ImmutablePair;
import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jetty.util.ajax.JSON;

import java.util.ArrayList;
import java.util.List;


/**
 * 表的血缘关系
 *
 * @author maolh
 * @date 2019/06/26
 */
public class TableRelation {


    /**
     * 源表
     */
    private Table srcTable;
    /**
     * 目标表
     */
    private Table dstTable;

    /**
     * 字段映射,格式形如[{"a":"b"},{"a","c"},{"b","z"}]
     */
    private List<Pair<String, String>> columnMapping;

    public TableRelation() {

    }

    public TableRelation(Table srcTable, Table dstTable) {
        this.srcTable = srcTable;
        this.dstTable = dstTable;
        srcTable.addColumn("-");
        dstTable.addColumn("-");
        this.addColumnMapping("-", "-");
    }

    public TableRelation(Table srcTable, Table dstTable, int flag) {
        this.srcTable = srcTable;
        this.dstTable = dstTable;
        srcTable.addColumn("-");
        dstTable.addColumn("-");
        this.addColumnMapping("-", "-");
    }

    public TableRelation(Table srcTable, Table dstTable, boolean flag) {
        this.srcTable = srcTable;
        this.dstTable = dstTable;
        if (flag) {
            srcTable.addColumn("-");
            dstTable.addColumn("-");
            this.addColumnMapping("-", "-");
        }
    }

    public Table getSrcTable() {
        return srcTable;
    }

    public void setSrcTable(Table srcTable) {
        this.srcTable = srcTable;
    }

    public String getSrcTableName() {
        return srcTable == null ? null : srcTable.getLabel();
    }

    public Table getDstTable() {
        return dstTable;
    }

    public void setDstTable(Table dstTable) {
        this.dstTable = dstTable;
    }

    public String getDstTableName() {
        return dstTable == null ? null : dstTable.getLabel();
    }

    public List<Pair<String, String>> getColumnMapping() {
        return columnMapping;
    }

//	public String getColumnMappingJson() {
//		return JSON.toJSONString(columnMapping);
//	}

    public void setColumnMapping(String json) {
        this.columnMapping = (List<Pair<String, String>>) JSON.parse(json);
    }

    public void setColumnMapping(List<Pair<String, String>> list) {
        this.columnMapping = list;
    }

    /**
     * 获取源表的字段
     *
     * @return
     */
    public List<String> getSrcColumns() {
        List<String> columns = new ArrayList<String>();
        for (Pair<String, String> pair : columnMapping) {
            if (columns.contains(pair.getKey()) == false) {
                columns.add(pair.getKey());
            }
        }
        return columns;
    }

    /**
     * 获取目标表的字段
     *
     * @return
     */
    public List<String> getDstColumns() {
        List<String> columns = new ArrayList<String>();
        for (Pair<String, String> pair : columnMapping) {
            if (columns.contains(pair.getValue()) == false) {
                columns.add(pair.getValue());
            }
        }
        return columns;
    }

    /**
     * 检查字段映射是否存在
     *
     * @return
     */
    public boolean existsColumnMapping(String srcCloumn, String dstColumn) {
        if (columnMapping != null) {
            for (Pair<String, String> pair : columnMapping) {
                if (pair.getKey().equalsIgnoreCase(srcCloumn) && pair.getValue().equalsIgnoreCase(dstColumn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 添加一个映射关系
     *
     * @param srcCloumn
     * @param dstColumn
     */
    public void addColumnMapping(String srcCloumn, String dstColumn) {
        if (getSrcTable() != null && !getSrcTable().getType().contentEquals(LineageTableEnum.TEMP.getMsg())) {
            if (srcCloumn.contains(".")) {
                String[] split = srcCloumn.split("\\.");
                srcCloumn = split[1].replaceAll("\"", "");
            }
        }
        if (getDstTable() != null && !getDstTable().getType().contentEquals(LineageTableEnum.TEMP.getMsg())) {
            if (dstColumn.contains(".")) {
                String[] split = dstColumn.split("\\.");
                dstColumn = split[1].replaceAll("\"", "");
            }
        }
        if (false == existsColumnMapping(srcCloumn, dstColumn)) {
            Pair<String, String> pair = new ImmutablePair<String, String>(srcCloumn, dstColumn);
            if (columnMapping == null) {
                columnMapping = new ArrayList<Pair<String, String>>();
            }
            columnMapping.add(pair);
        }
    }

    public void addColumnMappingList(List<String> srcCloumns, List<String> dstColumns) {

        if (null != srcCloumns && null != dstColumns && srcCloumns.size() == dstColumns.size()) {
            for (int i = 0, size = srcCloumns.size(); i < size; i++) {
                this.addColumnMapping(srcCloumns.get(i), dstColumns.get(i));
            }
        }
    }

    @Override
    public String toString() {
        return "TableRelation [srcTable=" + srcTable + ", dstTable=" + dstTable + ", columnMapping=" + columnMapping
                + "]";
    }

}
