package com.github.PiotrDuma.imageshack.api.passwordreset;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recover")
public class PasswordResetController {
  private static final String EMAIL_ATTRIBUTE_MISSING = "Missing session attribute: email";
  private final PasswordResetService service;
  private final Validator emailValidator;
  private final Validator passwordValidator;

  @Autowired
  public PasswordResetController(PasswordResetService service,
      @Qualifier("emailValidator") Validator emailValidator,
      @Qualifier("passwordValidator") Validator passwordValidator) {
    this.service = service;
    this.emailValidator = emailValidator;
    this.passwordValidator = passwordValidator;
  }

  @GetMapping
  public String getRecoverTemplate(Model model){
    model.addAttribute("emailDto", new EmailDto());
    return "recover";
  }

  @PostMapping
  public String sendEmail(@Valid @ModelAttribute("emailDto") EmailDto email,
      BindingResult bindingResult, Model model, HttpServletResponse response) {
  //TODO:
    return "recover";
  }

  @RequestMapping("/confirm")
  public String confirm(@RequestParam("email") String email, @RequestParam("token") String token,
      RedirectAttributes attributes) throws BadRequestException{
    try{
      this.service.authenticate(email, token);
    }catch(Exception ex){
      throw new BadRequestException(ex.getMessage());
    }
    attributes.addFlashAttribute("email", email);
    return "redirect:/recover/reset";
  }

  @GetMapping("/reset")
  public String getResetTemplate(Model model) throws BadRequestException{
    checkSessionAttribute(model);

    model.addAttribute("passDto", new PassDto());
    return "reset";
  }

  @PostMapping("/reset")
  public String reset(@Valid @ModelAttribute("passDto") PassDto passDto,
      BindingResult bindingResult, Model model, HttpServletResponse response){
    checkSessionAttribute(model);
    //TODO: add method arguments
    //TODO: reset service call
    return "redirect:/login";
  }

  private void checkSessionAttribute(Model model) throws BadRequestException{
    if(!model.containsAttribute("email")){
      throw new BadRequestException(EMAIL_ATTRIBUTE_MISSING);
    }
  }

  private static class EmailDto {
    @Email(message = "It's not email format")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 64, message = "Email address is too long")
    private String address;

    private EmailDto() {
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String emailAddress) {
      this.address = emailAddress;
    }
  }

  private static class PassDto {
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Size(max = 32, message = "Password must have less than 32 characters")
    private String password;

    private String password2;

    private PassDto() {
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getPassword2() {
      return password2;
    }

    public void setPassword2(String password2) {
      this.password2 = password2;
    }
  }
}
