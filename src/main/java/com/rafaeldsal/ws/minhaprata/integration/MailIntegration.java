package com.rafaeldsal.ws.minhaprata.integration;

public interface MailIntegration {

  void send(String mailTo, String message, String subject);
}
