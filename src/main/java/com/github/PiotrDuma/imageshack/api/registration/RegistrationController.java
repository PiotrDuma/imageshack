package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
  private final Validator emailValidator;
  private final Validator usernameValidator;
  private final Validator passwordValidator;

  @Autowired
  public RegistrationController(
      @Qualifier("emailValidator") Validator emailValidator,
      @Qualifier("usernameValidator") Validator usernameValidator,
      @Qualifier("passwordValidator") Validator passwordValidator){
    this.emailValidator = emailValidator;
    this.usernameValidator = usernameValidator;
    this.passwordValidator = passwordValidator;
  }

  @GetMapping
  public String getRegistrationPage(Model model){
    model.addAttribute("dto", new AppUserDTO());
    return "register";
  }

  @PostMapping
  public String registerUser(@Valid @ModelAttribute("dto") AppUserDTO dto,
                       BindingResult bindingResult, Model model){
    //TODO:validate input.
    return "register";
  }
}
