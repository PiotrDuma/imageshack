package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
  private static final String REGISTRATION_FAILURE = "Something went wrong. Try again later.";
  private final Validator emailValidator;
  private final Validator usernameValidator;
  private final Validator passwordValidator;
  private final RegistrationService registrationService;

  @Autowired
  public RegistrationController(
      @Qualifier("emailValidator") Validator emailValidator,
      @Qualifier("usernameValidator") Validator usernameValidator,
      @Qualifier("passwordValidator") Validator passwordValidator,
      RegistrationService registrationService){
    this.emailValidator = emailValidator;
    this.usernameValidator = usernameValidator;
    this.passwordValidator = passwordValidator;
    this.registrationService = registrationService;
  }

  @GetMapping
  public String getRegistrationPage(Model model){
    model.addAttribute("dto", new AppUserDTO());
    return "register";
  }

  @PostMapping
  public String registerUser(@Valid @ModelAttribute("dto") AppUserDTO dto,
                       BindingResult bindingResult, Model model){
    if(checkFields(dto, bindingResult).hasErrors()){
      return "register";
    }
    try{
      this.registrationService.register(dto);
      this.registrationService.sendAccountAuthenticationToken(dto.getEmail());
    }catch(RegistrationEmailSendingException ex){
      ObjectError error = new ObjectError("sendingFailure", ex.getMessage());
      bindingResult.addError(error);
      return "register";
    }catch (RuntimeException ex){
      ObjectError error = new ObjectError("registrationFailure", REGISTRATION_FAILURE);
      bindingResult.addError(error);
      return "register";
    }
    return "login";
  }

  private BindingResult checkFields(AppUserDTO dto, BindingResult bindingResult){
    if(!emailValidator.validate(dto.getEmail())){
      bindingResult.addError(new FieldError("dto", "email", dto.getEmail(),
          false, null, null, emailValidator.getExceptionMessage()));
    }
    if(!usernameValidator.validate(dto.getUsername())){
      bindingResult.addError(new FieldError("dto", "username", dto.getUsername(),
          true, null, null, usernameValidator.getExceptionMessage()));
    }
    if(!passwordValidator.validate(dto.getPassword())){
      bindingResult.addError(new FieldError("dto", "password",
          passwordValidator.getExceptionMessage()));
    }
    return bindingResult;
  }
}
