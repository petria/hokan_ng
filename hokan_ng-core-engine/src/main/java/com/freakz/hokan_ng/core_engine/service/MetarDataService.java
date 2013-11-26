package com.freakz.hokan_ng.core_engine.service;

import java.util.List;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 1:05 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface MetarDataService {

  List<MetarData> getMetarData(String station);

}
