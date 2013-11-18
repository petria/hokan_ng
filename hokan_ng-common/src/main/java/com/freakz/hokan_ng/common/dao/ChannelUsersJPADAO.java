package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelUser;
import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
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
 * Date: 11/13/13
 * Time: 3:04 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("ChannelUsers")
@Transactional
@Slf4j
public class ChannelUsersJPADAO implements ChannelUsersDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public ChannelUser createChannelUser(Channel channel, User user) throws HokanDAOException {
    try {
      ChannelUser channelUser = new ChannelUser(channel, user);
      this.entityManager.persist(channelUser);
      return channelUser;
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public void removeChannelUser(Channel channel, User user) throws HokanDAOException {
    try {
      Query query = this.entityManager.createQuery("DELETE FROM ChannelUser cu WHERE cu.channel = :channel AND cu.user = :user");
      query.setParameter("channel", channel);
      query.setParameter("user", user);
      int res = query.executeUpdate();
      log.info("Removed {} ChannelUser", res);
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public void removeChannelUser(User user) throws HokanDAOException {
    try {
      Query query = this.entityManager.createQuery("DELETE FROM ChannelUser cu WHERE cu.user = :user");
      query.setParameter("user", user);
      int res = query.executeUpdate();
      log.info("Removed {} ChannelUser", res);
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public List<ChannelUser> findChannelUsers(Channel channel) throws HokanDAOException {
    try {
      TypedQuery<ChannelUser> query = this.entityManager.createQuery("SELECT cu FROM ChannelUser cu WHERE cu.channel = :channel", ChannelUser.class);
      query.setParameter("channel", channel);
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public void clearChannelUsers(Channel channel) throws HokanDAOException {
    try {
      Query query = this.entityManager.createQuery("DELETE FROM ChannelUser cu WHERE cu.channel = :channel");
      query.setParameter("channel", channel);
      int res = query.executeUpdate();
      log.info("Removed {} ChannelUser", res);
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public void clearChannelUsers() throws HokanDAOException {
    try {
      Query query = this.entityManager.createQuery("DELETE FROM ChannelUser cu");
      int res = query.executeUpdate();
      log.info("Removed {} ChannelUser", res);
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

}
