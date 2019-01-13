package utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Parser {
    //公共路径前缀（设置为打包路径）
    private static final String pathPrefix = "D:\\Project\\Java\\My Page\\out\\artifacts\\My_Page_war_exploded";

    //从HttpServletRequest中读取JSON，返回字符串
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

    //从HttpServletRequest中读取文件流，保存到目标路径
    public static void saveFile(HttpServletRequest request, String targetPath, String fileName) throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1048576);//超过1MB的文件先缓存到磁盘

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(1048576);//文件最大为1MB

        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items)
            if (item.getName() != null) {
                File savedFile = new File(pathPrefix + targetPath, fileName);
                //文件不存在则创建
                if (savedFile.exists() || savedFile.createNewFile())
                    item.write(savedFile);
            }
        System.out.println(fileName + " is saved successfully");
    }
}
