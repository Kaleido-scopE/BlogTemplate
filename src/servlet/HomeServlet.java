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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "HomeServlet", urlPatterns = {"/getHomeInfo", "/setHomeInfo"}, loadOnStartup = 1)
public class HomeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/getHomeInfo")) {
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

        if (request.getServletPath().equals("/setHomeInfo")) {
            String JSONStr = Parser.getData(request);
            JSONObject requestObject = JSONObject.fromObject(JSONStr);
            String content = requestObject.getString("content");

            JSONObject responseObject = new JSONObject();
            responseObject.put("code", -1);
            responseObject.put("status", "Unknown Error!");

            //首先判断Session中是否有当前登录的用户
            HttpSession session = request.getSession();
            String currentUserTel = (String) session.getAttribute("currentUserTel");

            //如果session对象是新的，或session中没有电话属性则返回设置失败信息
            if (session.isNew() || currentUserTel == null) {
                responseObject.put("code", 0);
                responseObject.put("status", "Illegal Request!");
            }
            else //否则将content存入数据库
                try {
                    HomeDao dao = new HomeDao((Connection) getServletContext().getAttribute("Connection"));
                    HomeEntity entity = new HomeEntity();
                    entity.setTel(currentUserTel);
                    entity.setContent(content);
                    dao.updateHomeContent(entity);
                    responseObject.put("code", 1);
                    responseObject.put("status", "Success!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            Parser.sendRes(response, responseObject.toString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
