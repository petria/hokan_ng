package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.TranslateService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_WORD;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 9:14 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class TranzslateCmd extends Cmd {

  @Autowired
  private TranslateService translateService;


  public TranzslateCmd() {
    super();
    setHelp("FIN-ENG-FIN dictionary..");

    UnflaggedOption flg = new UnflaggedOption(ARG_WORD)
        .setRequired(true)
        .setGreedy(true);
    registerParameter(flg);

  }

  public List<String> getTranslation(String word) {

    List<String> engFi = translateService.translateEngFi(word);
    List<String> fiEng = translateService.translateFiEng(word);

    List<String> v = new ArrayList<>();
    v.addAll(engFi);
    v.addAll(fiEng);
    return v;
  }

  public String tranzlateWords(String[] words) {
    StringBuilder sb = new StringBuilder();

    for (int xx = 0; xx < words.length; xx++) {
      if (xx != 0) {
        sb.append(" ");
      }
      String word = words[xx].replaceAll("\"\\,\\.\\!\\?\\:\\;\\-", "");
      List<String> v = getTranslation(word);
      if (v == null || v.size() == 0) {
        sb.append(words[xx]);
      } else {
        int rnd = (int) (Math.random() * v.size());
        sb.append(v.get(rnd));
      }
    }
    return sb.toString();
  }

  @Override
  public String getMatchPattern() {
    return "!tranz.*";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    response.addResponse(tranzlateWords(results.getStringArray(ARG_WORD)));
  }

}
