package com.github.PiotrDuma.imageshack.api.passwordreset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ControllerTestConfig
@WebMvcTest(PasswordResetController.class)
class PasswordResetControllerTest {
  private static final String EMAIL = "u123@java.com";
  private static final String TOKEN = "tr2_805b_4rav4QK1GuQpbqsjK_16n6v";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PasswordResetService service;

  @Test
  void getRecoverEndpointShouldReturnTemplateWithDto() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/recover"))
        .andExpectAll(status().isOk(), content().contentType("text/html;charset=UTF-8"))
        .andExpect(view().name("recover"))
        .andExpect(model().attributeExists("emailDto"));
  }

  @Test
  void requestConfirmEndpointShouldThrow403WhenServiceAuthThrowsException() throws Exception{
    doThrow(RuntimeException.class).when(this.service).authenticate(any(), any());//TODO: replace exception

    mockMvc.perform(MockMvcRequestBuilders.post("/recover/confirm")
            .param("email", EMAIL)
            .param("token", TOKEN))
        .andExpectAll(status().isBadRequest());

    mockMvc.perform(MockMvcRequestBuilders.get("/recover/confirm")
            .param("email", EMAIL)
            .param("token", TOKEN))
        .andExpectAll(status().isBadRequest());
  }

  @Test
  void requestConfirmEndpointShouldRedirectToResetTemplate() throws Exception{
      doNothing().when(this.service).authenticate(any(), any());

      mockMvc.perform(MockMvcRequestBuilders.post("/recover/confirm")
              .param("email", EMAIL)
              .param("token", TOKEN))
          .andExpectAll(status().is3xxRedirection(),
              redirectedUrl("/recover/reset"));

      mockMvc.perform(MockMvcRequestBuilders.get("/recover/confirm")
              .param("email", EMAIL)
              .param("token", TOKEN))
          .andExpectAll(status().is3xxRedirection(),
              redirectedUrl("/recover/reset"));
      verify(this.service, times(2)).authenticate(EMAIL, TOKEN);
    }
}