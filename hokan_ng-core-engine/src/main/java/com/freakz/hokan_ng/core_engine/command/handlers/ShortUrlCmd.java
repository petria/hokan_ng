package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.UrlLoggerService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_URL;

/**
 * Created by pairio on 14.4.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class ShortUrlCmd extends Cmd {

  @Autowired
  private UrlLoggerService urlLoggerService;

  public ShortUrlCmd() {
    super();
    setHelp("Shortens URLs using http://www.shorturl.com/");

    UnflaggedOption flg = new UnflaggedOption(ARG_URL)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    setMasterUserOnly(true);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String longUrl = results.getString(ARG_URL);
    String shortUrl = urlLoggerService.createShortUrl(longUrl);

    if (shortUrl != null) {
      response.addResponse("Short URL created: %s", shortUrl);
    }
  }

}
