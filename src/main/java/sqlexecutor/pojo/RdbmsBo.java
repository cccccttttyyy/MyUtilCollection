package sqlexecutor.pojo;

/**
 * @author wangsz
 * 关系型数据库
 */
public class RdbmsBo extends Database {

    private String id;
    //数据源描述
    private String dbase_desc;
    //数据库类型	0 oracle; 1 mysql; 2 sqlserver; 3 postgresql; 4 hive; 5 hbase; 6 mongodb; 7 ftp; 8 txt; 9 db2
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

    //创建时间
    private String create_time;
    //更新时间
    private String update_time;
    //创建人
    private String create_user;

    //数据库连接参数，主要用于mysql
    private String connect_params;

    public String getConnect_params() {
        return connect_params;
    }

    public void setConnect_params(String connect_params) {
        this.connect_params = connect_params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "RdbmsBo [id=" + id + ", dbase_desc=" + dbase_desc + ", dbase_type=" + dbase_type + ", dbase_ip="
                + dbase_ip + ", dbase_port=" + dbase_port + ", dbase_user=" + dbase_user + ", dbase_passwd="
                + dbase_passwd + ", dbase_name=" + dbase_name + ", create_time=" + create_time + ", update_time="
                + update_time + ", create_user=" + create_user + ", connect_params=" + connect_params + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connect_params == null) ? 0 : connect_params.hashCode());
        result = prime * result + ((dbase_ip == null) ? 0 : dbase_ip.hashCode());
        result = prime * result + ((dbase_name == null) ? 0 : dbase_name.hashCode());
        result = prime * result + ((dbase_passwd == null) ? 0 : dbase_passwd.hashCode());
        result = prime * result + dbase_port;
        result = prime * result + dbase_type;
        result = prime * result + ((dbase_user == null) ? 0 : dbase_user.hashCode());
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
        RdbmsBo other = (RdbmsBo) obj;
        if (connect_params == null) {
            if (other.connect_params != null)
                return false;
        } else if (!connect_params.equals(other.connect_params))
            return false;
        if (dbase_ip == null) {
            if (other.dbase_ip != null)
                return false;
        } else if (!dbase_ip.equals(other.dbase_ip))
            return false;
        if (dbase_name == null) {
            if (other.dbase_name != null)
                return false;
        } else if (!dbase_name.equals(other.dbase_name))
            return false;
        if (dbase_passwd == null) {
            if (other.dbase_passwd != null)
                return false;
        } else if (!dbase_passwd.equals(other.dbase_passwd))
            return false;
        if (dbase_port != other.dbase_port)
            return false;
        if (dbase_type != other.dbase_type)
            return false;
        if (dbase_user == null) {
            if (other.dbase_user != null)
                return false;
        } else if (!dbase_user.equals(other.dbase_user))
            return false;
        return id == other.id;
    }
}
