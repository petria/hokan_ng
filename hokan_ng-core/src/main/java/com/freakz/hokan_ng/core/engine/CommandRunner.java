package com.freakz.hokan_ng.core.engine;

/**
 * User: petria
 * Date: 11/5/13
 * Time: 12:09 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class CommandRunner implements Runnable {

  private long myPid;
  private CommandRunnable runnable;
  private CommandPoolImpl commandPool;
  private Object args;

  public CommandRunner(long myPid, CommandRunnable runnable, CommandPoolImpl commandPool, Object args) {
    this.myPid = myPid;
    this.runnable = runnable;
    this.commandPool = commandPool;
    this.args = args;
  }

  @Override
  public void run() {
    this.runnable.handleRun(args);
    this.commandPool.runnerFinished(this);
  }
}
