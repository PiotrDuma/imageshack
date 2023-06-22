package com.github.PiotrDuma.imageshack.exception;

import com.github.PiotrDuma.imageshack.exception.badrequest.BadRequestException;
import java.time.ZonedDateTime;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public String handle(Exception ex, Model model, HttpServletResponse response){
    ExceptionDTO exceptionInfo = getExceptionInfo(ex);
    model.addAttribute("exception", exceptionInfo);
    response.setStatus(exceptionInfo.getStatus().value());
    return "error";
  }

  private ExceptionDTO getExceptionInfo(Exception ex){
    String message = getExceptionMessage(ex);
    HttpStatus status = getExceptionStatus(ex);

    return new ExceptionDTO(message, status, ZonedDateTime.now());
  }

  private HttpStatus getExceptionStatus(Exception ex) {
    if(ex instanceof BadRequestException){
      return HttpStatus.BAD_REQUEST;
    }
    if(ex instanceof NoHandlerFoundException){
      return HttpStatus.NOT_FOUND;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private String getExceptionMessage(Exception ex) {
    String message = ex.getMessage();
    if(message == null){
      message = "";
    }
    if(ex instanceof NoHandlerFoundException){
      message = "Page not found.";
    }
    return message;
  }
}
