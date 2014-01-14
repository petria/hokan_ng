package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.Alias;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.AliasService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_ALIAS;
import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_COMMAND;

/**
 * User: petria
 * Date: 1/14/14
 * Time: 3:41 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class AliasCmd extends Cmd {

  @Autowired
  private AliasService aliasService;

  public AliasCmd() {
    super();
    setHelp("Lists and sets aliases.");
    addToHelpGroup(HelpGroup.ALIAS, this);

    UnflaggedOption flg = new UnflaggedOption(ARG_ALIAS)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);

    flg = new UnflaggedOption(ARG_COMMAND)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;

    if (null == results.getString(ARG_ALIAS)) {
      List<Alias> aliases = aliasService.findAliases();
      if (aliases.size() > 0) {
        for (Alias alias : aliases) {
          response.addResponse("%3d: %s = %s\n", alias.getAliasId(), alias.getAlias(), alias.getCommand());
        }
      } else {
        response.addResponse("No aliases defined!");
      }
    } else {
      String alias = results.getString(ARG_ALIAS);
      String command = results.getString(ARG_COMMAND);
      Alias a = aliasService.findAlias(alias);
      if (a == null) {
        a = new Alias();
        a.setAlias(alias);
      }
      a.setCommand(command);
      a = aliasService.updateAlias(a);
      response.addResponse("Alias set, %3d: %s = %s", a.getAliasId(), a.getAlias(), a.getCommand());
    }
  }

}
