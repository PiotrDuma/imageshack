package com.github.PiotrDuma.imageshack.api.registration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterTransactionException;
import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import com.github.PiotrDuma.imageshack.config.extensions.ExtendedMockMvcResultMatchers;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ControllerTestConfig
@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {
  private static final String USERNAME = "usr123";
  private static final String EMAIL = "u123@java.com";
  private static final String PASSWORD = "Passwd123";

  @Autowired
  private MockMvc mockMvc;

  @MockBean(name = "emailValidator")
  private Validator emailValidator;
  @MockBean(name = "usernameValidator")
  private Validator usernameValidator;
  @MockBean(name = "passwordValidator")
  private Validator passwordValidator;
  @MockBean
  private RegistrationService registrationService;

  @Test
  void shouldReturnTemplateWithDto() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/register"))
        .andExpectAll(status().isOk(), content().contentType("text/html;charset=UTF-8"))
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("dto"));
  }

  @Test
  void postMethodShouldReturnErrorWhenUserRegistrationThrowsAuthException()throws Exception{
    String message = "Something went wrong. Try again later.";

    skipIOValidation();
    doThrow(new RegisterTransactionException(message)).when(this.registrationService).register(any());

    mockMvc.perform(getRequestBuilder())
        .andExpect(view().name("register"))
        .andExpect(status().is5xxServerError()) //500
        .andExpect(model().hasErrors())
        .andExpect(ExtendedMockMvcResultMatchers
            .hasGlobalError("dto", "registrationFailure", message))
        .andExpect(model().errorCount(1));
  }

  @Test
  void postMethodWithInvalidEmailShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";

    when(emailValidator.getExceptionMessage()).thenReturn(message);
    when(emailValidator.validate(any())).thenReturn(false);
    when(usernameValidator.validate(any())).thenReturn(true);
    when(passwordValidator.validate(any())).thenReturn(true);

    mockMvc.perform(getRequestBuilder())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("dto"))
        .andExpect(model().attributeHasErrors("dto"))
        .andExpect(model().attributeHasFieldErrors("dto", "email"));
  }

  @Test
  void postMethodWithInvalidUsernameShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";

    when(usernameValidator.getExceptionMessage()).thenReturn(message);
    when(emailValidator.validate(any())).thenReturn(true);
    when(usernameValidator.validate(any())).thenReturn(false);
    when(passwordValidator.validate(any())).thenReturn(true);

    mockMvc.perform(getRequestBuilder())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("dto"))
        .andExpect(model().attributeHasErrors("dto"))
        .andExpect(model().attributeHasFieldErrors("dto", "username"));
  }

  @Test
  void postMethodWithInvalidPasswordShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";

    when(passwordValidator.getExceptionMessage()).thenReturn(message);
    when(emailValidator.validate(any())).thenReturn(true);
    when(usernameValidator.validate(any())).thenReturn(true);
    when(passwordValidator.validate(any())).thenReturn(false);

    mockMvc.perform(getRequestBuilder())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("dto"))
        .andExpect(model().attributeHasErrors("dto"))
        .andExpect(model().attributeHasFieldErrors("dto", "password"));
  }

  @Test
  void postMethodWithInvalidAllFieldsShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";

    when(emailValidator.validate(any())).thenReturn(false);
    when(emailValidator.getExceptionMessage()).thenReturn(message);
    when(usernameValidator.validate(any())).thenReturn(false);
    when(usernameValidator.getExceptionMessage()).thenReturn(message);
    when(passwordValidator.validate(any())).thenReturn(false);
    when(passwordValidator.getExceptionMessage()).thenReturn(message);

    mockMvc.perform(getRequestBuilder())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("dto"))
        .andExpect(model().attributeHasErrors("dto"))
        .andExpect(model().attributeHasFieldErrors("dto", "email"))
        .andExpect(model().attributeHasFieldErrors("dto", "username"))
        .andExpect(model().attributeHasFieldErrors("dto", "password"));
  }

  private MockHttpServletRequestBuilder getRequestBuilder(){
    AppUserDTO dto = new AppUserDTO(USERNAME, EMAIL, PASSWORD);
    return MockMvcRequestBuilders.post("/register", dto)
        .param("username", USERNAME)
        .param("email", EMAIL)
        .param("password", PASSWORD);
  }

  private void skipIOValidation(){
    when(emailValidator.validate(any())).thenReturn(true);
    when(usernameValidator.validate(any())).thenReturn(true);
    when(passwordValidator.validate(any())).thenReturn(true);
  }
}