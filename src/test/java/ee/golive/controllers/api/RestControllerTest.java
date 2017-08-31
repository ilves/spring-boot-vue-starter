package ee.golive.controllers.api;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
abstract class RestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  ResultActions _post(String url, String request, int status) throws Exception {
    return __(post(url).content(request), status);
  }

  ResultActions _put(String url, String request, int status) throws Exception {
    return __(put(url).content(request), status);
  }

  ResultActions _get(String url, int status) throws Exception {
    return __(get(url), status);
  }

  ResultActions _delete(String url, int status) throws Exception {
    return __(delete(url), status);
  }

  private ResultActions __(MockHttpServletRequestBuilder b, int status) throws Exception {
    ResultActions actions = this.mockMvc.perform(b.contentType(MediaType.APPLICATION_JSON));
    return commonExpectations(actions, status);
  }

  private ResultActions commonExpectations(ResultActions resultActions, int status) throws Exception {
    return resultActions
      .andExpect(status().is(status))
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }
}
