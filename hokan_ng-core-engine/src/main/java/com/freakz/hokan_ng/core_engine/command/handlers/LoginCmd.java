package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.AccessControlService;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PASSWORD;

/**
 * User: petria
 * Date: 12/19/13
 * Time: 9:21 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
public class LoginCmd extends Cmd {

  @Autowired
  private AccessControlService accessControlService;

  public LoginCmd() {
    super();
    setHelp("Login in to the Bot.");
    addToHelpGroup(HelpGroup.USERS, this);
    setPrivateOnly(true);

    UnflaggedOption flg = new UnflaggedOption(ARG_PASSWORD)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    User loggedIn = accessControlService.login(ir, results.getString(ARG_PASSWORD));
    if (loggedIn != null) {
      response.addResponse("Login ok!");
      return;
    }
    response.addResponse("Login failed!");
  }

}
