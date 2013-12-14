package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Url;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 10:11 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("Url")
@Transactional
public class UrlJPADAO implements UrlDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Url findUrl(String url) {
    TypedQuery<Url> query = entityManager.createQuery("SELECT url FROM Url url WHERE url.url = :url", Url.class);
    query.setParameter("url", url);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      //
    }
    return null;
  }

  @Override
  public Url storeUrl(Url entity) {
    return entityManager.merge(entity);
  }

  @Override
  public Url createUrl(String url, String sender, String channel, Date date) {
    Url entity = new Url(url, sender, channel, date);
    entityManager.persist(entity);
    return entity;
  }

}
