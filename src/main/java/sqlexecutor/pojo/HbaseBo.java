package sqlexecutor.pojo;

/**
 * @author wangsz
 * hbase数据库
 */
public class HbaseBo extends Database {
    private int id;
    //数据源描述
    private String dbase_desc;
    //数据库类型	0 oracle; 1 mysql; 2 sqlserver; 3 postgresql; 4 hive; 5 hbase; 6 mongodb; 7 ftp; 8 txt
    private int dbase_type;
    //zk地址
    private String zk_quorum;
    //zk端口号
    private int zk_port;
    //hbase根目录
    private String hbase_rootdir;
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

    public static HbaseBo newInstance() {
        HbaseBo bo = new HbaseBo();
        bo.setId(13);
        bo.setDbase_type(5);
        bo.setZk_port(2181);
        bo.setZk_quorum("192.168.119.119");
        bo.setId(5);
        return bo;
    }

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

    public String getZk_quorum() {
        return zk_quorum;
    }

    public void setZk_quorum(String zk_quorum) {
        this.zk_quorum = zk_quorum;
    }

    public int getZk_port() {
        return zk_port;
    }

    public void setZk_port(int zk_port) {
        this.zk_port = zk_port;
    }

    public String getHbase_rootdir() {
        return hbase_rootdir;
    }

    public void setHbase_rootdir(String hbase_rootdir) {
        this.hbase_rootdir = hbase_rootdir;
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
        return "HbaseBo [id=" + id + ", dbase_desc=" + dbase_desc + ", dbase_type=" + dbase_type + ", zk_quorum="
                + zk_quorum + ", zk_port=" + zk_port + ", hbase_rootdir=" + hbase_rootdir + ", kerberos_enable="
                + kerberos_enable + ", kerberos_principal=" + kerberos_principal + ", kerberos_keytab_filepath="
                + kerberos_keytab_filepath + ", create_time=" + create_time + ", update_time=" + update_time
                + ", create_user=" + create_user + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dbase_type;
        result = prime * result + ((hbase_rootdir == null) ? 0 : hbase_rootdir.hashCode());
        result = prime * result + id;
        result = prime * result + (kerberos_enable ? 1231 : 1237);
        result = prime * result + ((kerberos_keytab_filepath == null) ? 0 : kerberos_keytab_filepath.hashCode());
        result = prime * result + ((kerberos_principal == null) ? 0 : kerberos_principal.hashCode());
        result = prime * result + zk_port;
        result = prime * result + ((zk_quorum == null) ? 0 : zk_quorum.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HbaseBo other = (HbaseBo) obj;
        if (dbase_type != other.dbase_type)
            return false;
        if (hbase_rootdir == null) {
            if (other.hbase_rootdir != null)
                return false;
        } else if (!hbase_rootdir.equals(other.hbase_rootdir))
            return false;
        if (id != other.id)
            return false;
        if (kerberos_enable != other.kerberos_enable)
            return false;
        if (kerberos_keytab_filepath == null) {
            if (other.kerberos_keytab_filepath != null)
                return false;
        } else if (!kerberos_keytab_filepath.equals(other.kerberos_keytab_filepath))
            return false;
        if (kerberos_principal == null) {
            if (other.kerberos_principal != null)
                return false;
        } else if (!kerberos_principal.equals(other.kerberos_principal))
            return false;
        if (zk_port != other.zk_port)
            return false;
        if (zk_quorum == null) {
            return other.zk_quorum == null;
        } else return zk_quorum.equals(other.zk_quorum);
    }
}
