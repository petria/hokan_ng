package com.freakz.hokan_ng.common.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: petria
 * Date: 2/24/12
 * Time: 5:53 PM
 */
@Entity
@Table(name = "TvNotify")
public class TvNotify implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long notifyId;

  @ManyToOne
  @JoinColumn(name = "CHANNEL_ID_FK", referencedColumnName = "ID", nullable = false)
  private Channel channel;

  @Column(name = "NOTIFY_PATTERN")
  private String notifyPattern;

  @Column(name = "NOTIFY_OWNER")
  private String notifyOwner;


  @Column(name = "ONLY_ONCE")
  private int onlyOnce;

  public TvNotify() {
  }

  public TvNotify(String notifyPattern, String notifyOwner, Channel channel, boolean once) {
    this.notifyPattern = notifyPattern;
    this.notifyOwner = notifyOwner;
    this.channel = channel;
    if (once) {
      this.onlyOnce = 1;
    } else {
      this.onlyOnce = 0;
    }
  }

  public long getId() {
    return notifyId;
  }

  public void setId(long notifyId) {
    this.notifyId = notifyId;
  }

  public String getNotifyPattern() {
    return notifyPattern;
  }

  public void setNotifyPattern(String notifyPattern) {
    this.notifyPattern = notifyPattern;
  }

  public String getNotifyOwner() {
    return notifyOwner;
  }

  public void setNotifyOwner(String notifyOwner) {
    this.notifyOwner = notifyOwner;
  }

  public Channel getNotifyChannel() {
    return channel;
  }

  public void setNotifyChannel(Channel channel) {
    this.channel = channel;
  }

  public int getOnlyOnce() {
    return onlyOnce;
  }

  public void setOnlyOnce(int onlyOnce) {
    this.onlyOnce = onlyOnce;
  }

}
