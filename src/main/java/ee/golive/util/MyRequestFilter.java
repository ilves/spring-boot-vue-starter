package ee.golive.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class MyRequestFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  throws java.io.IOException, ServletException {
    MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest((HttpServletRequest) request);
    chain.doFilter(requestWrapper, response);
  }

  @Override
  public void destroy() {

  }
}
