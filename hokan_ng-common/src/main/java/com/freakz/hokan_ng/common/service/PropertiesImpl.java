package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 12/4/13
 * Time: 9:36 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class PropertiesImpl implements Properties {

  @Autowired
  private PropertyService service;

  private Property getProperty(PropertyName name, Object def) {
    try {
      Property property = service.findProperty(name);
      if (property == null) {
        property = new Property(name, "" + def, "");
      }
      return property;
    } catch (HokanException e) {
      log.error("property error", e);
    }
    return null;
  }

  @Override
  public int getPropertyAsInt(PropertyName name, int def) {
    Property property = getProperty(name, def);
    int val = Integer.parseInt(property.getValue());
    return val;
  }

  @Override
  public long getPropertyAsLong(PropertyName name, long def) {
    Property property = getProperty(name, def);
    long val = Long.parseLong(property.getValue());
    return val;
  }

  @Override
  public boolean getPropertyAsBoolean(PropertyName name, boolean def) {
    Property property = getProperty(name, def);
    boolean val = Boolean.parseBoolean(property.getValue());
    return val;
  }

  @Override
  public Property saveProperty(Property property) {
    try {
      return service.saveProperty(property);
    } catch (HokanException e) {
      log.error("property error", e);
    }
    return null;
  }

}
