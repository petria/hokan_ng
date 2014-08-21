package com.freakz.hokan_ng.common.updaters.weather;

import java.io.Serializable;

/**
 * WeatherData
 * <p>
 *
 * @author Petri Airio
 */

public class WeatherData implements Cloneable, Comparable, Serializable {

  private static int _count;

  private String _road;
  private String _station;

  private String _time;
  private String _date;

  private double _temp1;
  private double _temp2;

  private String _cond1;
  private String _cond2;

  private int _pos;
  private String[] _data;

  public boolean OK = true;

  public WeatherData(Double temp1) {
    _road = "?";
    _station = "?";
    _time = "?";
    _temp1 = temp1;
    _temp2 = 0.0;
    _cond1 = "?";
    _cond2 = "?";
    _data = new String[10];

  }

  public WeatherData(String[] data) throws NumberFormatException {
    //        _data = new String[data.length];
    //        for (int xx = 0; xx < data.length; xx++) {
    //  	_data[xx] = data[xx];
    //        }
    _data = data;
    _road = data[0];
    _station = data[1];
    _time = data[2];
    try {
      _temp1 = Double.parseDouble(data[3]);
    } catch (Exception e) {
      OK = false;
    }
    try {
      _temp2 = Double.parseDouble(data[4]);
    } catch (Exception e) {
      OK = false;
    }
    _cond1 = data[5];
    _cond2 = data[6];

  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String toString() {
    return _road + ":" + _station + ":" + _time + ":" + _temp1 + ":" + _temp2 + ":" + _cond1 + ":" + _cond2;
  }

  public String[] getData() {
    String[] d = new String[10];
    System.arraycopy(_data, 0, d, 0, _data.length);
    d[7] = "" + _pos;
    d[8] = "" + _count;
    d[9] = _date;
    return d;
  }

  public int getCount() {
    return _count;
  }

  public void setCount(int count) {
    _count = count;
  }

  public int getPos() {
    return _pos;
  }

  public void setPos(int pos) {
    _pos = pos;
  }

  public String getRoad() {
    return _road;
  }

  public String getCity() {
    return _station;
  }

  public String getTime() {
    return _time;
  }

  public double getTemp1() {
    return _temp1;
  }

  public double getTemp2() {
    return _temp2;
  }

  public String getCond1() {
    return _cond1;
  }

  public String getCond2() {
    return _cond2;
  }

  public void setDate(String date) {
    _date = date;
  }

  public String getDate() {
    return _date;
  }

  public int compareTo(Object other) {
    WeatherData w1 = this;
    WeatherData w2 = (WeatherData) other;

    if (w1.getTemp1() > w2.getTemp1()) {
      return 1;
    }
    if (w1.getTemp1() < w2.getTemp1()) {
      return -1;
    }
    return 0;
  }
}
