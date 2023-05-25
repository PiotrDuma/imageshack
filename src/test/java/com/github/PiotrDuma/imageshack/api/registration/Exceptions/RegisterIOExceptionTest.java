package com.github.PiotrDuma.imageshack.api.registration.Exceptions;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.imageshack.api.registration.AppUserDTO.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterIOExceptionTest {
  private RegisterIOException exception;

  @BeforeEach
  void setUp(){
    this.exception = new RegisterIOException();
  }

  @Test
  void addNewMessageToFieldShouldReturnOneList(){
    String message1 = "test1";
    String message2 = "test2";
    exception.addError(Field.EMAIL, message1);
    exception.addError(Field.EMAIL, message2);

    assertEquals(1, exception.getErrorMessages().size());
  }

  @Test
  void addTwoMessagesToFieldShouldReturnTwoMessages(){
    String message1 = "test1";
    String message2 = "test2";
    exception.addError(Field.EMAIL, message1);
    exception.addError(Field.EMAIL, message2);

    assertEquals(2, exception.getErrorMessages().get(Field.EMAIL).size());
    assertEquals(2, exception.getErrorMessage(Field.EMAIL).size());
    assertTrue(exception.getErrorMessage(Field.EMAIL).contains(message1));
    assertTrue(exception.getErrorMessage(Field.EMAIL).contains(message2));
  }

  @Test
  void addMessagesToDistinctFieldsShouldReturnCorrespondingErrorLists(){
    String message1 = "test1";
    String message2 = "test2";
    exception.addError(Field.EMAIL, message1);
    exception.addError(Field.PASSWORD, message2);

    assertEquals(2, exception.getErrorMessages().size());
    assertEquals(1, exception.getErrorMessage(Field.EMAIL).size());
    assertEquals(1, exception.getErrorMessage(Field.PASSWORD).size());
    assertTrue(exception.getErrorMessage(Field.EMAIL).contains(message1));
    assertTrue(exception.getErrorMessage(Field.PASSWORD).contains(message2));
  }

  @Test
  void getMessageFromNonExistingFieldErrorShouldReturnNull(){
    exception.addError(Field.EMAIL, "message1");

    assertEquals(null, exception.getErrorMessage(Field.USERNAME));
  }
}