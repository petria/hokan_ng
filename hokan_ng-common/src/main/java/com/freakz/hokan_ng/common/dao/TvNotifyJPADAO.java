package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.TvNotify;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 6:29 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("TvNotify")
@Transactional
public class TvNotifyJPADAO implements TvNotifyDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public TvNotify addTvNotify(Channel channel, String pattern, String owner) {
    TvNotify notify = new TvNotify(pattern, owner, channel, false);
    return entityManager.merge(notify);
  }

  @Override
  public List<TvNotify> getTvNotifies(Channel channel) throws HokanDAOException {
    try {
      TypedQuery<TvNotify> query
          = entityManager.createQuery("SELECT tv FROM TvNotify tv WHERE tv.channel = :channel", TvNotify.class);
      query.setParameter("channel", channel);
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public TvNotify getTvNotify(Channel channel, String pattern) throws HokanDAOException {
    try {
      TypedQuery<TvNotify> query
          = entityManager.createQuery("SELECT tv FROM TvNotify tv WHERE tv.channel = :channel AND tv.notifyPattern = :pattern", TvNotify.class);
      query.setParameter("channel", channel);
      query.setParameter("pattern", pattern);
      return query.getSingleResult();
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public TvNotify getTvNotifyById(long id) {
    return entityManager.find(TvNotify.class, id);
  }

  @Override
  public int delTvNotifies(Channel channel) {
    Query query = entityManager.createQuery("DELETE FROM TvNotify tv WHERE tv.channel = :channel");
    query.setParameter("channel", channel);
    return query.executeUpdate();
  }

  @Override
  public void delTvNotify(TvNotify notify) {
    Query query = entityManager.createQuery("DELETE FROM TvNotify tv WHERE tv.notifyId = :notifyId");
    query.setParameter("notifyId", notify.getId());
    query.executeUpdate();
  }

}
