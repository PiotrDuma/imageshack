package com.github.PiotrDuma.imageshack.tools.email;

import com.github.PiotrDuma.imageshack.common.EmailAddress;

public interface EmailService{
  void sendMail(EmailAddress to, AbstractEmailContentProvider emailContentProvider);
}
