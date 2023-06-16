package com.github.PiotrDuma.imageshack.api.registration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.github.PiotrDuma.imageshack.api.registration.AppUserDTO.Field;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterTransactionException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthAccountException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException;
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
  void postMethodShouldReturnRedirectToLoginPage()throws Exception{
    skipIOValidation();
    doNothing().when(this.registrationService).register(any());
    doNothing().when(this.registrationService).sendAccountAuthenticationToken(any());

    mockMvc.perform(getRequestBuilder())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));
  }

  @Test
  void postMethodShouldReturnWithFieldErrorWhenRegisterIOExceptionIsThrown()throws Exception{
    RegisterIOException exception = new RegisterIOException();
    String usernameMessage = "USERNAME ERROR MESSAGE";
    String passwordMessage = "PASSWORD ERROR MESSAGE";

    exception.addError(Field.USERNAME, usernameMessage);
    exception.addError(Field.PASSWORD, passwordMessage);

    skipIOValidation();
    doThrow(exception).when(this.registrationService).register(any());

    mockMvc.perform(getRequestBuilder())
        .andExpectAll(view().name("register"),
            status().isConflict())
        .andExpectAll(model().attributeExists("dto"),
            model().attributeHasErrors("dto"),
            model().errorCount(2))
        .andExpect(model().attributeHasFieldErrors("dto", "username", "password"));
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
  void postMethodShouldRedirectWhenRegistrationAuthProcessingExceptionIsThrown()throws Exception{
    skipIOValidation();
    doThrow(new RegistrationAuthProcessingException(""))
        .when(this.registrationService).sendAccountAuthenticationToken(any());

    mockMvc.perform(getRequestBuilder())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/register/auth/unsent"));
  }

  @Test
  void postMethodShouldRedirectWhenRegistrationAuthAccountExceptionIsThrown()throws Exception{
    skipIOValidation();
    doThrow(new RegistrationAuthAccountException(""))
        .when(this.registrationService).sendAccountAuthenticationToken(any());

    mockMvc.perform(getRequestBuilder())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/register/auth/unsent"));
  }

  @Test
  void postMethodWithInvalidEmailShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";

    when(emailValidator.getExceptionMessage()).thenReturn(message);
    when(emailValidator.validate(any())).thenReturn(false);
    when(usernameValidator.validate(any())).thenReturn(true);
    when(passwordValidator.validate(any())).thenReturn(true);

    mockMvc.perform(getRequestBuilder())
        .andExpectAll(view().name("register"),
            status().isConflict())
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
        .andExpectAll(view().name("register"),
            status().isConflict())
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
        .andExpectAll(view().name("register"),
            status().isConflict())
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
        .andExpectAll(view().name("register"),
            status().isConflict())
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