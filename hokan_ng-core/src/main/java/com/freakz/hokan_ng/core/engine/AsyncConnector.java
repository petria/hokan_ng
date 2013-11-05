package com.freakz.hokan_ng.core.engine;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.core.model.Connector;
import com.freakz.hokan_ng.core.model.EngineConnector;
import lombok.extern.slf4j.Slf4j;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Date: 11/1/13
 * Time: 12:56 PM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Slf4j
public class AsyncConnector implements Connector, CommandRunnable {

  @Autowired
  private CommandPool commandPool;

  private final String botNick;
  private final EngineConnector engineConnector;
  private final IrcServerConfig configuredServer;

  private boolean aborted = false;

  public AsyncConnector(String nick, EngineConnector engineConnector, IrcServerConfig configuredServer) {
    this.botNick = nick;
    this.engineConnector = engineConnector;
    this.configuredServer = configuredServer;
  }

  @Override
  public void abortConnect() {
    this.aborted = true;
  }

  @Override
  public void connect() {
    commandPool.startRunnable(this);
  }

  @Override
  public void handleRun(Object args) {
    int tryCount = 5; // TODO get from properties
    String server = this.configuredServer.getServer();
    int serverPort = this.configuredServer.getPort();
    String serverPassword = this.configuredServer.getServerPassword();

    boolean connectOk = false;
    int connectAttemps = 0;
    HokanCore engine = null;
    while (tryCount > 0 && aborted == false) {
      connectAttemps++;
      try {
        engine = new HokanCore(this.botNick, this.configuredServer);

        if (serverPassword == null || serverPassword.length() == 0) {
          engine.connect(server, serverPort);
        } else {
          engine.connect(server, serverPort, serverPassword);
        }
        connectOk = true;

      } catch (NickAlreadyInUseException e) {
        engine.disconnect();
        this.engineConnector.engineConnectorNickAlreadyInUse(this, this.configuredServer, this.botNick);
        aborted = true;

      } catch (Exception e) {
        if (engine != null) {
          engine.disconnect();
        }
      }
      if (connectOk) {
        aborted = true;
        this.engineConnector.engineConnectorGotOnline(this, engine);
      } else {
        tryCount--;
        try {
          int sleep = 5000 + ((connectAttemps - 1) * 5000);
          log.info("[" + connectAttemps + "] Sleeping " + (sleep / 1000) + " seconds");
          Thread.sleep(sleep);
          log.info("Trying again: " + connectAttemps);
        } catch (InterruptedException e) { /* do nothing */}
      }
    } // while

  }

}
