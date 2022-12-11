package com.github.PiotrDuma.imageshack.api.registration.EmailAuthentication;

import com.github.PiotrDuma.imageshack.api.registration.AppUserDTO;
import com.github.PiotrDuma.imageshack.api.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register/confirm")
public class EmailAuth {
  private final RegistrationService registrationService;

  @Autowired
  public EmailAuth(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @GetMapping
  public String getPage(Model model){
    return "registerConfirm";
  }

  @PostMapping
  public String sendEmail(Model model){
    return "registerConfirm";
  }
}
