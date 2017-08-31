package ee.golive.controllers.api;

import ee.golive.controllers.api.exceptions.ForbiddenException;
import ee.golive.controllers.api.exceptions.NotFoundException;
import ee.golive.controllers.api.exceptions.ValidationException;
import ee.golive.controllers.api.models.PageableRestResponse;
import ee.golive.controllers.api.models.RestResponse;
import ee.golive.controllers.api.models.user.CreateUser;
import ee.golive.controllers.api.models.user.UserResponse;
import ee.golive.controllers.api.models.user.UpdateUser;
import ee.golive.security.Auth;
import ee.golive.services.UserServiceImpl;
import ee.golive.validators.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Api(value = "/users", description = "Users related operations", tags = "Users")
public class UsersRestController {

  final private UserServiceImpl userService;
  final private UserValidator userValidator;
  final private Auth auth;

  @Autowired
  public UsersRestController(UserServiceImpl userService, UserValidator userValidator, Auth auth) {
    this.userService = userService;
    this.userValidator = userValidator;
    this.auth = auth;
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(userValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @ApiOperation(value = "all", notes = "Returns list of users")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Request successful"),
    @ApiResponse(code = 401, message = "User not authorized")})
  public PageableRestResponse<List<UserResponse>> listAllUsers(
    @RequestParam(value = "page", defaultValue = "1", required = false) int page,
    @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
    return new PageableRestResponse<>(userService.findAll(page, size));
  }

  @RequestMapping(method = RequestMethod.POST)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Request successful"),
    @ApiResponse(code = 401, message = "User not authorized"),
    @ApiResponse(code = 403, message = "User has not permission")})
  public RestResponse<UserResponse> create(@Valid @RequestBody CreateUser userDTO, Errors errors) throws Exception {
    if (!auth.getPrincipal().getUser().getAdmin()) {
      throw new ForbiddenException("Forbidden, you need administrator rights");
    }
    if (!errors.hasErrors()) {
      return new RestResponse<>(userService.create(userDTO));
    }
    throw new ValidationException(errors);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Request successful"),
    @ApiResponse(code = 401, message = "User not authorized")})
  public RestResponse<UserResponse> get(@PathVariable Long id) throws Exception {
    UserResponse userDTO = userService.findById(id);
    if (userDTO == null) {
      throw new NotFoundException("Not found");
    }
    return new RestResponse<>(userDTO);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Request successful"),
    @ApiResponse(code = 401, message = "User not authorized"),
    @ApiResponse(code = 403, message = "User has not permission")})
  public RestResponse<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUser dto) throws Exception {
    Thread.sleep(5000);
    if (!auth.getPrincipal().getUser().getAdmin()) {
      throw new ForbiddenException("Forbidden, you need administrator rights");
    }
    UserResponse userDTO = userService.findById(id);
    if (userDTO == null) {
      throw new NotFoundException("Not found");
    }
    return new RestResponse<>(userService.update(id, dto));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Request successful"),
    @ApiResponse(code = 401, message = "User not authorized"),
    @ApiResponse(code = 403, message = "User has not permission")})
  public RestResponse<Object> delete(@PathVariable Long id) throws Exception {
    if (!auth.getPrincipal().getUser().getAdmin()) {
      throw new ForbiddenException("Forbidden, you need administrator rights");
    }
    UserResponse userDTO = userService.findById(id);
    if (userDTO == null) {
      throw new NotFoundException("User not found");
    }
    userService.delete(id);
    return new RestResponse<>();
  }
}
