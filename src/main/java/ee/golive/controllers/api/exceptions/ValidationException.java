package ee.golive.controllers.api.exceptions;

import org.springframework.validation.Errors;

public class ValidationException extends Exception {

  private Errors errors;

  public ValidationException(Errors errors) {
    this.errors = errors;
  }

  public Errors getErrors() {
    return errors;
  }
}
