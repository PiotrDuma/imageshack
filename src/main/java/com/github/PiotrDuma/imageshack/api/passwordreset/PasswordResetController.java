package com.github.PiotrDuma.imageshack.api.passwordreset;

import com.github.PiotrDuma.imageshack.exception.type.BadRequestException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recover")
public class PasswordResetController {

  private final PasswordResetService service;

  @Autowired
  public PasswordResetController(PasswordResetService service) {
    this.service = service;
  }

  @GetMapping
  public String getRecoverPage(Model model){
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
  public String confirm(@RequestParam("email") String email, @RequestParam("token") String token){
    try{
      this.service.authenticate(email, token);
    }catch(Exception ex){
      throw new BadRequestException(ex.getMessage());
    }
    return "redirect:/recover/reset";
  }

  @GetMapping("/reset")
  public String getResetTemplate(Model model){
    //TODO: add password attribute
    return "reset";
  }

  @PostMapping("/reset")
  public String reset(){
    //TODO: add method arguments
    //TODO: reset service call
    return "redirect:/login";
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
}
