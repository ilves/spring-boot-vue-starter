package ee.golive.controllers.api.models.user;

public class UserResponse extends UserCommon {
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
