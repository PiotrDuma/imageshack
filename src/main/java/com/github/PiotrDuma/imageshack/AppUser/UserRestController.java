package com.github.PiotrDuma.imageshack.AppUser;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/securitytest")
public class UserRestController {

  @GetMapping(path = "info")
  public String getInfo() {
    System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
    System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    return "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
  }

  @PreAuthorize("hasAnyRole('ROLE_USER')")
  @GetMapping(path = "user")
  public String getUserMessage() {
    return "helloUser";
  }

  @GetMapping(path = "mode")
  public String getModeMessage() {
    return "helloMode";
  }

  @GetMapping(path = "admin")
  public String getAdminMessage() {
    return "helloAdmin";
  }

  @PreAuthorize("hasAnyRole('ROLE_OWNER')")
  @GetMapping(path = "owner")
  public String getOwnerMessage() {
    return "helloOwner";
  }
}