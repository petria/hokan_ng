package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelProperty;
import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;

import java.util.List;

/**
 * User: petria
 * Date: 12/4/13
 * Time: 9:33 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface Properties {

  String getPropertyAsString(PropertyName name, String def);

  int getPropertyAsInt(PropertyName property, int def);

  long getPropertyAsLong(PropertyName property, long def);

  boolean getPropertyAsBoolean(PropertyName property, boolean def);

  boolean getChannelPropertyAsBoolean(Channel channel, PropertyName property, boolean def);

//  Property saveProperty(Property property);

  //  ChannelProperty saveChannelProperty(ChannelProperty property);
  ChannelProperty setChannelProperty(Channel channel, PropertyName property, String value);

  List<Property> getAllProperties();

  PropertyName getPropertyName(String property);

  List getChannelProperties(Channel channel);

  List<Channel> getChannelsWithProperty(PropertyName propertyName);

}
