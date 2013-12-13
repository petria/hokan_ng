package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelProperty;
import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;

import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:50 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface PropertyService {

  List<Property> getProperties();

  Property findProperty(PropertyName name) throws HokanException;

  Property setProperty(PropertyName name, String value) throws HokanException;

  Property saveProperty(Property property) throws HokanException;

  ChannelProperty saveChannelProperty(ChannelProperty property) throws HokanException;

  List<ChannelProperty> getChannelProperties(Channel... channel);

  List<Channel> getChannelsWithProperty(PropertyName propertyName);

  ChannelProperty findChannelProperty(Channel channel, PropertyName name) throws HokanException;
}
