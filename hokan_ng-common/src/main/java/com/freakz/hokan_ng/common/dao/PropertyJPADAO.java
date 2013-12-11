package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelProperty;
import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 10:52 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository(value = "Properties")
@Transactional
public class PropertyJPADAO implements PropertyDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Property> getProperties() {
    TypedQuery<Property> query =
        entityManager.createQuery("SELECT p FROM Property p ORDER BY p.property", Property.class);
    return query.getResultList();
  }

  @Override
  public Property findProperty(PropertyName property) throws HokanDAOException {
    TypedQuery<Property> query =
        entityManager.createQuery("SELECT p FROM Property p WHERE p.property = :property", Property.class);
    query.setParameter("property", property);
    return query.getSingleResult();
  }

  @Override
  public Property setProperty(PropertyName name, String value) throws HokanDAOException {
    Property property = null;
    try {
      property = findProperty(name);
    } catch (Exception e) {
      //
    } finally {
      if (property == null) {
        property = new Property(name, value, "");
      } else {
        property.setValue(value);
      }
    }
    return entityManager.merge(property);
  }

  @Override
  public Property saveProperty(Property property) throws HokanDAOException {
    return entityManager.merge(property);
  }

  @Override
  public List<Channel> getChannelsWithProperty(PropertyName property) throws HokanDAOException {
    TypedQuery<Channel> query =
        entityManager.createQuery("SELECT p.channel FROM ChannelProperty p WHERE property = :property ORDER BY p.property", Channel.class);
    query.setParameter("property", property);
    return query.getResultList();
  }

  @Override
  public List<ChannelProperty> getChannelProperties(Channel... channel) throws HokanDAOException {
    TypedQuery<ChannelProperty> query;
    List<Channel> channels = Arrays.asList(channel);
    if (channels.isEmpty()) {
      query =
          entityManager.createQuery("SELECT p FROM ChannelProperty p ORDER BY p.property", ChannelProperty.class);

    } else {
      query =
          entityManager.createQuery("SELECT p FROM ChannelProperty p WHERE p.channel IN (:channels) GROUP BY p.channel ORDER BY p.property", ChannelProperty.class);
      query.setParameter("channels", channels);
    }
    return query.getResultList();
  }

  @Override
  public ChannelProperty findChannelProperty(Channel channel, PropertyName property) throws HokanDAOException {
    TypedQuery<ChannelProperty> query =
        entityManager.createQuery("SELECT p FROM ChannelProperty p WHERE p.channel = :channel AND p.property = :property GROUP BY p.channel ORDER BY p.property", ChannelProperty.class);
    query.setParameter("channel", channel);
    query.setParameter("property", property);
    return query.getSingleResult();
  }

  @Override
  public ChannelProperty setChannelProperty(Channel channel, PropertyName name, String value) throws HokanDAOException {
    ChannelProperty property = new ChannelProperty(channel, name, value, "");
    return entityManager.merge(property);
  }

  @Override
  public ChannelProperty saveChannelProperty(ChannelProperty property) throws HokanDAOException {
    return entityManager.merge(property);
  }
}
