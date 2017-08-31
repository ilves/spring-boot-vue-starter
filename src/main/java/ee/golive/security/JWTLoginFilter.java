package ee.golive.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.golive.controllers.api.models.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;



public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

  public JWTLoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest req, HttpServletResponse res)
    throws AuthenticationException, IOException, ServletException {
    LoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);
    return getAuthenticationManager().authenticate(
      new UsernamePasswordAuthenticationToken(
        creds.getEmail(),
        creds.getPassword(),
        Collections.emptyList()
      )
    );
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
                                          HttpServletResponse res,
                                          FilterChain chain,
                                          Authentication auth) throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(auth);
    TokenAuthenticationService.addAuthentication(res, auth);
    chain.doFilter(req, res);
  }
}
