package com.github.PiotrDuma.imageshack.validators.EmailValidator;

import com.github.PiotrDuma.imageshack.validators.AbstractValidator;
import java.util.regex.Pattern;

public class EmailValidator extends AbstractValidator {
  private static final Integer MIN_LENGTH = 3;
  private static final Integer MAX_LENGTH = 254;
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  @Override
  protected Pattern setPattern() {
    return EMAIL_PATTERN;
  }

}
