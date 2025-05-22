package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.service.impl.RandomCodeGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RandomCodeGeneratorTest {

  @InjectMocks
  private RandomCodeGenerator randomCodeGenerator;

  @Test
  void given_generate_when_generateCodeWithSucceeded_then_returnCode() {
    String result = randomCodeGenerator.generate();

    assertNotNull(result);
    assertEquals(4, result.length());
    assertTrue(result.matches("\\d{4}"));
  }

}