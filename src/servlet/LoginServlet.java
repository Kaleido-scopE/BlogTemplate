package servlet;

import dao.UserDao;
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

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String JSONStr = Parser.getData(request);
        JSONObject requestObject = JSONObject.fromObject(JSONStr);
        String tel = requestObject.getString("inputtedTel");
        String verificationCode = requestObject.getString("veriCode");

        JSONObject responseObject = new JSONObject();
        responseObject.put("code", -1);
        responseObject.put("status", "Unknown Error!");
        UserDao dao = new UserDao((Connection) getServletContext().getAttribute("Connection"));
        HttpSession session = request.getSession();
        String sessionVeriCode = (String) session.getAttribute("verificationCode");

        //检查用户是否存在
        try {
            if (dao.isUserExist(tel)) {
                //检查验证码是否正确
                if (sessionVeriCode.equals(verificationCode)){
                    responseObject.put("code", 1);
                    responseObject.put("status", "Login Successfully!");

                    //将tel放入Session，用于后台管理时使用
                    session.setAttribute("currentUserTel", tel);
                }
                else {
                    responseObject.put("code", 0);
                    responseObject.put("status", "The verification code you input is wrong!");
                }
            }
            else {
                responseObject.put("code", 0);
                responseObject.put("status", "The telephone num you input is not exist,\nplease contact the admin of this site!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Parser.sendRes(response, responseObject.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
