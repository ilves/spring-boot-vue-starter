package ee.golive.controllers.api.models.user;

public class CreateUser extends UserCommon {
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
