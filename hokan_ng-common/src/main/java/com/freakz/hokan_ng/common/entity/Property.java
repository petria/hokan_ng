package com.freakz.hokan_ng.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User: petria
 * Date: 2/19/12
 * Time: 4:35 PM
 */
@Entity
@Table(name = "Properties")
/*@NamedQueries({
    @NamedQuery(name = "PROPERTY.findProperty", query = "SELECT prop FROM Property prop WHERE prop.property = ?1"),
    @NamedQuery(name = "PROPERTY.getAllProperties", query = "SELECT prop FROM Property prop ORDER BY prop.property"),
    @NamedQuery(name = "PROPERTY.findPropertyLike", query = "SELECT prop FROM Property prop WHERE prop.property LIKE ?1 ORDER BY prop.property")
})*/
public class Property implements Serializable {

  @Id
  @Column(name = "PROPERTY")
  @Enumerated(EnumType.STRING)
  private PropertyName property;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "FLAGS")
  private String flags;

  public Property() {
  }

  public Property(PropertyName property, String value, String flags) {
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
