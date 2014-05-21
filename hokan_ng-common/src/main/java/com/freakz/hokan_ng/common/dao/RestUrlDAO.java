package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.RestUrl;
import com.freakz.hokan_ng.common.entity.RestUrlType;

import java.util.List;

/**
 * User: petria
 * Date: 1/24/14
 * Time: 12:09 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface RestUrlDAO {

  int deleteRestUrls(String instanceKey);

  List<RestUrl> getRestUrls(String instanceKey);

  List<RestUrl> getRestUrls(String instanceKey, RestUrlType restUrlType);

  List<RestUrl> getRestUrls(RestUrlType restUrlType);

  RestUrl addRestUrl(String instanceKey, RestUrlType restUrlType, String restUrl, String lineMatcherRegexp);

}
