package dao;

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

    //删除指定用户的所有记录
    public void deleteAllItemByTel(String tel) throws Exception {
        Statement stm = con.createStatement();
        String sql = "delete from about_content";

        stm.executeUpdate(sql);
        System.out.println(sql);
        stm.close();
    }

    //根据传入的entity新增记录
    public void insertAboutItem(AboutEntity entity) throws Exception {
        Statement stm = con.createStatement();
        String sql = "insert into about_content (tel, item_name, item_content, priority) values " +
                "('" + entity.getTel() + "', '" + entity.getItemName() + "', '" + entity.getItemContent() + "','" + entity.getPriority() + "')";

        stm.executeUpdate(sql);
        System.out.println(sql);
        stm.close();
    }
}
