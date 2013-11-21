package com.freakz.hokan_ng.common.updaters.horo;

/**
 * User: petria
 * Date: Jul 15, 2009
 * Time: 8:29:17 AM
 */
public class HoroHolder {
  private int _horoscope;
  private String _horoscopeText;

  public HoroHolder(int horoscope, String horoscopeText) {
    _horoscope = horoscope;
    _horoscopeText = horoscopeText;
  }

  public int getHoroscope() {
    return _horoscope;
  }

  public String getHoroscopeText() {
    return _horoscopeText;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(HoroUpdater.HORO_NAMES[getHoroscope()]);
    sb.append(" (");
    sb.append(HoroUpdater.HORO_DATES[getHoroscope()]);
    sb.append("): ");
    sb.append(getHoroscopeText());
    return sb.toString();
  }

}
