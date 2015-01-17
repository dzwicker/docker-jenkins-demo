package demo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.spi.CorsHeaders;

@WebFilter("/*")
public class CORSFilter implements Filter {

    private static final String HEADERS = "Origin, X-Requested-With, Content-Type, Accept, Auth-Token";
    private static final String METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
    private static final String TRUE = "true";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp,
            final FilterChain chain) throws ServletException, IOException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) resp;

        // TODO: This needs to be restricted in the future.
        final String origin = request.getHeader(CorsHeaders.ORIGIN);
        response.addHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);

        response.addHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HEADERS);
        response.addHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, METHODS);
        response.addHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, TRUE);

        chain.doFilter(req, resp);

    }

    @Override
    public void init(final FilterConfig config) throws ServletException {
    }

}
