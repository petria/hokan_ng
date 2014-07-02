package com.freakz.hokan_ng.core_engine.functions;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pairio on 23.6.2014.
 *
 * @author Petri Airio (petri.airio@polar.com)
 */
@Component
public class FunctionHandlerImpl implements FunctionHandler {

  private List parseCmds(List parsed, String line, List nested) {
    String[] COMMANDS = {"Eval", "SendToIRC", "LastChannel", "Echo", "MD5", "Haxorize", "Birthday", "TimeTo"};

    String[] cmds = new String[COMMANDS.length];

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < COMMANDS.length; i++) {
      StringBuilder sb2 = new StringBuilder();
      sb2.append("@");
      sb2.append(COMMANDS[i]);
      sb2.append("(");
      cmds[i] = sb2.toString();

      if (i != 0) {
        sb.append("|");
      }
      sb.append("(@");
      sb.append(COMMANDS[i]);
      sb.append("\\((.*)\\))");
    }
    String pattern = sb.toString();
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(line);
    m.find();
    int c = m.groupCount();
    for (int i = 1; i < m.groupCount(); i++) {
      String group = m.group(i);
      System.out.println(i + " group: " + group);

    }

    for (int i = 0; i < cmds.length; i++) {
      parsed = parseCmd(cmds[i], parsed, line, nested);
    }
    return parsed;
  }

  private String handleLine(String evalStr) {
    List<String> list = new ArrayList<>();
    List<String> nested = new ArrayList<>();
    list = parseCmds(list, evalStr, nested);

    StringBuffer sb = new StringBuffer(evalStr);
    Iterator iter = list.iterator();
    while (iter.hasNext()) {
      evalStr = (String) iter.next();
      //String r =
//      doEval(sb, evalStr, 0, null);
    }
    return sb.toString();

  }


  private List parseCmd(String cmd, List<String> parsed, String line, List<String> nested) {
    int alku = line.indexOf(cmd);
    int loppu = 0;
    int auki = 0;
    int kiinni = 0;
    boolean eka = true;
    if (alku != -1) {
      for (int x = alku; x < line.length(); x++) {
        if (line.charAt(x) == '(') {
          auki++;
          eka = false;
        }
        if (line.charAt(x) == ')') {
          kiinni++;
        }
        if (auki == kiinni && !eka) {
          loppu = x + 1;
          x = line.length();
        }
      }
      String parsedStr = line.substring(alku, loppu);
      int idx1 = parsedStr.indexOf("(");
      int idx2 = parsedStr.indexOf(")");
      String args = parsedStr.substring(idx1 + 1, idx2 + 1);
      nested.add(args);
      parseCmd(cmd, parsed, line.substring(loppu), nested);
      parsed.add(parsedStr);

    }
    return parsed;
  }


  @Override
  public String handleFunctions(String inputLine) {
    return handleLine(inputLine);
  }

  public boolean hasFunction(String line) {
//    return line.matches("@\\p{Upper}\\p{Lower}+\\(.*?\\)|@\\p{Upper}\\p{Lower}+\\(\\)");
    return line.matches(".*@\\p{Upper}\\p{Lower}+\\(.*?\\).*");
  }

  public List<HokanFunction> parseFunctions(String line) {
    List<HokanFunction> functions = new ArrayList<>();
    boolean loop = true;
    while (loop) {

      HokanFunction hokanFunction = null;
      if (line.contains("@")) {
        int start = line.indexOf("@");
        int parenOpen = line.indexOf("(", start) + 1;
        int parenClose = -1;

        int opens = 0;
        for (int i = parenOpen; i < line.length(); i++) {
          char ch = line.charAt(i);
          if (ch == '(') {
            opens++;
            continue;
          }
          if (ch == ')') {
            if (opens == 0) {
              parenClose = i;
              break;
            } else {
              opens--;
            }
          }
        }

        if (parenOpen != -1 && parenClose != -1) {
          hokanFunction = new HokanFunction();
          hokanFunction.functionString = line.substring(start, parenClose + 1);
          hokanFunction.functionArgs = line.substring(parenOpen, parenClose);

          hokanFunction.posX = start;
          hokanFunction.posY = parenClose + 1;
          functions.add(hokanFunction);
          line = line.substring(parenClose + 1);
        } else {
          loop = false;
        }

      } else {
        loop = false;
      }
    }
    return functions;
  }


}
