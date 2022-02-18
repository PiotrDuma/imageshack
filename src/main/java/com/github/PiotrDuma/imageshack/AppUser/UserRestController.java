package com.github.PiotrDuma.imageshack.AppUser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/securitytest")
public class UserRestController {


  @GetMapping(path = "user")
  public String getMessage(){
    return "helloUser";
  }
}
