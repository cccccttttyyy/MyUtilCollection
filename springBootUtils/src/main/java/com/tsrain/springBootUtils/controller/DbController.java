package com.tsrain.springBootUtils.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 测试连接数据库  jdbcTemplate方式
 * 数据库使用data_new aaa表测试
 */
@RestController
@RequestMapping("/mydb")
public class DbController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/hello")
    public String index() {
        return "Hello hello World";
    }
    @RequestMapping("/getUsers")
    public List<Map<String, Object>> getDbType() {
        String sql = "select * from aaa";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            Set<Entry<String, Object>> entries = map.entrySet();
            if (entries != null) {
                Iterator<Entry<String, Object>> iterator = entries.iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key + ":" + value);
                }
            }
        }
        return list;
    }
    @RequestMapping("/user/{id}")
    public Map<String, Object> getUser(@PathVariable String id) {
        Map<String, Object> map = null;
        List<Map<String, Object>> list = getDbType();
        for (Map<String, Object> dbmap : list) {
            Set<String> set = dbmap.keySet();
            for (String key : set) {
                if (key.equals("a")) {
                    if (dbmap.get(key).equals(id)) {
                        map = dbmap;
                    }
                }
            }
        }
        if (map == null)
            map = list.get(0);
        return map;
    }

}