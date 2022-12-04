package com.github.PiotrDuma.imageshack.tools.validators;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractValidator implements Validator{

  protected abstract Pattern setPattern();

  @Override
  public boolean validate(String text) {

    Matcher mather = setPattern().matcher(text);
    return mather.matches();
  }

  public abstract String getExceptionMessage();
}
