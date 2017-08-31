package ee.golive.controllers.api;

import ee.golive.controllers.api.models.LoginRequest;
import ee.golive.controllers.api.models.LoginResponse;
import ee.golive.model.Principal;
import ee.golive.controllers.api.models.RestResponse;
import ee.golive.security.Auth;
import ee.golive.security.TokenAuthenticationService;
import ee.golive.services.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/account", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/account", description = "Account related operations", tags = "Account")
public class AccountRestController {

  private final Auth auth;
  private final UserServiceImpl userService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AccountRestController(Auth auth,
                               UserServiceImpl userService,
                               AuthenticationManager authenticationManager) {
    this.auth = auth;
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  @RequestMapping(value = "login", method = RequestMethod.POST)
  @ApiOperation(value = "login", notes = "Login operation")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Success"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 401, message = "Unauthorized")})
  public RestResponse<LoginResponse> loginRequest(@RequestBody LoginRequest loginRequest,
                                                  HttpServletResponse res) {
    Authentication authResult = attemptAuthentication(loginRequest);
    SecurityContextHolder.getContext().setAuthentication(authResult);
    TokenAuthenticationService.addAuthentication(res, authResult);
    Principal principal = auth.getPrincipal();
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setId(principal.getId());
    loginResponse.setAdmin(principal.getUser().getAdmin());
    loginResponse.setJwtToken(principal.getJwtToken());
    loginResponse.setUser(userService.convertToDTO(principal.getUser()));
    return new RestResponse<>(loginResponse);
  }

  private Authentication attemptAuthentication(LoginRequest creds) {
    return authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        creds.getEmail(),
        creds.getPassword(),
        Collections.emptyList()
      )
    );
  }
}
