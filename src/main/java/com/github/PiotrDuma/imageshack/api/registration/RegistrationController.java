package com.github.PiotrDuma.imageshack.api.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

  @GetMapping(value = "/register")
  public String getRegistrationPage(){
    return "register";
  }

  @PostMapping(value = "/register")
  public String registerUser(){ //TODO: post mapping atributes.
    return "register";
  }
}
