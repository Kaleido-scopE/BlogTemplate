package config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//过滤所有HTTP请求，防止盗链
@WebFilter(filterName = "RequestFilter", urlPatterns = "/*")
public class RequestFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();

        //当session中没有电话属性且请求路径为"/background.html*"时，返回403
        if (session.getAttribute("currentUserTel") == null && requestURI.startsWith("/background.html"))
            response.sendError(403);
        else
            chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
