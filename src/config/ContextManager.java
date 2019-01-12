package config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;

//用于加载web.xml时进行初始操作
public class ContextManager implements ServletContextListener {
    //应用初始化前的操作，如打开数据库连接
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("Connection", DBConnector.createDBConnection());
    }

    //应用销毁前的操作，如关闭数据库连接
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Connection con = (Connection) sce.getServletContext().getAttribute("Connection");
        DBConnector.destroyDBConnection(con);
    }
}
