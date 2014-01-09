package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.service.UserService;
import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/19/13
 * Time: 10:50 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class WhoCmd extends Cmd {

  @Autowired
  private UserService userService;

  public WhoCmd() {
    super();
    setHelp("Shows users logged in to the bot.");
    addToHelpGroup(HelpGroup.USERS, this);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    InternalRequest ir = (InternalRequest) request;
    List<User> users = userService.getLoggedInUsers();
    if (users.size() > 0) {
      String ret = String.format("%10s  %20s %10s  %-35s\n", "USER", "FROM", "COMMAND", "LASTSEEN");
      for (User user : users) {
//        ret += String.format("%10s  %20s %10s  %-35s\n", user.getNick(), user.getLastHost(), user.getLastCommand(), user.getLastSeen());
        ret += String.format("%10s  TODO!\n", user.getNick()); // TODO
      }
      response.addResponse(ret);
    } else {
      response.addResponse("No one logged in!");
    }
  }

}
