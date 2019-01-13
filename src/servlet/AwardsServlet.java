package servlet;

import dao.AwardsDao;
import entity.AwardsEntity;
import net.sf.json.JSONArray;
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
import java.util.List;

@WebServlet(name = "AwardsServlet", urlPatterns = {"/getAwardsInfo", "/setAwardsInfo"})
public class AwardsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/getAwardsInfo")) {
            String JSONStr = Parser.getData(request);
            JSONObject requestObject = JSONObject.fromObject(JSONStr);
            String tel = requestObject.getString("userTel");

            AwardsDao dao = new AwardsDao((Connection) getServletContext().getAttribute("Connection"));
            try {
                List<AwardsEntity> awardsList = dao.getAwardsEntityListByTel(tel);
                JSONObject responseObject = new JSONObject();
                responseObject.put("awardsList", awardsList);
                Parser.sendRes(response, responseObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (request.getServletPath().equals("/setAwardsInfo")) {
            String JSONStr = Parser.getData(request);
            JSONArray requestArray = JSONArray.fromObject(JSONStr);

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
            else //否则先删除指定用户的所有记录，再将传入的数据全部存入数据库
                try {
                    AwardsDao dao = new AwardsDao((Connection) getServletContext().getAttribute("Connection"));
                    dao.deleteAllItemByTel(currentUserTel);

                    AwardsEntity entity;
                    for (int i = 0; i < requestArray.size(); i++) {
                        entity = new AwardsEntity();
                        entity.setTel(currentUserTel);
                        entity.setTime(requestArray.getJSONObject(i).getString("key"));
                        entity.setDetail(requestArray.getJSONObject(i).getString("value"));
                        entity.setPriority(i + 1);
                        dao.insertAwardsItem(entity);
                    }
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
