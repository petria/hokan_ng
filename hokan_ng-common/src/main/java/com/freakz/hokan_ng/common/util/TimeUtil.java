package com.freakz.hokan_ng.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 10:46 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class TimeUtil {

  private final static TimeZone tz = TimeZone.getTimeZone("EET");

  public static Calendar getCalendar() {
    Calendar cal = new GregorianCalendar(tz);
    cal.setTimeZone(tz);
    cal.add(Calendar.HOUR_OF_DAY, 2); // TODO fix
    return cal;
  }

  public static Date getDate() {
    return getCalendar().getTime();
  }

}
