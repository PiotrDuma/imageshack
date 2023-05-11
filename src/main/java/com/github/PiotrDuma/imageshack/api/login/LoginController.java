package com.github.PiotrDuma.imageshack.api.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller of login domain. Handle login endpoint and a few chosen endpoint methods to redirect
 * into login page with specified message based on exception thrown in
 * {@link com.github.PiotrDuma.imageshack.api.login.CustomAuthenticationFailureHandler}
 */
@Controller
@RequestMapping("/login")
public class LoginController{
  private static final String SUSPENDED = "Your account has been suspended for violating our "
      + "Terms of Service. If you believe this was done in error, please contact "
      + "us at support@imageshack.com.";
  private static final String INVALID_PASSWORD = "Incorrect password, try again.";
  private static final String INVALID_LOGIN = "User does not exist.";
  private static final String FAILURE = "Something went wrong, try again.";

  @GetMapping
  public String login(Model model) {
    return "login";
  }

  @RequestMapping("/error")
  public String failure(RedirectAttributes attributes){
    attributes.addFlashAttribute("error", FAILURE);
    return "redirect:/login";
  }

  @RequestMapping("/error/password")
  public String invalidPassword(RedirectAttributes attributes){
    attributes.addFlashAttribute("error", INVALID_PASSWORD);
    return "redirect:/login";
  }

  @RequestMapping("/error/login")
  public String invalidLogin(RedirectAttributes attributes){
    attributes.addFlashAttribute("error", INVALID_LOGIN);
    return "redirect:/login";
  }

  @RequestMapping("/error/suspended")
  public String suspended(RedirectAttributes attributes){
    attributes.addFlashAttribute("error", SUSPENDED);
    return "redirect:/login";
  }

  @RequestMapping("/error/inactive")
  public String inactive(RedirectAttributes attributes){
    attributes.addFlashAttribute("inactive", true);
    return "redirect:/login";
  }
}
