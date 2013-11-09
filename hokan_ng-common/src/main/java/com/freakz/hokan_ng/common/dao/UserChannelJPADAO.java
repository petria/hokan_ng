package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.entity.UserChannel;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: petria
 * Date: 11/9/13
 * Time: 4:05 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("UserChannel")
@Transactional
public class UserChannelJPADAO implements UserChannelDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public UserChannelJPADAO() {
  }

  @Override
  public UserChannel createUserChannel(User user, Channel channel) throws HokanException {
    try {
      UserChannel userChannel = new UserChannel(user, channel);
      entityManager.persist(userChannel);
      return userChannel;
    } catch (Exception e) {
      throw new HokanException(e);
    }
  }

  @Override
  public UserChannel getUserChannel(User user, Channel channel) throws HokanException {
    try {
      TypedQuery<UserChannel> query
          = entityManager.createQuery("SELECT userChannel FROM UserChannel userChannel WHERE userChannel.user = :user AND userChannel.channel = :channel", UserChannel.class);
      query.setParameter("user", user);
      query.setParameter("channel", channel);
      return query.getSingleResult();
    } catch (Exception e) {
      throw new HokanException(e);
    }
  }

  @Override
  public List<UserChannel> findUserChannels(User user) throws HokanException {
    try {
      TypedQuery<UserChannel> query
          = entityManager.createQuery("SELECT userChannel FROM UserChannel userChannel WHERE userChannel.user = :user", UserChannel.class);
      query.setParameter("user", user);
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanException(e);
    }
  }

}
