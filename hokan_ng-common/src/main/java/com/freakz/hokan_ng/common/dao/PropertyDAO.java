package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:51 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface PropertyDAO {

  Property findProperty(PropertyName name) throws HokanException;

  Property setProperty(PropertyName name, String value) throws HokanException;

}