package com.freakz.hokan_ng.common.updaters;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Petri Airio on 23.6.2015.
 *
 */
public class KelikameratWeatherData implements Comparable, Serializable {

  private DateTime time;

  private KelikameratUrl url;

  private String place;
  private String placeFromUrl;

  private Float air;
  private Float road;
  private Float ground;

  private Float humidity;
  private Float dewPoint;
  private int pos;
  private int count;


  public KelikameratWeatherData() {
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public DateTime getTime() {
    return time;
  }

  public void setTime(DateTime time) {
    this.time = time;
  }

  public KelikameratUrl getUrl() {
    return url;
  }

  public void setUrl(KelikameratUrl url) {
    this.url = url;
  }

  public Float getAir() {
    return air;
  }

  public void setAir(Float air) {
    this.air = air;
  }

  public Float getRoad() {
    return road;
  }

  public void setRoad(Float road) {
    this.road = road;
  }

  public Float getGround() {
    return ground;
  }

  public void setGround(Float ground) {
    this.ground = ground;
  }

  public Float getHumidity() {
    return humidity;
  }

  public void setHumidity(Float humidity) {
    this.humidity = humidity;
  }

  public Float getDewPoint() {
    return dewPoint;
  }

  public void setDewPoint(Float dewPoint) {
    this.dewPoint = dewPoint;
  }

  public void setPlaceFromUrl(String placeFromUrl) {
    this.placeFromUrl = placeFromUrl;
  }

  public String getPlaceFromUrl() {
    return this.placeFromUrl;
  }

  @Override
  public int compareTo(Object other) {
    KelikameratWeatherData w1 = this;
    KelikameratWeatherData w2 = (KelikameratWeatherData) other;

    if (w1.getAir() > w2.getAir()) {
      return 1;
    }
    if (w1.getAir() < w2.getAir()) {
      return -1;
    }
    return 0;
  }

  public void setPos(int i) {
    this.pos = i;
  }

  public void setCount(int size) {
    this.count = size;
  }

  public int getPos() {
    return pos;
  }

  public int getCount() {
    return count;
  }
}
