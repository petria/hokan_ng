package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Url;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
  public List<Url> findUrls(String url, String... nicks) {

    TypedQuery<Url> query;
    if (nicks == null || nicks.length == 0) {
      query = entityManager.createQuery("SELECT url FROM Url url WHERE url.url LIKE :url ORDER BY url.created DESC", Url.class);
    } else {
      List<String> nickList = Arrays.asList(nicks);
      query = entityManager.createQuery("SELECT url FROM Url url WHERE url.url LIKE :url AND url.sender IN (:nickList) ORDER BY url.created DESC", Url.class);
      query.setParameter("nickList", nickList);
    }
    query.setParameter("url", "%" + url + "%");
    try {
      return query.getResultList();
    } catch (Exception e) {
      //
    }
    return null;
  }

  @Override
  public Url findUrl(String url, String... nicks) {
    List<Url> urlList = findUrls(url, nicks);
    if (urlList.size() == 1) {
      return urlList.get(0);
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
