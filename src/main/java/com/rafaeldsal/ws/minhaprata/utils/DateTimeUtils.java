package com.rafaeldsal.ws.minhaprata.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

  private DateTimeUtils(){}

  public static String timestamp() {
    return FORMATTER.format(Instant.now());
  }

  public static LocalDateTime now() {
    return LocalDateTime.now();
  }
}
