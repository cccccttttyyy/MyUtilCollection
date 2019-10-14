package sqlexecutor.pojo;

import java.util.Date;

public class DmasDataSource {
    private String id;

    private String databaseDescription;

    private String databaseType;

    private String databaseIp;

    private Integer databasePort;

    private String databaseUser;

    private String databasePasswd;

    private String databaseName;

    private String zookeeperQuorum;

    private String zookeeperPort;

    private String hbaseRootdir;

    private String kerberosEnable;

    private String kerberosPrincipal;

    private String kerberosKeytabFilePath;

    private String ftpProtocol;

    private String txtPath;

    private Date createdTime;

    private Date updateTime;

    private String createUser;

    private String connectParams;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatabaseDescription() {
        return databaseDescription;
    }

    public void setDatabaseDescription(String databaseDescription) {
        this.databaseDescription = databaseDescription == null ? null : databaseDescription.trim();
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType == null ? null : databaseType.trim();
    }

    public String getDatabaseIp() {
        return databaseIp;
    }

    public void setDatabaseIp(String databaseIp) {
        this.databaseIp = databaseIp == null ? null : databaseIp.trim();
    }

    public Integer getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(Integer databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser == null ? null : databaseUser.trim();
    }

    public String getDatabasePasswd() {
        return databasePasswd;
    }

    public void setDatabasePasswd(String databasePasswd) {
        this.databasePasswd = databasePasswd == null ? null : databasePasswd.trim();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName == null ? null : databaseName.trim();
    }

    public String getZookeeperQuorum() {
        return zookeeperQuorum;
    }

    public void setZookeeperQuorum(String zookeeperQuorum) {
        this.zookeeperQuorum = zookeeperQuorum == null ? null : zookeeperQuorum.trim();
    }

    public String getZookeeperPort() {
        return zookeeperPort;
    }

    public void setZookeeperPort(String zookeeperPort) {
        this.zookeeperPort = zookeeperPort == null ? null : zookeeperPort.trim();
    }

    public String getHbaseRootdir() {
        return hbaseRootdir;
    }

    public void setHbaseRootdir(String hbaseRootdir) {
        this.hbaseRootdir = hbaseRootdir == null ? null : hbaseRootdir.trim();
    }

    public String getKerberosEnable() {
        return kerberosEnable;
    }

    public void setKerberosEnable(String kerberosEnable) {
        this.kerberosEnable = kerberosEnable == null ? null : kerberosEnable.trim();
    }

    public String getKerberosPrincipal() {
        return kerberosPrincipal;
    }

    public void setKerberosPrincipal(String kerberosPrincipal) {
        this.kerberosPrincipal = kerberosPrincipal == null ? null : kerberosPrincipal.trim();
    }

    public String getKerberosKeytabFilePath() {
        return kerberosKeytabFilePath;
    }

    public void setKerberosKeytabFilePath(String kerberosKeytabFilePath) {
        this.kerberosKeytabFilePath = kerberosKeytabFilePath == null ? null : kerberosKeytabFilePath.trim();
    }

    public String getFtpProtocol() {
        return ftpProtocol;
    }

    public void setFtpProtocol(String ftpProtocol) {
        this.ftpProtocol = ftpProtocol == null ? null : ftpProtocol.trim();
    }

    public String getTxtPath() {
        return txtPath;
    }

    public void setTxtPath(String txtPath) {
        this.txtPath = txtPath == null ? null : txtPath.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getConnectParams() {
        return connectParams;
    }

    public void setConnectParams(String connectParams) {
        this.connectParams = connectParams == null ? null : connectParams.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Database toDatabase() {
        Database database = null;
        if (databaseType == null)
            return null;
        DatabaseTypeEnum databaseTypeEnum = DatabaseTypeEnum.getById(Integer.valueOf(databaseType));
        switch (databaseTypeEnum) {
            case ORACLE:
            case MYSQL:
            case POSTGRESQL:
            case SQLSERVER:
            case DB2:
                RdbmsBo rdbmsBo = new RdbmsBo();
                rdbmsBo.setConnect_params(this.connectParams);
                rdbmsBo.setCreate_user(this.createUser);
                rdbmsBo.setDbase_ip(this.databaseIp);
                rdbmsBo.setDbase_name(this.databaseName);
                rdbmsBo.setDbase_passwd(this.databasePasswd);
                rdbmsBo.setDbase_port(this.databasePort);
                rdbmsBo.setDbase_type(Integer.valueOf(this.databaseType));
                rdbmsBo.setDbase_user(this.databaseUser);
                rdbmsBo.setId(id);
                database = rdbmsBo;
                break;
            case HIVE:
                HiveBo hiveBo = new HiveBo();
                hiveBo.setConnect_params(this.connectParams);
                hiveBo.setCreate_user(this.createUser);
                hiveBo.setDbase_ip(this.databaseIp);
                hiveBo.setDbase_name(this.databaseName);
                hiveBo.setDbase_passwd(this.databasePasswd);
                hiveBo.setDbase_port(this.databasePort);
                hiveBo.setDbase_type(Integer.valueOf(this.databaseType));
                hiveBo.setDbase_user(this.databaseUser);
                hiveBo.setId(id);
                database = hiveBo;
                break;
            case HBASE:
                HbaseBo hbaseBo = new HbaseBo();
                hbaseBo.setDbase_type(Integer.valueOf(this.databaseType));
                hbaseBo.setHbase_rootdir(this.hbaseRootdir);
                hbaseBo.setId(id);
                hbaseBo.setKerberos_enable(Boolean.valueOf(this.kerberosEnable));
                hbaseBo.setKerberos_keytab_file_path(this.kerberosKeytabFilePath);
                hbaseBo.setKerberos_principal(this.kerberosPrincipal);
                hbaseBo.setZk_port(Integer.valueOf(this.zookeeperPort));
                hbaseBo.setZk_quorum(this.zookeeperQuorum);
                database = hbaseBo;
                break;
            default:
                break;
        }
        return database;
    }
}