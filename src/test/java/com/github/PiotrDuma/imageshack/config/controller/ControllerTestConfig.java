package com.github.PiotrDuma.imageshack.config.controller;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

@Import({})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles(value = "test")
public @interface ControllerTestConfig{

  /**
   * Sets security config class.
   */
  @AliasFor(annotation = Import.class, attribute = "value")
  Class<? extends AbstractSecurityConfig>[] value() default {BasicSecurityTestConfig.class};
}
