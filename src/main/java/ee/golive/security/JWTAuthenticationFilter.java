package ee.golive.security;

import ee.golive.services.UserServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

  private UserServiceImpl userService;

  public JWTAuthenticationFilter(UserServiceImpl userService) {
    this.userService = userService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext.getAuthentication() == null || !securityContext.getAuthentication().isAuthenticated()) {
      Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest)request, userService);
      securityContext.setAuthentication(authentication);
    }
    filterChain.doFilter(request,response);
  }
}
