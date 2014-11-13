package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.freakz.hokan_ng.common.util.FileUtil;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
@Slf4j
public class NimipaivaCmd extends Cmd {

  private static final String NIMIPAIVAT_TXT = "/Nimipäivät.txt";

  private List<String> nimiPvmList = new ArrayList<>();

  public NimipaivaCmd() {
    super();
    setHelp("Nimipäivät");

    UnflaggedOption flg = new UnflaggedOption(ARG_NIMI_OR_PVM)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String nimiOrPvm = results.getString(ARG_NIMI_OR_PVM);

    if (nimiPvmList == null) {
      FileUtil fileUtil = new FileUtil();
      StringBuilder contents = new StringBuilder();
      try {
        fileUtil.copyResourceToTmpFile(NIMIPAIVAT_TXT, contents);
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
