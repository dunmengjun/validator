package com.dmj.validation.exception;

public class UnknownException extends RuntimeException {

  public UnknownException() {
    super("Unknown error");
  }
}
