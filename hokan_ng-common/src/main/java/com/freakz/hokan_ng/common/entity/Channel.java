package com.freakz.hokan_ng.common.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 3/4/11
 * Time: 10:02 AM
 */
@Entity
@Table(name = "Channel")
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

  @Column(name = "CHANNEL_STATE", nullable = false)
  @Enumerated(EnumType.STRING)
  private ChannelState channelState;

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

  @Column(name = "TOPIC")
  private String topic;

  @Column(name = "TOPIC_SET_BY")
  private String topicSetBy;

  @Column(name = "TOPIC_SET_DATE")
  private Date topicSetDate;


  public Channel(Network network, String name) {
    this.network = network;
    this.channelName = name;
    this.channelState = ChannelState.NEW;
  }

  public Channel() {
    this.channelState = ChannelState.NEW;
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

  public ChannelState getChannelState() {
    return channelState;
  }

  public void setChannelState(ChannelState channelState) {
    this.channelState = channelState;
  }

  public Date getLastActive() {
    return lastActive;
  }

  public void setLastActive(Date lastActive) {
    this.lastActive = lastActive;
  }

  public long getIdleTime() {
    if (lastActive == null) {
      return -1;
    }
    return (new Date().getTime() - lastActive.getTime()) / 1000;
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

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getTopicSetBy() {
    return topicSetBy;
  }

  public void setTopicSetBy(String topicSetBy) {
    this.topicSetBy = topicSetBy;
  }

  public Date getTopicSetDate() {
    return topicSetDate;
  }

  public void setTopicSetDate(Date topicSetDate) {
    this.topicSetDate = topicSetDate;
  }
}
