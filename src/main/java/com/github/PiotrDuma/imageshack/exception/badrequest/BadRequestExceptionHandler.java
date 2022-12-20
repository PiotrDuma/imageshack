package com.github.PiotrDuma.imageshack.exception.badrequest;

import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class BadRequestExceptionHandler {

  @ExceptionHandler(value = {BadRequestException.class})
  public String handle(BadRequestException ex, Model model, HttpServletResponse response){
      model.addAttribute("exception", ex.getExceptionInfo());
      response.setStatus(ex.getStatus().value());
    return "error";
  }
}
