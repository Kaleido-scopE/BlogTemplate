package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDao {
    private Connection con;

    public UserDao(Connection con) {
        this.con = con;
    }

    //查询当前电话标记的用户是否存在
    public boolean isUserExist(String tel) throws Exception {
        Statement stm = con.createStatement();
        String sql = "select * from user_tel where tel='" + tel + "'";
        ResultSet rs = stm.executeQuery(sql);
        System.out.println(sql);
        return rs.next();
    }
}
