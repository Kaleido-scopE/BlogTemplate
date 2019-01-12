package dao;

import entity.AwardsEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AwardsDao {
    private Connection con;

    public AwardsDao(Connection con) {
        this.con = con;
    }

    //查询指定用户的获奖信息，按设置的优先级排序（数值小的靠前）
    public List<AwardsEntity> getAwardsEntityListByTel(String tel) throws Exception {
        List<AwardsEntity> resultList = new ArrayList<>();
        Statement stm = con.createStatement();
        String sql = "select * from awards_content where tel='" + tel + "' order by priority asc";
        ResultSet rs = stm.executeQuery(sql);
        System.out.println(sql);

        AwardsEntity awardsEntity;
        while (rs.next()) {
            awardsEntity = new AwardsEntity();
            awardsEntity.setTel(rs.getString("tel"));
            awardsEntity.setTime(rs.getString("time"));
            awardsEntity.setDetail(rs.getString("detail"));
            awardsEntity.setPriority(rs.getInt("priority"));
            resultList.add(awardsEntity);
        }
        rs.close();
        stm.close();
        return resultList;
    }

    //根据传入的entity新增或删除记录
    public void updateAwardsItem(AwardsEntity entity) throws Exception {
        Statement stm = con.createStatement();
        String delSql = "delete from awards_content where tel='" + entity.getTel() + "' and time='" + entity.getTime() + "' and detail='" + entity.getDetail() + "'";
        String insertSql = "insert into awards_content (tel, time, detail, priority) values " +
                "('" + entity.getTel() + "', '" + entity.getTime() + "', '" + entity.getDetail() + "', '" + entity.getPriority() + "')";

        stm.executeUpdate(delSql);
        System.out.println(delSql);
        stm.executeUpdate(insertSql);
        System.out.println(insertSql);
        stm.close();
    }
}
