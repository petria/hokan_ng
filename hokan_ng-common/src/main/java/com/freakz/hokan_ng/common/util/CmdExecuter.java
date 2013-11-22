package com.freakz.hokan_ng.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CmdExecuter {

  private List<String> output;

  public CmdExecuter(String cmd, String inputEncoding) {

    Process p;
//    OutputStreamWriter out;
    BufferedReader br;
    output = new ArrayList<String>();

    try {

      p = Runtime.getRuntime().exec(cmd);
//      out = new OutputStreamWriter(p.getOutputStream());
      br = new BufferedReader(new InputStreamReader(p.getInputStream(),
          inputEncoding));

      String l;
      do {
        l = br.readLine();
        if (l != null) {
          output.add(l);
        }

      } while (l != null);
      p.destroy();

    } catch (Exception e) {
      log.error("Exception", e);
      output.add("ERROR");
    }
  }

//~ Methods ................................................................

  public String[] getOutput() {
    return output.toArray(new String[output.size()]);
  }


}
