package ee.golive.controllers.api.exceptions;

public class ForbiddenException extends Exception {
  public ForbiddenException(String msg) {
    super(msg);
  }
}
