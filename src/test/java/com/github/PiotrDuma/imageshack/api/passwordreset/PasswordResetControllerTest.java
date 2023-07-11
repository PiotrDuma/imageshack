package com.github.PiotrDuma.imageshack.api.passwordreset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import com.github.PiotrDuma.imageshack.config.extensions.ExtendedMockMvcResultMatchers;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ControllerTestConfig
@WebMvcTest(PasswordResetController.class)
class PasswordResetControllerTest {
  private static final String EMAIL = "u123@java.com";
  private static final String TOKEN = "tr2_805b_4rav4QK1GuQpbqsjK_16n6v";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EmailService emailService;
  @MockBean
  private PasswordResetService service;
  @MockBean(name = "emailValidator")
  private Validator emailValidator;
  @MockBean(name = "passwordValidator")
  private Validator passwordValidator;

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
  void postSendEmailEndpointShouldCallService() throws Exception{
    String message = "message";
    String subject = "subject";
    mockMvc.perform(MockMvcRequestBuilders.post("/recover")
        .param("email", EMAIL))
        .andExpectAll(
            status().isOk(),
            view().name("recover"));

    verify(this.emailService, times(1)).sendMail(EMAIL, subject, message, false);
  }

  @Test
  void postSendEmailEndpointShouldReturnErrorWhenEmailSendingExceptionIsThrown() throws Exception{
    String message = "exception message";
    doThrow(new EmailSendingException(message)).when(this.emailService)
        .sendMail(any(),any(),any(),any());

    mockMvc.perform(MockMvcRequestBuilders.post("/recover")
            .param("email", EMAIL))
        .andExpectAll(view().name("recover"),
            status().is5xxServerError(),
            model().hasErrors())
        .andExpect(ExtendedMockMvcResultMatchers
            .hasGlobalError("email", "failure", message))
        .andExpect(model().errorCount(1));
  }

  @Test
  void postSendEmailEndpointShouldReturnErrorWhenInvalidEmailAddressExceptionIsThrown() throws Exception{
    String message = "exception message";
    doThrow(new InvalidEmailAddressException(message)).when(this.emailService)
        .sendMail(any(),any(),any(),any());

    mockMvc.perform(MockMvcRequestBuilders.post("/recover")
            .param("email", EMAIL))
        .andExpectAll(view().name("recover"),
            status().is5xxServerError(),
            model().hasErrors())
        .andExpect(ExtendedMockMvcResultMatchers
            .hasGlobalError("email", "failure", message))
        .andExpect(model().errorCount(1));
  }

  @Test
  void postSendEmailEndpointShouldReturnBackWhenEmailIsInvalid() throws Exception{
    String message = "exception message";
    when(this.emailValidator.validate(any())).thenReturn(false);

    mockMvc.perform(MockMvcRequestBuilders.post("/recover")
            .param("email", EMAIL))
        .andExpectAll(view().name("recover"),
            status().isConflict(),
            model().attributeHasFieldErrors("emailDto", "address"))
        .andExpect(model().errorCount(1));
  }

  @Test
  void requestConfirmEndpointShouldRedirectToResetTemplate() throws Exception{
      doNothing().when(this.service).authenticate(any(), any());

    MvcResult postResponse = mockMvc.perform(MockMvcRequestBuilders.post("/recover/confirm")
            .param("email", EMAIL)
            .param("token", TOKEN))
        .andExpectAll(status().is3xxRedirection(),
            redirectedUrl("/recover/reset"))
        .andReturn();

    MvcResult getResponse = mockMvc.perform(MockMvcRequestBuilders.get("/recover/confirm")
              .param("email", EMAIL)
              .param("token", TOKEN))
          .andExpectAll(status().is3xxRedirection(),
              redirectedUrl("/recover/reset"))
          .andReturn();

      verify(this.service, times(2)).authenticate(EMAIL, TOKEN);
      assertEquals(EMAIL, postResponse.getRequest().getSession().getAttribute("email"));
      assertEquals(EMAIL, getResponse.getRequest().getSession().getAttribute("email"));
  }

  @Test
  void getResetEndpointShouldReturnTemplateWithAttributes() throws Exception{
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/recover/reset")
            .sessionAttr("email", EMAIL))
        .andExpectAll(status().isOk(),
            model().attributeExists("passDto"),
            view().name("reset"))
        .andReturn();

    assertEquals(EMAIL, mvcResult.getRequest().getSession().getAttribute("email"));
  }

  @Test
  void getResetEndpointShouldThrow400WhenEmailAttributeIsMissing() throws Exception{
    String message = "Missing session attribute: email";

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/recover/reset"))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertTrue(result.getResponse().getContentAsString().contains(message));
  }

  @Test
  void postResetEndpointShouldThrow400WhenEmailAttributeIsMissing() throws Exception{
    String message = "Missing session attribute: email";

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/recover/reset"))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertTrue(result.getResponse().getContentAsString().contains(message));
  }

  @Test
  void postResetEndpointShouldReturnWhenPasswordIsNotValid() throws Exception{
    String password = "Pass%G G$#^%wd123";
    String password2 = "Pass%G G$#^%wd123";

    when(this.passwordValidator.getExceptionMessage()).thenReturn("validator message");
    when(this.passwordValidator.validate(any())).thenReturn(false);

    mockMvc.perform(MockMvcRequestBuilders.post("/recover/reset")
            .sessionAttr("email", EMAIL)
            .param("password", password)
            .param("password2", password2))
        .andExpectAll(status().isConflict(),
            view().name("reset"),
            model().attributeHasFieldErrors("passDto", "password"));
  }

  @Test
  void postResetEndpointShouldReturnWhenPasswordsNotMatch() throws Exception{
    String password = "Pass%G G$#^%wd123";
    String password2 = "Pass%G";

    when(this.passwordValidator.getExceptionMessage()).thenReturn("validator message");
    when(this.passwordValidator.validate(any())).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.post("/recover/reset")
            .sessionAttr("email", EMAIL)
            .param("password", password)
            .param("password2", password2))
        .andExpectAll(status().isConflict(),
            view().name("reset"),
            ExtendedMockMvcResultMatchers.hasGlobalError("passDto", "duplicateError"));
  }

  @Test
  void postResetEndpointShouldRedirectToLogin() throws Exception{
    String password = "Passwd123";
    String password2 = "Passwd123";

    when(this.passwordValidator.getExceptionMessage()).thenReturn("validator message");
    when(this.passwordValidator.validate(any())).thenReturn(true);
    doNothing().when(this.service).reset(any(), any());

    mockMvc.perform(MockMvcRequestBuilders.post("/recover/reset")
            .sessionAttr("email", EMAIL)
            .param("password", password)
            .param("password2", password2))
        .andExpectAll(status().is3xxRedirection(),
            redirectedUrl("/login"));
  }
}