package com.freakz.hokan_ng.common.service;

import com.freakz.hokan_ng.common.dao.UrlDAO;
import com.freakz.hokan_ng.common.engine.CommandPool;
import com.freakz.hokan_ng.common.engine.CommandRunnable;
import com.freakz.hokan_ng.common.engine.HokanCore;
import com.freakz.hokan_ng.common.entity.Url;
import com.freakz.hokan_ng.common.rest.IrcMessageEvent;
import com.freakz.hokan_ng.common.util.HttpPageFetcher;
import com.freakz.hokan_ng.common.util.StringStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: petria
 * Date: 12/14/13
 * Time: 9:45 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Service
@Slf4j
public class UrlLoggerServiceImpl implements UrlLoggerService {

  @Autowired
  private CommandPool commandPool;

  @Autowired
  private UrlDAO urlDAO;

  static class HTMLEditorKit2 extends HTMLEditorKit {
    public Document createDefaultDocument() {
      HTMLDocument doc = (HTMLDocument) (super.createDefaultDocument());
      doc.setAsynchronousLoadPriority(-1); // load synchronously
      return doc;
    }
  }

  public void getTitle(final IrcMessageEvent iEvent, final String url, final String encoding, final boolean isWanha, final String wanhaAadd, final HokanCore core) {

    CommandRunnable cmdRunnable = new CommandRunnable() {

      public void handleRun(long myPid, Object args) {
        String title = null;
        try {

          JEditorPane editorPane = new JEditorPane();
          JEditorPane.registerEditorKitForContentType
              ("text/html", "HTMLEditorKit2");
          editorPane.setEditorKitForContentType
              ("text/html", new HTMLEditorKit2());
          editorPane.setPage(new URL(url));
          title = (String) editorPane.getDocument().getProperty("title");

        } catch (IOException ioe) {
          log.info("Reverting to old <title> finding method: " + ioe.getMessage());
          try {
            final String START_TAG = "<title>";
            final String END_TAG = "</title>";

            HttpPageFetcher page;
            page = new HttpPageFetcher(url, encoding);

            String html = page.getHtmlBuffer().toString().replaceAll("\n|\r", "");
            html = html.replaceAll("<TITLE>", "<title>");
            html = html.replaceAll("</TITLE>", "</title>");
            int idx1 = html.indexOf(START_TAG);
            int idx2 = html.indexOf(END_TAG, idx1);
            title = html.substring(idx1 + START_TAG.length(), idx2);
          } catch (Exception e) {
            log.error("Urls", e);
          }

        } catch (Exception e) {
          log.error("Urls", e);
        }

        if (title != null) {
          title = StringStuff.htmlEntitiesToText(title);
          title = title.replaceAll("\t", "");


          Url entity = urlDAO.findUrl(url);

/*          if (entity == null) {
            entity = new Url();
          }*/

          entity.setUrlTitle(title);
          urlDAO.storeUrl(entity);

          if (url.contains("http://www.imdb.com/title/")) {
            try {
              String ratings = getIMDBData(url);
              if (ratings != null) {
                title += " | " + ratings;
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        } else {
          title = url;
        }

        if (isWanha) {
          title = title + " | wanha" + wanhaAadd;
        }
        processReply(iEvent, title, core);


      }
    };
    commandPool.startRunnable(cmdRunnable);

  }

  public String getIMDBData(String url) throws Exception {
    HttpPageFetcher page = new HttpPageFetcher(url, "UTF-8");

    String html = page.getHtmlBuffer().toString().replaceAll("\n|\r", "");
    Pattern pattern = Pattern.compile("<span itemprop=\"ratingValue\">(.*?)</span>.*<span itemprop=\"ratingCount\">(.*?)</span> users</a>");
    Matcher matcher = pattern.matcher(html);
    String ratings = null;
    if (matcher.find()) {
      String rating = matcher.group(1);
      String users = matcher.group(2);
      ratings = String.format("Ratings: %s/10 from %s users", rating, users);
    }
    return ratings;
  }

  public long logUrl(IrcMessageEvent iEvent, String url) {
    long isWanha = 0;


    Url entity = urlDAO.findUrl(url);
    if (entity != null) {
      entity.addWanhaCount(1);
      isWanha = entity.getWanhaCount();
      urlDAO.storeUrl(entity);
    } else {
      entity = urlDAO.createUrl(url, iEvent.getSender(), iEvent.getChannel(), new Date());
    }
    log.info("Logged URL: {}", entity);

// TODO   StatsHandler.getInstance().handleChannelUrlStats(iEvent, isWanha > 0);

    return isWanha;
  }

  /**
   * Catches URLs from incoming IrcEvent message line. All URLs are stored. If URL found from the
   * message was already stored it is announced to the source channel with " | wanha".
   * <p/>
   * <b>Note: This method is called before IrcEvent is proceeded by actual CmdHandler mechanism which
   * means this should not block as then it's preventing any further command executing while doing so.</b>
   *
   * @param iEvent the incoming IrcEvent
   */
  public void catchUrls(IrcMessageEvent iEvent, HokanCore core) {

    String msg = iEvent.getMessage();
    String regexp = "(https?://|www\\.)\\S+";

    Pattern p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(msg);
    while (m.find()) {
      String url = m.group();
      long isWanha = logUrl(iEvent, url);
      String ignoreTitles = ".*(jpg|gif|jpeg|avi|iso|mkv|mp3|mp4|torrent|mpeg|mpg|mov|exe|gz|zip|bz|7z|tar|twitter.com.*)";

      String wanhaAdd = "";
      for (int i = 0; i < isWanha; i++) {
        wanhaAdd += "!";
      }
      if (!StringStuff.match(url, ignoreTitles, true)) {
        log.info("Finding title: " + url);
        getTitle(iEvent, url, "utf-8", isWanha > 0, wanhaAdd, core);
      } else {
        log.info("SKIP finding title: " + url);
        if (isWanha > 0) {
          processReply(iEvent, url + " | wanha" + wanhaAdd, core);
        }
      }
    }

  }

  private void processReply(IrcMessageEvent iEvent, String reply, HokanCore core) {
    core.handleSendMessage(iEvent.getChannel(), reply);
  }

  @Override
  public List<Url> findUrls(String url, String... nick) {
    return urlDAO.findUrls(url, nick);
  }

  @Override
  public Url findUrl(String url, String... nick) {
    return urlDAO.findUrl(url, nick);
  }
}
