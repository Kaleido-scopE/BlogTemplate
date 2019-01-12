package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {

    //创建数据库连接
    public static Connection createDBConnection() {
        Connection con = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/mypages?serverTimezone=GMT%2B8";
        String user = "root";
        String password = "QWE220806@";

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            if (!con.isClosed())
                System.out.println("DB Connecting Success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

    //关闭数据库连接
    public static void destroyDBConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Disconnected from DB");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
