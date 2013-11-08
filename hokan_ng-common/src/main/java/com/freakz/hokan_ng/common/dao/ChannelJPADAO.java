package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.ChannelState;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 2:13 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("Channel")
public class ChannelJPADAO implements ChannelDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Channel> findChannels(Network network, ChannelState state) throws HokanException {
    TypedQuery<Channel> query;
    if (state == ChannelState.ALL) {
      query = entityManager.createQuery(
          "SELECT ch FROM Channel ch WHERE ch.network = :network", Channel.class);
    } else {
      query = entityManager.createQuery(
          "SELECT ch FROM Channel ch WHERE ch.network = :network AND ch.channelState = :state", Channel.class
      );
      query.setParameter("state", state);
    }
    query.setParameter("network", network);
    try {
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanException(e.getMessage());
    }
  }

  @Override
  public Channel findChannelByName(Network network, String name) throws HokanException {
    TypedQuery<Channel> query = entityManager.createQuery(
        "SELECT ch FROM Channel ch WHERE ch.network = :network AND ch.channelName= :name", Channel.class
    );
    query.setParameter("network", network);
    query.setParameter("name", name);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      throw new HokanException(e.getMessage());
    }
  }

  @Override
  public Channel createChannel(Network network, String name) throws HokanException {
    Channel channel = new Channel(network, name);
    try {
      entityManager.persist(channel);
    } catch (Exception e) {
      throw new HokanException(e.getMessage());
    }
    return channel;
  }

  @Override
  public Channel updateChannel(Channel channel) throws HokanException {
    try {
      return entityManager.merge(channel);
    } catch (Exception e) {
      throw new HokanException(e.getMessage());
    }
  }

}
