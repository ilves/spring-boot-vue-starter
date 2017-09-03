package ee.golive.validators;

import ee.golive.controllers.api.models.user.*;
import ee.golive.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

  private final UserServiceImpl userService;

  @Autowired
  public UserValidator(UserServiceImpl userService) {
    this.userService = userService;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return getScenario(aClass) != null;
  }

  private Scenario getScenario(Class<?> aClass) {
    if (CreateUser.class.equals(aClass)) {
      return Scenario.CREATE;
    } else if (UpdateUser.class.equals(aClass)) {
      return Scenario.UPDATE;
    } else if (RegisterUser.class.equals(aClass)) {
      return Scenario.REGISTER;
    }
    return null;
  }

  @Override
  public void validate(Object o, Errors errors) {
    Scenario scenario = getScenario(o.getClass());
    Assert.notNull(scenario, "Scenario can't be null");
    UserCommon user = (UserCommon)o;
    validateCommon(user, errors);
    switch (scenario) {
      case REGISTER:
        validateRegister((RegisterUser)o, errors);
        break;
      case CREATE:
        validateCreate((CreateUser)o, errors);
        break;
      case UPDATE:
        validateUpdate((UpdateUser)o, errors);
        break;
    }
  }

  private void validateCommon(UserCommon user, Errors errors) {
    ValidationUtils.rejectIfEmpty(errors, "name", "user.name.empty");
    ValidationUtils.rejectIfEmpty(errors, "email", "user.name.empty");
    Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    if (user.getEmail() != null && !(pattern.matcher(user.getEmail()).matches())) {
      errors.rejectValue("email", "user.email.invalid");
    }
  }

  private void validateRegister(RegisterUser user, Errors errors) {
    checkEmail(user.getEmail(), null, errors);
    checkPasswordRepeat(user.getPassword(), user.getPasswordRepeat(), errors);
  }

  private void validateCreate(CreateUser user, Errors errors) {
    checkEmail(user.getEmail(), null, errors);
  }

  private void validateUpdate(UpdateUser user, Errors errors) {
    checkEmail(user.getEmail(), user.getId(), errors);
    checkPasswordRepeat(user.getPassword(), user.getPasswordRepeat(), errors);
  }

  private void checkEmail(String email, Long id, Errors errors) {
    UserResponse existingEmail = userService.findByEmail(email);
    if (existingEmail != null && (id == null || !id.equals(existingEmail.getId()))) {
      errors.rejectValue("email", "user.email.existing", "User with this email already exists");
    }
  }

  private void checkPasswordRepeat(String password, String passwordRepeat, Errors errors) {
    if (!password.equals(passwordRepeat)) {
      errors.rejectValue("passwordRepeat",
        "user.passwordRepeat.no_mach",
        "Password does not match the confirm password");
    }
  }

  enum Scenario {
    CREATE, UPDATE, REGISTER
  }
}
