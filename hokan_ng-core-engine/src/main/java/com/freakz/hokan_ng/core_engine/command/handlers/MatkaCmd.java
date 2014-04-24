package com.freakz.hokan_ng.core_engine.command.handlers;


import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.util.HttpPostFetcher;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CITY_1;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_CITY_2;

/**
 * User: petria
 * Date: Apr 19, 2010
 * Time: 10:00:55 PM
 */
@Component
@Scope("prototype")
@Slf4j
public class MatkaCmd extends Cmd {

  public MatkaCmd() {
    super();
    setHelp("Distance between city1 <-> city2");

    UnflaggedOption flg = new UnflaggedOption(ARG_CITY_1)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

    flg = new UnflaggedOption(ARG_CITY_2)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    StringBuilder sb = new StringBuilder();
    String city1 = StringStuff.capitalize(results.getString(ARG_CITY_1));
    String city2 = StringStuff.capitalize(results.getString(ARG_CITY_2));

    String from = "MISTÄ=" + city1;
    String to = "MIHIN=" + city2;
    String speed = "NOPEUS=80";

    try {
      HttpPostFetcher post = context.getBean(HttpPostFetcher.class);

      post.fetch("http://alk.tiehallinto.fi/cgi-bin/pq9.cgi", "windows-1252", from, to, speed);

      String page = post.getHtmlBuffer();

      Pattern p0 = Pattern.compile(".*<li> Välimatka on (\\d+) km<li>.*");
      Matcher m0 = p0.matcher(page);
      String dist = null;
      if (m0.find()) {
        dist = m0.group(1);
      }
      sb.append(city1);
      sb.append(" <-> ");
      sb.append(city2);
      sb.append(" = ");
      sb.append(dist);

    } catch (IOException e1) {
      e1.printStackTrace();
      sb.append("Cmd failed: ");
      sb.append(e1);
    }

    response.addResponse(sb.toString());
  }

}
