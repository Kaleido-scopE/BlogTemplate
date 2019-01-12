package servlet;

import dao.AboutDao;
import entity.AboutEntity;
import net.sf.json.JSONObject;
import utils.Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "AboutServlet", urlPatterns = "/getAboutInfo")
public class AboutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String JSONStr = Parser.getData(request);
        JSONObject requestObject = JSONObject.fromObject(JSONStr);
        String tel = requestObject.getString("userTel");

        AboutDao dao = new AboutDao((Connection) getServletContext().getAttribute("Connection"));
        try {
            List<AboutEntity> aboutList = dao.getAboutEntityListByTel(tel);
            JSONObject responseObject = new JSONObject();
            responseObject.put("aboutList", aboutList);
            responseObject.put("aboutImgPath", "/img/" + tel + "_about.jpg");
            Parser.sendRes(response, responseObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
