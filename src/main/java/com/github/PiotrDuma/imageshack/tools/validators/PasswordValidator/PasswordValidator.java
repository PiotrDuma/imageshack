package com.github.PiotrDuma.imageshack.tools.validators.PasswordValidator;

import com.github.PiotrDuma.imageshack.tools.validators.AbstractValidator;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("passwordValidator")
public class PasswordValidator extends AbstractValidator {
  //Pattern regex statements: https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
  private static final String REQUIRE_LOWER_CASE = "(?=.*[a-z])";
  private static final String REQUIRE_UPPER_CASE = "(?=.*[A-Z])";
  private static final String REQUIRE_DIGIT = "(?=.*[0-9])";
  private static final String REQUIRE_SPECIAL_SIGN = "(?=.*[@#$%^&+=])";
  private static final String REQUIRE_NO_BLANK_SIGNS = "(?=\\S+$)";
  private static final Integer MIN_LENGTH = 8;
  private static final Integer MAX_LENGTH = 32;

  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
      "^"+ REQUIRE_DIGIT
      + REQUIRE_LOWER_CASE
      + REQUIRE_UPPER_CASE
      + REQUIRE_NO_BLANK_SIGNS
      + ".{" + MIN_LENGTH +"," + MAX_LENGTH + "}"
      + "$"
  );

  @Override
  protected Pattern setPattern() {
    return PASSWORD_PATTERN;
  }
}
