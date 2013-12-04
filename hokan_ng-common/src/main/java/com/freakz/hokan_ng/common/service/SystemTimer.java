package com.freakz.hokan_ng.common.service;

/**
 * User: petria
 * Date: 12/3/13
 * Time: 11:37 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface SystemTimer {

  void addSystemTimerUser(SystemTimerUser user);

  void start();

  void stop();

  long getTickCount();

}
