package ee.golive.controllers.api;

import ee.golive.controllers.api.exceptions.ForbiddenException;
import ee.golive.controllers.api.exceptions.ValidationException;
import ee.golive.controllers.api.models.LoginRequest;
import ee.golive.controllers.api.models.LoginResponse;
import ee.golive.controllers.api.models.user.CreateUser;
import ee.golive.controllers.api.models.user.RegisterUser;
import ee.golive.controllers.api.models.user.UserResponse;
import ee.golive.model.Principal;
import ee.golive.controllers.api.models.RestResponse;
import ee.golive.security.Auth;
import ee.golive.security.TokenAuthenticationService;
import ee.golive.services.UserServiceImpl;
import ee.golive.validators.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/account", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/account", description = "Account related operations", tags = "Account")
public class AccountRestController {

  private final Auth auth;
  private final UserServiceImpl userService;
  private final AuthenticationManager authenticationManager;
  final private UserValidator userValidator;

  @Autowired
  public AccountRestController(Auth auth,
                               UserServiceImpl userService,
                               UserValidator userValidator,
                               AuthenticationManager authenticationManager) {
    this.auth = auth;
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.userValidator = userValidator;
  }

  @InitBinder("registerUser")
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(userValidator);
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
    userService.updateLastLogin(auth.getPrincipal().getId(), new Date());
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setId(principal.getId());
    loginResponse.setAdmin(principal.getUser().getAdmin());
    loginResponse.setJwtToken(principal.getJwtToken());
    loginResponse.setUser(userService.convertToDTO(principal.getUser()));
    return new RestResponse<>(loginResponse);
  }

  @RequestMapping(value = "register", method = RequestMethod.POST)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Request successful")})
  public RestResponse<UserResponse> register(@Valid @RequestBody RegisterUser userData, Errors errors) throws Exception {
    if (!errors.hasErrors()) {
      return new RestResponse<>(userService.register(userData));
    }
    throw new ValidationException(errors);
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
