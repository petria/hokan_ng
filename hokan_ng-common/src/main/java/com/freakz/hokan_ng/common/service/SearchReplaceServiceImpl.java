package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.SearchReplaceDAO;
import com.freakz.hokan_ng.common.entity.SearchReplace;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pairio on 28.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Service
@Slf4j
public class SearchReplaceServiceImpl implements SearchReplaceService {

  @Autowired
  private SearchReplaceDAO searchReplaceDAO;

  @Override
  public SearchReplace addSearchReplace(String owner, String search, String replace) {
    try {
      SearchReplace searchReplace = searchReplaceDAO.addSearchReplace(owner, search, replace);
      return searchReplace;
    } catch (HokanDAOException e) {
      log.warn("SearchReplace error", e);
    }
    return null;
  }

  @Override
  public List<SearchReplace> findSearchReplaces(String search) {
    try {
      return searchReplaceDAO.findSearchReplaces(search);
    } catch (HokanDAOException e) {
      log.warn("SearchReplace error", e);
    }
    return new ArrayList<>();
  }

  @Override
  public SearchReplace getSearchReplace(long id) {
    try {
      return searchReplaceDAO.getSearchReplace(id);
    } catch (HokanDAOException e) {
      log.warn("SearchReplace error", e);
    }
    return null;
  }

  @Override
  public List<SearchReplace> getSearchReplaces() {
    try {
      return searchReplaceDAO.getSearchReplaces();
    } catch (HokanDAOException e) {
      log.warn("SearchReplace error", e);
    }
    return new ArrayList<SearchReplace>();
  }

  @Override
  public void deleteSearchReplaces() {
    try {
      searchReplaceDAO.deleteSearchReplaces();
    } catch (HokanDAOException e) {
      log.warn("SearchReplace error", e);
    }
  }

  @Override
  public void deleteSearchReplace(SearchReplace replace) {
    try {
      searchReplaceDAO.deleteSearchReplace(replace);
    } catch (HokanDAOException e) {
      log.warn("SearchReplace error", e);
    }
  }

}
