package com.github.PiotrDuma.imageshack.api.index;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.PiotrDuma.imageshack.config.controller.BasicSecurityTestConfig;
import com.github.PiotrDuma.imageshack.config.controller.ControllerTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ControllerTestConfig(BasicSecurityTestConfig.class)
@WebMvcTest(IndexController.class)
class IndexControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void initController(){
    assertNotNull(mockMvc);
  }

  @Test
  void getMethodShouldReturnIndexTemplate() throws Exception{
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("index"));
  }

  @Test
  void indexEndpointShouldRedirectToMainPage() throws Exception{
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/index"))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.header().string("Location", "/"));
  }
}