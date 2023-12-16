package com.github.PiotrDuma.imageshack.tools.email;

import org.springframework.lang.NonNull;

public abstract class AbstractEmailContentProvider {
  @NonNull
  private final String subject;
  @NonNull
  private final String message;
  @NonNull
  private final boolean isHTML;

  public AbstractEmailContentProvider(String subject, String message, boolean isHTML) {
    this.subject = subject;
    this.message = message;
    this.isHTML = isHTML;
  }

  public String getSubject() {
    return subject;
  }

  public String getMessage() {
    return message;
  }
  public boolean isHTML() {
    return isHTML;
  }
}
