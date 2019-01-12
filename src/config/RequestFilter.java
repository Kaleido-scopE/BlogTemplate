package config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//过滤所有HTTP请求，防止盗链
@WebFilter(filterName = "RequestFilter", urlPatterns = "/*")
public class RequestFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String referer = request.getHeader("Referer");
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();

        //当referer为空或请求URL起始与本机不符，而请求路径不为"/"且不为"/favicon.ico"时，返回403
        if ((referer == null || !requestURL.startsWith(referer)) && !requestURI.equals("/") && !requestURI.equals("/favicon.ico"))
            response.sendError(403);
        else
            chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
