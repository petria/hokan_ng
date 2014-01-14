package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Alias;

import java.util.List;

/**
 * User: petria
 * Date: 1/14/14
 * Time: 11:22 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface AliasDAO {

  Alias findAlias(String alias);

  List<Alias> findAliases();

  Alias removeAlias(String alias);

}
