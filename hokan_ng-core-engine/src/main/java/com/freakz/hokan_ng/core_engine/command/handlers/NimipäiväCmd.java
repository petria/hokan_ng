package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.freakz.hokan_ng.common.util.FileUtil;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_NIMI_OR_PVM;

/**
 * User: petria
 * Date: 1/13/14
 * Time: 12:36 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class NimipäiväCmd extends Cmd {

  private static final String NIMIPÄIVÄT_TXT = "/Nimipäivät.txt";

  private List<String> nimiPvmList = new ArrayList<>();

  public NimipäiväCmd() {
    super();
    setHelp("Nimipäivät");

    UnflaggedOption flg = new UnflaggedOption(ARG_NIMI_OR_PVM)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String nimiOrPvm = results.getString(ARG_NIMI_OR_PVM);

    if (nimiPvmList == null) {
      FileUtil fileUtil = new FileUtil();
      StringBuilder contents = new StringBuilder();
      try {
        fileUtil.copyResourceToTmpFile(NIMIPÄIVÄT_TXT, contents);
        this.nimiPvmList = Arrays.asList(contents.toString().split("\n"));
      } catch (IOException e) {
        throw new HokanException("Nimipäivät.txt", e);
      }
    }
    for (String nimiPvm : nimiPvmList) {
      if (nimiPvm.toLowerCase().contains(nimiOrPvm.toLowerCase())) {
        response.addResponse("%s\n", nimiPvm);
      }
    }
  }

}
