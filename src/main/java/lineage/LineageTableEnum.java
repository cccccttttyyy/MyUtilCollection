package lineage;

/**
 * Table在血缘关系表中的四种类型
 *
 * @author cuitianyu
 */
public enum LineageTableEnum implements CodeEnum {

    /**
     * 真实表或视图
     */
    TABLE(0, "table"),
    /**
     * 中间表
     */
    TEMP(1, "temp"),
    /**
     * update ,insert等操作的目标表
     */
    DSTTABLE(2, "dstTbale");

    private Integer code;
    private String msg;

    LineageTableEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;

    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;

    }
}