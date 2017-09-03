package ee.golive.controllers.api.models.user;

import java.util.Date;

public class UserResponse extends UserCommon {
  private Long id;
  private Date lastLogin;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }
}
