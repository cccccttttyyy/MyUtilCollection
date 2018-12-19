package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OracleUtils {
	public static List<Map<String,String>> OracleConn(String sqlString) throws SQLException{
        List<Map<String,String>> resultList = new LinkedList();
        Connection c = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            c = DriverManager.getConnection("jdbc:oracle:thin:@172.22.4.69:1521:xcdb2", "crawlm2_test", "Crawlm2_test%1");
            Statement sql = null;
            ResultSet rs = null;
            sql = c.createStatement();
            rs = sql.executeQuery(sqlString);
            while (rs.next()) {
            	for(int i = rs.getMetaData().getColumnCount();i!=0;i--) {
                	Map<String,String> tempMap = new HashMap();
                    tempMap.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                    resultList.add(tempMap);
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	c.close();
        }
        return resultList;
    }
}
