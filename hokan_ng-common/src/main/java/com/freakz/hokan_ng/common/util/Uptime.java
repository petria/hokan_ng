package com.freakz.hokan_ng.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Calculates time difference between two time objects.
 * This time is usually called uptime.
 * <p/>
 * Date: 11/6/13
 * Time: 6:52 PM
 *
 * @author Petri Airio
 */
public class Uptime {

//~ Instance/static variables ..............................................

  private long _time;

//~ Constructors ...........................................................

  public Uptime() {
    _time = new Date().getTime();
  }

  public Uptime(Calendar cal) {
    this(cal.getTime());
  }

  public Uptime(Date d) {
    this(d.getTime());
  }

  /**
   * startup time. uptime is calculated against this time.
   */
  public Uptime(long time) {
    _time = time;
    //    System.out.println("Target time: " + new Date(time) +
    //		       " (" + _time + ")");
  }

  //~ Methods ................................................................

  public Integer[] getTimeDiff() {
    return getTimeDiff(new Date().getTime());
  }

  public Integer[] getTimeDiff(Calendar cal) {
    return getTimeDiff(cal.getTime());
  }

  public Integer[] getTimeDiff(Date d) {
    return getTimeDiff(d.getTime());
  }


  public Integer[] getTimeDiff(long time2) {
    long utime = Math.abs(_time - time2);

    if (utime == 0) {
      return new Integer[]{0, 0, 0, 0};
    }

    long ut_secs = utime / 1000;

    int dd = (int) ut_secs / (60 * 60 * 24);
    int hh = (int) (ut_secs / (60 * 60)) - (dd * 24);
    int mm = (int) (ut_secs / 60) - (dd * 1440) - (hh * 60);
    int ss = (int) ut_secs - (dd * 24 * 60 * 60) - (hh * 60 * 60) - (mm * 60);

    Integer[] ret = new Integer[4];
    ret[0] = ss;
    ret[1] = mm;
    ret[2] = hh;
    ret[3] = dd;

    return ret;
  }

  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    String[] format = {"00", "00", "00", "0"};
    StringBuilder sb = new StringBuilder();
    Integer[] ut = getTimeDiff();
    sb.append(sdf.format(new Date()));
    sb.append(" up %3 day");
    if (ut[3] > 1) {
      sb.append("s");
    }
    sb.append(" %2:%1:%0, ");
    return StringStuff.fillTemplate(sb.toString(), ut, format);
  }

}
