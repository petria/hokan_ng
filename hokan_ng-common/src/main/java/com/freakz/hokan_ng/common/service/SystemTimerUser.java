package com.freakz.hokan_ng.common.service;

import java.util.Calendar;

/**
 * User: petria
 * Date: 12/3/13
 * Time: 11:38 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface SystemTimerUser {

  void doSubscribe();

  void timerTick(Calendar cal, int hh, int mm, int ss) throws Exception;

}
