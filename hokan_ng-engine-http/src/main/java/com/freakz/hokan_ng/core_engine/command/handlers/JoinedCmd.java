package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;

/**
 * User: petria
 * Date: 03-Mar-2009
 * Time: 09:08:27
 *
 * @author Petri Airio (petri.j.airio@gmail.com) *
 */
public class JoinedCmd extends Cmd {

  public JoinedCmd() {
    super();
  }


  public String getMatchPattern() {
    return "!joined";
  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {

//    HokanCore[] engines = NewConnectionManager.getInstance().getPircBotEngines();
    /*    StringBuilder sb = new StringBuilder();


    int count = 0;
    for (PircBotEngine engine : engines) {
      sb.append("[" + count + "]");
      count++;
      sb.append("[" + engine.getIrcServerConfig().getServer() + "]\n");

      String[] chans = engine.getChannels();
      for (String chan : chans) {
        sb.append("  " + chan + "\n");
        User[] users = engine.getUsers(chan);
        sb.append("  ");
        int c = 0;
        for (User user : users) {
          sb.append(user.toString() + "  ");
          c++;
          if (c == 10) {
            sb.append("\n  ");
          }
        }
        sb.append("\n");
      }

    }*/
  }
}
