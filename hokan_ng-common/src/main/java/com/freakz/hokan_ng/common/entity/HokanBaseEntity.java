package com.freakz.hokan_ng.common.entity;

import com.freakz.hokan_ng.common.util.StaticStrings;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by AirioP on 21.8.2014.
 */
@MappedSuperclass
public class HokanBaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long id;

  @Column(name = "CREATED")
  private Date created;

  @Column(name = "MODIFIED")
  private Date modified;

  @Column(name = "CREATOR_NAME")
  private String creatorName;

  public HokanBaseEntity() {
    this.created = new Date();
    this.modified = created;
    this.creatorName = StaticStrings.UNKNOWN;
  }

  public HokanBaseEntity(String creatorName) {
    this();
    this.creatorName = creatorName;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  @PreUpdate
  public void preUpdate() {
    this.modified = new Date();
  }

  @PrePersist
  public void prePersist() {
    Date now = new Date();
    this.created = now;
    this.modified = now;
  }

}
