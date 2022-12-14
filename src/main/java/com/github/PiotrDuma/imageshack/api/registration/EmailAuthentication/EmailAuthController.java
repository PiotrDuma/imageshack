package com.github.PiotrDuma.imageshack.api.registration.EmailAuthentication;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.api.registration.RegistrationService;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
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
@RequestMapping("/register/auth")
public class EmailAuthController {
  private static final String INVALID_EMAIL = "Invalid email address";
  private static final String SUCCESS_MESSAGE = "Mail has been sent. Please check your email";
  private final RegistrationService registrationService;
  private final Validator validator;

  @Autowired
  public EmailAuthController(RegistrationService registrationService,
      @Qualifier("emailValidator") Validator validator) {
    this.registrationService = registrationService;
    this.validator = validator;
  }

  @GetMapping
  public String getPage(Model model){
    model.addAttribute("email", new Email());
    return "auth";
  }

  @PostMapping
  public String sendEmail(@ModelAttribute("email") Email email, BindingResult bindingResult, Model model){
    if(!validator.validate(email.getEmailAddress())){
      bindingResult.addError(new FieldError("emailAddress", "email",
          email.getEmailAddress(), false, null, null, INVALID_EMAIL));
      return "auth";
    }
    try{
      this.registrationService.sendAccountAuthenticationToken(email.getEmailAddress());
      model.addAttribute("message", SUCCESS_MESSAGE);
    }catch (RegistrationEmailSendingException ex){
      ObjectError error = new ObjectError("sendingFailure", ex.getMessage());
      bindingResult.addError(error);
      return "auth";
    }
    return "auth";
  }

  private class Email{
    private String emailAddress;

    public Email() {
    }

    public String getEmailAddress() {
      return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
    }
  }
}
