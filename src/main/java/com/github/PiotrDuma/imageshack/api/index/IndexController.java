package com.github.PiotrDuma.imageshack.api.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

  @GetMapping
  public String getMainPage() {
    return "index";
  }

  @RequestMapping("/index")
  public String redirect() {
    return "redirect:/";
  }
}
