package sqlexecutor.listener;

import sqlexecutor.pool.DBManagerCache;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.DriverManager;

/**
 * servlet容器销毁后，关闭数据源的操作
 *
 * @author cuitianyu
 */
//@WebListener
public class AppContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent event) {
        try {
            while (DriverManager.getDrivers().hasMoreElements()) {
                DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
            }
            DBManagerCache.removeAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        String rootPath = context.getRealPath("/");
        System.setProperty("rootPath", rootPath);
    }
}