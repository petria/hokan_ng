package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.PropertyDAO;
import com.freakz.hokan_ng.common.entity.Property;
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
  public Property getProperty(String key) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Property setProperty(String key, String value) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
