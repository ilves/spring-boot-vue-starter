package ee.golive.controllers.api;

import ee.golive.entity.User;
import ee.golive.controllers.api.models.LoginRequest;
import ee.golive.model.Principal;
import ee.golive.security.Auth;
import ee.golive.services.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static ee.golive.TestData.testPrincipal;
import static ee.golive.TestData.testUser;
import static ee.golive.TestData.testUserDTO;
import static ee.golive.Utils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountRestController.class, secure=false)
public class AccountRestControllerTest extends RestControllerTest {

  @MockBean
  private Auth auth;

  @MockBean
  private UserServiceImpl userService;

  @MockBean
  AuthenticationManager authenticationManager;

  private User user;
  private Principal principal;

  @Before
  public void setup() {
    user = testUser(1L);
    principal = testPrincipal(user, "X.YZ");
  }

  @Test
  public void successfulLogin() throws Exception {
    given(userService.loadUserByUsername(any())).willReturn(principal);
    given(auth.getPrincipal()).willReturn(principal);
    given(userService.convertToDTO(user)).willReturn(testUserDTO(1L));
    given(authenticationManager.authenticate(any())).willReturn(new UsernamePasswordAuthenticationToken(principal, null));
    _post("/api/account/login", asJsonString(testLoginRequest("admin@admin.ee", "admin")), 200)
      .andExpect(jsonPath("$.data.id", is(1)))
      .andExpect(jsonPath("$.data.admin", is(false)))
      .andExpect(jsonPath("$.data.user.id").exists())
      .andExpect(jsonPath("$.data.jwtToken").isNotEmpty());
  }

  @Test
  public void wrongUsernameOrPassword() throws Exception {
    given(userService.loadUserByUsername(any())).willReturn(principal);
    given(auth.getPrincipal()).willReturn(null);
    given(userService.convertToDTO(user)).willReturn(testUserDTO(1L));
    given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("Wrong username or password"));
    _post("/api/account/login", asJsonString(testLoginRequest("user@user.ee", "admin")), 401)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error[0]").isNotEmpty());
  }

  @Test
  public void noLoginRequest() throws Exception {
    _post("/api/account/login", asJsonString(null), 400)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error[0]").isNotEmpty());
  }

  @Test
  public void randomBody() throws Exception {
    _post("/api/account/login", "{'random:'XYZ'}", 400)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error[0]").isNotEmpty());
  }

  private LoginRequest testLoginRequest(String email, String password) {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);
    return loginRequest;
  }
}
