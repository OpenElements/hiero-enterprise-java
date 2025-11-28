package com.openelements.hiero.base;

import org.jspecify.annotations.NonNull;

/** Represents an exception that occurred while interacting with a Hiero network. */
public class HieroException extends Exception {

  /**
   * Constructs a new Exception with the specified detail message.
   *
   * @param message The detail message.
   */
  public HieroException(@NonNull String message) {
    super(message);
  }

  /**
   * Constructs a new Exception with the specified detail message and cause.
   *
   * @param message The detail message.
   * @param cause The cause.
   */
  public HieroException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}
