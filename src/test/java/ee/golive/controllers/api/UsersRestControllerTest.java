package ee.golive.controllers.api;

import ee.golive.controllers.api.models.user.UserResponse;
import ee.golive.entity.User;
import ee.golive.security.Auth;
import ee.golive.services.UserServiceImpl;
import ee.golive.validators.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ee.golive.TestData.testPrincipal;
import static ee.golive.TestData.testUserDTO;
import static ee.golive.Utils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UsersRestController.class, secure=false)
public class UsersRestControllerTest extends RestControllerTest {

  @MockBean
  private UserServiceImpl userService;

  @MockBean
  private Auth auth;

  @SpyBean
  private UserValidator userValidator;

  @Before
  public void setup() {
    User user = new User();
    user.setAdmin(true);
    when(userValidator.supports(any(Class.class))).thenReturn(true);
    when(auth.getPrincipal()).thenReturn(testPrincipal(user, "X.Z"));
  }

  @Test
  public void listAllUsersHasData() throws Exception {
    given(userService.findAll(anyInt(), anyInt())).willReturn(testUserPage(
      Arrays.asList(testUserDTO(1L), testUserDTO(2L)), new PageRequest(1,10), 2L));
    _get("/api/users/", 200)
    .andExpect(jsonPath("$.data").isArray())
    .andExpect(jsonPath("$.data[0].id", is(1)));
  }

  @Test
  public void listAllUsersNoData() throws Exception {
    given(userService.findAll(anyInt(), anyInt())).willReturn(testUserPage(
      Collections.emptyList(), new PageRequest(1,10), 0L));
    _get("/api/users/", 200)
      .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  public void getUser() throws Exception {
    given(userService.findById(1L)).willReturn(testUserDTO(1L));
    _get("/api/users/1", 200)
      .andExpect(jsonPath("$.data.id", is(1)));
  }

  @Test
  public void getUserNotFound() throws Exception {
    given(userService.findById(1L)).willReturn(null);
    _get("/api/users/1", 404)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error[0]", is("Not found")));
  }

  @Test
  public void createUser() throws Exception {
    UserResponse newUser = testUserDTO(null, "Test User", "user@user.ee");
    given(userService.create(any())).willReturn(testUserDTO(1L, "Test User", "user@user.ee"));
    _post("/api/users", asJsonString(newUser), 200)
      .andExpect(jsonPath("$.data.id", is(1)))
      .andExpect(jsonPath("$.data.name", is("Test User")))
      .andExpect(jsonPath("$.data.email", is("user@user.ee")));
  }

  @Test
  public void createUserValidationError() throws Exception {
    doAnswer(invocationOnMock -> {
      Errors errors = (Errors) invocationOnMock.getArguments()[1];
      errors.reject("forcing some error");
      return null;
    }).when(userValidator).validate(anyObject(), any(Errors.class));
    _post("/api/users", asJsonString(testUserDTO(1L)), 400)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error").isNotEmpty());
  }

  @Test
  public void deleteUser() throws Exception {
    given(userService.findById(1L)).willReturn(testUserDTO(1L));
    _delete("/api/users/1", 200)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error").isEmpty());
  }

  @Test
  public void deleteUserNotFound() throws Exception {
    given(userService.findById(1L)).willReturn(null);
    _delete("/api/users/1", 404)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error").isNotEmpty());
  }

  @Test
  public void updateUser() throws Exception {
    UserResponse oldUser = testUserDTO(1L, "Old Name", "user@user.ee");
    UserResponse updateData = testUserDTO(null, "New Name", "user@user.ee");
    given(userService.findById(1L)).willReturn(oldUser);
    given(userService.update(any(), any())).willReturn(testUserDTO(1L, "New Name", "user@user.ee"));
    _put("/api/users/1", asJsonString(updateData), 200)
      .andExpect(jsonPath("$.data.id", is(1)))
      .andExpect(jsonPath("$.data.name", is("New Name")));
  }

  @Test
  public void updateUserNotFound() throws Exception {
    UserResponse updateData = testUserDTO(null, "New Name", "user@user.ee");
    given(userService.findById(1L)).willReturn(null);
    _put("/api/users/1", asJsonString(updateData), 404)
      .andExpect(jsonPath("$.data").isEmpty())
      .andExpect(jsonPath("$.error").isNotEmpty());
  }

  protected Page<UserResponse> testUserPage(List<UserResponse> dtoList, Pageable pageable, Long total) {
    return new PageImpl<>(dtoList, pageable, total);
  }
}
