package com.github.PiotrDuma.imageshack.tools.validators;

public interface Validator {
  boolean validate(String text);
  String getExceptionMessage();
}
