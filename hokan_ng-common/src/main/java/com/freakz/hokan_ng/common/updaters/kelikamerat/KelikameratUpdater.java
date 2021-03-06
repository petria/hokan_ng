package com.freakz.hokan_ng.common.updaters.kelikamerat;

import com.freakz.hokan_ng.common.updaters.KelikameratUrl;
import com.freakz.hokan_ng.common.updaters.KelikameratWeatherData;
import com.freakz.hokan_ng.common.updaters.Updater;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by Petri Airio on 22.6.2015.
 *
 */
@Component
//@Scope("prototype")
@Slf4j
public class KelikameratUpdater extends Updater {

  private static final String BASE_ULR = "http://www.kelikamerat.info";

  private static final String[] KELIKAMERAT_URLS =
      {
          "http://www.kelikamerat.info/kelikamerat/Etel%C3%A4-Karjala",
          "http://www.kelikamerat.info/kelikamerat/Etel%C3%A4-Pohjanmaa",
          "http://www.kelikamerat.info/kelikamerat/Etel%C3%A4-Savo",
          "http://www.kelikamerat.info/kelikamerat/Kainuu",
          "http://www.kelikamerat.info/kelikamerat/Kanta-H%C3%A4me",
          "http://www.kelikamerat.info/kelikamerat/Keski-Pohjanmaa",
          "http://www.kelikamerat.info/kelikamerat/Keski-Suomi",
          "http://www.kelikamerat.info/kelikamerat/Kymenlaakso",
          "http://www.kelikamerat.info/kelikamerat/Lappi",
          "http://www.kelikamerat.info/kelikamerat/P%C3%A4ij%C3%A4t-H%C3%A4me",
          "http://www.kelikamerat.info/kelikamerat/Pirkanmaa",
          "http://www.kelikamerat.info/kelikamerat/Pohjanmaa",
          "http://www.kelikamerat.info/kelikamerat/Pohjois-Karjala",
          "http://www.kelikamerat.info/kelikamerat/Pohjois-Pohjanmaa",
          "http://www.kelikamerat.info/kelikamerat/Pohjois-Savo",
          "http://www.kelikamerat.info/kelikamerat/Satakunta",
          "http://www.kelikamerat.info/kelikamerat/Uusimaa",
          "http://www.kelikamerat.info/kelikamerat/Varsinais-Suomi"
      };

  private List<KelikameratUrl> stationUrls;
  private List<KelikameratWeatherData> weatherDataList;

  public void updateStations() throws IOException {
    List<KelikameratUrl> stations = new ArrayList<>();
    for (String url : KELIKAMERAT_URLS) {
      Document doc = Jsoup.connect(url).get();
      Elements elements = doc.getElementsByClass("road-camera");
      for (int xx = 0 ; xx < elements.size(); xx++) {
        Element div = elements.get(xx);
        Element href =     div.child(0);
        String hrefUrl = BASE_ULR + href.attributes().get("href");
        KelikameratUrl kelikameratUrl = new KelikameratUrl(url, hrefUrl);
        stations.add(kelikameratUrl);
      }
    }
    this.stationUrls = stations;
  }

  private Float parseFloat(String str) {
    String f = str.split(" ")[0];
    if (!f.equals("-") && f.length() > 0) {
      return Float.parseFloat(f);
    } else {
      return null;
    }
  }

  public KelikameratWeatherData updateKelikameratWeatherData(KelikameratUrl url) {
    Document doc;
    try {
      doc = Jsoup.connect(url.getStationUrl()).get();
    } catch (IOException e) {
      log.error("Can't update data: {}", url);
      return null;
    }
    String titleText = doc.getElementsByTag("title").get(0).text();
    titleText = titleText.replaceFirst("Kelikamerat - ", "").replaceFirst("\\| Kelikamerat", "").trim();

    Elements elements = doc.getElementsByClass("weather-details");
    Element div = elements.get(0);
    Element table = div.child(0);
    Element tbody = table.child(0);

    KelikameratWeatherData data = new KelikameratWeatherData();
    data.setPlace(titleText);
    data.setUrl(url);

    int idx = url.getStationUrl().lastIndexOf("/");
    data.setPlaceFromUrl(StringStuff.htmlEntitiesToText(url.getStationUrl().substring(idx + 1)));

    String air = tbody.child(0).child(1).text();
    data.setAir(parseFloat(air));

    String road = tbody.child(1).child(1).text();
    data.setRoad(parseFloat(road));

    String ground = tbody.child(2).child(1).text();
    data.setGround(parseFloat(ground));

    String humidity = tbody.child(3).child(1).text();
    data.setHumidity(parseFloat(humidity));

    String dewPoint = tbody.child(4).child(1).text();
    data.setDewPoint(parseFloat(dewPoint));

    Elements elements2 = doc.getElementsByClass("date-time");
    if (elements2.size() > 0) {
      String timestamp = elements2.get(0).text().substring(12);
      String pattern = "dd.MM.yyyy HH:mm:ss";
      DateTime dateTime  = DateTime.parse(timestamp, DateTimeFormat.forPattern(pattern));
      data.setTime(dateTime);
    }

    return data;
  }

  public List<KelikameratUrl> getStationUrls() {
    return stationUrls;
  }

  @Override
  public void doUpdateData() throws Exception {
    if (this.stationUrls == null) {
      updateStations();
    }
    List<KelikameratWeatherData> weatherDataList = new ArrayList<>();
    for (KelikameratUrl url : this.stationUrls) {
      KelikameratWeatherData data = updateKelikameratWeatherData(url);
      weatherDataList.add(data);
//      log.debug("{}", String.format("%s: %1.2f °C", data.getPlaceFromUrl(), data.getAir()));
    }
    this.weatherDataList = weatherDataList;
  }

  @Override
  public Object doGetData(String... args) {
    if (this.weatherDataList != null) {
//      return sortData(this.weatherDataList, true);
      return this.weatherDataList;
    }
    return null;
  }


  @Override
  public Calendar calculateNextUpdate() {
    Calendar cal = new GregorianCalendar();
    cal.add(Calendar.MINUTE, 30);
    return cal;
  }

  @SuppressWarnings("unchecked")
  public List<KelikameratWeatherData> sortData(List<KelikameratWeatherData> v, boolean reverse) {

    KelikameratWeatherComparator comparator = new KelikameratWeatherComparator(reverse);

    Collections.sort(v, comparator);

    KelikameratWeatherData wd = null;
    for (int xx = 0; xx < v.size(); xx++) {
      wd = v.get(xx);
      wd.setPos(xx + 1);
    }
    if (wd != null) {
      wd.setCount(v.size());
    }
    return v;
  }


  public static class KelikameratWeatherComparator implements Comparator {
    private boolean reverse;

    public KelikameratWeatherComparator(boolean reverse) {
      this.reverse = reverse;
    }

    public int compare(Object o1, Object o2) {
      KelikameratWeatherData w1 = (KelikameratWeatherData) o1;
      KelikameratWeatherData w2 = (KelikameratWeatherData) o2;
      int comp = w1.compareTo(w2);
      if (reverse) {
        comp = comp * -1;
      }
      return comp;
    }
  }
}
