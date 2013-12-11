package com.freakz.hokan_ng.common.engine;

import java.util.List;

/**
 * User: petria
 * Date: 11/5/13
 * Time: 12:13 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public interface CommandPool {

  void startRunnable(CommandRunnable runnable, Object args);

  void startRunnable(CommandRunnable runnable);

  void startSyncRunnable(CommandRunnable runnable, Object... args);

  void runnerFinished(CommandRunner runner);

  List<CommandRunner> getActiveRunners();

  void addCommandHistory(CommandHistory history);

  List<CommandHistory> getCommandHistory();

}
