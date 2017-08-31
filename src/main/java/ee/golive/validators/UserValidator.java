package ee.golive.validators;

import ee.golive.controllers.api.models.user.CreateUser;
import ee.golive.controllers.api.models.user.UpdateUser;
import ee.golive.controllers.api.models.user.UserCommon;
import ee.golive.controllers.api.models.user.UserResponse;
import ee.golive.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

  @Autowired
  UserServiceImpl userService;

  @Override
  public boolean supports(Class<?> aClass) {
    return UpdateUser.class.equals(aClass) || CreateUser.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    String scenario = "CREATE";
    UserCommon user = (UserCommon)o;
    if (user.getClass().equals(UpdateUser.class)) {
      scenario = "UPDATE";
    }
    ValidationUtils.rejectIfEmpty(errors, "name", "user.name.empty");
    ValidationUtils.rejectIfEmpty(errors, "email", "user.name.empty");
    Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    if (user.getEmail() != null && !(pattern.matcher(user.getEmail()).matches())) {
      errors.rejectValue("email", "user.email.invalid");
    }

    UserResponse existingEmail = userService.findByEmail(user.getEmail());
     /**if (existingEmail != null && (scenario.equals("CREATE") || !((UpdateUser)user).getId().equals(existingEmail.getId()))) {
      errors.rejectValue("email", "user.email.existing", "User with this email already exists");
    }**/
  }
}
