package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.NetworkDAO;
import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;
import org.springframework.stereotype.Service;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 11:44 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
public class NetworkServiceImpl implements NetworkService {

  private NetworkDAO networkDAO;

  public NetworkServiceImpl() {
  }

  @Override
  public Network getNetwork(String name) throws HokanException {
    return networkDAO.getNetwork(name);
  }

  @Override
  public Network createNetwork(String name) throws HokanException {
    return networkDAO.createNetwork(name);
  }

  @Override
  public Network updateNetwork(Network network) throws HokanException {
    return networkDAO.updateNetwork(network);
  }

}
