package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Date: 3.6.2013
 * Time: 10:39
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Entity
@Table(name = "IrcServerConfig")
public class IrcServerConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long id;

  @Column(name = "NETWORK")
  private String network;

  @Column(name = "SERVER")
  private String server;

  @Column(name = "SERVER_PASSWORD")
  private String serverPassword;

  @Column(name = "SERVER_PORT")
  private int port;

  @Column(name = "USE_THROTTLE")
  private int useThrottle;

  @Column(name = "CHANNELS_TO_JOIN")
  private String channelsToJoin;

  public IrcServerConfig() {
  }

  public String getNetwork() {
    return network;
  }

  public void setNetwork(String network) {
    this.network = network;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getServerPassword() {
    return serverPassword;
  }

  public void setServerPassword(String serverPassword) {
    this.serverPassword = serverPassword;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getUseThrottle() {
    return useThrottle;
  }

  public void setUseThrottle(int useThrottle) {
    this.useThrottle = useThrottle;
  }

  public boolean throttleInUse() {
    return this.useThrottle > 0;
  }

  public String getChannelsToJoin() {
    return channelsToJoin;
  }

  public String[] getChannels() {
    return this.getChannelsToJoin().split(",");
  }

  public void setChannelsToJoin(String channels) {
    this.channelsToJoin = channels;
  }

  public String toString() {
    return String.format("[%s] %s:%d(%s) - %s ", this.network, this.server, this.port, this.serverPassword, this.channelsToJoin);
  }
}
