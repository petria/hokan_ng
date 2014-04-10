package com.freakz.hokan_ng.common.updaters.horo;

import com.freakz.hokan_ng.common.updaters.Updater;
import com.freakz.hokan_ng.common.util.HttpPageFetcher;
import com.freakz.hokan_ng.common.util.StringStuff;
import com.freakz.hokan_ng.common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * User: petria
 * Date: 11/21/13
 * Time: 1:32 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
//@Scope("prototype")
@Slf4j
public class HoroUpdater extends Updater {

  @Autowired
  ApplicationContext context;

  public final static String[] HORO_NAMES =
      {"Oinas", "Härkä", "Kaksoset", "Rapu", "Leijona", "Neitsyt",
          "Vaaka", "Skorpioni", "Jousimies", "Kauris",
          "Vesimies", "Kalat"};

  public final static String[] HORO_DATES =
      {
          "21.3. - 19.4.", "20.4. - 20.5.", "21.5. - 20.6.", "21.6. - 22.7.",
          "23.7. - 22.8.", "23.8. - 22.9.", "23.9. - 23.10.", "24.10. - 22.11.",
          "23.11. - 21.12.", "22.12. - 19.1.", "20.1. - 19.2.", "20.2. - 20.3."
      };

  private List<HoroHolder> horos;

  public HoroUpdater() {
  }

  @Override
  public Calendar calculateNextUpdate() {
    Calendar cal = TimeUtil.getCalendar();
    cal.add(Calendar.MINUTE, 60);
    return cal;
  }

  @Override
  public void doUpdateData() throws Exception {
    List<HoroHolder> horos = updateIL();
    if (horos != null) {
      this.horos = horos;
    }
  }

  @Override
  public Object doGetData(String... args) {
    return this.getHoro(args[0]);
  }


  public List<HoroHolder> updateIL() throws Exception {
    List<HoroHolder> horos = new ArrayList<HoroHolder>();
    String url = "http://www.iltalehti.fi/viihde/horoskooppi1_ho.shtml";
    HttpPageFetcher page = context.getBean(HttpPageFetcher.class);
    page.fetch(url, "ISO-8859-1");

    String horoLine = page.findLine("Oinas 21.*", false);

    horoLine = horoLine.replaceAll("Oinas 21\\.3\\. - 19\\.4\\.", "");
    horoLine = horoLine.replaceAll("Härkä 20\\.4\\. - 20\\.5\\.", "|");
    horoLine = horoLine.replaceAll("Kaksoset 21.5. - 20.6.", "|");
    horoLine = horoLine.replaceAll("Rapu 21.6. - 22.7.", "|");
    horoLine = horoLine.replaceAll("Leijona 23.7. - 22.8.", "|");
    horoLine = horoLine.replaceAll("Neitsyt 23.8. - 22.9.", "|");
    horoLine = horoLine.replaceAll("Vaaka 23.9. - 23.10.", "|");
    horoLine = horoLine.replaceAll("Skorpioni 24.10. - 22.11.", "|");
    horoLine = horoLine.replaceAll("Jousimies 23.11. - 21.12.", "|");
    horoLine = horoLine.replaceAll("Kauris 22.12. - 19.1.", "|");
    horoLine = horoLine.replaceAll("Vesimies 20.1. - 19.2.", "|");
    horoLine = horoLine.replaceAll("Kalat 20.2. - 20.3.", "|");

    String[] horosTxt = horoLine.split("\\|");
    int i = 0;
    for (String txt : horosTxt) {
      HoroHolder hh = new HoroHolder(i, txt);
      i++;
      horos.add(hh);
    }
    return horos;
  }

  private HoroHolder generateHolder(int horoIdx) {
    String horoTxt = "Mugalabuglala baubuaagugug tsimszalabimpero!";
    if (horos.size() != 0) {
      List<String> textList = new ArrayList<String>();
      for (HoroHolder hh : horos) {
        String txt = hh.getHoroscopeText();
        String[] split = txt.split("\\. ");
        for (String ss : split) {
          if (ss.matches("\\d.*")) {
            continue;
          }
          textList.add(ss);
        }
      }
      Collections.shuffle(textList);
      int rnd = 2 + (int) (Math.random() * 3);
      horoTxt = "";
      while (rnd > 0) {
        horoTxt += textList.get(0) + ". ";
        textList.remove(0);
        rnd--;
        if (textList.size() == 0) {
          break;
        }
      }
    }
    return new HoroHolder(horoIdx, horoTxt);
  }

  public HoroHolder getHoro(String horo) {

    int horoIdx = -1;
    int idx = 0;
    for (String horoName : HORO_NAMES) {
      if (StringStuff.match(horoName, ".*" + horo + ".*", true)) {
        horoIdx = idx;
        break;
      }
      idx++;
    }
    if (horoIdx != -1 && horos != null) {
      HoroHolder holder;
      try {
        holder = horos.get(horoIdx);
      } catch (IndexOutOfBoundsException ex) {
        holder = generateHolder(horoIdx);
      }
      return holder;
    }
    return null;
  }


}
