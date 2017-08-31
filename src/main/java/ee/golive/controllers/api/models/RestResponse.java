package ee.golive.controllers.api.models;

import java.util.List;

public class RestResponse<T> {

  boolean success;
  T data;
  List<Error> error;

  public RestResponse() {
    this.success = true;
  }

  public RestResponse(List<Error> error) {
    this.error = error;
    this.success = false;
  }

  public RestResponse(T data) {
    this.data = data;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public List<Error> getError() {
    return error;
  }

  public void setError(List<Error> error) {
    this.error = error;
  }

  public static class Error {
    private String code;
    private String message;
    private String field;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public String getField() {
      return field;
    }

    public void setField(String field) {
      this.field = field;
    }
  }
}
