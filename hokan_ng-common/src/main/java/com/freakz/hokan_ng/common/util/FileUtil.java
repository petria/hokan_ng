package com.freakz.hokan_ng.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 8:55 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class FileUtil {

  public FileUtil() {
  }

  public String extractPath(String filename) {
    int idx = filename.lastIndexOf("/");
    String dir = filename.substring(0, idx + 1);
    return dir;

  }

  public String getTmpDirectory() throws IOException {
    File tmpFile = File.createTempFile("foo", "bar");
    String path = tmpFile.getCanonicalPath();
//    log.info("path directory: {}", path);
    return extractPath(path);
  }

  public String copyResourceToFile(String resource, File target, StringBuilder contents) throws IOException {
    InputStream inputStream = this.getClass().getResourceAsStream(resource);
    if (inputStream == null) {
      log.error("Resource not found: {}", resource);
      return null;
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
    BufferedWriter bw = new BufferedWriter(new FileWriter(target));
    String line;
    while (true) {
      line = br.readLine();
      if (line == null) {
        break;
      }
      bw.write(line + "\n");
      if (contents != null) {
        contents.append(line);
        contents.append("\n");
      }
    }
    br.close();
    bw.flush();
    bw.close();

    String tmpResourcePath = target.getAbsolutePath();

    log.info("Copied resource {} to {}", resource, tmpResourcePath);
    return tmpResourcePath;
  }

  public String copyResourceToFile(String resource, File target) throws IOException {
    return copyResourceToFile(resource, target, null);
  }

  public String copyResourceToTmpFile(String resource) throws IOException {
    File tmpFile = File.createTempFile(resource, "");
    return copyResourceToFile(resource, tmpFile);
  }

  public boolean deleteTmpFile(String tmpFile) {
    File f = new File(tmpFile);
    if (f.exists()) {
      log.info("Deleting file: {}", tmpFile);
      return f.delete();
    }
    return false;
  }

}
