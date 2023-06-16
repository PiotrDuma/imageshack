package com.github.PiotrDuma.imageshack.api.registration.EmailAuthentication;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthAccountException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException;
import com.github.PiotrDuma.imageshack.api.registration.RegistrationService;
import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import com.github.PiotrDuma.imageshack.config.extensions.ExtendedMockMvcResultMatchers;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ControllerTestConfig
@WebMvcTest(EmailAuthController.class)
class EmailAuthControllerTest {
  private static final String EMAIL = "u123@java.com";
  private static final String TOKEN = "tr2_805b_4rav4QK1GuQpbqsjK_16n6v";
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RegistrationService registrationService;
  @MockBean(name = "emailValidator")
  private Validator validator;

  @Test
  void getAuthEndpointShouldReturnTemplateWithAttribute() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/register/auth"))
        .andExpectAll(status().isOk(),
            view().name("auth"))
        .andExpect(model().attributeExists("emailDTO"));
  }

  @Test
  void postAuthEndpointShouldReturnAuthTemplateWithSuccessMessage()throws Exception{
    String message = "Invalid email address";
    when(this.validator.validate(any())).thenReturn(true);
    doNothing().when(this.registrationService).sendAccountAuthenticationToken(any());

    mockMvc.perform(MockMvcRequestBuilders.post("/register/auth")
            .param("emailAddress", EMAIL))
        .andExpectAll(status().isOk(),
            view().name("auth"),
            model().attributeExists("message"));
    verify(this.registrationService, times(1))
        .sendAccountAuthenticationToken(EMAIL);
  }

  @Test
  void postAuthEndpointShouldReturnFieldErrorWhenValidationFails()throws Exception{
    String message = "Invalid email address";
    when(this.validator.validate(any())).thenReturn(false);

    mockMvc.perform(MockMvcRequestBuilders.post("/register/auth")
            .param("emailAddress", EMAIL))
        .andExpectAll(status().isConflict(),
            view().name("auth"),
            model().attributeHasFieldErrors("emailDTO", "emailAddress"));
    verify(this.registrationService, times(0))
        .sendAccountAuthenticationToken(EMAIL);
  }

  @Test
  void postAuthEndpointShouldReturnGlobalErrorWhenRegistrationAuthProcessingExceptionIsThrown()
      throws Exception{
    String message = "exception message";
    when(this.validator.validate(any())).thenReturn(true);
    doThrow(new RegistrationAuthProcessingException(message))
        .when(this.registrationService).sendAccountAuthenticationToken(any());

    mockMvc.perform(MockMvcRequestBuilders.post("/register/auth")
            .param("emailAddress", EMAIL))
        .andExpectAll(status().isServiceUnavailable(),
            view().name("auth"),
            ExtendedMockMvcResultMatchers.hasGlobalError("emailDTO",
                "sendingFailure", message));
    verify(this.registrationService, times(1))
        .sendAccountAuthenticationToken(EMAIL);
  }

  @Test
  void postAuthEndpointShouldReturnWhenRegistrationAuthAccountExceptionIsThrown()
      throws Exception{
    String message = "exception message";
    when(this.validator.validate(any())).thenReturn(true);
    doThrow(new RegistrationAuthAccountException(message))
        .when(this.registrationService).sendAccountAuthenticationToken(any());

    mockMvc.perform(MockMvcRequestBuilders.post("/register/auth")
            .param("emailAddress", EMAIL))
        .andExpectAll(status().isServiceUnavailable(),
            view().name("auth"),
            ExtendedMockMvcResultMatchers.hasGlobalError("emailDTO",
                "sendingFailure", message));
    verify(this.registrationService, times(1))
        .sendAccountAuthenticationToken(EMAIL);
  }

  @Test
  void postConfirmEndpointShouldThrowBadRequestExceptionWhenRegistrationAuthExceptionIsThrown()
      throws Exception{
    String message = "Exception message.";
    doThrow(new RegistrationAuthException(message)).when(this.registrationService)
        .authenticate(any(), any());

    mockMvc.perform(MockMvcRequestBuilders.post("/register/auth/confirm")
        .param("email", EMAIL)
        .param("token", TOKEN))
        .andExpectAll(status().isBadRequest(),
            result -> {
              Exception expectedException = result.getResolvedException();
              assertNotNull(expectedException);
              String expected = result.getResolvedException().getMessage();
              assertNotNull(expected);
              assertEquals(message, result.getResolvedException().getMessage());
            });
    verify(this.registrationService, times(1)).authenticate(EMAIL, TOKEN);
  }

  @Test
  void requestConfirmEndpointShouldRedirectToLoginPage()
      throws Exception{
    doNothing().when(this.registrationService).authenticate(any(), any());

    mockMvc.perform(MockMvcRequestBuilders.post("/register/auth/confirm")
            .param("email", EMAIL)
            .param("token", TOKEN))
        .andExpectAll(status().is3xxRedirection(),
            redirectedUrl("/login"));

    mockMvc.perform(MockMvcRequestBuilders.get("/register/auth/confirm")
            .param("email", EMAIL)
            .param("token", TOKEN))
        .andExpectAll(status().is3xxRedirection(),
            redirectedUrl("/login"));
    verify(this.registrationService, times(2)).authenticate(EMAIL, TOKEN);
  }

  @Test
  void requestUnsentEndpointShouldRedirectToAuthPageWithMessage()
      throws Exception{
    String message = "Email sending failure";
    doNothing().when(this.registrationService).authenticate(any(), any());

    mockMvc.perform(MockMvcRequestBuilders.post("/register/auth/unsent"))
        .andExpectAll(status().is3xxRedirection(),
            redirectedUrl("/register/auth"))
        .andExpect(flash().attribute("sendingFailure", message));

    mockMvc.perform(MockMvcRequestBuilders.get("/register/auth/unsent"))
        .andExpectAll(status().is3xxRedirection(),
            redirectedUrl("/register/auth"))
        .andExpect(flash().attribute("sendingFailure", message));
  }
}