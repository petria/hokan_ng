package com.freakz.hokan_ng.common.entity;

import javax.persistence.*;

/**
 * User: petria
 * Date: 11/11/13
 * Time: 10:19 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Entity
@Table(name = "JoinedUser")
public class JoinedUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long id;

  @ManyToOne
  @JoinColumn(name = "CHANNEL", referencedColumnName = "ID", nullable = false)
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "USER", referencedColumnName = "ID", nullable = false)
  private User user;

  @Column(name = "IS_OP")
  private boolean isOp;

  public JoinedUser() {
  }

  public JoinedUser(Channel channel, User user) {
    this.channel = channel;
    this.user = user;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }


  public void setOp(boolean isOp) {
    this.isOp = isOp;
  }

  public boolean isOp() {
    return this.isOp;
  }

}
