package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.entity.RestUrl;
import com.freakz.hokan_ng.common.entity.RestUrlType;

import java.util.List;

/**
 * Created by pairio on 20.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public interface RestUrlService {

  List<RestUrl> getRestUrls(String instanceKey, RestUrlType restUrlType);

}
