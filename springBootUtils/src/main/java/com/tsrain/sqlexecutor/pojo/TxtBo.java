package com.tsrain.sqlexecutor.pojo;

/**
 * @author wangsz
 * txt数据库
 */
public class TxtBo {
    private int id;
    //数据源描述
    private String dbase_desc;
    //数据库类型	0 oracle; 1 mysql; 2 sqlserver; 3 postgresql; 4 hive; 5 hbase; 6 mongodb; 7 ftp; 8 txt
    private int dbase_type;
    //txt文件路径
    private String txt_path;
    //创建时间
    private String create_time;
    //更新时间
    private String update_time;
    //创建人
    private String create_user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDbase_desc() {
        return dbase_desc;
    }

    public void setDbase_desc(String dbase_desc) {
        this.dbase_desc = dbase_desc;
    }

    public int getDbase_type() {
        return dbase_type;
    }

    public void setDbase_type(int dbase_type) {
        this.dbase_type = dbase_type;
    }

    public String getTxt_path() {
        return txt_path;
    }

    public void setTxt_path(String txt_path) {
        this.txt_path = txt_path;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    @Override
    public String toString() {
        return "TxtBo [id=" + id + ", dbase_desc=" + dbase_desc + ", dbase_type=" + dbase_type + ", txt_path="
                + txt_path + ", create_time=" + create_time + ", update_time=" + update_time + ", create_user="
                + create_user + "]";
    }
}
