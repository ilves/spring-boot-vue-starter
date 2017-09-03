package ee.golive.validators;

import ee.golive.controllers.api.models.user.UserResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static ee.golive.TestData.testUserDTO;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class UserValidatorTest {

  @Autowired
  UserValidator userValidator;

  @Before
  public void setup() {
  }

  @Test
  public void allCorrect() {
    UserResponse user = testUserDTO(1L, "Taavi Ilves", "test@fb.com");
    Errors errors = validate(user);
    assertFalse(errors.hasErrors());
  }

  @Test
  public void wrongEmail() {
    UserResponse user = testUserDTO(1L, "Taavi Ilves", "weong@");
    Errors errors = validate(user);
    assertTrue(errors.hasErrors());
    assertNotEquals(null, errors.getFieldError("email"));
  }

  @Test
  public void missingEmail() {
    UserResponse user = testUserDTO(1L, "Taavi Ilves", null);
    Errors errors = validate(user);
    assertTrue(errors.hasErrors());
    assertNotEquals(null, errors.getFieldError("email"));
  }

  @Test
  public void missingName() {
    UserResponse user = testUserDTO(1L, null, "test@fb.com");
    Errors errors = validate(user);
    assertTrue(errors.hasErrors());
    assertNotEquals(null, errors.getFieldError("name"));
  }

  private Errors validate(UserResponse userDTO) {
    Errors errors = new BeanPropertyBindingResult(userDTO, "");
    userValidator.validate(userDTO, errors);
    return errors;
  }
}
