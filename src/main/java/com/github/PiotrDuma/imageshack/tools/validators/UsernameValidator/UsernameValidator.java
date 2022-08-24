package com.github.PiotrDuma.imageshack.tools.validators.UsernameValidator;

import com.github.PiotrDuma.imageshack.tools.validators.AbstractValidator;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("usernameValidator")
public class UsernameValidator extends AbstractValidator {
  private static final Integer MIN_LENGTH = 3;
  private static final Integer MAX_LENGTH = 64;
  private static final Pattern PATTERN = Pattern.compile(
      "^" + "[a-zA-Z\\d._\\-]"
          + "{" + MIN_LENGTH +"," + MAX_LENGTH + "}"
          + "$"
  );

  @Override
  protected Pattern setPattern() {
    return PATTERN;
  }
}
