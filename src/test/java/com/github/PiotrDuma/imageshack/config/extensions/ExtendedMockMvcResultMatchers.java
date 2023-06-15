package com.github.PiotrDuma.imageshack.config.extensions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

public class ExtendedMockMvcResultMatchers extends MockMvcResultMatchers {

  public static ExtendedMockMvcResultMatchers globalErrors() {
    return new ExtendedMockMvcResultMatchers();
  }

  /**
   * Asserts the model contains GlobalError object with given error name.
   *
   * @param attributeName the declared value of @ModelAttribute provided as argument in controller
   *                      method attached to BindingResults.
   * @param name the name of GlobalError attribute object
   */
  public static ResultMatcher hasGlobalError(String attributeName, String name){
    return result -> {
        assertNotNull(result.getModelAndView());
        assertTrue(getBindingResults(result.getModelAndView(), attributeName).getGlobalErrors().stream()
            .anyMatch(err -> err.getObjectName().equals(name)));
    };
  }

  /**
   * Asserts the model contains GlobalError object with given error name and message.
   *
   * @param attributeName the declared value of @ModelAttribute provided as argument in controller
   *                      method attached to BindingResults.
   * @param name the name of GlobalError attribute object
   * @param message the default message of GlobalError object
   */
  public static ResultMatcher hasGlobalError(String attributeName, String name, String message){
    return result -> {
      assertNotNull(result.getModelAndView());
      assertTrue(getBindingResults(result.getModelAndView(), attributeName).getGlobalErrors().stream()
          .filter(err -> err.getObjectName().equals(name))
          .anyMatch(err -> message.equals(err.getDefaultMessage())));
    };
  }

  private static BindingResult getBindingResults(ModelAndView model, String attributeName) {
//    Model map stores errors with object key made with concatenation of BindingResult key prefix
//    and the attribute name provided as controller's method argument.
    BindingResult result = (BindingResult) model.getModel().get(BindingResult.MODEL_KEY_PREFIX + attributeName);
    assertNotNull(result);
    return result;
  }

}
