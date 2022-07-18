package com.github.PiotrDuma.imageshack.api.login.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController{

  @GetMapping(value = "/login")
  public String login(Model model) {
    return "login";
  }

//  @RequestMapping("/login-error")//TODO: failure: login?error=true
//  public String loginError(Model model) {
//    model.addAttribute("loginError", true);
//    return "login";
//  }
}
