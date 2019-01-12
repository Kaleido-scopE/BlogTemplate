package servlet;

import net.sf.json.JSONObject;
import utils.Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CommonServlet", urlPatterns = "/getCommonInfo")
public class CommonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String JSONStr = Parser.getData(request);
        JSONObject requestObject = JSONObject.fromObject(JSONStr);
        String tel = requestObject.getString("userTel");

        JSONObject responseObject = new JSONObject();
        responseObject.put("avatarPath", "/img/" + tel + "_avatar.jpg");
        Parser.sendRes(response, responseObject.toString());//写回页面公共信息
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
