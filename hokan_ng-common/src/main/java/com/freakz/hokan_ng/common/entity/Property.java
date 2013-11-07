package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User: petria
 * Date: 2/19/12
 * Time: 4:35 PM
 */
//@Entity
@Table(name = "PROPERTIES")
/*@NamedQueries({
    @NamedQuery(name = "PROPERTY.findProperty", query = "SELECT prop FROM Property prop WHERE prop.property = ?1"),
    @NamedQuery(name = "PROPERTY.getAllProperties", query = "SELECT prop FROM Property prop ORDER BY prop.property"),
    @NamedQuery(name = "PROPERTY.findPropertyLike", query = "SELECT prop FROM Property prop WHERE prop.property LIKE ?1 ORDER BY prop.property")
})*/
public class Property implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private long propertyId;

  @Column(name = "PROPERTY")
  private String property;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "FLAGS")
  private String flags;

  public Property() {
    this.property = "";
    this.value = "";
    this.flags = "";
  }

  public Property(String property, String value, String flags) {
    this.property = property;
    this.value = value;
    this.flags = flags;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
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

  public long getId() {
    return propertyId;
  }

  public void setId(long id) {
    this.propertyId = id;
  }
}
