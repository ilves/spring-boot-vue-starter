package ee.golive;

import ee.golive.controllers.api.models.user.UserResponse;
import ee.golive.entity.User;
import ee.golive.model.Principal;

public class TestData {

  public static UserResponse testUserDTO(Long id) {
    return testUserDTO(id, null, null);
  }

  public static UserResponse testUserDTO(Long id, String name, String email) {
    UserResponse userDTO = new UserResponse();
    userDTO.setId(id);
    userDTO.setName(name);
    userDTO.setEmail(email);
    return userDTO;
  }

  public static User testUser(Long id) {
    User user = new User();
    user.setId(id);
    user.setAdmin(false);
    return user;
  }

  public static Principal testPrincipal(User user, String token) {
    Principal principal = new Principal(user);
    principal.setJwtToken(token);
    return principal;
  }
}
