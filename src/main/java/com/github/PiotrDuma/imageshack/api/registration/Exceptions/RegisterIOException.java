package com.github.PiotrDuma.imageshack.api.registration.Exceptions;


import com.github.PiotrDuma.imageshack.api.registration.AppUserDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterIOException extends IOException {

  /**
   * Map of exception messages' lists attached to corresponding {@link AppUserDTO} fields.
   */
  private final Map<AppUserDTO.Field, List<String>> errorMessages;

  public RegisterIOException() {
    this.errorMessages = new HashMap<>();
  }

  /**
   * Creates new List object if element with given {@param field} not exist in map and adds new
   * message to given collection.
   *
   * @param field the field of {@link AppUserDTO}, which the exception occurs
   * @param message the message of exception
   */
  public void addError(AppUserDTO.Field field, String message){
    this.errorMessages.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
  }

  /**
   * @return the map of lists of exceptions. Every list can be obtained with key specified by
   * {@link AppUserDTO.Field} type.
   */
  public Map<AppUserDTO.Field, List<String>> getErrorMessages(){
    return this.errorMessages;
  }

  /**
   * @param field the field of {@link AppUserDTO}, which the exception occurs
   * @return the list of messages attached to this {@param field}
   */
  public List<String> getErrorMessage(AppUserDTO.Field field){
    return this.errorMessages.get(field);
  }

  /**
   * Avoid initializing class with message constructor. The message management is designed with Map
   * collection.
   */
  @Override
  public String getMessage() {
    return "Registration IO exception occurred";
  }
}
