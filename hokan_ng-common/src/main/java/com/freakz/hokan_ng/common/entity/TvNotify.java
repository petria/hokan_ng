package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User: petria
 * Date: 2/24/12
 * Time: 5:53 PM
 */
@Entity
@Table(name = "TvNotify")
@NamedQueries({
    @NamedQuery(name = "TVNOTIFY.getAllTvNotifies", query = "SELECT notify FROM TvNotify notify"),
    @NamedQuery(name = "TVNOTIFY.getTvNotifiesByChannel", query = "SELECT notify FROM TvNotify notify WHERE notify.channel = ?1"),
    @NamedQuery(name = "TVNOTIFY.findTvNotify", query = "SELECT notify FROM TvNotify notify WHERE notify.notifyPattern = ?1 AND notify.channel = ?2"),
    @NamedQuery(name = "TVNOTIFY.deleteAll", query = "DELETE FROM TvNotify")
//    @NamedQuery(name = "TVNOTIFY.deleteByChannel", query = "DELETE FROM TvNotify WHERE notify.channel = ?1")
})

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
