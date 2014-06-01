package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.SearchReplace;

import java.util.List;

/**
 * Created by pairio on 28.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public interface SearchReplaceService {

  SearchReplace addSearchReplace(String owner, String search, String replace);

  List<SearchReplace> findSearchReplaces(String search);

  SearchReplace getSearchReplace(long id);

  List<SearchReplace> getSearchReplaces();

  void deleteSearchReplaces();

  void deleteSearchReplace(SearchReplace replace);

}
