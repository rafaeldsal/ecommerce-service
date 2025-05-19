package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.service.CodeGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCodeGenerator implements CodeGenerator {

  @Override
  public String generate() {
    return String.format("%04d", new Random().nextInt(10000));
  }
}
