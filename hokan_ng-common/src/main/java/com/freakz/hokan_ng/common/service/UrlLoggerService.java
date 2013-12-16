package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.engine.HokanCore;
import com.freakz.hokan_ng.common.entity.Channel;
import com.freakz.hokan_ng.common.entity.Url;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;

import java.util.List;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 9:45 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UrlLoggerService {

  void catchUrls(IrcMessageEvent iEvent, Channel ch, HokanCore core);

  List<Url> findUrls(String url, String... nick);

  Url findUrl(String url, String... nick);
}
