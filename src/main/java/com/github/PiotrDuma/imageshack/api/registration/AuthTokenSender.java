package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("authTokenSender")
class AuthTokenSender {
  private static final String MESSAGE = "Hello!\n\nYou have created an account in %s website.\n"
      + "Login: %s / %s\n\n"
      + "If you want to activate your profile please click in link below:\n\n"
      + "%s\n\n"
      + "If you didn't register please ignore this email. The profile will be deleted soon.\n"
      + "Activation time expires at: %s\n\n"
      + "This email has been automatically generated. Please do not reply.";

  @Value("${spring.application.name}") private String applicationName;
  @Value("${services.systemURL}") private String systemUrl;
  @Value("${services.registrationAuth}") private String endpoint;
  private final EmailService emailService;
  private final TokenAuthFacade tokenFacade;

  @Autowired
  public AuthTokenSender(EmailService emailService, TokenAuthFacade tokenFacade) {
    this.emailService = emailService;
    this.tokenFacade = tokenFacade;
  }

  /**
   * Creates authentication token and sends confirmation link via application's email.
   *
   * This method uses {@link com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade}
   * to initialize confirmation token with associated email. The token is used to generate
   * authentication URL and custom registration email message to send it using
   * {@link com.github.PiotrDuma.imageshack.tools.email.EmailService}
   *
   * @param email an email address of registered user required to send message.
   * @param username the name used only in email message.
   * @throws RegistrationAuthProcessingException throws when token is not created or message sending failed.
   */
  public void send(String email, String username) throws RegistrationAuthProcessingException {
    String subject = applicationName + ": account activation";
    TokenObject token = createToken(email);
    Instant expirationDate = getExpirationTimestamp(token);
    String message = generateMessage(email, username, token.getTokenValue(),
        expirationDate,  false);
    sendEmail(email, subject, message);
  }

  private void sendEmail(String email, String subject, String message) throws RegistrationAuthProcessingException{
    try{
      this.emailService.sendMail(email, subject, message, false);
    }catch (EmailSendingException ex){
      throw new RegistrationAuthProcessingException("Email sending has failed.", ex.getCause());
    }catch(InvalidEmailAddressException ex){
      throw new RegistrationAuthProcessingException("Invalid email address. Email sending has failed.", ex.getCause());
    }
  }

  private TokenObject createToken(String email) throws RegistrationAuthProcessingException {
    TokenObject token = null;
    try{
      token = this.tokenFacade.create(new TokenAuthDTO(email, TokenAuthType.ACCOUNT_CONFIRMATION));
    }catch(RuntimeException ex){
      throw new RegistrationAuthProcessingException("Token initialization has failed.", ex.getCause());
    }
    return token;
  }

  private Instant getExpirationTimestamp(TokenObject token){
    Instant timestamp = null;
    try{
      timestamp = this.tokenFacade.expiresAt(token);
    }catch(Exception ex){
      //improvised, but still better than sending null
      timestamp = Instant.now().plus(token.getTokenActiveTimeMinutes(), ChronoUnit.MINUTES);
    }
    return timestamp;
  }

  private String generateMessage(String email, String login, String tokenValue,
      Instant tokenExpiresAt, boolean isHtml) {
    String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss a")
        .withZone(ZoneId.systemDefault()).format(tokenExpiresAt);
    if(isHtml){
      return "empty"; //TODO
    }else{
      return String.format(MESSAGE, applicationName, email, login,
          getAuthURL(email, tokenValue), timestamp);
    }
  }

  private String getAuthURL(String email, String tokenValue){
    return new StringBuilder().append(systemUrl)
        .append(endpoint)
        .append("?email=")
        .append(email)
        .append("&token=")
        .append(tokenValue)
        .toString();
  }
}
