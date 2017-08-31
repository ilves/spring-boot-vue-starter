package ee.golive.controllers.api.models;

import ee.golive.controllers.api.models.user.UserResponse;

public class LoginResponse {

  private String jwtToken;
  private Long id;
  private UserResponse user;
  private Boolean isAdmin;

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserResponse getUser() {
    return user;
  }

  public void setUser(UserResponse user) {
    this.user = user;
  }

  public Boolean getAdmin() {
    return isAdmin;
  }

  public void setAdmin(Boolean admin) {
    isAdmin = admin;
  }
}
