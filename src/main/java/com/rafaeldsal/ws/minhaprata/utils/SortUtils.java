package com.rafaeldsal.ws.minhaprata.utils;

import org.springframework.data.domain.Sort;

public class SortUtils {

  private SortUtils() {

  }

  public static Sort.Direction getSortDirection(String sort) {
    return "DESC".equalsIgnoreCase(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;
  }
}
