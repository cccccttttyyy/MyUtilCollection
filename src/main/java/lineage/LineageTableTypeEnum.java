package lineage;

/**
 * 血缘关系中的表的类型
 *
 * @author maolh
 * @date 2019/09/24
 */
public enum LineageTableTypeEnum {

    SELECT_OF("SELECT_OF", "SELECT_OF_"),

    SELECT_OF_TABLE("SELECT_OF_TABLE", "SELECT_OF_TABLE_"),

    SELECT_OF_VIEW("SELECT_OF_VIEW", "SELECT_OF_VIEW_"),

    SELECT_OF_INSERT("SELECT_OF_INSERT", "SELECT_OF_INSERT_"),

    SELECT_OF_UPDATE("SELECT_OF_UPDATE", "SELECT_OF_UPDATE_"),

    SELECT_OF_REPLACE("SELECT_OF_REPLACE", "SELECT_OF_REPLACE_"),

    SELECT_OF_JOIN("SELECT_OF_JOIN", "SELECT_OF_JOIN_"),

    RESULT_OF_UNION("RESULT_OF_UNION", "RESULT_OF_UNION_"),

    RESULT_OF_UPDATE("RESULT_OF_UPDATE", "RESULT_OF_UPDATE_"),

    LEFT_OF_SELECT("LEFT_OF_SELECT", "LEFT_OF_SELECT_"),

    RIGHT_OF_SELECT("RIGHT_OF_SELECT", "RIGHT_OF_SELECT_"),

    RESULT_OF_SELECT("RESULT_OF_SELECT", "RESULT_OF_SELECT_"),

    //实际的物理表
    TABLE("TABLE", "TABLE_"),
    //视图
    VIEW("VIEW", "VIEW_");

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型前缀
     */
    private String prefix;

    LineageTableTypeEnum(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
