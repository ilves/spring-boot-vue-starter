package ee.golive.controllers.api;

import ee.golive.controllers.api.models.LoginRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static ee.golive.Utils.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@IfProfileValue(name="test-groups", value="integration")
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AccountRestControllerIntegrationTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void successfulLogin() throws Exception {
    mvc.perform(post("/api/account/login")
      .content(asJsonString(createLoginRequest("admin@admin.ee", "admin")))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(header().string("Authorization", containsString("Bearer ")))
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.data.id", is(1)));
  }

  @Test
  public void wrongPassword() throws Exception {
    mvc.perform(post("/api/account/login")
      .content(asJsonString(createLoginRequest("admin@admin.ee", "wrong")))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is4xxClientError())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  private LoginRequest createLoginRequest(String email, String password) {
    LoginRequest request = new LoginRequest();
    request.setPassword(password);
    request.setEmail(email);
    return request;
  }
}
