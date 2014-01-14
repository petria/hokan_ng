package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Alias;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * User: petria
 * Date: 1/14/14
 * Time: 11:24 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("Alias")
@Transactional
public class AliasJPADAO implements AliasDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Alias findAlias(String alias) {
    return null;
  }

  @Override
  public List<Alias> findAliases() {
    return null;
  }

  @Override
  public Alias removeAlias(String alias) {
    return null;
  }
}
