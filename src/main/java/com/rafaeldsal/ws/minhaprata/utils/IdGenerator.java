package com.rafaeldsal.ws.minhaprata.utils;

import java.util.UUID;

public class IdGenerator {

  private IdGenerator() {}

  public static String UUIDGenerator(String model) {
    return model + "-" + UUID.randomUUID().toString();
  }
}
