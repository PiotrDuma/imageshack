package com.github.PiotrDuma.imageshack.api.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
  private final RegistrationService registrationService;

  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @GetMapping(value = "/register")
  public String getRegistrationPage(){
    return "register";
  }

  @PostMapping(value = "/register")
  public String registerUser(){ //TODO: post mapping atributes.
    this.registrationService.sendAccountAuthenticationToken("any");
    return "register";
  }
}
