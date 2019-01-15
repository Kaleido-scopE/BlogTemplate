package servlet;

import net.sf.json.JSONObject;
import utils.Parser;
import utils.SMSManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "VerifyServlet", urlPatterns = "/verify")
public class VerifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String JSONStr = Parser.getData(request);
        JSONObject requestObject = JSONObject.fromObject(JSONStr);
        String tel = requestObject.getString("inputtedTel");

        SMSManager smsManager = new SMSManager(tel, "1");//默认有效时间为1分钟
        String verificationCode = smsManager.sendMessage();

        //将验证码放入Session中，用于在登录时验证
        HttpSession session = request.getSession();
        session.setAttribute("verificationCode", verificationCode);

        Parser.sendRes(response, "");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
