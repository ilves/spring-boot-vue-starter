package ee.golive.controllers.api.common;

import ee.golive.controllers.api.exceptions.NotFoundException;
import ee.golive.controllers.api.exceptions.ValidationException;
import ee.golive.controllers.api.models.RestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = { BadCredentialsException.class })
  protected ResponseEntity<Object> handleWrongUsernameOrPassword(RuntimeException ex, WebRequest request) {
    RestResponse restResponse = new RestResponse(Collections.singletonList(
      createError(null,"Wrong username or password", null)));
    return new ResponseEntity<>(restResponse, HttpStatus.UNAUTHORIZED);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    RestResponse restResponse = new RestResponse(Collections.singletonList(
      createError(null, "Message is not readable", null)));
    return new ResponseEntity<>(restResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {NotFoundException.class})
  protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
    RestResponse restResponse = new RestResponse(Collections.singletonList(
      createError(null, ex.getMessage(), null)));
    return new ResponseEntity<>(restResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {ValidationException.class})
  protected ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
    List<RestResponse.Error> errors = new LinkedList<>();
    ex.getErrors().getFieldErrors().forEach(e -> errors.add(createError(e.getCode(), e.getDefaultMessage(), e.getField())));
    RestResponse restResponse = new RestResponse(errors);
    return new ResponseEntity<>(restResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> handleAllOtherExceptions(Exception ex, WebRequest request) {
    List<RestResponse.Error> errors = new LinkedList<>();
    RestResponse restResponse = new RestResponse(errors);
    errors.add(createError(null, "Unknown error", null));
    ex.printStackTrace();
    return new ResponseEntity<>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private RestResponse.Error createError(String code, String message, String field) {
    RestResponse.Error error = new RestResponse.Error();
    error.setCode(code);
    error.setMessage(message);
    error.setField(field);
    return error;
  }
}
