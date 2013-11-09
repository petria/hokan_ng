package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
  public Property findProperty(PropertyName name) throws HokanException {
    return entityManager.find(Property.class, name);
  }

  @Override
  public Property setProperty(PropertyName name, String value) throws HokanException {
    Property property = new Property(name, value, "");
    return entityManager.merge(property);
  }

}
