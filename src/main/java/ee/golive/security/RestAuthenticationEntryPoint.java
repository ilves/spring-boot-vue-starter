package ee.golive.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.golive.controllers.api.models.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
    throws IOException, ServletException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, new RestResponse<>(new String[] {e.getMessage()}));
    out.flush();
  }
}
