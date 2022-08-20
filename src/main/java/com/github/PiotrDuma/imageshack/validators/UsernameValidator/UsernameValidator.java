package com.github.PiotrDuma.imageshack.validators.UsernameValidator;

import com.github.PiotrDuma.imageshack.validators.AbstractValidator;
import java.util.regex.Pattern;

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
