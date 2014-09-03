package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Url;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.service.UrlLoggerService;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_NICK;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_URL_PATTERN;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 12:14 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class FindUrlCmd extends Cmd {

  @Autowired
  private UrlLoggerService urlLoggerService;

  public FindUrlCmd() {
    super();
    setHelp("Finds matching urls from URL database.");
    addToHelpGroup(HelpGroup.URLS, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_URL_PATTERN)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    flg = new UnflaggedOption(ARG_NICK)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    String urlPattern = results.getString(ARG_URL_PATTERN);
    String nick = results.getString(ARG_NICK);
    List<Url> urlList;
    if (nick == null) {
      urlList = urlLoggerService.findUrls(urlPattern);
    } else {
      urlList = urlLoggerService.findUrls(urlPattern, nick);
    }

    int shown = 0;
    String ret = null;

    for (Url row : urlList) {

      if (ret == null) {
        ret = "";
      }

      if (shown > 0) {
        ret += " ";
      }

      shown++;

      if (shown == 5) {
        break;
      }

      ret += shown + ") " + row.getSender() + ": ";
      ret += row.getUrl();
      if (row.getUrlTitle() != null) {
        ret += " \"t: " + row.getUrlTitle() + "\"";
      }
      ret += " [" + StringStuff.formatNiceDate(row.getCreated(), false) + "]";
      response.addResponse(ret);
    }

  }

}
