package com.github.PiotrDuma.imageshack.tools.validators.EmailValidator;

import com.github.PiotrDuma.imageshack.tools.validators.AbstractValidator;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("emailValidator")
public class EmailValidator extends AbstractValidator {
  private static final Integer MIN_LENGTH = 3;
  private static final Integer MAX_LENGTH = 64;
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  @Override
  protected Pattern setPattern() {
    return EMAIL_PATTERN;
  }

  @Override
  public boolean validate(String text) {
    if(text.length() < MIN_LENGTH || text.length()>MAX_LENGTH){
      return false;
    }
    return super.validate(text);
  }
}
