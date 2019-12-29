package hu.gamesgeek;

import hu.gamesgeek.websocket.dto.UserDTO;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GameFilter implements Filter {

    private final List<String> allowedOrigins = Arrays.asList("http://localhost:3000");

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            addHeaders((HttpServletRequest) request, (HttpServletResponse) response);
        }

        UserDTO user = (UserDTO) ((HttpServletRequestWrapper) request).getSession().getAttribute("user");

        if (user != null){
            chain.doFilter(request, response);
            return;
        }

        String URI = ((HttpServletRequestWrapper) request).getRequestURI();
        switch (URI){
            case "/user":
            case "/login":
            case "/logout":
                chain.doFilter(request, response);
                return;
            default:
//                ((HttpServletResponseWrapper) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
        }
    }

    private void addHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");

        response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "");
        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " + "X-CSRF-TOKEN");
    }

    public void destroy() {

    }

    public void init(FilterConfig filterConfig) {

    }
}
