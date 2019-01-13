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

    //删除指定用户的所有记录
    public void deleteAllItemByTel(String tel) throws Exception {
        Statement stm = con.createStatement();
        String sql = "delete from awards_content";

        stm.executeUpdate(sql);
        System.out.println(sql);
        stm.close();
    }

    //根据传入的entity新增记录
    public void insertAwardsItem(AwardsEntity entity) throws Exception {
        Statement stm = con.createStatement();
        String sql = "insert into awards_content (tel, time, detail, priority) values " +
                "('" + entity.getTel() + "', '" + entity.getTime() + "', '" + entity.getDetail() + "', '" + entity.getPriority() + "')";

        stm.executeUpdate(sql);
        System.out.println(sql);
        stm.close();
    }
}
