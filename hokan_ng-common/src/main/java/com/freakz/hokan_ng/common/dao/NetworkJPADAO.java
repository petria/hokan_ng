package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 11:23 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository(value = "Network")
@Transactional
public class NetworkJPADAO implements NetworkDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Network getNetwork(String name) throws HokanException {
    Network network = entityManager.find(Network.class, name);
    if (network == null) {
      throw new HokanException("Network not found: " + name);
    }
    return network;
  }

  @Override
  public Network createNetwork(String name) throws HokanException {
    try {
      Network network = new Network(name);
      entityManager.persist(network);
      return network;
    } catch (Exception e) {
      throw new HokanException(e.getMessage());
    }
  }

  @Override
  public Network updateNetwork(Network network) throws HokanException {
    try {
      return entityManager.merge(network);
    } catch (Exception e) {
      throw new HokanException(e.getMessage());
    }
  }

}
