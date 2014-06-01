package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.SearchReplace;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by pairio on 28.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Slf4j
@Repository("SearchReplace")
@Transactional
public class SearchReplaceJPADAO implements SearchReplaceDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public SearchReplace addSearchReplace(String owner, String search, String replace) throws HokanDAOException {
    try {
      SearchReplace searchReplace = new SearchReplace(owner, search, replace);
      return entityManager.merge(searchReplace);

    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public SearchReplace getSearchReplace(long id) throws HokanDAOException {
    try {
      return entityManager.find(SearchReplace.class, id);
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }

  }

  @Override
  public List<SearchReplace> getSearchReplaces() throws HokanDAOException {
    try {
      TypedQuery<SearchReplace> query
          = entityManager.createQuery("SELECT sr FROM SearchReplace sr", SearchReplace.class);
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public List<SearchReplace> findSearchReplaces(String search) throws HokanDAOException {
    try {
      TypedQuery<SearchReplace> query
          = entityManager.createQuery("SELECT sr FROM SearchReplace sr WHERE sr.theSearch = :theSearch", SearchReplace.class);
      query.setParameter("theSearch", search);
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }


  @Override
  public void deleteSearchReplace(SearchReplace replace) throws HokanDAOException {
    try {
      SearchReplace del = entityManager.find(SearchReplace.class, replace.getId());
      entityManager.remove(del);
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }


  @Override
  public void deleteSearchReplace(String search) throws HokanDAOException {
    try {
      Query query = entityManager.createQuery("DELETE FROM SearchReplace sr WHERE sr.theSearch = :search");
      query.setParameter("search", search);
      query.executeUpdate();
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }
  }

  @Override
  public void deleteSearchReplaces() throws HokanDAOException {
    try {
      Query query = entityManager.createQuery("DELETE FROM SearchReplace sr");
      int res = query.executeUpdate();
      log.info("Removed {} Search/Replaces", res);
    } catch (Exception e) {
      throw new HokanDAOException(e);
    }

  }
}
