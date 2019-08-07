package com.tsrain.sqlexecutor.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格
 *
 * @author maolh
 * @date 2018-06-24
 */
public class Table {

    /**
     * 设置表名字，可以不设置
     */
    private String name;

    /**
     * 设置表的标签，用于HTML展示
     */
    private String label;

    /**
     * 表的类型：实体表、视图、SELECT表、WITH表
     */
    private String type;

    /**
     * 表的唯一id，实体表、视图的id就是其name
     */
    private String id;

    /**
     * 列的集合
     */
    private ColumnMeta meta;
    /**
     * 行的集合
     */
    private List<Row> rows;

    /**
     * 树形结构相关属性
     */
    private String parentId;

    private String parentIds;

    private int level = 0;

    private int x = 0;

    private int y = 0;

    private List<Table> childList = new ArrayList<>();

    public Table(ColumnMeta meta) {
        this.meta = meta;
    }

    public Table() {
        meta = new ColumnMeta();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Table> getChildList() {
        return childList;
    }

    public void setChildList(List<Table> childList) {
        this.childList = childList;
    }

    public ColumnMeta getMeta() {
        return meta;
    }

    public void setMeta(ColumnMeta meta) {
        this.meta = meta;
    }

    public List<Row> getRows() {
        return rows;
    }

    public boolean setRows(List<Row> rows) {
        boolean flag = true;
        List<Row> oldRows = this.rows;
        for (Row row : rows) {
            flag = flag && addRows(row);
        }
        if (flag) {
            this.rows = oldRows;
        }
        return flag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String name) {
        this.label = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 如果列的个数与meta不同，则返回失败
     *
     * @param row
     * @return
     */
    public boolean addRows(Row row) {
        if (row.size() == meta.size()) {
            if (this.rows == null) {
                this.rows = new ArrayList<Row>();
            }
            this.rows.add(row);
            return true;
        }
        return false;
    }

    /**
     * 增加列的数据
     *
     * @param meta
     */
    public void addMeta(ColumnMeta meta) {
        if (meta != null) {
            List<String> columns = meta.getColumns();
            if (columns != null) {
                if (this.meta == null) {
                    this.meta = new ColumnMeta();
                }
                for (String column : columns) {
                    this.meta.add(column);
                }
            }
        }
    }

    /**
     * @param alias
     */
    public void addColumn(String column) {
        if (this.meta == null) {
            this.meta = new ColumnMeta();
        }
        this.meta.add(column);
    }

    public boolean isCursor() {

        return null != this.type && "cursor".equals(this.type);
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", meta=" + meta +
                ", rows=" + rows +
                ", parentId='" + parentId + '\'' +
                ", parentIds='" + parentIds + '\'' +
                ", level=" + level +
                ", x=" + x +
                ", y=" + y +
                ", childList=" + childList +
                '}';
    }

    /**
     * 列名字集合
     */
    public static class ColumnMeta {
        /**
         * 列名字
         */
        private List<String> columns;

        public ColumnMeta(List<String> columns) {
            this.columns = columns;
        }

        public ColumnMeta() {
            this.columns = new ArrayList<String>();
        }

        /**
         * 获取列的个数
         *
         * @return
         */
        public int size() {
            return columns.size();
        }

        public List<String> getColumns() {
            return columns;
        }

        public void setColumns(List<String> columns) {
            this.columns = columns;
        }

        public void add(List<String> columns) {
            if (this.columns == null) {
                this.columns = new ArrayList<String>();
            }
            this.columns.addAll(columns);
        }

        public void add(String column) {
            if (this.columns == null) {
                this.columns = new ArrayList<String>();
            }
            if (this.columns.contains(column) == false) {
                this.columns.add(column);
            }
        }

        @Override
        public String toString() {
            return "ColumnMeta [columns=" + columns + "]";
        }

    }

    public static class Row {
        /**
         * 列的值
         */
        private List<String> columns;

        public Row(List<String> columns) {
            this.columns = columns;
        }

        /**
         * 获取列的个数
         *
         * @return
         */
        public int size() {
            return columns.size();
        }

        public List<String> getColumns() {
            return columns;
        }

    }
}
