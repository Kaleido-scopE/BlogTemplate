package dao;

import config.DBConnector;
import entity.AboutEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AboutDao {
    private Connection con;

    public AboutDao(Connection con) {
        this.con = con;
    }

    //查询指定用户的所有个人信息项，按设置的优先级排序（数值小的靠前）
    public List<AboutEntity> getAboutEntityListByTel(String tel) throws Exception {
        List<AboutEntity> resultList = new ArrayList<>();
        Statement stm = con.createStatement();
        String sql = "select * from about_content where tel='" + tel + "' order by priority asc";
        ResultSet rs = stm.executeQuery(sql);
        System.out.println(sql);

        AboutEntity aboutEntity;
        while (rs.next()) {
            aboutEntity = new AboutEntity();
            aboutEntity.setTel(rs.getString("tel"));
            aboutEntity.setItemName(rs.getString("item_name"));
            aboutEntity.setItemContent(rs.getString("item_content"));
            aboutEntity.setPriority(rs.getInt("priority"));
            resultList.add(aboutEntity);
        }
        rs.close();
        stm.close();
        return resultList;
    }

    //根据传入的entity新增或删除记录
    public void updateAboutItem(AboutEntity entity) throws Exception {
        Statement stm = con.createStatement();
        String delSql = "delete from about_content where tel='" + entity.getTel() + "' and item_name='" + entity.getItemName() + "'";
        String insertSql = "insert into about_content (tel, item_name, item_content, priority) values " +
                "('" + entity.getTel() + "', '" + entity.getItemName() + "', '" + entity.getItemContent() + "','" + entity.getPriority() + "')";

        stm.executeUpdate(delSql);
        System.out.println(delSql);
        stm.executeUpdate(insertSql);
        System.out.println(insertSql);
        stm.close();
    }

    public static void main(String[] args) {
        try {
            AboutDao dao = new AboutDao(DBConnector.createDBConnection());
            AboutEntity aboutEntity = new AboutEntity();
            aboutEntity.setTel("15526088820");
            aboutEntity.setItemName("Major in");
            aboutEntity.setItemContent("CS&T");
            aboutEntity.setPriority(5);
            dao.updateAboutItem(aboutEntity);
            DBConnector.destroyDBConnection(dao.con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
