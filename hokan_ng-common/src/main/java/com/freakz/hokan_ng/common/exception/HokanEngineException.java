package com.freakz.hokan_ng.common.exception;

import java.io.Serializable;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 9:18 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class HokanEngineException extends HokanException implements Serializable {

  private static final long serialVersionUID = 1L;

  private String exceptionClassName;

  public HokanEngineException() {
    super();
  }

  public HokanEngineException(Class exceptionClass) {
    super();
    this.exceptionClassName = exceptionClass.getName();
  }

  public String getExceptionClassName() {
    return exceptionClassName;
  }

  public void setExceptionClassName(String exceptionClassName) {
    this.exceptionClassName = exceptionClassName;
  }
}
