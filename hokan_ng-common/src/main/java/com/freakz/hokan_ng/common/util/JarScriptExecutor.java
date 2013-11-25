package com.freakz.hokan_ng.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: petria
 * Date: 11/25/13
 * Time: 4:27 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
public class JarScriptExecutor {

  private static final String SHELL = "/bin/sh";
  private final String scriptName;
  private final String charset;

  public JarScriptExecutor(String scriptName, String charset) {
    this.scriptName = scriptName;
    this.charset = charset;
  }

  public String[] executeJarScript(String... args) {
    InputStream inputStream = this.getClass().getResourceAsStream(scriptName);
    try {
      File tmpFile = File.createTempFile(scriptName, "");

      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));
      String line;
      while (true) {
        line = br.readLine();
        if (line == null) {
          break;
        }
        bw.write(line + "\n");
      }
      br.close();
      bw.flush();
      bw.close();

      String tmpScriptName = tmpFile.getAbsolutePath();
      String[] cmdArray = new String[]{SHELL, tmpScriptName};

      Process p;
      List<String> output = new ArrayList<>();


      p = Runtime.getRuntime().exec(cmdArray);
      br = new BufferedReader(new InputStreamReader(p.getInputStream(), this.charset));

      String l;
      do {
        l = br.readLine();
        if (l != null) {
          output.add(l);
        }

      } while (l != null);
      p.destroy();
      return output.toArray(new String[output.size()]);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new String[0];
  }

}
