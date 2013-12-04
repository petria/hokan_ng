package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:50 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface PropertyService {

  Property findProperty(PropertyName name) throws HokanException;

  Property setProperty(PropertyName name, String value) throws HokanException;

  Property saveProperty(Property property) throws HokanException;

}
