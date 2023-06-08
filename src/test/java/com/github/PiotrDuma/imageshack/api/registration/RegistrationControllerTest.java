package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(RegistrationController.class)
@ControllerTestConfig
class RegistrationControllerTest {

  @MockBean
  private PasswordEncoder passwordEncoder;
  @MockBean
  private UserDetailsService userDetailsService;

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
  void method() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/register"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("register"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("dto"));
  }
}