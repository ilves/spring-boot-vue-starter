package ee.golive.security;

import ee.golive.model.Principal;
import ee.golive.services.UserServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static java.util.Collections.emptyList;

public class TokenAuthenticationService {

  static final long EXPIRATIONTIME = 864_000_000; // 10 days
  static final String SECRET = "ThisIsASecret";
  static final String TOKEN_PREFIX = "Bearer";
  static final String HEADER_STRING = "Authorization";

  public static void addAuthentication(HttpServletResponse res, Authentication authentication) {
    Principal principal = ((Principal) authentication.getPrincipal());
    String JWT = Jwts.builder()
      .setSubject(principal.getId().toString())
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
      .signWith(SignatureAlgorithm.HS512, SECRET)
      .compact();
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    principal.setJwtToken(JWT);
  }

  static Authentication getAuthentication(HttpServletRequest request, UserServiceImpl userService) {
    String token = request.getHeader(HEADER_STRING);
    if (token != null) {
      String _userId = Jwts.parser()
        .setSigningKey(SECRET)
        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
        .getBody()
        .getSubject();

      UserDetails userDetails = null;
      if (_userId != null) {
        userDetails = userService.loadByUserId(Long.valueOf(_userId));
      }

      return userDetails != null ?
        new UsernamePasswordAuthenticationToken(userDetails, null, emptyList()) :
        null;
    }
    return null;
  }
}
