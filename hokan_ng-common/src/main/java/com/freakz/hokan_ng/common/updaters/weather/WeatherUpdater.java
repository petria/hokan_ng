package com.freakz.hokan_ng.common.updaters.weather;

import com.freakz.hokan_ng.common.updaters.Updater;
import com.freakz.hokan_ng.common.util.HttpPageFetcher;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 3:05 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
public class WeatherUpdater extends Updater {

  private final static String URL =
      "http://www.tiehallinto.fi/alk/tiesaa/tiesaa_maak_";

  private List<WeatherData> data;

  public WeatherUpdater() {
  }

  @Override
  public Object doGetData() {
    return data;
  }

  @Override
  public void doUpdateData() {
    List<WeatherData> data = new ArrayList<WeatherData>();
    for (int xx = 1; xx <= 19; xx++) {
      String url = URL + xx + ".html";
      HttpPageFetcher pf;
      try {
        pf = new HttpPageFetcher(url, "8859_1");
      } catch (Exception e) {
        log.info("INVALID URL: {}", url);
        continue;
      }
      while (pf.hasMoreLines()) {
        String l = pf.nextLine();
        if (l.matches("^Tie \\d+.*$")) {
          String[] d = new String[7];
          StringTokenizer st = new StringTokenizer(l, " ");

          try {

            d[0] = st.nextToken() + " " + st.nextToken(); // Tie XX
            d[1] = "";
            while (st.hasMoreTokens()) {
              d[1] += st.nextToken() + " ";
            }
            d[1] = d[1].trim(); // Paikka
            d[2] = pf.nextLine().trim(); // kello
            d[3] = pf.nextLine().trim(); // tie lämpo
            d[4] = pf.nextLine().trim(); // ilma lämpö
            d[5] = pf.nextLine().trim(); // sää kuvaus
            d[6] = pf.nextLine().trim(); // tie kuvaus

            WeatherData wd = new WeatherData(d);
            if (wd.OK) {
              wd.setDate(StringStuff.formatTime(new Date(), StringStuff.STRING_STUFF_DF_DDMMHHMM));
              data.add(wd);
            }
          } catch (Exception e) {
            log.info(e + "ERROR LINE: " + l);
          }

        }
      }
    } // for
    this.data = sortData(data, true);
  }

  @SuppressWarnings("unchecked")
  public List<WeatherData> sortData(List<WeatherData> v, boolean reverse) {

    WeatherComparator comparator = new WeatherComparator(reverse);

    Collections.sort(v, comparator);

    WeatherData wd = null;
    for (int xx = 0; xx < v.size(); xx++) {
      wd = v.get(xx);
      wd.setPos(xx + 1);
    }
    if (wd != null) {
      wd.setCount(v.size());
    }
    return v;
    //    return sorted;
  }

  public static class WeatherComparator implements Comparator {
    private static boolean reverse;

    public WeatherComparator(boolean reverse) {
      reverse = reverse;
    }

    public static void setReverse(boolean b) {
      reverse = b;
    }

    public int compare(Object o1, Object o2) {
      WeatherData w1 = (WeatherData) o1;
      WeatherData w2 = (WeatherData) o2;
      int comp = w1.compareTo(w2);
      if (reverse) {
        comp = comp * -1;
      }
      return comp;
    }
  }

}
