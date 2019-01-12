package dao;

import entity.PicturesEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PicturesDao {
    private Connection con;

    public PicturesDao(Connection con) {
        this.con = con;
    }

    //查询指定用户图片墙中的所有年份
    public List<String> getAllYearByTel(String tel) throws Exception {
        List<String> resultList = new ArrayList<>();
        Statement stm = con.createStatement();
        String sql = "select distinct pic_year from pic_content where tel='" + tel + "'";
        ResultSet rs = stm.executeQuery(sql);
        System.out.println(sql);

        while (rs.next())
            resultList.add(rs.getString("pic_year"));
        stm.close();
        return resultList;
    }

    //查询指定用户图片墙中指定年份所有图片的路径
    public List<String> getPathByTelAndYear(String tel, String year) throws Exception {
        List<String> resultList = new ArrayList<>();
        Statement stm = con.createStatement();
        String sql = "select * from pic_content where tel='" + tel + "' and pic_year='" + year + "'";
        ResultSet rs = stm.executeQuery(sql);
        System.out.println(sql);

        while (rs.next())
            resultList.add(rs.getString("pic_pathname"));
        stm.close();
        return resultList;
    }

    //根据传入的路径删除图片记录
    public void deletePictureItem(String path) throws Exception {
        Statement stm = con.createStatement();
        String sql = "delete from pic_content where pic_pathname='" + path + "'";

        stm.executeUpdate(sql);
        System.out.println(sql);
        stm.close();
    }

    //根据传入的entity新建图片记录
    public void createPictureItem(PicturesEntity entity) throws Exception {
        Statement stm = con.createStatement();
        String sql = "insert into pic_content (tel, pic_year, pic_pathname) values " +
                "('" + entity.getTel() + "', '" + entity.getPicYear() + "', '" + entity.getPicPathName() + "')";

        stm.executeUpdate(sql);
        System.out.println(sql);
        stm.close();
    }
}
