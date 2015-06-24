package com.freakz.hokan_ng.common.updaters;

import java.io.Serializable;

/**
 * Created by Petri Airio on 22.6.2015.
 *
 */
public class KelikameratUrl implements Serializable {

  private String areaUrl;

  private String stationUrl;

  public KelikameratUrl() {
  }

  public KelikameratUrl(String areaUrl, String stationUrl) {
    this.areaUrl = areaUrl;
    this.stationUrl = stationUrl;
  }

  public String getAreaUrl() {
    return areaUrl;
  }

  public void setAreaUrl(String areaUrl) {
    this.areaUrl = areaUrl;
  }

  public String getStationUrl() {
    return stationUrl;
  }

  public void setStationUrl(String stationUrl) {
    this.stationUrl = stationUrl;
  }
}
