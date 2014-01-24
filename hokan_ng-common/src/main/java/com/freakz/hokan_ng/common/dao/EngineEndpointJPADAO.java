package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.EngineEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: petria
 * Date: 1/24/14
 * Time: 12:11 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository(value = "EngineEndpoint")
@Slf4j
@Transactional
public class EngineEndpointJPADAO implements EngineEndpointDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public int deleteEngineEndpoints(String instanceKey) {
    Query query = entityManager.createQuery("DELETE FROM EngineEndpoint ee WHERE ee.instanceKey = :instanceKey");
    query.setParameter("instanceKey", instanceKey);
    return query.executeUpdate();
  }

  @Override
  public List<EngineEndpoint> getEngineEndpoints(String instanceKey) {
    TypedQuery<EngineEndpoint> query = entityManager.createQuery("SELECT ee FROM EngineEndpoint ee WHERE ee.instanceKey = :instanceKey", EngineEndpoint.class);
    query.setParameter("instanceKey", instanceKey);
    return query.getResultList();
  }

  @Override
  public EngineEndpoint addEngineEndpoint(String instanceKey, String endpointAddress, String lineMatcherRegexp) {
    EngineEndpoint ee = new EngineEndpoint();
    ee.setInstanceKey(instanceKey);
    ee.setEndpointAddress(endpointAddress);
    ee.setLineMatcherRegexp(lineMatcherRegexp);
    return entityManager.merge(ee);
  }

}
