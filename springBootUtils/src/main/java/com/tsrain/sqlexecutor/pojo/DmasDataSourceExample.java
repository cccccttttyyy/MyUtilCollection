package com.tsrain.sqlexecutor.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DmasDataSourceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DmasDataSourceExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionIsNull() {
            addCriterion("DATABASE_DESCRIPTION is null");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionIsNotNull() {
            addCriterion("DATABASE_DESCRIPTION is not null");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionEqualTo(String value) {
            addCriterion("DATABASE_DESCRIPTION =", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionNotEqualTo(String value) {
            addCriterion("DATABASE_DESCRIPTION <>", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionGreaterThan(String value) {
            addCriterion("DATABASE_DESCRIPTION >", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("DATABASE_DESCRIPTION >=", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionLessThan(String value) {
            addCriterion("DATABASE_DESCRIPTION <", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionLessThanOrEqualTo(String value) {
            addCriterion("DATABASE_DESCRIPTION <=", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionLike(String value) {
            addCriterion("DATABASE_DESCRIPTION like", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionNotLike(String value) {
            addCriterion("DATABASE_DESCRIPTION not like", value, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionIn(List<String> values) {
            addCriterion("DATABASE_DESCRIPTION in", values, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionNotIn(List<String> values) {
            addCriterion("DATABASE_DESCRIPTION not in", values, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionBetween(String value1, String value2) {
            addCriterion("DATABASE_DESCRIPTION between", value1, value2, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseDescriptionNotBetween(String value1, String value2) {
            addCriterion("DATABASE_DESCRIPTION not between", value1, value2, "databaseDescription");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeIsNull() {
            addCriterion("DATABASE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeIsNotNull() {
            addCriterion("DATABASE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeEqualTo(String value) {
            addCriterion("DATABASE_TYPE =", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeNotEqualTo(String value) {
            addCriterion("DATABASE_TYPE <>", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeGreaterThan(String value) {
            addCriterion("DATABASE_TYPE >", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeGreaterThanOrEqualTo(String value) {
            addCriterion("DATABASE_TYPE >=", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeLessThan(String value) {
            addCriterion("DATABASE_TYPE <", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeLessThanOrEqualTo(String value) {
            addCriterion("DATABASE_TYPE <=", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeLike(String value) {
            addCriterion("DATABASE_TYPE like", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeNotLike(String value) {
            addCriterion("DATABASE_TYPE not like", value, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeIn(List<String> values) {
            addCriterion("DATABASE_TYPE in", values, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeNotIn(List<String> values) {
            addCriterion("DATABASE_TYPE not in", values, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeBetween(String value1, String value2) {
            addCriterion("DATABASE_TYPE between", value1, value2, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseTypeNotBetween(String value1, String value2) {
            addCriterion("DATABASE_TYPE not between", value1, value2, "databaseType");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpIsNull() {
            addCriterion("DATABASE_IP is null");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpIsNotNull() {
            addCriterion("DATABASE_IP is not null");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpEqualTo(String value) {
            addCriterion("DATABASE_IP =", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpNotEqualTo(String value) {
            addCriterion("DATABASE_IP <>", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpGreaterThan(String value) {
            addCriterion("DATABASE_IP >", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpGreaterThanOrEqualTo(String value) {
            addCriterion("DATABASE_IP >=", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpLessThan(String value) {
            addCriterion("DATABASE_IP <", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpLessThanOrEqualTo(String value) {
            addCriterion("DATABASE_IP <=", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpLike(String value) {
            addCriterion("DATABASE_IP like", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpNotLike(String value) {
            addCriterion("DATABASE_IP not like", value, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpIn(List<String> values) {
            addCriterion("DATABASE_IP in", values, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpNotIn(List<String> values) {
            addCriterion("DATABASE_IP not in", values, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpBetween(String value1, String value2) {
            addCriterion("DATABASE_IP between", value1, value2, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabaseIpNotBetween(String value1, String value2) {
            addCriterion("DATABASE_IP not between", value1, value2, "databaseIp");
            return (Criteria) this;
        }

        public Criteria andDatabasePortIsNull() {
            addCriterion("DATABASE_PORT is null");
            return (Criteria) this;
        }

        public Criteria andDatabasePortIsNotNull() {
            addCriterion("DATABASE_PORT is not null");
            return (Criteria) this;
        }

        public Criteria andDatabasePortEqualTo(Integer value) {
            addCriterion("DATABASE_PORT =", value, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortNotEqualTo(Integer value) {
            addCriterion("DATABASE_PORT <>", value, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortGreaterThan(Integer value) {
            addCriterion("DATABASE_PORT >", value, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortGreaterThanOrEqualTo(Integer value) {
            addCriterion("DATABASE_PORT >=", value, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortLessThan(Integer value) {
            addCriterion("DATABASE_PORT <", value, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortLessThanOrEqualTo(Integer value) {
            addCriterion("DATABASE_PORT <=", value, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortIn(List<Integer> values) {
            addCriterion("DATABASE_PORT in", values, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortNotIn(List<Integer> values) {
            addCriterion("DATABASE_PORT not in", values, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortBetween(Integer value1, Integer value2) {
            addCriterion("DATABASE_PORT between", value1, value2, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabasePortNotBetween(Integer value1, Integer value2) {
            addCriterion("DATABASE_PORT not between", value1, value2, "databasePort");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserIsNull() {
            addCriterion("DATABASE_USER is null");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserIsNotNull() {
            addCriterion("DATABASE_USER is not null");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserEqualTo(String value) {
            addCriterion("DATABASE_USER =", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserNotEqualTo(String value) {
            addCriterion("DATABASE_USER <>", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserGreaterThan(String value) {
            addCriterion("DATABASE_USER >", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserGreaterThanOrEqualTo(String value) {
            addCriterion("DATABASE_USER >=", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserLessThan(String value) {
            addCriterion("DATABASE_USER <", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserLessThanOrEqualTo(String value) {
            addCriterion("DATABASE_USER <=", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserLike(String value) {
            addCriterion("DATABASE_USER like", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserNotLike(String value) {
            addCriterion("DATABASE_USER not like", value, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserIn(List<String> values) {
            addCriterion("DATABASE_USER in", values, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserNotIn(List<String> values) {
            addCriterion("DATABASE_USER not in", values, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserBetween(String value1, String value2) {
            addCriterion("DATABASE_USER between", value1, value2, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabaseUserNotBetween(String value1, String value2) {
            addCriterion("DATABASE_USER not between", value1, value2, "databaseUser");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdIsNull() {
            addCriterion("DATABASE_PASSWD is null");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdIsNotNull() {
            addCriterion("DATABASE_PASSWD is not null");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdEqualTo(String value) {
            addCriterion("DATABASE_PASSWD =", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdNotEqualTo(String value) {
            addCriterion("DATABASE_PASSWD <>", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdGreaterThan(String value) {
            addCriterion("DATABASE_PASSWD >", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdGreaterThanOrEqualTo(String value) {
            addCriterion("DATABASE_PASSWD >=", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdLessThan(String value) {
            addCriterion("DATABASE_PASSWD <", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdLessThanOrEqualTo(String value) {
            addCriterion("DATABASE_PASSWD <=", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdLike(String value) {
            addCriterion("DATABASE_PASSWD like", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdNotLike(String value) {
            addCriterion("DATABASE_PASSWD not like", value, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdIn(List<String> values) {
            addCriterion("DATABASE_PASSWD in", values, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdNotIn(List<String> values) {
            addCriterion("DATABASE_PASSWD not in", values, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdBetween(String value1, String value2) {
            addCriterion("DATABASE_PASSWD between", value1, value2, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabasePasswdNotBetween(String value1, String value2) {
            addCriterion("DATABASE_PASSWD not between", value1, value2, "databasePasswd");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameIsNull() {
            addCriterion("DATABASE_NAME is null");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameIsNotNull() {
            addCriterion("DATABASE_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameEqualTo(String value) {
            addCriterion("DATABASE_NAME =", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotEqualTo(String value) {
            addCriterion("DATABASE_NAME <>", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameGreaterThan(String value) {
            addCriterion("DATABASE_NAME >", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameGreaterThanOrEqualTo(String value) {
            addCriterion("DATABASE_NAME >=", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameLessThan(String value) {
            addCriterion("DATABASE_NAME <", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameLessThanOrEqualTo(String value) {
            addCriterion("DATABASE_NAME <=", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameLike(String value) {
            addCriterion("DATABASE_NAME like", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotLike(String value) {
            addCriterion("DATABASE_NAME not like", value, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameIn(List<String> values) {
            addCriterion("DATABASE_NAME in", values, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotIn(List<String> values) {
            addCriterion("DATABASE_NAME not in", values, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameBetween(String value1, String value2) {
            addCriterion("DATABASE_NAME between", value1, value2, "databaseName");
            return (Criteria) this;
        }

        public Criteria andDatabaseNameNotBetween(String value1, String value2) {
            addCriterion("DATABASE_NAME not between", value1, value2, "databaseName");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumIsNull() {
            addCriterion("ZOOKEEPER_QUORUM is null");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumIsNotNull() {
            addCriterion("ZOOKEEPER_QUORUM is not null");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumEqualTo(String value) {
            addCriterion("ZOOKEEPER_QUORUM =", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumNotEqualTo(String value) {
            addCriterion("ZOOKEEPER_QUORUM <>", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumGreaterThan(String value) {
            addCriterion("ZOOKEEPER_QUORUM >", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumGreaterThanOrEqualTo(String value) {
            addCriterion("ZOOKEEPER_QUORUM >=", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumLessThan(String value) {
            addCriterion("ZOOKEEPER_QUORUM <", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumLessThanOrEqualTo(String value) {
            addCriterion("ZOOKEEPER_QUORUM <=", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumLike(String value) {
            addCriterion("ZOOKEEPER_QUORUM like", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumNotLike(String value) {
            addCriterion("ZOOKEEPER_QUORUM not like", value, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumIn(List<String> values) {
            addCriterion("ZOOKEEPER_QUORUM in", values, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumNotIn(List<String> values) {
            addCriterion("ZOOKEEPER_QUORUM not in", values, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumBetween(String value1, String value2) {
            addCriterion("ZOOKEEPER_QUORUM between", value1, value2, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperQuorumNotBetween(String value1, String value2) {
            addCriterion("ZOOKEEPER_QUORUM not between", value1, value2, "zookeeperQuorum");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortIsNull() {
            addCriterion("ZOOKEEPER_PORT is null");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortIsNotNull() {
            addCriterion("ZOOKEEPER_PORT is not null");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortEqualTo(String value) {
            addCriterion("ZOOKEEPER_PORT =", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortNotEqualTo(String value) {
            addCriterion("ZOOKEEPER_PORT <>", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortGreaterThan(String value) {
            addCriterion("ZOOKEEPER_PORT >", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortGreaterThanOrEqualTo(String value) {
            addCriterion("ZOOKEEPER_PORT >=", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortLessThan(String value) {
            addCriterion("ZOOKEEPER_PORT <", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortLessThanOrEqualTo(String value) {
            addCriterion("ZOOKEEPER_PORT <=", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortLike(String value) {
            addCriterion("ZOOKEEPER_PORT like", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortNotLike(String value) {
            addCriterion("ZOOKEEPER_PORT not like", value, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortIn(List<String> values) {
            addCriterion("ZOOKEEPER_PORT in", values, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortNotIn(List<String> values) {
            addCriterion("ZOOKEEPER_PORT not in", values, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortBetween(String value1, String value2) {
            addCriterion("ZOOKEEPER_PORT between", value1, value2, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andZookeeperPortNotBetween(String value1, String value2) {
            addCriterion("ZOOKEEPER_PORT not between", value1, value2, "zookeeperPort");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirIsNull() {
            addCriterion("HBASE_ROOTDIR is null");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirIsNotNull() {
            addCriterion("HBASE_ROOTDIR is not null");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirEqualTo(String value) {
            addCriterion("HBASE_ROOTDIR =", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirNotEqualTo(String value) {
            addCriterion("HBASE_ROOTDIR <>", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirGreaterThan(String value) {
            addCriterion("HBASE_ROOTDIR >", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirGreaterThanOrEqualTo(String value) {
            addCriterion("HBASE_ROOTDIR >=", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirLessThan(String value) {
            addCriterion("HBASE_ROOTDIR <", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirLessThanOrEqualTo(String value) {
            addCriterion("HBASE_ROOTDIR <=", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirLike(String value) {
            addCriterion("HBASE_ROOTDIR like", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirNotLike(String value) {
            addCriterion("HBASE_ROOTDIR not like", value, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirIn(List<String> values) {
            addCriterion("HBASE_ROOTDIR in", values, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirNotIn(List<String> values) {
            addCriterion("HBASE_ROOTDIR not in", values, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirBetween(String value1, String value2) {
            addCriterion("HBASE_ROOTDIR between", value1, value2, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andHbaseRootdirNotBetween(String value1, String value2) {
            addCriterion("HBASE_ROOTDIR not between", value1, value2, "hbaseRootdir");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableIsNull() {
            addCriterion("KERBEROS_ENABLE is null");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableIsNotNull() {
            addCriterion("KERBEROS_ENABLE is not null");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableEqualTo(String value) {
            addCriterion("KERBEROS_ENABLE =", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableNotEqualTo(String value) {
            addCriterion("KERBEROS_ENABLE <>", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableGreaterThan(String value) {
            addCriterion("KERBEROS_ENABLE >", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableGreaterThanOrEqualTo(String value) {
            addCriterion("KERBEROS_ENABLE >=", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableLessThan(String value) {
            addCriterion("KERBEROS_ENABLE <", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableLessThanOrEqualTo(String value) {
            addCriterion("KERBEROS_ENABLE <=", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableLike(String value) {
            addCriterion("KERBEROS_ENABLE like", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableNotLike(String value) {
            addCriterion("KERBEROS_ENABLE not like", value, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableIn(List<String> values) {
            addCriterion("KERBEROS_ENABLE in", values, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableNotIn(List<String> values) {
            addCriterion("KERBEROS_ENABLE not in", values, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableBetween(String value1, String value2) {
            addCriterion("KERBEROS_ENABLE between", value1, value2, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosEnableNotBetween(String value1, String value2) {
            addCriterion("KERBEROS_ENABLE not between", value1, value2, "kerberosEnable");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalIsNull() {
            addCriterion("KERBEROS_PRINCIPAL is null");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalIsNotNull() {
            addCriterion("KERBEROS_PRINCIPAL is not null");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalEqualTo(String value) {
            addCriterion("KERBEROS_PRINCIPAL =", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalNotEqualTo(String value) {
            addCriterion("KERBEROS_PRINCIPAL <>", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalGreaterThan(String value) {
            addCriterion("KERBEROS_PRINCIPAL >", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalGreaterThanOrEqualTo(String value) {
            addCriterion("KERBEROS_PRINCIPAL >=", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalLessThan(String value) {
            addCriterion("KERBEROS_PRINCIPAL <", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalLessThanOrEqualTo(String value) {
            addCriterion("KERBEROS_PRINCIPAL <=", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalLike(String value) {
            addCriterion("KERBEROS_PRINCIPAL like", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalNotLike(String value) {
            addCriterion("KERBEROS_PRINCIPAL not like", value, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalIn(List<String> values) {
            addCriterion("KERBEROS_PRINCIPAL in", values, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalNotIn(List<String> values) {
            addCriterion("KERBEROS_PRINCIPAL not in", values, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalBetween(String value1, String value2) {
            addCriterion("KERBEROS_PRINCIPAL between", value1, value2, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosPrincipalNotBetween(String value1, String value2) {
            addCriterion("KERBEROS_PRINCIPAL not between", value1, value2, "kerberosPrincipal");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathIsNull() {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH is null");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathIsNotNull() {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH is not null");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathEqualTo(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH =", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathNotEqualTo(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH <>", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathGreaterThan(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH >", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH >=", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathLessThan(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH <", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathLessThanOrEqualTo(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH <=", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathLike(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH like", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathNotLike(String value) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH not like", value, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathIn(List<String> values) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH in", values, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathNotIn(List<String> values) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH not in", values, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathBetween(String value1, String value2) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH between", value1, value2, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andKerberosKeytabFilePathNotBetween(String value1, String value2) {
            addCriterion("KERBEROS_KEYTAB_FILE_PATH not between", value1, value2, "kerberosKeytabFilePath");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolIsNull() {
            addCriterion("FTP_PROTOCOL is null");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolIsNotNull() {
            addCriterion("FTP_PROTOCOL is not null");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolEqualTo(String value) {
            addCriterion("FTP_PROTOCOL =", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolNotEqualTo(String value) {
            addCriterion("FTP_PROTOCOL <>", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolGreaterThan(String value) {
            addCriterion("FTP_PROTOCOL >", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolGreaterThanOrEqualTo(String value) {
            addCriterion("FTP_PROTOCOL >=", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolLessThan(String value) {
            addCriterion("FTP_PROTOCOL <", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolLessThanOrEqualTo(String value) {
            addCriterion("FTP_PROTOCOL <=", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolLike(String value) {
            addCriterion("FTP_PROTOCOL like", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolNotLike(String value) {
            addCriterion("FTP_PROTOCOL not like", value, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolIn(List<String> values) {
            addCriterion("FTP_PROTOCOL in", values, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolNotIn(List<String> values) {
            addCriterion("FTP_PROTOCOL not in", values, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolBetween(String value1, String value2) {
            addCriterion("FTP_PROTOCOL between", value1, value2, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andFtpProtocolNotBetween(String value1, String value2) {
            addCriterion("FTP_PROTOCOL not between", value1, value2, "ftpProtocol");
            return (Criteria) this;
        }

        public Criteria andTxtPathIsNull() {
            addCriterion("TXT_PATH is null");
            return (Criteria) this;
        }

        public Criteria andTxtPathIsNotNull() {
            addCriterion("TXT_PATH is not null");
            return (Criteria) this;
        }

        public Criteria andTxtPathEqualTo(String value) {
            addCriterion("TXT_PATH =", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathNotEqualTo(String value) {
            addCriterion("TXT_PATH <>", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathGreaterThan(String value) {
            addCriterion("TXT_PATH >", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathGreaterThanOrEqualTo(String value) {
            addCriterion("TXT_PATH >=", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathLessThan(String value) {
            addCriterion("TXT_PATH <", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathLessThanOrEqualTo(String value) {
            addCriterion("TXT_PATH <=", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathLike(String value) {
            addCriterion("TXT_PATH like", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathNotLike(String value) {
            addCriterion("TXT_PATH not like", value, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathIn(List<String> values) {
            addCriterion("TXT_PATH in", values, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathNotIn(List<String> values) {
            addCriterion("TXT_PATH not in", values, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathBetween(String value1, String value2) {
            addCriterion("TXT_PATH between", value1, value2, "txtPath");
            return (Criteria) this;
        }

        public Criteria andTxtPathNotBetween(String value1, String value2) {
            addCriterion("TXT_PATH not between", value1, value2, "txtPath");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("CREATED_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("CREATED_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(Date value) {
            addCriterion("CREATED_TIME =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(Date value) {
            addCriterion("CREATED_TIME <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(Date value) {
            addCriterion("CREATED_TIME >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATED_TIME >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(Date value) {
            addCriterion("CREATED_TIME <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(Date value) {
            addCriterion("CREATED_TIME <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<Date> values) {
            addCriterion("CREATED_TIME in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<Date> values) {
            addCriterion("CREATED_TIME not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(Date value1, Date value2) {
            addCriterion("CREATED_TIME between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(Date value1, Date value2) {
            addCriterion("CREATED_TIME not between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("CREATE_USER is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("CREATE_USER is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("CREATE_USER =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("CREATE_USER <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("CREATE_USER >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("CREATE_USER >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("CREATE_USER <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("CREATE_USER <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("CREATE_USER like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("CREATE_USER not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("CREATE_USER in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("CREATE_USER not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("CREATE_USER between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("CREATE_USER not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andConnectParamsIsNull() {
            addCriterion("CONNECT_PARAMS is null");
            return (Criteria) this;
        }

        public Criteria andConnectParamsIsNotNull() {
            addCriterion("CONNECT_PARAMS is not null");
            return (Criteria) this;
        }

        public Criteria andConnectParamsEqualTo(String value) {
            addCriterion("CONNECT_PARAMS =", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsNotEqualTo(String value) {
            addCriterion("CONNECT_PARAMS <>", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsGreaterThan(String value) {
            addCriterion("CONNECT_PARAMS >", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsGreaterThanOrEqualTo(String value) {
            addCriterion("CONNECT_PARAMS >=", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsLessThan(String value) {
            addCriterion("CONNECT_PARAMS <", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsLessThanOrEqualTo(String value) {
            addCriterion("CONNECT_PARAMS <=", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsLike(String value) {
            addCriterion("CONNECT_PARAMS like", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsNotLike(String value) {
            addCriterion("CONNECT_PARAMS not like", value, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsIn(List<String> values) {
            addCriterion("CONNECT_PARAMS in", values, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsNotIn(List<String> values) {
            addCriterion("CONNECT_PARAMS not in", values, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsBetween(String value1, String value2) {
            addCriterion("CONNECT_PARAMS between", value1, value2, "connectParams");
            return (Criteria) this;
        }

        public Criteria andConnectParamsNotBetween(String value1, String value2) {
            addCriterion("CONNECT_PARAMS not between", value1, value2, "connectParams");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("ADDRESS is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("ADDRESS is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("ADDRESS =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("ADDRESS <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("ADDRESS >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("ADDRESS >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("ADDRESS <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("ADDRESS <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("ADDRESS like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("ADDRESS not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("ADDRESS in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("ADDRESS not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("ADDRESS between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("ADDRESS not between", value1, value2, "address");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }
}