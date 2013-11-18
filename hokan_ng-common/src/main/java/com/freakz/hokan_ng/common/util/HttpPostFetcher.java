package com.freakz.hokan_ng.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * <br>
 * <p/>
 * User: petria<br>
 * Date: 29-Jun-2007<br>
 * Time: 12:53:37<br>
 * <p/>
 */
@Slf4j
public class HttpPostFetcher {

  private StringBuilder _htmlBuffer;
  private int _bytesIn;

  public HttpPostFetcher(String urlStr, String encoding, String... params) throws IOException {

    URL url = new URL(urlStr);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("User-Agent", HttpPageFetcher.USER_AGENT);
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);

    PrintWriter out = new PrintWriter(connection.getOutputStream());

    // encode parameters and concate
    StringBuilder sb = new StringBuilder();
    for (String param : params) {
      sb.append(URLEncoder.encode(param, encoding));
//      sb.append(param);
      sb.append("&");
    }
    out.println(sb.toString());
    out.close();

    String headerEncoding;
    if (encoding != null) {
      headerEncoding = encoding;
    } else {
      headerEncoding = HttpPageFetcher.getEncodingFromHeaders(connection);
    }

    InputStream in = connection.getInputStream();
    InputStreamReader isr = new InputStreamReader(in, headerEncoding);

    BufferedReader br =
        new BufferedReader(isr);

    _htmlBuffer = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
      _htmlBuffer.append(line);
//      _htmlBuffer.append("\n");
      _bytesIn += line.length();
    }
    in.close();

    log.info("HttpPost fetched: " + urlStr + " --> " + _bytesIn);

  }

  /**
   * @return the String containing returned HTML-page from the URL
   */
  public String getHtmlBuffer() {
    return _htmlBuffer.toString();
  }

  /**
   * @return amount of characters read from the URL
   */
  public int getBytesIn() {
    return _bytesIn;
  }

}
