package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Property;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:50 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface PropertyService {

  Property getProperty(String key);

  Property setProperty(String key, String value);

}
