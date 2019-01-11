package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Parser {
    //从HttpServlet中读取JSON，返回字符串
    public static String getData(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = request.getInputStream();

        int bytesRead;
        byte[] bytes = new byte[1024];

        while ((bytesRead = in.read(bytes)) > 0)
            out.write(bytes, 0, bytesRead);

        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    //向HttpServletResponse中写入指定的JSON字符串
    public static void sendRes(HttpServletResponse response, String resStr) throws IOException {
        response.setHeader("content-type", "application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(resStr.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
