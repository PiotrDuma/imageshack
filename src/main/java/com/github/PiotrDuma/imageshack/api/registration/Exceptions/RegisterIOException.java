package com.github.PiotrDuma.imageshack.api.registration.Exceptions;


import com.github.PiotrDuma.imageshack.api.registration.AppUserDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterIOException extends IOException {
  private final Map<AppUserDTO.Field, List<String>> errorMessages;

  public RegisterIOException() {
    this.errorMessages = new HashMap<>();
  }

  public void addError(AppUserDTO.Field field, String message){
    this.errorMessages.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
  }

  public Map<AppUserDTO.Field, List<String>> getErrorMessages(){
    return this.errorMessages;
  }

  public List<String> getErrorMessage(AppUserDTO.Field field){
    return this.errorMessages.get(field);
  }
}
