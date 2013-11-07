package com.freakz.hokan_ng.common.exception;

/**
 * Date: 3.6.2013
 * Time: 13:25
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public class HokanException extends Exception {
  private static final long serialVersionUID = 1L;

  public HokanException(Exception t) {
    super(t);
  }

  public HokanException(String message) {
    super(message);
  }

  public HokanException(String message, Throwable cause) {
    super(message, cause);
  }


}
