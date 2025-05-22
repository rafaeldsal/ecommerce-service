package com.rafaeldsal.ws.minhaprata.integration;

import com.rafaeldsal.ws.minhaprata.integration.impl.MailIntegrationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MailIntegrationTest {

  @Mock
  private JavaMailSender javaMailSender;

  @InjectMocks
  private MailIntegrationImpl mailIntegration;

  @Test
  void given_send_when_validParameters_then_emailIsSent() {
    String mailTo = "rafael@email.com";
    String message = "Mensagem de teste";
    String subject = "Assunto do teste";

    mailIntegration.send(mailTo, message, subject);

    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }

  @Test
  void given_send_when_mailSendingFails_then_throwsException() {
    String mailTo = "rafael@email.com";
    String message = "Mensagem de teste";
    String subject = "Assunto do teste";

    doThrow(new RuntimeException("Failed to send email"))
        .when(javaMailSender).send(any(SimpleMailMessage.class));

    assertThrows(RuntimeException.class, () -> mailIntegration.send(mailTo, message, subject));
  }

}