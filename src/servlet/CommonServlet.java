package servlet;

import net.sf.json.JSONObject;
import utils.Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CommonServlet", urlPatterns = {"/getCommonInfo", "/setAvatar"}, loadOnStartup = 1)
public class CommonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/getCommonInfo")) {
            String JSONStr = Parser.getData(request);
            JSONObject requestObject = JSONObject.fromObject(JSONStr);
            String tel = requestObject.getString("userTel");

            JSONObject responseObject = new JSONObject();
            responseObject.put("avatarPath", "/img/" + tel + "_avatar.jpg");
            Parser.sendRes(response, responseObject.toString());//写回页面公共信息
        }

        if (request.getServletPath().equals("/setAvatar")) {
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
            else //将头像存到img下
                try {
                    Parser.saveFile(request, "/img/", currentUserTel + "_avatar.jpg");
                    responseObject.put("code", 1);
                    responseObject.put("status", "Set Avatar Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            Parser.sendRes(response, responseObject.toString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
