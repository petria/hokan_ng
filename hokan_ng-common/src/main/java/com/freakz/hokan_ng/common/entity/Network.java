package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 3/20/11
 * Time: 1:31 PM
 */
@Entity
@Table(name = "Network")
public class Network implements Serializable {

  @Id
  @Column(name = "NETWORK_NAME")
  private String networkName;

  @Column(name = "FIRST_CONNECTED")
  private Date firstConnected;

  @Column(name = "CONNECT_COUNT")
  private int connectCount;

  @Column(name = "LINES_SENT")
  private int linesSent;

  @Column(name = "LINES_RECEIVED")
  private int linesReceived;

  @Column(name = "CHANNELS_JOINED")
  private int channelsJoined;

  public Network() {
  }

  public Network(String name) {
    this.networkName = name;
  }

  public String getName() {
    return networkName;
  }

  public void setName(String name) {
    this.networkName = name;
  }

  public Date getFirstConnected() {
    return firstConnected;
  }

  public void setFirstConnected(Date firstConnected) {
    this.firstConnected = firstConnected;
  }

  public int getConnectCount() {
    return connectCount;
  }

  public void setConnectCount(int connectCount) {
    this.connectCount = connectCount;
  }

  public void addToConnectCount(int delta) {
    this.connectCount += delta;
  }

  public int getLinesSent() {
    return linesSent;
  }

  public void setLinesSent(int linesSent) {
    this.linesSent = linesSent;
  }

  public void addToLinesSent(int delta) {
    this.linesSent += delta;
  }

  public int getLinesReceived() {
    return linesReceived;
  }

  public void setLinesReceived(int linesReceived) {
    this.linesReceived = linesReceived;
  }

  public void addToLinesReceived(int delta) {
    this.linesReceived += delta;
  }

  public int getChannelsJoined() {
    return channelsJoined;
  }

  public void setChannelsJoined(int channelsJoined) {
    this.channelsJoined = channelsJoined;
  }

  public void addToChannelsJoined(int delta) {
    this.channelsJoined += delta;
  }

  public String toString() {
    return String.format("[%s]", this.networkName);
  }

}
