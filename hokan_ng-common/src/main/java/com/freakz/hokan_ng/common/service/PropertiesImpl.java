package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelProperty;
import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
  public ChannelProperty saveChannelProperty(ChannelProperty property) {
    try {
      return service.saveChannelProperty(property);
    } catch (HokanException e) {
      log.error("property error", e);
    }
    return null;
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

  @Override
  @SuppressWarnings("unchecked")
  public List getAllProperties() {
    List allProperties = new ArrayList<>();
    List<Property> props1 = service.getProperties();
    List<ChannelProperty> props2 = service.getChannelProperties();
    allProperties.addAll(props1);
    allProperties.addAll(props2);
    return allProperties;
  }

  @Override
  public List getChannelProperties(Channel channel) {
    return service.getChannelProperties(channel);
  }

  @Override
  public List<Channel> getChannelsWithProperty(PropertyName propertyName) {
    return service.getChannelsWithProperty(propertyName);
  }

  @Override
  public PropertyName getPropertyName(String property) {
    for (PropertyName prop : PropertyName.values()) {
      if (StringStuff.match(prop.toString(), property, true)) {
        return prop;
      }
    }
    return null;
  }
}
