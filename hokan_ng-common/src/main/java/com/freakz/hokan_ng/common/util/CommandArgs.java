package com.freakz.hokan_ng.common.util;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 3:32 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class CommandArgs {

  private final String[] args;

  public CommandArgs(String line) {
    args = line.split(" ");
  }

  public int getArgCount() {
    return args.length;
  }

  public String getCmd() {
    return args[0];
  }

  public boolean hasArgs() {
    return args.length > 1;
  }

  public String getArg(int index) {
    if (index >= 0 && index < args.length) {
      return args[index];
    } else {
      return null;
    }
  }

  public String getArgs() {
    return joinArgs(1);
  }

  public String joinArgs(int fromArg) {
    boolean isFirst = true;
    StringBuilder sb = new StringBuilder();
    for (int i = fromArg; i < args.length; i++) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(" ");
      }
      sb.append(args[i]);
    }
    return sb.toString();
  }


}
