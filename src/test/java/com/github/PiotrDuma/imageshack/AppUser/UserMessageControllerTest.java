package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ControllerTestConfig
@WebMvcTest(UserMessageController.class)
class UserMessageControllerTest {
  private static final String URL_LOGIN = "http://localhost/login";
  private static final String URL_ANONYMOUS = "/api/securitytest/anonymous";
  @Autowired
  private MockMvc mvc;

  @Test
  @WithMockUser(username = "username", password = "passwd", roles = "USER", authorities = {"OP_READ"})
  public void getRequestShouldAccessWithCorrectUserAuthorities() throws Exception {
    String protectedUrlByAuthority = "/api/securitytest/user/authorityREAD";
    mvc.perform(MockMvcRequestBuilders.get(protectedUrlByAuthority))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void getAnonymousRequestShouldAccessUnprotectedEndpoint() throws Exception{
    mvc.perform(MockMvcRequestBuilders.get(URL_ANONYMOUS))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("hello anonymous"));
  }

  @Test
  public void getAnonymousAuthorizedRequestShouldRedirectToLogin() throws Exception{
    String protectedUrlByAuthority = "/api/securitytest/user/authorityREAD";
    mvc.perform(MockMvcRequestBuilders.get(protectedUrlByAuthority))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl(URL_LOGIN));
  }

  @Test
  @WithMockUser(username = "commonUser", password = "pass", roles = "USER")
  public void getRequestByUserCannotAccessOwnerEndpoint() throws Exception{
    String protectedUrlByRole = "/api/securitytest/owner/role";
    mvc.perform(MockMvcRequestBuilders.get(protectedUrlByRole))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @WithMockUser(username = "commonUser", password = "pass", roles = "USER")
  public void getRequestWithDoubledPrefixPreauthorizeShouldPass() throws Exception{
    String protectedUrlByRole = "/api/securitytest/user/role2";
    mvc.perform(MockMvcRequestBuilders.get(protectedUrlByRole))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("user request works"));
  }

  @Test
  @WithMockUser(username = "owner", password = "pass123", roles = "OWNER")
  public void getRequestByOwnerShouldAccessOwnerEndpointByRole() throws Exception{
    String protectedUrlByRole = "/api/securitytest/owner/role";
    mvc.perform(MockMvcRequestBuilders.get(protectedUrlByRole))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("hello owner by role"));
  }

  @Test
  @WithMockUser(username = "owner", password = "pass123", authorities = "OP_MANAGE")
  public void getRequestByOwnerShouldAccessOwnerEndpointByPrivledge() throws Exception{
    String protectedUrlByRole = "/api/securitytest/owner/authority";
    mvc.perform(MockMvcRequestBuilders.get(protectedUrlByRole))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("hello owner by authority"));
  }

}