package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.EngineEndpoint;

import java.util.List;

/**
 * User: petria
 * Date: 1/24/14
 * Time: 12:09 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface EngineEndpointDAO {

  int deleteEngineEndpoints(String instanceKey);

  List<EngineEndpoint> getEngineEndpoints(String instanceKey);

  EngineEndpoint addEngineEndpoint(String instanceKey, String endpointAddress, String lineMatcherRegexp);

}
