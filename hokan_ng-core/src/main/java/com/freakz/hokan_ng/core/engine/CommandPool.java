package com.freakz.hokan_ng.core.engine;

import java.util.List;

/**
 * User: petria
 * Date: 11/5/13
 * Time: 12:13 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface CommandPool {

  public void startRunnable(CommandRunnable runnable, Object args);

  public void startRunnable(CommandRunnable runnable);

  public void runnerFinished(CommandRunner runner);

  public List<CommandRunner> getActiveRunners();

}
