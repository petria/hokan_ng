package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User: petria
 * Date: 3/1/11
 * Time: 2:17 PM
 */
@Entity
@Table(name = "User")
@NamedQueries({
    @NamedQuery(name = "USER.findUser", query = "SELECT ude FROM User ude WHERE ude.nick = ?1"),
    @NamedQuery(name = "USER.findUsersByMask", query = "SELECT ude FROM User ude WHERE ude.mask LIKE ?1"),
    @NamedQuery(name = "USER.findUsers", query = "SELECT ude FROM User ude"),
    @NamedQuery(name = "USER.findLoggedInUsers", query = "SELECT ude FROM User ude where ude.loggedIn > 0"),
    @NamedQuery(name = "USER.resetLoggedInUsers", query = "UPDATE User ude SET ude.loggedIn = 0"),
    @NamedQuery(name = "USER.resetOlpos", query = "UPDATE User ude SET ude.olpo = 0")
})
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long userId;

  @Column(name = "NICK")
  private String nick;


  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "MASK")
  private String mask;
  @Column(name = "REAL_MASK")
  private String realMask;

  @Column(name = "FLAGS")
  private String flags;

  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "EMAIL")
  private String email;

  @Column(name = "LOGGED_IN")
  private int loggedIn;

  @Column(name = "OLPO")
  private int olpo;

  public User() {
    this.nick = "";
    this.password = "";
    this.realMask = "";
    this.flags = "";
    this.fullName = "";
    this.phone = "";
    this.email = "";
    this.loggedIn = 0;
    this.olpo = 0;
  }

  public User(String nick) {
    this();
    this.nick = nick;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long id) {
    this.userId = id;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMask() {
    return mask;
  }

  public void setMask(String mask) {
    this.mask = mask;
  }

  public String getFlags() {
    return flags;
  }

  public void setFlags(String flags) {
    this.flags = flags;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(int loggedIn) {
    this.loggedIn = loggedIn;
  }

  public int isOlpo() {
    return olpo;
  }

  public void setOlpo(int olpo) {
    this.olpo = olpo;
  }

  public String getRealMask() {
    return realMask;
  }

  public void setRealMask(String realMask) {
    this.realMask = realMask;
  }

}
