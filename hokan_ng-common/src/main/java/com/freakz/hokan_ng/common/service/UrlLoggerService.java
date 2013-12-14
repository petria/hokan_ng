package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.engine.HokanCore;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 9:45 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface UrlLoggerService {

  void catchUrls(IrcMessageEvent iEvent, HokanCore core);

}
