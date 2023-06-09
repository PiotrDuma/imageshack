package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


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
  void initController(){
    assertNotNull(mockMvc);
  }

  @Test
  void shouldReturnTemplateWithDto() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/register"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("register"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("dto"));
  }

  @Test
  void postMethodWithInvalidEmailShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";
    AppUserDTO dto = new AppUserDTO(USERNAME, EMAIL, PASSWORD);
    when(emailValidator.getExceptionMessage()).thenReturn(message);
    when(emailValidator.validate(any())).thenReturn(false);
    when(usernameValidator.validate(any())).thenReturn(true);
    when(passwordValidator.validate(any())).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.post("/register", dto)
        .param("username", USERNAME)
        .param("email", EMAIL)
        .param("password", PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("register"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasErrors("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("dto", "email"));
  }

  @Test
  void postMethodWithInvalidUsernameShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";
    AppUserDTO dto = new AppUserDTO(USERNAME, EMAIL, PASSWORD);
    when(usernameValidator.getExceptionMessage()).thenReturn(message);
    when(emailValidator.validate(any())).thenReturn(true);
    when(usernameValidator.validate(any())).thenReturn(false);
    when(passwordValidator.validate(any())).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.post("/register", dto)
            .param("username", USERNAME)
            .param("email", EMAIL)
            .param("password", PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("register"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasErrors("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("dto", "username"));
  }

  @Test
  void postMethodWithInvalidPasswordShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";
    AppUserDTO dto = new AppUserDTO(USERNAME, EMAIL, PASSWORD);
    when(passwordValidator.getExceptionMessage()).thenReturn(message);
    when(emailValidator.validate(any())).thenReturn(true);
    when(usernameValidator.validate(any())).thenReturn(true);
    when(passwordValidator.validate(any())).thenReturn(false);

    mockMvc.perform(MockMvcRequestBuilders.post("/register", dto)
            .param("username", USERNAME)
            .param("email", EMAIL)
            .param("password", PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("register"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasErrors("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("dto", "password"));
  }

  @Test
  void postMethodWithInvalidAllFieldsShouldReturnBackWithFieldError() throws Exception{
    String message = "INVALID FIELD VALUE MESSAGE";
    AppUserDTO dto = new AppUserDTO(USERNAME, EMAIL, PASSWORD);

    when(emailValidator.validate(any())).thenReturn(false);
    when(emailValidator.getExceptionMessage()).thenReturn(message);
    when(usernameValidator.validate(any())).thenReturn(false);
    when(usernameValidator.getExceptionMessage()).thenReturn(message);
    when(passwordValidator.validate(any())).thenReturn(false);
    when(passwordValidator.getExceptionMessage()).thenReturn(message);

    mockMvc.perform(MockMvcRequestBuilders.post("/register", dto)
            .param("username", USERNAME)
            .param("email", EMAIL)
            .param("password", PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("register"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasErrors("dto"))
        .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("dto", "email"))
        .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("dto", "username"))
        .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("dto", "password"));
  }
}