package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.NetworkDAO;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import com.freakz.hokan_ng.common.exception.HokanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 11:44 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class NetworkServiceImpl implements NetworkService {

  @Autowired
  private NetworkDAO networkDAO;

  public NetworkServiceImpl() {
  }

  @Override
  public Network getNetwork(String name) {
    try {
      return networkDAO.getNetwork(name);
    } catch (HokanDAOException e) {
      log.error("Network error", e);
    }
    return null;
  }

  @Override
  public List<Network> getNetworks() throws HokanException {
    return networkDAO.getNetworks();
  }

  @Override
  public Network createNetwork(String name) throws HokanException {
    return networkDAO.createNetwork(name);
  }

  @Override
  public Network updateNetwork(Network network) {
    try {
      return networkDAO.updateNetwork(network);
    } catch (HokanDAOException e) {
      log.error("Network update error", e);
    }
    return null;
  }

}
