package com.freakz.hokan_ng.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 8:57 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Slf4j
public class FileUtilTest {

  private FileUtil fileUtil;

  @Before
  public void before() {
    this.fileUtil = new FileUtil();
  }

  @Test
  public void testGetTmpDirectory() throws IOException {
    String directory = fileUtil.getTmpDirectory();
    log.info("tmp directory: {}", directory);
    Assert.assertNotNull("Directory not null", directory);
    Assert.assertTrue("Ends with /", directory.endsWith("/"));
  }

  @Test
  public void testCopyResourceToTmpFile() throws IOException {
    String tmpFile = fileUtil.copyResourceToTmpFile("/uptime.sh");
    log.info("tmp file: {}", tmpFile);
    Assert.assertNotNull("tmp file not null", tmpFile);

    File f1 = new File(tmpFile);
    boolean exists1 = f1.exists();
    Assert.assertTrue("Tmp file exists", exists1);

    fileUtil.deleteTmpFile(tmpFile);

    File f2 = new File(tmpFile);
    boolean exists2 = f2.exists();
    Assert.assertFalse("Tmp file is deleted", exists2);

  }


}
