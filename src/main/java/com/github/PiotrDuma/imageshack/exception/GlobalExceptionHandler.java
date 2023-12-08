package com.github.PiotrDuma.imageshack.exception;

import com.github.PiotrDuma.imageshack.exception.type.BadRequestException;
import java.time.ZonedDateTime;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final String NOT_FOUND = "Page not found";
  private static final String ACCESS_DENIED = "Access denied";

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
    if(ex instanceof NoHandlerFoundException){
      return HttpStatus.NOT_FOUND;
    }
    if(ex instanceof AccessDeniedException){
      return HttpStatus.FORBIDDEN;
    }
    if(ex instanceof AbstractGlobalException){
      return ((AbstractGlobalException) ex).getStatus();
    }
    if(ex instanceof ResponseStatusException){
      return ((ResponseStatusException) ex).getStatus();
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private String getExceptionMessage(Exception ex) {
    if(ex instanceof NoHandlerFoundException) return NOT_FOUND;
    if(ex instanceof AccessDeniedException) return ACCESS_DENIED;
    return ex.getMessage()==null?"":ex.getMessage();
  }
}
