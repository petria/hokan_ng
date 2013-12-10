package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * User: petria
 * Date: 12/10/13
 * Time: 2:01 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@MappedSuperclass
public class PropertyBase implements Serializable {

  @Id
  @Column(name = "PROPERTY")
  @Enumerated(EnumType.STRING)
  private PropertyName property;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "FLAGS")
  private String flags;

  public PropertyBase() {
  }

  public PropertyBase(PropertyName property, String value, String flags) {
    this.property = property;
    this.value = value;
    this.flags = flags;
  }

  public PropertyName getProperty() {
    return property;
  }

  public void setProperty(PropertyName property) {
    this.property = property;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getFlags() {
    return flags;
  }

  public void setFlags(String flags) {
    this.flags = flags;
  }
}
