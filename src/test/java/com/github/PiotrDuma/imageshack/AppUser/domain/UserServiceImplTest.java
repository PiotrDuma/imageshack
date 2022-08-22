package com.github.PiotrDuma.imageshack.AppUser.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.NoSuchRoleException;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Role;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.RoleService;
import com.github.PiotrDuma.imageshack.AppUser.domain.exceptions.UserNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepo;
  @Mock
  private RoleService roleService;
  @Mock
  private PasswordEncoder passwordEncoder;

  private UserService userService;

  @BeforeEach
  void setUp(){
    this.userService = new UserServiceImpl(userRepo, roleService, passwordEncoder);
  }

  @Test
  void addRoleToUserTest(){
    Long id = 1L;
    AppRoleType roleType = AppRoleType.MODERATOR;
    User user = new User();
    Role role = Mockito.mock(Role.class);
    //when
    Mockito.doReturn(Optional.of(user)).when(userRepo).findById(id);
    Mockito.when(role.toString()).thenReturn(roleType.name());
    Mockito.when(roleService.findRoleByRoleType(roleType)).thenReturn(role);

    //then
    UserDetailsWrapper result = this.userService.addRole(id, roleType);

    assertEquals(1, result.getUser().getRoles().size());
    assertTrue(result.getUser().getRoles().contains(role));
    assertEquals(roleType.name(),result.getUser().getRoles().stream().findFirst().get().toString());
  }

  @Test
  void addRoleToUserShouldThrowWhenUserNotFound(){
    Long id = 123L;
    String message = "User with id= %d not found";
    Mockito.when(userRepo.findById(Mockito.anyLong())).thenThrow(new UserNotFoundException(String.format(message, id)));

    Exception ex = assertThrows(UserNotFoundException.class, () -> this.userService.addRole(id, AppRoleType.OWNER));
    assertEquals(String.format(message, id), ex.getMessage());
  }

  @Test
  void removeRoleFromUserShouldThrowWhenUserNotFound(){
    Long id = 123L;
    String message = "User with id= %d not found";
    Mockito.when(userRepo.findById(id)).thenThrow(new UserNotFoundException(String.format(message, id)));

    Exception ex = assertThrows(UserNotFoundException.class, () -> this.userService.removeRole(id, AppRoleType.OWNER));
    assertEquals(String.format(message, id), ex.getMessage());
  }

  @Test
  void removeRoleFromUserShouldThrowWhenRoleNotFound(){
    Long id = 123L;
    String message = "ROLE %s MOT FOUND";
    AppRoleType roleType = AppRoleType.OWNER;
    User user = Mockito.mock(User.class);
    Mockito.when(userRepo.findById(id)).thenReturn(Optional.of(user));
    Mockito.when(this.roleService.findRoleByRoleType(roleType)).thenThrow(new NoSuchRoleException(String.format(message, roleType)));


    Exception ex = assertThrows(NoSuchRoleException.class, () -> this.userService.removeRole(id, roleType));
    assertEquals(String.format(message, roleType), ex.getMessage());
  }

  @Test
  void removeRoleFromUserTest(){
    Long id = 1L;
    AppRoleType roleType = AppRoleType.MODERATOR;
    User user = Mockito.mock(User.class);
    Role role = Mockito.mock(Role.class);

    //when
    Mockito.when(user.getRoles()).thenReturn(new HashSet<Role>(Set.of(role)));
    Mockito.doReturn(Optional.of(user)).when(userRepo).findById(id);
    Mockito.when(roleService.findRoleByRoleType(roleType)).thenReturn(role);

    //then
    assertEquals(1, user.getRoles().size());
    assertTrue(user.getRoles().contains(role));
    UserDetailsWrapper result = this.userService.removeRole(id, roleType);
    assertEquals(0, user.getRoles().size());
    assertFalse(result.getUser().getRoles().contains(role));
  }

  @Test
  void findUsersByRoleReturnsCorrectTypeTest(){
    User user1 = Mockito.mock(User.class);
    User user2 = Mockito.mock(User.class);
    User user3 = Mockito.mock(User.class);
    List<User> userList = new LinkedList<>(List.of(user1, user2, user3));

    Mockito.when(userRepo.findAllByRole(Mockito.any())).thenReturn(userList);

    List <?> result = userService.findUsersByRole(Mockito.any());

    assertEquals(3, result.size());
    assertTrue(result.get(0) instanceof UserDetailsWrapper);
  }

  @Test
  void findUserByRoleReturnsEmptyListTest(){
    List<User> list = new LinkedList<>();
    Mockito.when(this.userRepo.findAllByRole(Mockito.any())).thenReturn(list);

    List<UserDetailsWrapper> result = this.userService.findUsersByRole(Mockito.any());

    assertEquals(0, result.size());
  }

  @Test
  void findUserByIdShouldThrowWhenNotFound(){
    Long id = 123L;
    String message = "User with id= %d not found";
    Mockito.when(this.userRepo.findById(Mockito.anyLong())).thenThrow(new UserNotFoundException(String.format(message, id)));

    Exception ex = assertThrows(UserNotFoundException.class, () -> this.userService.findUserById(id));
    assertEquals(String.format(message, id), ex.getMessage());
  }

  @Test
  void findUserByIdShouldReturnCorrectClassType(){
    User user = Mockito.mock(User.class);
    Mockito.when(this.userRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

    Object result = this.userService.findUserById(Mockito.anyLong());
    assertTrue(result instanceof UserDetailsWrapper);
  }

  @Test
  void loadUserByUsernameShouldCallRepoWhenLoginIsUsername(){
    User user = Mockito.mock(User.class);
    String login = "username123";
    Mockito.when(this.userRepo.findByUsername(login)).thenReturn(Optional.of(user));
    Mockito.when(user.getUsername()).thenReturn(login);

    UserDetails wrapper = this.userService.loadUserByUsername(login);
    Mockito.verify(this.userRepo, Mockito.times(1)).findByUsername(login);
    Mockito.verify(this.userRepo, Mockito.times(0)).findByEmail(login);
    assertEquals(login, wrapper.getUsername());
  }

  @Test
  void loadUserByUsernameShouldCallRepoWhenLoginIsEmail(){
    User user = Mockito.mock(User.class);
    String login = "username123@imageshack.com";
    Mockito.when(this.userRepo.findByEmail(login)).thenReturn(Optional.of(user));
    Mockito.when(user.getUsername()).thenReturn(login);

    UserDetails wrapper = this.userService.loadUserByUsername(login);
    Mockito.verify(this.userRepo, Mockito.times(0)).findByUsername(login);
    Mockito.verify(this.userRepo, Mockito.times(1)).findByEmail(login);
    assertEquals(login, wrapper.getUsername());
  }

  @Test
  void loadUserByUsernameShouldThrowExceptionWhenEmailAndUsernameIsNotValid(){
    User user = Mockito.mock(User.class);
    String login = "   username1 23@imageshack.com";
    String message = String.format("User %s not found", login);

    Exception ex = assertThrows(UsernameNotFoundException.class,()-> this.userService.loadUserByUsername(login));
    assertEquals(message, ex.getMessage());
    }

  @Test
  void loadUserByUsernameShouldThrowExceptionWhenEmailNotFound(){
    String login = "username123@imageshack.com";
    String message = String.format("User %s not found", login);
    UsernameNotFoundException exception = new UsernameNotFoundException(String.format("User %s not found", login));
    Mockito.when(this.userRepo.findByEmail(login)).thenThrow(exception);

    Exception ex = assertThrows(UsernameNotFoundException.class,()-> this.userService.loadUserByUsername(login));
    Mockito.verify(this.userRepo, Mockito.times(1)).findByEmail(login);
    assertEquals(message, ex.getMessage());
  }

  @Test
  void loadUserByUsernameShouldThrowExceptionWhenUsernameNotFound(){
    String login = "username123";
    String message = String.format("User %s not found", login);
    UsernameNotFoundException exception = new UsernameNotFoundException(String.format("User %s not found", login));
    Mockito.when(this.userRepo.findByUsername(login)).thenThrow(exception);

    Exception ex = assertThrows(UsernameNotFoundException.class,()-> this.userService.loadUserByUsername(login));
    Mockito.verify(this.userRepo, Mockito.times(1)).findByUsername(login);
    assertEquals(message, ex.getMessage());
  }
}