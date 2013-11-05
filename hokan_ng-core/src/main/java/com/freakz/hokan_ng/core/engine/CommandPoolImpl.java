package com.freakz.hokan_ng.core.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: petria
 * Date: 11/5/13
 * Time: 12:04 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
public class CommandPoolImpl implements CommandPool {

  private static long pidCounter = 1;

  private ExecutorService executor = Executors.newCachedThreadPool();
  private List<CommandRunner> activeRunners = new ArrayList<>();

  public CommandPoolImpl() {
  }

  @Override
  public void startRunnable(CommandRunnable runnable) {
    startRunnable(runnable, null);
  }

  @Override
  public void startRunnable(CommandRunnable runnable, Object args) {
    CommandRunner runner = new CommandRunner(pidCounter, runnable, this, args);
    activeRunners.add(runner);
    this.executor.execute(runner);
    pidCounter++;
  }

  @Override
  public void runnerFinished(CommandRunner runner) {
    this.activeRunners.remove(runner);
  }

  @Override
  public List<CommandRunner> getActiveRunners() {
    return this.activeRunners;
  }
}
