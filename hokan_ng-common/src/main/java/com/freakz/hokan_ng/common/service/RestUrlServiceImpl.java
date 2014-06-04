package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.RestUrlDAO;
import com.freakz.hokan_ng.common.entity.RestUrl;
import com.freakz.hokan_ng.common.entity.RestUrlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pairio on 20.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Service
@Slf4j
public class RestUrlServiceImpl implements RestUrlService {

  @Autowired
  private RestUrlDAO restUrlDAO;

  @Override
  public List<RestUrl> getRestUrls(String instanceKey, RestUrlType restUrlType) {
    return restUrlDAO.getRestUrls(instanceKey, restUrlType);
  }

}
