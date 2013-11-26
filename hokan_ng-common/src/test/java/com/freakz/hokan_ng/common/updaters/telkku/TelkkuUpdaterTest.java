package com.freakz.hokan_ng.common.updaters.telkku;

import com.freakz.hokan_ng.common.util.FileUtil;
import junit.framework.Assert;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 10:01 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public class TelkkuUpdaterTest {

  private TelkkuUpdater updater;

  //  @Before
  public void before() {
    this.updater = new TelkkuUpdater();
    this.updater.setFileUtil(new FileUtil());
  }

  //  @Test
  public void testTvGrab() throws Exception {
    String output = updater.runTvGrab();
    Assert.assertNotNull("Output file not null", output);
  }

}
