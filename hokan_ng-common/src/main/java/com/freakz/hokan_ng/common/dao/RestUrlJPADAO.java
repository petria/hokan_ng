package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.RestUrl;
import com.freakz.hokan_ng.common.entity.RestUrlType;
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
public class RestUrlJPADAO implements RestUrlDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public int deleteRestUrls(String instanceKey) {
    Query query = entityManager.createQuery("DELETE FROM RestUrl ee WHERE ee.instanceKey = :instanceKey");
    query.setParameter("instanceKey", instanceKey);
    return query.executeUpdate();
  }


  @Override
  public List<RestUrl> getRestUrls(String instanceKey) {
    TypedQuery<RestUrl> query = entityManager.createQuery("SELECT ee FROM RestUrl ee WHERE ee.instanceKey = :instanceKey", RestUrl.class);
    query.setParameter("instanceKey", instanceKey);
    return query.getResultList();
  }

  @Override
  public List<RestUrl> getRestUrls(String instanceKey, RestUrlType restUrlType) {
    TypedQuery<RestUrl> query = entityManager.createQuery("SELECT ee FROM RestUrl ee WHERE ee.instanceKey = :instanceKey AND ee.restUrlType = :restUrlType", RestUrl.class);
    query.setParameter("instanceKey", instanceKey);
    query.setParameter("restUrlType", restUrlType);
    return query.getResultList();
  }

  @Override
  public List<RestUrl> getRestUrls(RestUrlType restUrlType) {
    TypedQuery<RestUrl> query = entityManager.createQuery("SELECT ee FROM RestUrl ee WHERE ee.restUrlType = :restUrlType", RestUrl.class);
    query.setParameter("restUrlType", restUrlType);
    return query.getResultList();
  }


  @Override
  public RestUrl addRestUrl(String instanceKey, RestUrlType restUrlType, String restUrl, String lineMatcherRegexp) {
    RestUrl ee = new RestUrl();
    ee.setInstanceKey(instanceKey);
    ee.setRestUrlType(restUrlType);
    ee.setRestUrl(restUrl);
    ee.setLineMatcherRegexp(lineMatcherRegexp);
    entityManager.persist(ee);
    return ee;
  }

}
