package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Alias;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.service.AliasService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_ALIAS;

/**
 * User: petria
 * Date: 1/14/14
 * Time: 4:11 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class UnAliasCmd extends Cmd {

  @Autowired
  private AliasService aliasService;

  public UnAliasCmd() {
    super();
    setHelp("Removes an alias.");
    addToHelpGroup(HelpGroup.ALIAS, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_ALIAS)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    Alias a = aliasService.findAlias(results.getString(ARG_ALIAS));
    if (a != null) {
      int r = aliasService.removeAlias(a.getAlias());
      if (r > 0) {
        response.addResponse("Alias removed, %3d: %s = %s", a.getAliasId(), a.getAlias(), a.getCommand());
      } else {
        response.addResponse("Alias not removed: %s", results.getString(ARG_ALIAS));
      }
    } else {
      response.addResponse("Unkown alias: %s", results.getString(ARG_ALIAS));
    }

  }

}
