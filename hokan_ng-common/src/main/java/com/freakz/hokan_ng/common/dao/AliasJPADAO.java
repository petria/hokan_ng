package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.Alias;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
  public Alias createAlias(String alias, String command) {
    Alias entity = new Alias();
    entity.setAlias(alias);
    entity.setCommand(command);
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Alias findAlias(String alias) throws HokanDAOException {
    TypedQuery<Alias> query = entityManager.createQuery(
        "SELECT a FROM Alias a WHERE a.alias = :alias", Alias.class);
    query.setParameter("alias", alias);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public List<Alias> findAliases() throws HokanDAOException {
    TypedQuery<Alias> query = entityManager.createQuery(
        "SELECT a FROM Alias a ORDER BY a.alias", Alias.class);
    try {
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public int removeAlias(String alias) throws HokanDAOException {
    Query query = this.entityManager.createQuery("DELETE FROM Alias a WHERE a.alias = :alias");
    query.setParameter("alias", alias);
    try {
      return query.executeUpdate();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public Alias updateAlias(Alias alias) throws HokanDAOException {
    try {
      return entityManager.merge(alias);
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

}
