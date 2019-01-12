package dao;

import entity.ExperienceEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExperienceDao {
    private Connection con;

    public ExperienceDao(Connection con) {
        this.con = con;
    }

    //查询指定用户的个人经历信息，按设置的优先级排序（数值小的靠前）
    public List<ExperienceEntity> getExperienceEntityListByTel(String tel) throws Exception {
        List<ExperienceEntity> resultList = new ArrayList<>();
        Statement stm = con.createStatement();
        String sql = "select * from exp_content where tel='" + tel + "' order by priority asc";
        ResultSet rs = stm.executeQuery(sql);
        System.out.println(sql);

        ExperienceEntity experienceEntity;
        while (rs.next()) {
            experienceEntity = new ExperienceEntity();
            experienceEntity.setTel(rs.getString("tel"));
            experienceEntity.setItemTitle(rs.getString("item_title"));
            experienceEntity.setItemContent(rs.getString("item_content"));
            experienceEntity.setPriority(rs.getInt("priority"));
            resultList.add(experienceEntity);
        }
        rs.close();
        stm.close();
        return resultList;
    }

    //根据传入的entity新增或删除记录
    public void updateExperienceItem(ExperienceEntity entity) throws Exception {
        Statement stm = con.createStatement();
        String delSql = "delete from exp_content where tel='" + entity.getTel() +"' and item_title='" + entity.getItemTitle() +
                "' and item_content='" + entity.getItemContent() + "'";
        String insertSql = "insert into exp_content (tel, item_title, item_content, priority) values " +
                "('" + entity.getTel() + "', '" + entity.getItemTitle() + "', '" + entity.getItemContent() + "','" + entity.getPriority() + "')";

        stm.executeUpdate(delSql);
        System.out.println(delSql);
        stm.executeUpdate(insertSql);
        System.out.println(insertSql);
        stm.close();
    }
}
