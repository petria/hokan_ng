package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.PropertyDAO;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelProperty;
import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import com.freakz.hokan_ng.common.exception.HokanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:54 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class PropertyServiceImpl implements PropertyService {

  @Autowired
  private PropertyDAO propertyDAO;

  @Override
  public List<Property> getProperties() {
    return propertyDAO.getProperties();
  }

  @Override
  public Property findProperty(PropertyName name) throws HokanException {
    return propertyDAO.findProperty(name);
  }

  @Override
  public Property setProperty(PropertyName name, String value) throws HokanException {
    return propertyDAO.setProperty(name, value);
  }

  @Override
  public Property saveProperty(Property property) throws HokanException {
    return propertyDAO.saveProperty(property);
  }

  @Override
  public ChannelProperty saveChannelProperty(ChannelProperty property) throws HokanException {
    return propertyDAO.saveChannelProperty(property);
  }

  @Override
  public List<ChannelProperty> getChannelProperties(Channel... channel) {
    try {
      return propertyDAO.getChannelProperties(channel);
    } catch (HokanDAOException e) {
      log.error("Property error", e);
    }
    return new ArrayList<>();
  }
}
