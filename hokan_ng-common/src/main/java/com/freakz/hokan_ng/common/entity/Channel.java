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
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 3/4/11
 * Time: 10:02 AM
 */
@Entity
@Table(name = "CHANNEL")
@NamedQueries({
    @NamedQuery(name = "CHANNEL.findChannel", query = "SELECT ch FROM Channel ch WHERE ch.network = ?1 AND ch.channelName = ?2"),
    @NamedQuery(name = "CHANNEL.getActiveChannels", query = "SELECT ch FROM Channel ch WHERE ch.active > 0"),
    @NamedQuery(name = "CHANNEL.getReportChannels", query = "SELECT ch FROM Channel ch WHERE ch.reportChannel > 0"),
    @NamedQuery(name = "CHANNEL.resetActiveChannels", query = "UPDATE Channel ch SET ch.active = 0")
})
public class Channel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long channelId;

  @ManyToOne
  @JoinColumn(name = "NETWORK_NAME_FK", referencedColumnName = "NETWORK_NAME")
  private Network network;

  @Column(name = "CHANNEL_NAME")
  private String channelName;

  @Column(name = "FIRST_JOINED")
  private Date firstJoined;

  @Column(name = "MAX_USER_COUNT")
  private int maxUserCount;

  @Column(name = "MAX_USER_COUNT_DATE")
  private Date maxUserCountDate;

  @Column(name = "LINES_SENT")
  private int linesSent;

  @Column(name = "LINES_RECEIVED")
  private int linesReceived;

  @Column(name = "ACTIVE")
  private int active;

  @Column(name = "LAST_ACTIVE")
  private Date lastActive;

  //---
  @Column(name = "LAST_WRITER")
  private String lastWriter;

  @Column(name = "LAST_WRITER_SPREE")
  private int lastWriterSpree;

  @Column(name = "WRITER_SPREE_RECORD")
  private int writerSpreeRecord;

  @Column(name = "WRITER_SPREE_OWNER")
  private String writerSpreeOwner;

  @Column(name = "REPORT_CHANNEL")
  private int reportChannel;

  @Column(name = "COMMANDS_HANDLED")
  private int commandsHandled;

  public Channel(Network network) {
    this.network = network;
  }

  public Channel(String name, Network network) {
    this.channelName = name;
    this.network = network;
  }

  public Channel() {
  }

  public long getChannelId() {
    return channelId;
  }

  public void setChannelId(long channelId) {
    this.channelId = channelId;
  }

  public Date getFirstJoined() {
    return firstJoined;
  }

  public void setFirstJoined(Date firstJoined) {
    this.firstJoined = firstJoined;
  }

  public int getMaxUserCount() {
    return maxUserCount;
  }

  public void setMaxUserCount(int maxUserCount) {
    this.maxUserCount = maxUserCount;
  }

  public Date getMaxUserCountDate() {
    return maxUserCountDate;
  }

  public void setMaxUserCountDate(Date maxUserCountDate) {
    this.maxUserCountDate = maxUserCountDate;
  }

  public Network getNetwork() {
    return network;
  }

  public void setNetwork(Network network) {
    this.network = network;
  }

  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
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

  public boolean isActive() {
    return active > 0;
  }

  public void setActive(boolean active) {
    if (active) {
      this.active = 1;
    } else {
      this.active = 0;
    }
  }

  public long getId() {
    return channelId;
  }

  public Date getLastActive() {
    return lastActive;
  }

  public void setLastActive(Date lastActive) {
    this.lastActive = lastActive;
  }

  public String getLastWriter() {
    return lastWriter;
  }

  public void setLastWriter(String lastWriter) {
    this.lastWriter = lastWriter;
  }

  public int getLastWriterSpree() {
    return lastWriterSpree;
  }

  public void setLastWriterSpree(int lastWriterSpree) {
    this.lastWriterSpree = lastWriterSpree;
  }

  public int getWriterSpreeRecord() {
    return writerSpreeRecord;
  }

  public void setWriterSpreeRecord(int writerSpreeRecord) {
    this.writerSpreeRecord = writerSpreeRecord;
  }

  public String getWriterSpreeOwner() {
    return writerSpreeOwner;
  }

  public void setWriterSpreeOwner(String writerSpreeOwner) {
    this.writerSpreeOwner = writerSpreeOwner;
  }

  public int getReportChannel() {
    return reportChannel;
  }

  public void setReportChannel(int reportChannel) {
    this.reportChannel = reportChannel;
  }

  public int getCommandsHandled() {
    return commandsHandled;
  }

  public void setCommandsHandled(int commandsHandled) {
    this.commandsHandled = commandsHandled;
  }

  public void addCommandsHandled(int delta) {
    this.commandsHandled += delta;
  }

}
