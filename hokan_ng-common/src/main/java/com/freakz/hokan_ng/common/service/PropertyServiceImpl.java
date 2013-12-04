package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.PropertyDAO;
import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:54 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class PropertyServiceImpl implements PropertyService {

  @Autowired
  private PropertyDAO propertyDAO;

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
}
