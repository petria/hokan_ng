package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.Alias;

import java.util.List;

/**
 * User: petria
 * Date: 1/14/14
 * Time: 1:35 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface AliasService {

  Alias createAlias(String alias, String command);

  Alias findAlias(String alias);

  List<Alias> findAliases();

  int removeAlias(String alias);

  Alias updateAlias(Alias alias);

}
