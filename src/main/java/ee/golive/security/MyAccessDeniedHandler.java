package ee.golive.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.golive.controllers.api.models.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class MyAccessDeniedHandler implements AccessDeniedHandler, AuthenticationFailureHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
    throws IOException, ServletException {
    System.out.println("EEE");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, new RestResponse<>(new String[] {e.getMessage()}));
    out.flush();
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
    throws IOException, ServletException {
    System.out.println("EEE");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, new RestResponse<>(new String[] {e.getMessage()}));
    out.flush();
  }
}
