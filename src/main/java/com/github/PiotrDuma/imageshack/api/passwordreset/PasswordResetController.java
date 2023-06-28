package com.github.PiotrDuma.imageshack.api.passwordreset;

import com.github.PiotrDuma.imageshack.exception.type.BadRequestException;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

@Controller
@RequestMapping("/recover")
public class PasswordResetController {
  private static final String EMAIL_ATTRIBUTE_MISSING = "Missing session attribute: email";
  private static final String INVALID_DUPLICATE = "Provided passwords don't match";
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
      HttpSession session) throws BadRequestException{
    try{
      this.service.authenticate(email, token);
      session.setAttribute("email", email);
    }catch(Exception ex){
      throw new BadRequestException(ex.getMessage());
    }
    return "redirect:/recover/reset";
  }

  @GetMapping("/reset")
  public String getResetTemplate(Model model, HttpSession session) throws BadRequestException{
    checkSessionAttribute(session);
    model.addAttribute("passDto", new PassDto());
    return "reset";
  }

  @PostMapping("/reset")
  public String reset(@Valid @ModelAttribute("passDto") PassDto passDto,
      BindingResult bindingResult, Model model, HttpServletResponse response, HttpSession session)
      throws RuntimeException{

    checkSessionAttribute(session);
    validatePassword(passDto, bindingResult);
    if(bindingResult.hasErrors()){
      response.setStatus(HttpServletResponse.SC_CONFLICT);
      return "reset";
    }

    try{
      String email = (String) session.getAttribute("email");
      this.service.reset(email, passDto.getPassword());
    }catch (ClassCastException ex){
      throw new RuntimeException(EMAIL_ATTRIBUTE_MISSING);
    }catch(PasswordResetException ex){
      throw new RuntimeException(ex.getMessage());
    }
    return "redirect:/login";
  }

  private boolean validatePassword(PassDto passDto, BindingResult bindingResult) {
    boolean result = this.passwordValidator.validate(passDto.getPassword());
    if(!result){
      String message = this.passwordValidator.getExceptionMessage();
      bindingResult.addError(new FieldError("passDto", "password",
          passDto.getPassword(),false, null, null, message));
      bindingResult.addError(new FieldError("passDto", "password2",
          "",false, null, null, null));
    }
    if(!passDto.getPassword().equals(passDto.getPassword2())){
      ObjectError error = new ObjectError("duplicateError", INVALID_DUPLICATE);
      bindingResult.addError(error);
    }
    return result;
  }

  private void checkSessionAttribute(HttpSession session) throws BadRequestException{
    if(session.getAttribute("email") == null){
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
