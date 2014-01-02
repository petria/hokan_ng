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
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 3/7/11
 * Time: 4:20 PM
 */
@Entity
@Table(name = "URL")
@NamedQueries({
    @NamedQuery(name = "URL.findUrl", query = "SELECT url FROM Url url WHERE url.url = ?1"),
    @NamedQuery(name = "URL.findUrls", query = "SELECT url FROM Url url ORDER BY url.created DESC"),
    @NamedQuery(name = "URL.findUrlsLike", query = "SELECT url FROM Url url WHERE url.url LIKE ?1 ORDER BY url.created DESC"),
    @NamedQuery(name = "URL.findUrlsLikeTitle", query = "SELECT url FROM Url url WHERE url.urlTitle LIKE ?1 ORDER BY url.created DESC"),
    @NamedQuery(name = "URL.findUrlsLikeAndNick", query = "SELECT url FROM Url url WHERE url.url LIKE ?1 AND url.sender = ?2 ORDER BY url.created DESC "),
    @NamedQuery(name = "URL.findUrlsByNick", query = "SELECT url FROM Url url WHERE url.sender = ?1 ORDER BY url.created DESC "),
    @NamedQuery(name = "URL.findUrlsByChannel", query = "SELECT url FROM Url url WHERE url.channel = ?1 ORDER BY url.created DESC "),
    @NamedQuery(name = "URL.findTopSenderByChannel", query = "SELECT url, count(url) FROM Url url WHERE url.channel = ?1 GROUP BY url.sender ORDER BY 2 DESC"),
    @NamedQuery(name = "URL.findTopSender", query = "SELECT url, count(url) FROM Url url GROUP BY url.sender ORDER BY 2 DESC")
})
public class Url implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long id;

  @Column(name = "URL", length = 1024)
  private String url;

  @Column(name = "URL_TITLE", length = 1024)
  private String urlTitle;

  @Column(name = "SENDER")
  private String sender;

  @Column(name = "CHANNEL")
  private String channel;

  @Column(name = "CREATED")
  private Date created;

  @Column(name = "WANHA_COUNT")
  private long wanhaCount;

  public Url() {

  }

  public Url(String url, String sender, String channel, Date created) {
    this.url = url;
    this.sender = sender;
    this.channel = channel;
    this.created = created;
    this.wanhaCount = 0;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrlTitle() {
    return urlTitle;
  }

  public void setUrlTitle(String urlTitle) {
    this.urlTitle = urlTitle;
  }


  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date time) {
    this.created = time;
  }

  public long getId() {
    return id;
  }

  public long getWanhaCount() {
    return this.wanhaCount;
  }

  public void addWanhaCount(long delta) {
    this.wanhaCount += delta;
  }

  public void setWanhaCount(long wanhaCount) {
    this.wanhaCount = wanhaCount;
  }
}