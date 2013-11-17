package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Network;
import com.freakz.hokan_ng.common.exception.HokanException;

import java.util.List;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 11:22 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface NetworkDAO {

  Network getNetwork(String name) throws HokanException;

  List<Network> getNetworks() throws HokanException;

  Network createNetwork(String name) throws HokanException;

  Network updateNetwork(Network network) throws HokanException;
}
