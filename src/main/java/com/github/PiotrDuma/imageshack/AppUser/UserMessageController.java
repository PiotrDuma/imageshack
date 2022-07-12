package com.github.PiotrDuma.imageshack.AppUser;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/securitytest")
@Profile(value = {"dev", "test"})
public class UserMessageController {
  // SpEL syntax:
  // to check ROLE: hasRole(USER) or hasRole(ROLE_USER) + hasAuthority(ROLE_USER)
  // to check permission: hasAuthority('OP_READ') or hasAuthority('ROLE_USER')

  @GetMapping(path = "/info")
  public String getTestMessage() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
        .getAuthentication().getAuthorities();
    if(principal instanceof UserDetails){
      UserDetailsWrapper userWrapper = (UserDetailsWrapper) principal;
      System.out.print("user_role: ");
      userWrapper.getUser().getRoles().stream()
          .forEach(role -> System.out.print(role.toString()+", "));
      System.out.println("\nPRINT PRIVILEDGES: " + userWrapper.getAuthorities());
    }
    return authorities!=null?"authorities: "+authorities:"null";
  }

  @GetMapping(path = "anonymous")
  public String getAnonymousMessage() {
    return "hello anonymous";
  }

  @PreAuthorize("hasAuthority('OP_READ')")
  @GetMapping(path = "user/authorityREAD")
  public String getUserMessageWithAuthority() {
    return "user " + "has 'OP_READ' authority";
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping(path = "user/role")
  public String getUserMessageWithRole() {
    return "user " + "has 'ROLE_USER' authority";
  }

  @PreAuthorize("hasAuthority('USER')")//FAIL
  @GetMapping(path = "user/failRole")
  public String getUserMessageWithNonExistentAuthority() {
    return "user authority doesn't exist.";
  }

  @PreAuthorize("hasRole('ROLE_USER')")//TRUE, but redundant prefix
  @GetMapping(path = "user/role2")
  public String getModeMessage() {
    return "user request works";
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")//TRUE
  @GetMapping(path = "admin")
  public String getAdminMessage() {
    return "hello Admin";
  }

  @PreAuthorize("hasRole('OWNER')") //TRUE
  @GetMapping(path = "owner/role")
  public String getOwnerMessageByRole() {
    return "hello owner by role";
  }

  @PreAuthorize("hasAuthority('OP_MANAGE')") //TRUE
  @GetMapping(path = "owner/authority")
  public String getOwnerMessageByAuthority() {
    return "hello owner by authority";
  }
}