package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.TranslateService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 9:03 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class TranslateCmd extends Cmd {

  @Autowired
  private TranslateService translateService;

  private static final String ARG_WORD = "Word";

  public TranslateCmd() {
    super();
    setHelp("FIN-ENG-FIN dictionary..");

    UnflaggedOption flg = new UnflaggedOption(ARG_WORD)
        .setRequired(true)
        .setGreedy(true);
    registerParameter(flg);
  }

  @Override
  public String getMatchPattern() {
    return "!trans.*";
  }

  public List<String> getTranslation(String word) {
    //

    List<String> engFi = translateService.translateEngFi(word);
    List<String> fiEng = translateService.translateFiEng(word);

    List<String> v = new ArrayList<>();
    v.addAll(engFi);
    v.addAll(fiEng);
    return v;
  }

  public String translateWord(String word) {

    List<String> v = getTranslation(word);
    StringBuilder sb = new StringBuilder();
    if (v != null) {
      for (int xx = 0; xx < v.size(); xx++) {
        if (xx != 0) {
          sb.append(", ");
        }
        String str = v.get(xx);
        sb.append(str);
      }
    }
    return sb.toString();
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    response.addResponse(translateWord(results.getString(ARG_WORD)));
  }

}
