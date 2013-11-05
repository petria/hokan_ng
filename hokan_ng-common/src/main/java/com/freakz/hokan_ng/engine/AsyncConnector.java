package com.freakz.hokan_ng.engine;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.model.Connector;

/**
 * Date: 11/1/13
 * Time: 12:56 PM
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public class AsyncConnector implements Connector {


  private final IrcServerConfig configureServer;
  private boolean aborted = false;

  public AsyncConnector(IrcServerConfig configuredServer) {
    this.configureServer = configuredServer;
  }

  @Override
  public void abortConnect() {
    this.aborted = true;
  }

  @Override
  public void connect() {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
