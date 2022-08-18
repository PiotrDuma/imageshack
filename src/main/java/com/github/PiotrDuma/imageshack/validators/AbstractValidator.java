package com.github.PiotrDuma.imageshack.validators;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractValidator implements Validator{

  protected abstract Pattern setPattern();
  protected abstract Integer setMaxLength();
  protected abstract Integer setMinLength();

  @Override
  public boolean validate(String text) {

    if(text.length() < setMinLength() || text.length() > setMaxLength()){
      return false;
    }
    if(setMinLength() != null){
      if(text.length()< setMinLength()) return false;
    }
    if(setMaxLength() != null){
      if(text.length()> setMaxLength()) return false;
    }

    Matcher mather = setPattern().matcher(text);
    return mather.matches();
  }
}
