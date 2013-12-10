package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelProperty;
import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanDAOException;

import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:51 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface PropertyDAO {

  List<Property> getProperties();

  Property findProperty(PropertyName name) throws HokanDAOException;

  Property setProperty(PropertyName name, String value) throws HokanDAOException;

  Property saveProperty(Property property) throws HokanDAOException;

  List<Channel> getChannelsWithProperty(PropertyName propertyName) throws HokanDAOException;

  List<ChannelProperty> getChannelProperties(Channel... channel) throws HokanDAOException;

  ChannelProperty findChannelProperty(Channel channel, PropertyName name) throws HokanDAOException;

  ChannelProperty setChannelProperty(Channel channel, PropertyName name, String value) throws HokanDAOException;

  ChannelProperty saveChannelProperty(ChannelProperty property) throws HokanDAOException;

}
