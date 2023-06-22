package com.github.PiotrDuma.imageshack.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.imageshack.api.index.IndexController;
import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import com.github.PiotrDuma.imageshack.exception.type.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ControllerTestConfig
@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

  @MockBean
  private IndexController exampleComponent;

  @Autowired
  private MockMvc mvc;

  @Test
  void shouldCatchNonExistentEndpoint() throws Exception{
    String expectedMessage = "Page not found";
    MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/login/this/location/not/exists"))
        .andExpect(status().isNotFound())
        .andExpect(model().attributeExists("exception"))
        .andReturn();

    assertTrue(result.getResponse().getContentAsString().contains(expectedMessage));
  }

  @Test
  void shouldCatchRuntimeException() throws Exception{
    String exMessage = "RUNTIME EXCEPTION THROWN";
    RuntimeException ex = new RuntimeException(exMessage);

    Mockito.doThrow(ex).when(exampleComponent).redirect();

    MvcResult result = mvc.perform(
            MockMvcRequestBuilders.get("/index"))//endpoint calling redirect method
        .andExpect(status().isInternalServerError())
        .andReturn();

    assertTrue(result.getResponse().getContentAsString().contains(exMessage));
  }

  @Test
  void shouldCatchCustomThrownException() throws Exception{
    String exMessage = "BAD REQUEST EXCEPTION";
    RuntimeException ex = new BadRequestException(exMessage);

    Mockito.doThrow(ex).when(exampleComponent).redirect();

    MvcResult result = mvc.perform(
            MockMvcRequestBuilders.get("/index"))//endpoint calling redirect method
        .andExpect(status().isBadRequest())
        .andReturn();

    assertTrue(result.getResponse().getContentAsString().contains(exMessage));
  }
}