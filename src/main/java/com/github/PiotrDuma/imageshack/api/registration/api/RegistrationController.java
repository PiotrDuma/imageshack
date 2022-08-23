package com.github.PiotrDuma.imageshack.api.registration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
  private final RegistrationService registrationService;

  @Autowired
  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  //TODO: create temnplate
  @GetMapping
  public String getController(){
    return "register";
  }

  //TODO: checkout parameters sent by template
  @PostMapping
  public void registerNewUser(@ModelAttribute AppUserDTO appUserDTO){
    registrationService.registerUser(appUserDTO);
  }
}
