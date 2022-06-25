package com.github.PiotrDuma.imageshack.AppUser;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/securitytest")
public class UserRestController {

  private final UserRepository repo;

  @Autowired
  public UserRestController(UserRepository repo) {
    this.repo = repo;
  }

  @GetMapping(path = "info")
  public String getInfo() {
    System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
    System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    return "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
  }

  // ROLE, SpEL: hasRole(USER) or hasRole(ROLE_USER) + hasAuthority(ROLE_USER)
  //customize spring security to create authorities as ROLE_USER or as permission OP_WRITE.
  // checkout for permissions by hasAutority + given name or hasRole + rolename with or withour prefix ROLE.
  //TODO:refactor userdetailsservice to add Operations to authorities + tests.

  @PreAuthorize("hasAuthority('OP_READ')")
  @GetMapping(path = "user")
  public String getUserRoleMessage() {

    return ""+"has Role Moderator";
  }

  @PreAuthorize("hasAuthority('USER')")//FALSE
  @GetMapping(path = "user1")
  public String getUserMessage() {
    System.out.println("passed");
    return "" +"helloUser";
  }

  @PreAuthorize("hasRole('ROLE_MODERATOR')")//TRUE
  @GetMapping(path = "mode")
  public String getModeMessage() {
    return ""+"truuuu";
  }


  @PreAuthorize("hasAuthority('ROLE_MODERATOR')")//true
  @GetMapping(path = "admin")
  public String getAdminMessage() {
    return ""+"helloAdmin";
  }

  @PreAuthorize("hasRole('USER')") //TRUE
  @GetMapping(path = "owner")
  public String getOwnerMessage() {
    return "helloOwner";
  }

  @PreAuthorize("hasRole('CREATE')")
  @GetMapping(path = "/test")
  public String getTestMessage() {

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
        .getAuthentication().getAuthorities();
    if(principal instanceof UserDetails){
      UserDetailsWrapper userWrapper = (UserDetailsWrapper) principal;
      userWrapper.getUser().getRoles().stream()
          .forEach(role -> System.out.println("user_role:  "+role.toString()));
      System.out.println("PRINT PRIVLEDGES: " + userWrapper.getAuthorities());
    }

    System.out.println("authorities  " +authorities);
    return authorities!=null?""+authorities:"null";
  }
}