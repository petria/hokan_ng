package com.freakz.hokan_ng.common.engine;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.common.exception.HokanException;
import lombok.extern.slf4j.Slf4j;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 11/1/13
 * Time: 12:56 PM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Slf4j
@Component
@Scope("prototype")
public class AsyncConnector implements Connector, CommandRunnable {

  @Autowired
  private ApplicationContext context;

  private CommandPool commandPool;
  private String botNick;
  private EngineConnector engineConnector;
  private IrcServerConfig configuredServer;

  private boolean aborted = false;

  public AsyncConnector() {
  }

  @Autowired
  public void setCommandPool(CommandPool commandPool) {
    this.commandPool = commandPool;
  }


  @Override
  public void abortConnect() {
    this.aborted = true;
  }

  @Override
  public void connect(String nick, EngineConnector engineConnector, IrcServerConfig configuredServer) {
    this.botNick = nick;
    this.engineConnector = engineConnector;
    this.configuredServer = configuredServer;
    commandPool.startRunnable(this);
  }

  @Override
  public void handleRun(long myPid, Object args) {
    int tryCount = 5; // TODO get from properties
    String server = this.configuredServer.getServer();
    int serverPort = this.configuredServer.getPort();
    String serverPassword = this.configuredServer.getServerPassword();

    boolean connectOk = false;
    int connectAttemps = 0;
    HokanCore engine;
    while (tryCount > 0 && !aborted) {
      connectAttemps++;
      engine = context.getBean(HokanCore.class);
      try {
        engine.init(this.botNick, this.configuredServer);

        if (serverPassword == null || serverPassword.length() == 0) {
          engine.connect(server, serverPort);
        } else {
          engine.connect(server, serverPort, serverPassword);
        }
        connectOk = true;

      } catch (NickAlreadyInUseException e) {
        engine.disconnect();
//        this.engineConnector.engineConnectorNickAlreadyInUse(this, this.configuredServer, this.botNick);
//        aborted = true;
        this.botNick = String.format("_%s_", botNick);

      } catch (Exception e) {
        String message = e.getMessage();
        if (message.contains("Nickname too long")) {
          this.botNick = this.botNick.substring(1);
        }
        if (engine != null) {
          engine.disconnect();
        }
      }
      if (connectOk) {
        aborted = true;
        try {
          this.engineConnector.engineConnectorGotOnline(this, engine);
        } catch (HokanException e) {
          e.printStackTrace(); // TODO handle
        }
      } else {
        tryCount--;
        if (tryCount == 0) {
          this.engineConnector.engineConnectorTooManyConnectAttempts(this, this.configuredServer);
          aborted = true;
        } else {
          try {
            int sleep = 5000 + ((connectAttemps - 1) * 1000);
            log.info("[" + connectAttemps + "] Sleeping " + (sleep / 1000) + " seconds");
            Thread.sleep(sleep);
            log.info("Trying again: " + connectAttemps);
          } catch (InterruptedException e) { /* do nothing */}
        }
      }
    } // while

  }

  public String toString() {
    return String.format("%s: [%s %s:%d]",
        this.getClass().toString(), this.configuredServer.getNetwork(), this.configuredServer.getServer(), this.configuredServer.getPort());
  }

}
