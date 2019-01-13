package servlet;

import dao.ExperienceDao;
import entity.ExperienceEntity;
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

@WebServlet(name = "ExperienceServlet", urlPatterns = {"/getExperienceInfo", "/setExperienceInfo"})
public class ExperienceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/getExperienceInfo")) {
            String JSONStr = Parser.getData(request);
            JSONObject requestObject = JSONObject.fromObject(JSONStr);
            String tel = requestObject.getString("userTel");

            ExperienceDao dao = new ExperienceDao((Connection) getServletContext().getAttribute("Connection"));
            try {
                List<ExperienceEntity> experienceList = dao.getExperienceEntityListByTel(tel);
                JSONObject responseObject = new JSONObject();
                responseObject.put("experienceList", experienceList);
                Parser.sendRes(response, responseObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (request.getServletPath().equals("/setExperienceInfo")) {

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
