package com.tsrain.sqlexecutor.pojo;

/**
 * @author wangsz
 * hdfs数据库
 */
public class HdfsBo {
    private int id;
    //数据源描述
    private String dbase_desc;
    //数据库类型	0 oracle; 1 mysql; 2 sqlserver; 3 postgresql; 4 hdfs; 5 hbase; 6 mongodb; 7 ftp; 8 txt
    private int dbase_type;
    //数据库ip地址
    private String dbase_ip;
    //数据库端口号
    private int dbase_port;
    //数据库用户名
    private String dbase_user;
    //数据库密码
    private String dbase_passwd;
    //数据库名
    private String dbase_name;
    //是否启用kerberos
    private boolean kerberos_enable;
    //Kerberos认证Principal名
    private String kerberos_principal;
    //Kerberos认证 keytab文件路径
    private String kerberos_keytab_filepath;
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

    public String getDbase_ip() {
        return dbase_ip;
    }

    public void setDbase_ip(String dbase_ip) {
        this.dbase_ip = dbase_ip;
    }

    public int getDbase_port() {
        return dbase_port;
    }

    public void setDbase_port(int dbase_port) {
        this.dbase_port = dbase_port;
    }

    public String getDbase_user() {
        return dbase_user;
    }

    public void setDbase_user(String dbase_user) {
        this.dbase_user = dbase_user;
    }

    public String getDbase_passwd() {
        return dbase_passwd;
    }

    public void setDbase_passwd(String dbase_passwd) {
        this.dbase_passwd = dbase_passwd;
    }

    public String getDbase_name() {
        return dbase_name;
    }

    public void setDbase_name(String dbase_name) {
        this.dbase_name = dbase_name;
    }

    public boolean isKerberos_enable() {
        return kerberos_enable;
    }

    public void setKerberos_enable(boolean kerberos_enable) {
        this.kerberos_enable = kerberos_enable;
    }

    public String getKerberos_principal() {
        return kerberos_principal;
    }

    public void setKerberos_principal(String kerberos_principal) {
        this.kerberos_principal = kerberos_principal;
    }

    public String getKerberos_keytab_filepath() {
        return kerberos_keytab_filepath;
    }

    public void setKerberos_keytab_filepath(String kerberos_keytab_filepath) {
        this.kerberos_keytab_filepath = kerberos_keytab_filepath;
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
        return "HdfsBo [id=" + id + ", dbase_desc=" + dbase_desc + ", dbase_type=" + dbase_type + ", dbase_ip="
                + dbase_ip + ", dbase_port=" + dbase_port + ", dbase_user=" + dbase_user + ", dbase_passwd="
                + dbase_passwd + ", dbase_name=" + dbase_name + ", kerberos_enable=" + kerberos_enable
                + ", kerberos_principal=" + kerberos_principal + ", kerberos_keytab_filepath="
                + kerberos_keytab_filepath + ", create_time=" + create_time + ", update_time=" + update_time
                + ", create_user=" + create_user + "]";
    }
}
