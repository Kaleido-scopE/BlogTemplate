package servlet;

import dao.PicturesDao;
import net.sf.json.JSONArray;
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

@WebServlet(name = "PictureServlet", urlPatterns = "/getPictures")
public class PictureServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String JSONStr = Parser.getData(request);
        JSONObject requestObject = JSONObject.fromObject(JSONStr);
        String tel = requestObject.getString("userTel");

        PicturesDao dao = new PicturesDao((Connection) getServletContext().getAttribute("Connection"));
        try {
            JSONObject object;
            JSONArray responseArray = new JSONArray();
            List<String> allYear = dao.getAllYearByTel(tel);
            for (String year : allYear) {
                object = new JSONObject();
                object.put("year", year);
                object.put("pathList", dao.getPathByTelAndYear(tel, year));
                responseArray.add(object);
            }
            Parser.sendRes(response, responseArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
