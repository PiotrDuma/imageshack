package com.github.PiotrDuma.imageshack.api.registration.EmailAuthentication;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException;
import com.github.PiotrDuma.imageshack.api.registration.RegistrationService;
import com.github.PiotrDuma.imageshack.exception.type.BadRequestException;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register/auth")
public class EmailAuthController {
  private static final String INVALID_EMAIL = "Invalid email address";
  private static final String SUCCESS_MESSAGE = "Mail has been sent. Please check your email";
  private static final String FAILURE_MESSAGE = "Email sending failure";
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
    model.addAttribute("emailDTO", new EmailDto());
    return "auth";
  }

  @PostMapping
  public String sendEmail(@Valid @ModelAttribute("emailDTO") EmailAuthController.EmailDto email,
      BindingResult bindingResult, Model model, HttpServletResponse response){
    if(!validator.validate(email.getEmailAddress())){
      bindingResult.addError(new FieldError("emailDTO", "emailAddress",
          email.getEmailAddress(), false, null, null, INVALID_EMAIL));
      response.setStatus(HttpServletResponse.SC_CONFLICT);
      return "auth";
    }
    try{
      this.registrationService.sendAccountAuthenticationToken(email.getEmailAddress());
      model.addAttribute("message", SUCCESS_MESSAGE);
    }catch (RegistrationException ex){
      ObjectError error = new ObjectError("sendingFailure", ex.getMessage());
      bindingResult.addError(error);
      response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      return "auth";
    }
    return "auth";
  }

  @RequestMapping("/confirm")
  public String confirm(@RequestParam("email") String email, @RequestParam("token") String token){
    try{
      this.registrationService.authenticate(email, token);
    }catch(RegistrationAuthException ex){
      throw new BadRequestException(ex.getMessage());
    }
    return "redirect:/login";
  }

  @RequestMapping("/unsent")
  public String sendingFailure(RedirectAttributes attributes){
    attributes.addFlashAttribute("sendingFailure", FAILURE_MESSAGE);
    return "redirect:/register/auth";
  }

  private static class EmailDto {
    @Email(message = "It's not email format")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 64, message = "Email address is too long")
    private String emailAddress;

    private EmailDto() {
    }

    public String getEmailAddress() {
      return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
     this.emailAddress = emailAddress;
    }
  }
}
