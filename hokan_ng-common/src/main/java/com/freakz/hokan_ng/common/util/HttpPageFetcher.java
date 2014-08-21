package com.freakz.hokan_ng.common.util;


import com.arthurdo.parser.HtmlStreamTokenizer;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.service.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

import static com.freakz.hokan_ng.common.util.StaticStrings.HTTP_USER_AGENT;

/**
 */
@Slf4j
@Component
@Scope("prototype")
public class HttpPageFetcher {

  @Autowired
  private Properties properties;

  private static long bytesIn = 0;

  protected boolean flag = true;
  protected StringBuffer textBuffer;
  protected StringBuffer htmlBuffer;
  protected String line = null;
  protected BufferedReader lines;


  public HttpPageFetcher() {
  }

  public void fetch(String urlStr) throws Exception {
    fetch(urlStr, "UTF-8");
  }

  /**
   * @param urlStr   the URL where to fetch the page
   * @param encoding encoding
   * @throws Exception
   */
  public void fetch(String urlStr, String encoding) throws Exception {

    URL url = new URL(urlStr);
    String proxyHost = properties.getPropertyAsString(PropertyName.PROP_SYS_HTTP_PROXY_HOST, null);
    int proxyPort = properties.getPropertyAsInt(PropertyName.PROP_SYS_HTTP_PROXY_PORT, -1);

    URLConnection conn;
    if (proxyHost != null && proxyPort != -1) {
      SocketAddress address = new
          InetSocketAddress(proxyHost, proxyPort);
      Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
//      log.info("Using proxy: {}", proxy);
      conn = url.openConnection(proxy);
    } else {
      conn = url.openConnection();
    }
    conn.setRequestProperty("User-Agent", properties.getPropertyAsString(PropertyName.PROP_SYS_HTTP_USER_AGENT, HTTP_USER_AGENT));

    String headerEncoding;
    if (encoding != null) {
      headerEncoding = encoding;
    } else {
      headerEncoding = getEncodingFromHeaders(conn);
    }

    InputStream in = conn.getInputStream();
    InputStreamReader isr = new InputStreamReader(in, headerEncoding);

    BufferedReader br =
        new BufferedReader(isr);

    htmlBuffer = new StringBuffer();
    String l = null;
    do {
      try {
        l = br.readLine();
      } catch (IOException ex) {
        log.info("read line failed", ex);
      }
      if (l != null) {
        htmlBuffer.append(l);
        htmlBuffer.append("\n");
        bytesIn += l.length();
      }
    } while (l != null);

    HtmlStreamTokenizer tok = new HtmlStreamTokenizer(new StringReader(htmlBuffer.toString()));

    textBuffer = new StringBuffer();
    while (tok.nextToken() != HtmlStreamTokenizer.TT_EOF) {

      int ttype = tok.getTokenType();
      if (ttype == HtmlStreamTokenizer.TT_TEXT) {
        textBuffer.append(tok.getStringValue());
      }

    }

    HtmlStreamTokenizer.unescape(textBuffer);
    reset();
  }


  public static String getEncodingFromHeaders(URLConnection conn) {
    String encoding = "utf-8";
    try {
      // Create a URLConnection object for a URL
//      URL url = new URL(urlStr);
//      URLConnection conn = url.openConnection();

      // List all the response headers from the server.
      // Note: The first call to getHeaderFieldKey() will implicit send
      // the HTTP request to the server.
      for (int i = 0; ; i++) {
        String headerName = conn.getHeaderFieldKey(i);
        String headerValue = conn.getHeaderField(i);
//        System.out.println(headerName + " = " + headerValue);
        if (headerName == null && headerValue == null) {
          // No more headers
          break;
        }
        if (headerName != null && headerName.equalsIgnoreCase("Content-Type")) {
          String val = headerValue.toLowerCase();
          if (val.matches(".*8859.*")) {
            encoding = "8859_1";
          } else {
            encoding = "UTF-8";
          }
        }
      }
    } catch (Exception e) {
      //
    }

    return encoding;
  }


  /**
   * Resets the read buffer state to the same point it was just after the page was fetched
   */
  public void reset() {
    lines = new BufferedReader(new StringReader(textBuffer.toString()));
    flag = true;
    line = getLine();
  }

  /**
   * @return the StringBuffer containing fetched page's content stripped from HTML tags
   */
  public StringBuffer getTextBuffer() {
    return textBuffer;
  }

  /**
   * @return the StringBuffer containing fetched page's content as it is in server
   */
  public StringBuffer getHtmlBuffer() {
    return htmlBuffer;
  }

  private String getLine() {
    String line = null;
    try {
      line = lines.readLine();
    } catch (IOException e) {
      flag = false;
    }
    if (line == null) {
      flag = false;
    }
    return line;
  }

  /**
   * @return the String containing next line from the textbuffer of fetched page
   */
  public String nextLine() {
    String line = this.line;
    this.line = getLine();
    return line;
  }

  /**
   * @return the boolean to check does the textbuffer has more lines available to get with nextLine()
   */
  public boolean hasMoreLines() {
    return flag;
  }

  /**
   * @param patt regular expression to find from the textbuffer line
   * @param show whether to print out the lines to the debug log while doing search
   * @return the String containing line which matche the pattern
   */
  public String findLine(String patt, boolean show) {
    String line;
    while (hasMoreLines()) {
      line = nextLine();
      if (show) {
        log.info(">> '" + line + "'");
      }
      if (StringStuff.match(line, patt, true)) {
        return line;
      }
    }
    return null;
  }

  /**
   * Removes lines from the buffer according to their length.
   * <p>
   * HttpPageFetcher pf = new HttpPageFetcher("http://my.url/");<br>
   * pf.trimLines(0); // removes all lines from the buffer that has no characters<br>
   * <p>
   *
   * @param minLength the number of characters on row must have so it it's not removed from the buffer.
   */
  public void trimLines(int minLength) {
    StringBuffer sb = new StringBuffer();
    while (hasMoreLines()) {
      String line = nextLine().trim();
      if (line.length() > minLength) {
        sb.append(line + "\n");
      }
    }
    //    System.out.println("--> " + sb);
    textBuffer = sb;
    reset();
  }

  /**
   * HttpPageFetcher class keeps count how much data has been fetched from the net. This
   * method can be used to get the data count.
   * <p>
   *
   * @return the amount of total bytes fetched by all instances created.
   */
  public static long getBytesIn() {
    return bytesIn;
  }

}
