package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.service.GoogleTranslatorService;
import com.google.api.translate.Language;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_TEXT;

/**
 * Created by Petri Airio (petri.j.airio@gmail.com) on 29.4.2015.
 *
 */
@Component
@Scope("prototype")
@Slf4j
public class GoogleTranslateCmd extends Cmd {

  @Autowired
  private GoogleTranslatorService translatorService;

  public GoogleTranslateCmd() {
    super();
    setHelp("Translate using Google API");

    UnflaggedOption flg = new UnflaggedOption(ARG_TEXT)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);
  }

  @Override
  public String getMatchPattern() {
    return "!googletrans.*|!gtrans.*";
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String text = results.getString(ARG_TEXT);
    String translated = translatorService.getTranslation(Language.AUTO_DETECT, Language.FINNISH, text);
    response.addResponse("%s", translated);

  }
}
