package dao;

import entity.HomeEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HomeDao {
    private Connection con;

    public HomeDao(Connection con) {
        this.con = con;
    }

    //查询指定用户的主页内容
    public HomeEntity getHomeEntityByTel(String tel) throws Exception {
        Statement stm = con.createStatement();
        String sql = "select * from home_content where tel='" + tel + "'";
        ResultSet rs = stm.executeQuery(sql);
        System.out.println(sql);

        HomeEntity homeEntity = new HomeEntity();
        while (rs.next()) {
            homeEntity.setTel(rs.getString("tel"));
            homeEntity.setContent(rs.getString("content"));
        }
        rs.close();
        stm.close();
        return homeEntity;
    }

    //根据传入的entity新增或更新记录
    public void updateHomeContent(HomeEntity entity) throws Exception {
        Statement stm = con.createStatement();
        String delSql = "delete from home_content where tel='" + entity.getTel() + "'";
        String insertSql = "insert into home_content (tel, content) values " +
                "('" + entity.getTel() + "', '" + entity.getContent() + "')";

        stm.executeUpdate(delSql);
        System.out.println(delSql);
        stm.executeUpdate(insertSql);
        System.out.println(insertSql);
        stm.close();
    }
}
