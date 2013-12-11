package com.freakz.hokan_ng.common.engine;

import com.freakz.hokan_ng.common.exception.HokanException;

/**
 * User: petria
 * Date: 11/5/13
 * Time: 12:06 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface CommandRunnable {

  public void handleRun(long myPid, Object args) throws HokanException;
}
