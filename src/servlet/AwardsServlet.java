package servlet;

import dao.AwardsDao;
import entity.AwardsEntity;
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

@WebServlet(name = "AwardsServlet", urlPatterns = "/getAwardsInfo")
public class AwardsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
