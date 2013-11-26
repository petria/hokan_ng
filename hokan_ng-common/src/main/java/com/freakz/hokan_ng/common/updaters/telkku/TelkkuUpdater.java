package com.freakz.hokan_ng.common.updaters.telkku;

import com.freakz.hokan_ng.common.exception.HokanServiceException;
import com.freakz.hokan_ng.common.updaters.Updater;
import com.freakz.hokan_ng.common.util.CmdExecutor;
import com.freakz.hokan_ng.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: petria
 * Date: 11/22/13
 * Time: 12:43 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class TelkkuUpdater extends Updater {

  private static final String XMLTV_DTD_FILE = "xmltv.dtd";

  private static final String TVGRAB_CONF_RESOURCE = "/tv_grab_fi.conf";

  private static final String TVGRAB_BIN = "/usr/bin/tv_grab_fi";

  private static final String FETCH_FILE = "telkku_progs.xml";
  private static final String FETCH_CHARSET = "UTF-8";

  private FileUtil fileUtil;

  private List<TelkkuProgram> programList;

  public TelkkuUpdater() {

  }

  @Autowired
  public void setFileUtil(FileUtil fileUtil) {
    this.fileUtil = fileUtil;
  }

  public String runTvGrab() throws Exception {
    String tmpDir = fileUtil.getTmpDirectory();
    File dtdFile = new File(tmpDir + XMLTV_DTD_FILE);
    if (!dtdFile.exists()) {
      fileUtil.copyResourceToFile("/" + XMLTV_DTD_FILE, dtdFile);
    }
    String tmpConf = fileUtil.copyResourceToTmpFile(TVGRAB_CONF_RESOURCE);
    File outputFile = File.createTempFile(FETCH_FILE, "");
    String cmd = TVGRAB_BIN + " --config-file " + tmpConf + " --output " + outputFile.getAbsolutePath();
    log.info("Running: {}", cmd);
    CmdExecutor cmdExecutor = new CmdExecutor(cmd, FETCH_CHARSET);
    log.info("Run done!");

    return outputFile.getAbsolutePath();
  }

  @Override
  public void doUpdateData() throws Exception {
    String fileName = runTvGrab();
    log.info("Tv data file: {}", fileName);
    try {
      List<String> channelNames = new ArrayList<String>();
      this.programList = readXmlFile(fileName, channelNames);
      this.fileUtil.deleteTmpFile(fileName);
    } catch (Exception e) {
      throw new HokanServiceException("Update TV data failed", e);
    }
  }

  @Override
  public Object doGetData(String... args) {
    return this.programList;
  }

  public List<TelkkuProgram> readXmlFile(String fileName, List<String> channels)
      throws ParserConfigurationException, IOException, SAXException {

    File file = new File(fileName);
    if (!file.exists()) {
      log.error("File not found: {}", file);
      return new ArrayList<>();
    }

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(file);
    doc.getDocumentElement().normalize();

    NodeList nodeLst = doc.getElementsByTagName("channel");

    Map<String, String> idDisplayNameMap = new HashMap<String, String>();

    for (int s = 0; s < nodeLst.getLength(); s++) {

      Node fstNode = nodeLst.item(s);

      if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
        String id;
        String displayName;
        Element fstElmnt = (Element) fstNode;

        id = fstElmnt.getAttribute("id");

        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("display-name");
        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
        NodeList fstNm = fstNmElmnt.getChildNodes();
        displayName = fstNm.item(0).getNodeValue();

        idDisplayNameMap.put(id, displayName);
        channels.add(displayName);
      }
    }

    nodeLst = doc.getElementsByTagName("programme");

    List<TelkkuProgram> programs = new ArrayList<TelkkuProgram>();

    for (int s = 0; s < nodeLst.getLength(); s++) {

      Node fstNode = nodeLst.item(s);

      if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
        String startStr;
        String stopStr;
        String channel;
        String title;
        String desc = null;

        Element fstElmnt = (Element) fstNode;

        startStr = fstElmnt.getAttribute("start");
        stopStr = fstElmnt.getAttribute("stop");
        if (stopStr == null || stopStr.length() == 0) {
          continue;
        }
        channel = fstElmnt.getAttribute("channel");

        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("title");
        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
        NodeList fstNm = fstNmElmnt.getChildNodes();
        title = fstNm.item(0).getNodeValue();

        fstNmElmntLst = fstElmnt.getElementsByTagName("desc");
        fstNmElmnt = (Element) fstNmElmntLst.item(0);
        if (fstNmElmnt != null) {
          fstNm = fstNmElmnt.getChildNodes();
          desc = fstNm.item(0).getNodeValue();
        }

        // 2009 01 14 06 25 00 +0200
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss Z");
        try {
          Date startD, endD;

          startD = formatter.parse(startStr);
          endD = formatter.parse(stopStr);

          TelkkuProgram program =
              new TelkkuProgram(startD, endD, title, desc, idDisplayNameMap.get(channel));


          Calendar start = Calendar.getInstance();
          start.setTime(program.getStartTimeD());

          Calendar end = Calendar.getInstance();
          end.setTime(program.getEndTimeD());

          programs.add(program);

        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
    } // for

    return programs;
  }

}
