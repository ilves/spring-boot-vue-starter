package ee.golive.security;

import ee.golive.model.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Auth {

  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public Principal getPrincipal() {
    Authentication authentication = getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return (Principal) authentication.getPrincipal();
    }
    return null;
  }
}
