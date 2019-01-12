package servlet;

import dao.HomeDao;
import entity.HomeEntity;
import net.sf.json.JSONObject;
import utils.Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "HomeServlet", urlPatterns = "/getHomeInfo", loadOnStartup = 1)
public class HomeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String JSONStr = Parser.getData(request);
        JSONObject requestObject = JSONObject.fromObject(JSONStr);
        String tel = requestObject.getString("userTel");

        HomeDao dao = new HomeDao((Connection) getServletContext().getAttribute("Connection"));
        try {
            HomeEntity entity = dao.getHomeEntityByTel(tel);
            String[] contentChips = entity.getContent().split("\n");//将内容分片，方便前端创建p标签
            JSONObject responseObject = new JSONObject();
            responseObject.put("contentChips", contentChips);
            responseObject.put("homeImgPath", "/img/" + tel + "_home.jpg");//写入主页图片路径
            Parser.sendRes(response, responseObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
